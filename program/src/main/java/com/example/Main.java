package com.example;

import java.sql.Connection;
import static java.sql.DriverManager.getConnection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        mySQLConnection();
        // access the test method from the GlobalClass
    }

    public static void mySQLConnection() {
        // MySQL connection string
        String url = "jdbc:mysql://localhost:3306/movie_society";
        String user = "root";
        String password = "";

        // Connection object
        Connection connection = null;

        try {

            // Establish the connection
            connection = getConnection(url, user, password);

            if (connection != null) {
                System.out.println("Connected to the database!");
            } else {
                System.out.println("Failed to connect to the database.");
            }

        } catch (SQLException e) {
            System.out.println("Connection failed. Check output console." + e.getMessage());
        } finally {
            // Close the connection if it was opened
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println("Connection Unable to close.");
                }
            }
        }
    }
}