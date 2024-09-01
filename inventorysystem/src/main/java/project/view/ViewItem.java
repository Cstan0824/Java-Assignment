package project.view;

import java.util.ArrayList;

import project.global.UserInputHandler;
import project.modules.item.Item;
import project.modules.user.User;

public class ViewItem {
    private static User user;
    private ArrayList<Item> items = new ArrayList<>();

    //getter
    public ArrayList<Item> getItems() {
        return this.items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public void setItems() {
        this.items = Item.GetAll();
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
        System.out.println(
                " ===================================================== Items =========================================================== ");
        System.out.println(
                String.format("| %-5s | %-15s | %-15s | %-20s | %-50s |", "No.", "Item Category", "Vendor", "Item Name",
                        "Item Description"));
        System.out.println(
                " ======================================================================================================================= ");
        for (int i = 0; i < items.size(); i++) {
            System.out.println(String.format("| %-5s %-100s", (i + 1) + ". ", items.get(i))); // Display items with index
        }
        System.out.println(
                " ======================================================================================================================= ");
        Item selectedItem = items
                .get(UserInputHandler.getInteger("Select Item by No", 1, items.size()) - 1);
        selectedItem.Get();
        return selectedItem;
    }

    // Method to edit item details (implementation placeholder)
    public void editItemDetails() {
        // Logic for editing item details can be added here
    }
}
