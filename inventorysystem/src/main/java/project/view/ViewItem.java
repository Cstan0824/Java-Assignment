package project.view;

import java.util.ArrayList;
import java.util.Scanner;

import project.modules.item.Item;
import project.modules.user.User;

public class ViewItem {
    private static User user;
    private static final Scanner scanner = new Scanner(System.in);
    private ArrayList<Item> items = null;

    //getter
    public ArrayList<Item> getItems() {
        return this.items;
    }

    // Constructor to initialize the user
    public ViewItem(User _user) {
        user = _user;
        this.items = Item.GetAll();

    }

    // Getter for User
    public User getUser() {
        return user;
    }

    // Method to select an item from the list
    public Item selectItemFromList() {
        // Fetch all items
        if (this.items.isEmpty()) {
            System.out.println("No items available.");
            return null; // Return null if no items are available
        }

        // Display the list of items
        System.out.println(String.format("| %-20s | %-20s | %-20s |", "Item ID", "Item Name", "Item Description"));
        for (int i = 1; i <= items.size(); i++) {
            System.out.println(i + ". " + items.get(i - 1)); // Display items with index
        }

        // Prompt user to select an item
        System.out.print("Select Item (enter the number): ");
        int itemIndex = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Validate the selected index
        if (itemIndex >= 0 && itemIndex <= items.size()) {
            Item selectedItem = items.get(itemIndex - 1);
            selectedItem.Get(); // Fetch full details of the selected item
            return selectedItem;
        } else {
            System.out.println("Invalid selection.");
            return null; // Return null if selection is invalid
        }
    }
    
    public Item selectItemFromList(ArrayList<Item> _items) {
         // Fetch all items
         if (_items == null || _items.isEmpty())
         {
             System.out.println("No items available.");
             return null; // Return null if no items are available
         }

        // Display the list of items
        System.out.println(String.format("| %-20s | %-20s | %-20s |", "Item ID", "Item Name", "Item Description"));
        for (int i = 1; i <= _items.size(); i++) {
            System.out.println(i + ". " + _items.get(i - 1)); // Display items with index
        }

        // Prompt user to select an item
        System.out.print("Select Item (enter the number): ");
        int itemIndex = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Validate the selected index
        if (itemIndex >= 0 && itemIndex <= _items.size()) {
            Item selectedItem = _items.get(itemIndex - 1);
            selectedItem.Get(); // Fetch full details of the selected item
            return selectedItem;
        } else {
            System.out.println("Invalid selection.");
            return null; // Return null if selection is invalid
        }
    }

    // Method to edit item details (implementation placeholder)
    public void editItemDetails() {
        // Logic for editing item details can be added here
    }
}
