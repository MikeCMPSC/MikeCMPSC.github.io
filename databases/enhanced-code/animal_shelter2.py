"""
Enhancements:
 - Secure authentication using environment variables:
     - Preferred: MONGO_URI
     - Fallback: MONGO_USER + MONGO_PASS (+ optional MONGO_HOST, MONGO_PORT)
 - Audit logging saved in MongoDB 'audit_logs' collection (primary forensic store).
 - Custom 'user' argument for CRUD methods so different app users can be accurately tracked.
 - Query tracking: every READ logs the query and attempts a best-effort explain() to capture index usage.
 - Stronger error handling and clear logging.
 - Local audit log file can be enabled via WRITE_LOCAL_AUDIT env var.

Usage:
    # set environment variables (example)
    export MONGO_URI="mongodb://user:pass@host:27017/?authSource=admin"
    # or set MONGO_USER/MONGO_PASS/MONGO_HOST/MONGO_PORT/MONGO_DB

    from animal_shelter2 import AnimalShelter
    shelter = AnimalShelter()  # uses env vars for auth
    shelter.create("alice", {"name": "Fido", "species": "Dog"})
    docs = shelter.read("alice", {"species": "Dog"})
"""

import os
import logging
import json
from datetime import datetime
from typing import Any, Dict, List, Optional

from pymongo import MongoClient, errors
from bson import ObjectId

# -------------------------
# Configuration & Logging
# -------------------------

# Optional: write a local audit log file in addition to MongoDB (disabled by default)
WRITE_LOCAL_AUDIT = os.getenv("WRITE_LOCAL_AUDIT", "false").lower() in ("1", "true", "yes")
LOCAL_AUDIT_DIR = os.getenv("GRAZ_LOG_DIR", ".")
LOCAL_AUDIT_FILE = os.path.join(LOCAL_AUDIT_DIR, "audit.log")

# Configure application logger (console; INFO level)
_logger = logging.getLogger("animal_shelter2")
if not _logger.handlers:
    _logger.setLevel(logging.INFO)
    ch = logging.StreamHandler()
    ch.setLevel(logging.INFO)
    ch.setFormatter(logging.Formatter("%(asctime)s - %(levelname)s - %(message)s"))
    _logger.addHandler(ch)

if WRITE_LOCAL_AUDIT:
    # ensure log directory exists if writing local audit file
    os.makedirs(LOCAL_AUDIT_DIR, exist_ok=True)
    fh = logging.FileHandler(LOCAL_AUDIT_FILE)
    fh.setLevel(logging.INFO)
    fh.setFormatter(logging.Formatter("%(asctime)s - %(message)s"))
    _logger.addHandler(fh)


# -------------------------
# Helper: MongoDB Connection
# -------------------------

def _get_mongo_client() -> MongoClient:
    """
    Return a MongoClient using environment-based secure configuration.

    Preferred:
        - MONGO_URI : full connection string (recommended)

    Fallback:
        - MONGO_USER, MONGO_PASS (required)
        - optional: MONGO_HOST (default localhost), MONGO_PORT (default 27017)

    Raises:
        EnvironmentError if credentials/URI are not provided.
    """
    uri = os.getenv("MONGO_URI")
    if uri:
        _logger.info("Using MONGO_URI for MongoDB connection.")
        try:
            return MongoClient(uri, serverSelectionTimeoutMS=5000)
        except Exception as e:
            _logger.exception("Failed to create MongoClient from MONGO_URI: %s", e)
            raise

    # Fallback to explicit credentials
    user = os.getenv("MONGO_USER")
    password = os.getenv("MONGO_PASS")
    host = os.getenv("MONGO_HOST", "localhost")
    port = int(os.getenv("MONGO_PORT", "27017"))

    if not (user and password):
        raise EnvironmentError(
            "Missing MongoDB credentials. Set MONGO_URI or set MONGO_USER and MONGO_PASS environment variables."
        )

    _logger.info("Using MONGO_USER/MONGO_PASS for MongoDB connection.")
    try:
        return MongoClient(host=host, port=port, username=user, password=password, serverSelectionTimeoutMS=5000)
    except Exception as e:
        _logger.exception("Failed to create MongoClient with user/pass: %s", e)
        raise


# -------------------------
# AnimalShelter Class
# -------------------------

