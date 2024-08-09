package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
 public static Connection mySQLConnection() {
        // MySQL connection string
        String url = "jdbc:mysql://localhost:3306/inventory";
        String user = "root";
        String password = "";

        // Connection object
        Connection connection = null;

        try {
            // Establish the connection
            connection = DriverManager.getConnection(url, user, password);

            if (connection != null) {
                System.out.println("Connected to the database!");
            } else {
                System.out.println("Failed to connect to the database.");
            }

        } catch (SQLException e) {
            System.out.println("Connection failed. Check output console." + e.getMessage());
        } 
        
        return connection; // Add this line to return the connection object
    }
}
