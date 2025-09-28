package RescueAnimals.Src;

import java.sql.*;

/**
 * Utility class for database operations.
 * Handles user authentication and forensic logging.
 */
public class DBUtil {

    // Path to SQLite database file
    // Make sure you created this using schema.sql
    private static final String DB_URL = "jdbc:sqlite:rescue_animals.db";

    /**
     * Connects to the SQLite database.
     * @return Connection object or null if connection fails.
     */
    public static Connection connect() {
        try {
            return DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * Validates user credentials against the users table.
     * @param userId   The entered user ID.
     * @param password The entered password.
     * @return true if credentials are valid, false otherwise.
     */
    public static boolean validateUser(String userId, String password) {
        String sql = "SELECT * FROM users WHERE user_id = ? AND password = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // returns true if user exists
        } catch (SQLException e) {
            System.out.println("Error validating user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Records a login attempt in the login_logs table.
     * @param userId   The entered user ID.
     * @param status   SUCCESS or FAILURE.
     */
    public static void logLoginAttempt(String userId, String status) {
        String sql = "INSERT INTO login_logs (user_id, status) VALUES (?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.setString(2, status);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error logging attempt: " + e.getMessage());
        }
    }
}
