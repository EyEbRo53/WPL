
package com.example.wpl;

import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DB_Functions {
    static private String url = "jdbc:sqlserver://localhost:1433;instanceName=SQLEXPRESS;databaseName=WP;user=lol;password=1234;trustServerCertificate=true";
    public static int signUp(String name, String email, String password) {
        // Check if the email is valid
        if (!isValidEmail(email)) {
            System.out.println("Invalid email address.");
            return 3;
        }
        boolean res = true;
        String sqlQuery = "{call AddCustomer(?, ?, ?)}";
        try (Connection conn = DriverManager.getConnection(url);
             CallableStatement pstmt = conn.prepareCall(sqlQuery)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.execute();
            System.out.println("User registered successfully.");
        } catch (SQLException e) {
            if (e.getMessage().contains("Error: Duplicate email. This email address already exists.")) {
                System.out.println("User registration failed due to unique constraint violation.");
                return 2;
            } else {
                System.out.println("User registration failed. Error: " + e.getMessage());
                e.printStackTrace();
                return 0;
            }
        }
        return 1;
    }
    public static boolean isValidEmail(String email) {
        // Regular expression for email validation with at least one character before @gmail.com
        String regex = "^[A-Za-z0-9+_.-]+@gmail.com$";

        // Compile the regex pattern
        Pattern pattern = Pattern.compile(regex);

        // Create matcher object
        Matcher matcher = pattern.matcher(email);

        // Perform matching and return result
        return matcher.matches();
    }
    public static boolean checkAdminCredentials(String email, String password) {
        // Database connection details
        String sqlQuery = "SELECT * FROM User_table WHERE email = ? AND UserPassword = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            // Handle SQL exceptions
            System.out.println("Error checking admin credentials: " + e.getMessage());
            e.printStackTrace();
            return false; // Indicate failure
        }
    }
    public static boolean checkCustomerCredentials(String email, String password) {
        // Database connection details
        String sqlQuery = "SELECT * FROM Customer WHERE email = ? AND Password = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            // Handle SQL exceptions
            System.out.println("Error checking customer credentials: " + e.getMessage());
            e.printStackTrace();
            return false; // Indicate failure
        }
}

}
