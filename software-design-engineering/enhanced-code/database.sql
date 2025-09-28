-- ==========================================================
-- Rescue Animals System Database Schema
-- Supports user authentication and forensic login logging
-- ==========================================================

-- Drop tables if they exist (for clean setup)
DROP TABLE IF EXISTS login_logs;
DROP TABLE IF EXISTS users;

-- ==========================================================
-- Users table
-- Stores system user accounts with credentials
-- ==========================================================
CREATE TABLE users (
    user_id VARCHAR(50) PRIMARY KEY,   -- Unique user ID
    password VARCHAR(255) NOT NULL     -- Plaintext here for demo, but should be hashed in production
);

-- Example users
INSERT INTO users (user_id, password) VALUES
('admin', 'admin123'),
('staff1', 'welcome1'),
('staff2', 'letmein');

-- ==========================================================
-- Login Logs table
-- Tracks all login attempts for forensic auditing
-- ==========================================================
CREATE TABLE login_logs (
    log_id INTEGER PRIMARY KEY AUTOINCREMENT, -- Unique log entry ID
    user_id VARCHAR(50),                      -- ID used during login
    status VARCHAR(10),                       -- SUCCESS or FAILURE
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP, -- Time of attempt
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Example entry for testing
INSERT INTO login_logs (user_id, status) VALUES ('admin', 'SUCCESS');