class AnimalShelter(object):
    """
    CRUD interface to the 'animals' collection with MongoDB-based audit logging.

    Design notes:
      - All CRUD methods require a custom 'user' argument (string) to identify the caller.
      - Audit logs are inserted into the 'audit_logs' collection in the same database.
      - READ operations attempt a best-effort explain() to capture index usage for forensic analysis.
    """

    def __init__(self, db_name: Optional[str] = None, collection_name: str = "animals"):
        """
        Initialize the AnimalShelter instance:
          - Create a MongoClient using secure credentials from environment variables.
          - Resolve database and collection references.
          - Prepare the audit_logs collection for writing audit entries.

        Parameters:
          - db_name: optional database name; will use MONGO_DB env var or default 'aac'
          - collection_name: name of the primary collection (default 'animals')
        """
        try:
            self.client = _get_mongo_client()
        except Exception as e:
            _logger.exception("Unable to create MongoDB client: %s", e)
            raise

        dbname = db_name or os.getenv("MONGO_DB", "aac")
        self.db = self.client[dbname]
        self.collection = self.db[collection_name]
        self.audit_collection = self.db["audit_logs"]

        _logger.info("Connected to MongoDB database '%s', collection '%s'. Audit logs -> '%s.audit_logs'.",
                     dbname, collection_name, dbname)

    # -------------------------
    # Audit Logging Helper
    # -------------------------

    def _write_audit(self,
                     user: str,
                     operation: str,
                     query: Optional[Dict[str, Any]] = None,
                     data: Optional[Dict[str, Any]] = None,
                     result_count: Optional[int] = None,
                     error: Optional[str] = None,
                     explain_info: Optional[Dict[str, Any]] = None) -> None:
        """
        Insert an audit entry into the 'audit_logs' collection.

        Each audit entry contains:
          - user: application user performing the action
          - timestamp: UTC ISO string
          - operation: CREATE / READ / UPDATE / DELETE
          - query: the query used (if applicable)
          - data: data inserted or update values (if applicable)
          - result_count: number of documents returned/modified/deleted
          - error: error string if any
          - explain_info: best-effort explain() output or index usage hints

        This function will not throw if the audit insert fails; it logs locally instead.
        """
        entry = {
            "user": user,
            "timestamp": datetime.utcnow(),
            "operation": operation,
            "collection": self.collection.name,
            "query": query or {},
            "data": data or {},
            "result_count": result_count,
            "error": error,
            "explain": explain_info,
        }

        try:
            # Primary forensic store: insert audit into MongoDB
            self.audit_collection.insert_one(entry)
        except Exception as e:
            # Audit insertion should not block application flow — log locally
            _logger.error("Failed to write audit entry to MongoDB: %s. Entry: %s", e, json.dumps(entry, default=str))

        if WRITE_LOCAL_AUDIT:
            try:
                # Optionally write a compact JSON line to local file (for offline reference)
                _logger.info(json.dumps({
                    "timestamp": entry["timestamp"].isoformat(),
                    "user": user,
                    "operation": operation,
                    "collection": self.collection.name,
                    "query": entry["query"],
                    "result_count": result_count,
                    "error": error,
                }, default=str))
            except Exception:
                # Never let audit logging raise to caller
                _logger.exception("Failed to write local audit log entry.")

    # -------------------------
    # CRUD Methods (require `user` argument)
    # -------------------------

    def create(self, user: str, data: Dict[str, Any]) -> bool:
        """
        Insert a document into the collection.

        Parameters:
          - user: string identifying the application user performing the operation
          - data: dictionary representing the document to insert

        Returns:
          - True if insert succeeds, False otherwise
        """
        if data is None:
            raise ValueError("Data parameter is empty")

        try:
            result = self.collection.insert_one(data)
            # Log audit: creation succeeded
            self._write_audit(user=user, operation="CREATE", data=data, result_count=1)
            _logger.info("CREATE by %s succeeded (inserted_id=%s).", user, str(result.inserted_id))
            return True
        except errors.PyMongoError as e:
            # Log audit: creation failed
            self._write_audit(user=user, operation="CREATE", data=data, error=str(e))
            _logger.exception("CREATE by %s failed: %s", user, e)
            return False

    def read(self, user: str, query: Dict[str, Any], limit: Optional[int] = 0) -> List[Dict[str, Any]]:
        """
        Read documents matching `query`. Logs the query and (best-effort) explain() info.

        Parameters:
          - user: application user calling the method
          - query: MongoDB filter dict
          - limit: optional max number of documents to return (0 or None => no limit)

        Returns:
          - List of documents (possibly empty)
        """
        if query is None:
            raise ValueError("Query parameter is empty")

        # Log that the read was requested (before execution)
        self._write_audit(user=user, operation="READ_REQUEST", query=query)

        try:
            cursor = self.collection.find(query)
            if limit and isinstance(limit, int) and limit > 0:
                cursor = cursor.limit(limit)
            results = list(cursor)
            result_count = len(results)

            explain_info = None
            # Attempt a best-effort explain() with 'queryPlanner' only.
            try:
                # explain can be expensive on very large collections; this is best-effort for forensic metadata.
                explain_out = self.collection.find(query).explain("queryPlanner")
                # Try to extract index-related hints from the explain output (if present)
                explain_info = {
                    "queryPlanner": explain_out.get("queryPlanner", {})
                }
            except Exception as ex:
                # Do not let explain failure stop the operation; record the error in audit explain_info
                explain_info = {"explain_error": str(ex)}

            # Log results
            self._write_audit(user=user, operation="READ_RESULT", query=query, result_count=result_count, explain_info=explain_info)
            _logger.info("READ by %s returned %d documents.", user, result_count)
            return results
        except errors.PyMongoError as e:
            self._write_audit(user=user, operation="READ_ERROR", query=query, error=str(e))
            _logger.exception("READ by %s failed: %s", user, e)
            return []

    def update(self, user: str, query: Dict[str, Any], update_values: Dict[str, Any], multiple: bool = False) -> int:
        """
        Update document(s) matching `query` with `update_values`.

        Parameters:
          - user: application user
          - query: filter dict
          - update_values: fields to set (dictionary)
          - multiple: if True, use update_many; else update_one

        Returns:
          - Number of documents modified
        """
        if not query or not update_values:
            raise ValueError("Query and update_values parameters must not be empty")

        try:
            if multiple:
                result = self.collection.update_many(query, {"$set": update_values})
            else:
                result = self.collection.update_one(query, {"$set": update_values})
            modified = result.modified_count
            self._write_audit(user=user, operation="UPDATE", query=query, data=update_values, result_count=modified)
            _logger.info("UPDATE by %s modified %d documents.", user, modified)
            return modified
        except errors.PyMongoError as e:
            self._write_audit(user=user, operation="UPDATE_ERROR", query=query, data=update_values, error=str(e))
            _logger.exception("UPDATE by %s failed: %s", user, e)
            return 0

    def delete(self, user: str, query: Dict[str, Any], multiple: bool = False) -> int:
        """
        Delete document(s) matching `query`.

        Parameters:
          - user: application user
          - query: filter dict
          - multiple: if True, delete_many; else delete_one

        Returns:
          - Number of documents deleted
        """
        if not query:
            raise ValueError("Query parameter must not be empty")

        try:
            if multiple:
                result = self.collection.delete_many(query)
            else:
                result = self.collection.delete_one(query)
            deleted = result.deleted_count
            self._write_audit(user=user, operation="DELETE", query=query, result_count=deleted)
            _logger.info("DELETE by %s removed %d documents.", user, deleted)
            return deleted
        except errors.PyMongoError as e:
            self._write_audit(user=user, operation="DELETE_ERROR", query=query, error=str(e))
            _logger.exception("DELETE by %s failed: %s", user, e)
            return 0

    # -------------------------
    # Optional utility: create indexes
    # -------------------------

    def create_indexes(self, fields: List[str]) -> Dict[str, Optional[str]]:
        """
        Convenience method to create single-field ascending indexes on the collection.

        Parameters:
          - fields: list of field names to index

        Returns:
          - Dict mapping field -> created index name (or None on failure)
        """
        created = {}
        for field in fields:
            try:
                idx_name = self.collection.create_index([(field, 1)], background=True)
                created[field] = idx_name
                _logger.info("Created index on '%s' -> %s", field, idx_name)
            except Exception as e:
                created[field] = None
                _logger.exception("Failed to create index on %s: %s", field, e)
        # Audit the index creation as an administrative action (user "admin" by default)
        self._write_audit(user="admin", operation="CREATE_INDEX", data={"fields": fields})
        return created


# -------------------------
# Example usage (not executed on import)
# -------------------------
if __name__ == "__main__":
    """
    Example quick test / demo:
      - Requires environment variables to be set (MONGO_URI or MONGO_USER & MONGO_PASS)
      - Demonstrates create/read/update/delete with audit logging
    """
    # Very simple demo flow (adjust or remove for production)
    try:
        shelter = AnimalShelter()  # Uses env vars
        demo_user = "demo_user"

        # Create
        shelter.create(demo_user, {"name": "Rover", "species": "Dog", "age": 4})

        # Read
        dogs = shelter.read(demo_user, {"species": "Dog"}, limit=10)
        print(f"Found {len(dogs)} dog(s).")

        # Update
        modified = shelter.update(demo_user, {"name": "Rover"}, {"age": 5})
        print(f"Modified {modified} document(s).")

        # Delete
        deleted = shelter.delete(demo_user, {"name": "Rover"})
        print(f"Deleted {deleted} document(s).")

        # Create an index for better reads (optional)
        idxs = shelter.create_indexes(["species", "name"])
        print("Created indexes:", idxs)

    except Exception as ex:
        _logger.exception("Demo encountered an error: %s", ex)
