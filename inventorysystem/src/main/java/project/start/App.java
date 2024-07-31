package project.start;

import java.util.ArrayList;

import project.IO.Output;
import project.modules.item.Item;

public class App {
    public static void main(String[] args) {
        testOutput();
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
        for (Item item : items) {
            System.out.println("Item read all: " + item.getItem_Name());
        }

        // Read all items by category
        ArrayList<Item> itemsByCategory = Item.Read("Item_Category_ID","1");
        for (Item item : itemsByCategory) {
            System.out.println("Item read all by category: " + item.getItem_Name());
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
        int itemId = 4;

        // Read the item by ID
        Item readItem = new Item(itemId);

        // Remove the item
        boolean removed = readItem.Remove(itemId);
        System.out.println("Item removed: " + removed);
    }

    public static void testOutput() {
        // Test Output
        ArrayList<Item> items = Item.ReadAll();
        Output.<Item>PrintClassFieldsAsTable(items, Item.class);

        //Menu
        Output.PrintCrudMenu(Item.class);

    }
}
