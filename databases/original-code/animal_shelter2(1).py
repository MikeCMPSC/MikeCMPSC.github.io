# Import necessary libraries
from pymongo import MongoClient, errors
from bson.objectid import ObjectId  # Import ObjectId for handling MongoDB document IDs

class AnimalShelter(object):
    # CRUD operations for Animal collection in MongoDB
    def __init__(self, username, password):
        # Connection Variables
        
        if username is not None:
            USER = username
        else:
            USER = 'aacuser'  # Username for MongoDB
        
        if password is not None:
            PASS = password
        else:
            PASS = 'Mike1357'  # Password for MongoDB
        
        HOST = 'nv-desktop-services.apporto.com'  # Hostname for MongoDB server 
        
        PORT =  31878   # Port number for MongoDB server
        
        DB = 'aac'  # Database name
        
        COL = 'animals'  # Collection name
                    
        # Initialize Connection
        try:
            # Create a connection to the MongoDB server using the provided credentials and host information
            self.client = MongoClient('mongodb://%s:%s@%s:%d' % (USER,PASS,HOST,PORT))
            # Access the specific database
            self.database = self.client['%s' % (DB)]
            # Access the specific collection within the database
            self.collection = self.database['%s' % (COL)]
            print("Connected successfully to MongoDB")
        except errors.ConnectionError as e:
            # Handle connection errors
            print(f"Could not connect to MongoDB: {e}")
                                
    def create(self, data):
        # Method to insert a new document into the collection
        if data is not None:
            try:
                # Insert the data into the collection; data should be a dictionary
                self.collection.insert_one(data)
                return True  # Return True if insertion is successful
            except errors.PyMongoError as e:
                # Handle errors that occur during insertion
                print(f"An error occurred while inserting data: {e}")
                return False  # Return False if an error occurs
        else:
            # Raise a ValueError if the data parameter is empty
            raise ValueError("Data parameter is empty")
                                                        
    def read(self, query):
        # Method to read documents from the collection
        if query is not None:
            try:
                # Find documents in the collection that match the query
                documents = list(self.collection.find(query))
                return documents  # Return the list of documents
            except errors.PyMongoError as e:
                # Handle errors that occur during the read operation
                print(f"An error occurred while reading data: {e}")
                return []  # Return an empty list if an error occurs
        else:
            # Raise a ValueError if the query parameter is empty
            raise ValueError("Query parameter is empty")
    
    
    def update(self, query, update_values, multiple=False):
        # Method to update document(s) in the collection
        if query and update_values:
            try:
                if multiple:
                    result = self.collection.update_many(query, {'$set': update_values})
                else:
                    result = self.collection.update_one(query, {'$set': update_values})
                return result.modified_count  # Return number of modified documents
            except errors.PyMongoError as e:
                print(f"An error occurred while updating data: {e}")
                return 0
        else:
            raise ValueError("Query and update_values parameters must not be empty")

    def delete(self, query, multiple=False):
        # Method to delete document(s) from the collection
        if query:
            try:
                if multiple:
                    result = self.collection.delete_many(query)
                else:
                    result = self.collection.delete_one(query)
                return result.deleted_count  # Return number of deleted documents
            except errors.PyMongoError as e:
                print(f"An error occurred while deleting data: {e}")
                return 0
        else:
            raise ValueError("Query parameter must not be empty")
