// Package declaration (original)
package RescueAnimals.Src;

// Import statements (fixed typos from original)
import java.util.ArrayList;   // For storing Dog and Monkey lists
import java.util.Scanner;    // For reading user input
import java.sql.*;           // For database connection, authentication, and logging

// Suppress warnings annotation (original, fixed syntax)
@SuppressWarnings("unused")

/**
 * Driver class for the Rescue Animals project.
 * Originally created for IT-145 to demonstrate
 * object-oriented design, collections, and menus.
 *
 * Enhanced to include:
 * - User authentication (login system)
 * - Forensic logging of login attempts
 * - Database connection placeholders
 */
public class Driver {

    // =========================================
    // ORIGINAL VARIABLES
    // =========================================
    // List of dogs in the system
    private static ArrayList<Dog> dogList = new ArrayList<Dog>();

    // List of monkeys in the system
    private static ArrayList<Monkey> monkeyList = new ArrayList<Monkey>();

    // Scanner for menu input
    private static Scanner scanner = new Scanner(System.in);

    // =========================================
    // ENHANCEMENT VARIABLES
    // =========================================
    private static Connection connection;  // Database connection object

    public static void main(String[] args) {
        // =========================================
        // ENHANCEMENT: Initialize database connection
        // =========================================
        initializeDatabaseConnection();

        // =========================================
        // ENHANCEMENT: Require user login before menu
        // =========================================
        boolean loggedIn = false;
        while (!loggedIn) {
            loggedIn = login();
        }

        // =========================================
        // ORIGINAL: Start the menu loop
        // =========================================
        displayMenu();
    }

    // =========================================
    // ENHANCEMENT: Login function
    // =========================================
    private static boolean login() {
        System.out.print("Enter UserID: ");
        String userId = scanner.nextLine();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        // Validate credentials against DB
        boolean valid = validateUser(userId, password);

        if (valid) {
            logLoginAttempt(userId, "SUCCESS");
            System.out.println("Login successful!\n");
            return true;
        } else {
            logLoginAttempt(userId, "FAILURE");
            System.out.println("Invalid login. Please try again.\n");
            return false;
        }
    }

    // =========================================
    // ORIGINAL: Menu-driven system
    // =========================================
    private static void displayMenu() {
        String choice = "";

        // Menu loop
        while (!choice.equalsIgnoreCase("q")) {
            System.out.println("Menu:");
            System.out.println("[1] Intake a new dog");
            System.out.println("[2] Intake a new monkey");
            System.out.println("[3] Reserve an animal");
            System.out.println("[4] Print list of animals");
            System.out.println("[q] Quit");
            System.out.print("Enter your choice: ");

            choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    intakeNewDog();
                    break;
                case "2":
                    intakeNewMonkey();
                    break;
                case "3":
                    reserveAnimal();
                    break;
                case "4":
                    printAnimals();
                    break;
                case "q":
                    System.out.println("Exiting system...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // =========================================
    // ORIGINAL: Placeholder methods for menu
    // =========================================
    private static void intakeNewDog() {
        // Original project logic to intake a dog would go here
        System.out.println("Intake new dog - (original functionality placeholder).");
    }

    private static void intakeNewMonkey() {
        // Original project logic to intake a monkey would go here
        System.out.println("Intake new monkey - (original functionality placeholder).");
    }

    private static void reserveAnimal() {
        // Original project logic to reserve an animal would go here
        System.out.println("Reserve animal - (original functionality placeholder).");
    }

    private static void printAnimals() {
        // Original project logic to print animals would go here
        System.out.println("Print animals - (original functionality placeholder).");
    }

    // =========================================
    // ENHANCEMENT PLACEHOLDERS
    // =========================================

    /**
     * Initializes the database connection.
     * (Placeholder – adjust DB URL, user, password as needed)
     */
    private static void initializeDatabaseConnection() {
        try {
            // Example: JDBC URL, update with actual DB info
            String url = "jdbc:mysql://localhost:3306/rescue_animals";
            String user = "root";
            String pass = "password";

            connection = DriverManager.getConnection(url, user, pass);
            System.out.println("Database connection established.");
        } catch (Exception e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
    }

    /**
     * Validates user credentials against the database.
     * (Placeholder query, assumes `users` table exists)
     */
    private static boolean validateUser(String userId, String password) {
        try {
            String sql = "SELECT * FROM users WHERE user_id = ? AND password = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, userId);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // true if user found
        } catch (Exception e) {
            System.out.println("Error validating user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Logs login attempts for forensic auditing.
     * (Placeholder – assumes `login_logs` table exists)
     */
    private static void logLoginAttempt(String userId, String status) {
        try {
            String sql = "INSERT INTO login_logs (user_id, status, timestamp) VALUES (?, ?, NOW())";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, userId);
            stmt.setString(2, status);
            stmt.executeUpdate();
            System.out.println("Login attempt logged.");
        } catch (Exception e) {
            System.out.println("Error logging attempt: " + e.getMessage());
        }
    }
}
