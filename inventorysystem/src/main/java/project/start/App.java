package project.start;

import java.sql.Connection;
import static java.sql.DriverManager.getConnection;
import java.sql.SQLException;
import java.util.ArrayList;

import project.modules.item.Item;

public class App {
    public static void main(String[] args) {
        testReadItem();
        //mySQLConnection();
    }

    public static void testCreateItem() 
    {
        // Create item
        Item newItem = new Item();
        newItem.setItem_Category_ID(1);
        newItem.setVendor_ID("vendor3");
        newItem.setItem_name("Test Item");
        newItem.setItem_Desc("This is a test item");
        newItem.setItem_Quantity(10);
        newItem.setItem_Price(100.50);

        boolean created = newItem.Create();
        System.out.println("Item created: " + created);
    }

    public static void testReadItem() {
        // Assuming the item was created with ID 1 for testing purposes
        int itemId = 1;

        // Read the item by ID
        Item readItem = new Item(itemId);
        System.out.println("Item read by ID: " + readItem.getItem_Name());

        // Read all items
        ArrayList<Item> items = Item.ReadAll();
        System.out.println("All items: ");
        for (Item item : items) 
        {
            System.out.println(item.getItem_Name());
        }

        // Read items by a specific field value
        ArrayList<Item> itemsByField = Item.Read("Item_name", "Updated Test Item");
        System.out.println("Items with name 'Updated Test Item': ");
        for (Item item : itemsByField) {
            System.out.println(item.getItem_Name());
        }
    }

    public static void testUpdateItem() {
        // Assuming the item was created with ID 1 for testing purposes
        int itemId = 1;

        // Read the item by ID
        Item readItem = new Item(itemId);
        readItem.setItem_name("Updated Test Item");
        readItem.setItem_Desc("This is an updated test item");
        boolean updated = readItem.Update(itemId);
        System.out.println("Item updated: " + updated);
    }

    public static void testDeleteItem() {
        // Assuming the item was created with ID 1 for testing purposes
        int itemId = 1;

        // Read the item by ID
        Item readItem = new Item(itemId);

        // Remove the item
        boolean removed = readItem.Remove(itemId);
        System.out.println("Item removed: " + removed);
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
