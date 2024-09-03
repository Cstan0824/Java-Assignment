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

    public void menu() {
        boolean exit = true;
        while (exit) {
            System.out.println("=== Item Management ===");
            System.out.println("1. Add Item");
            System.out.println("2. View Item");
            System.out.println("3. Update Item");
            System.out.println("4. Delete Item");
            System.out.println("5. View All Items");
            System.out.println("6. Back");
            System.out.println("=============================");

            switch (UserInputHandler.getInteger("Select an option", 1, 6)) {
                case 1:
                    Item newItem = addNewItem();
                    this.items.add(newItem);
                    break;
                case 2:
                    searchItem();
                    displayItemDetails();
                    break;
                case 3:
                    editItemDetails();
                    break;
                case 4:
                    setItems();
                    Item item = selectItemFromList();
                    if (item != null && UserInputHandler.getConfirmation("Are you sure you want to delete this item?")
                            .equalsIgnoreCase("Y")) {
                        item.Remove();
                    }
                    break;
                case 5:
                    setItems();
                    displayItemDetails();
                    UserInputHandler.systemPause("Press any key to continue...");
                    break;
                case 6:
                    exit = false;
                    break;
            }
        }
    }

    private void displayItemDetails() {
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
    }

    // Method to select an item from the list
    public Item selectItemFromList() {
        // Fetch all items
        if (this.items.isEmpty()) {
            System.out.println("No items available.");
            return null; // Return null if no items are available
        }

        displayItemDetails();
        Item selectedItem = items
                .get(UserInputHandler.getInteger("Select Item by No", 1, items.size()) - 1);
        selectedItem.Get();
        return selectedItem;
    }

    // Method to edit item details (implementation placeholder)
    public void editItemDetails() {
        // Select an item from the list
        Item item = selectItemFromList();
        if (item == null) {
            return;
        }

        // Display the item details
        System.out.println("Item Details:");
        System.out.println(item);

        //Select details to edit from menu
        System.out.println("========== Edit Item ==========");
        System.out.println("1. Edit Item Category");
        System.out.println("2. Edit Vendor ID");
        System.out.println("3. Edit Item Name");
        System.out.println("4. Edit Item Description");
        System.out.println("5. Edit Item Price");
        System.out.println("6. Back to Item Management");
        System.out.println("===============================");


        switch (UserInputHandler.getInteger("Select an option", 1, 6)) {
            case 1:
                //display Item category on menu
                item.setItem_Category_ID(UserInputHandler.getInteger("Enter Item Category", 1, 100));
                break;
            case 2:
                item.setVendor_ID(UserInputHandler.getString("Enter Vendor ID", 1, ".*"));
                break;
            case 3:
                item.setItem_name(UserInputHandler.getString("Enter Item Name", 1, ".*"));
                break;
            case 4:
                item.setItem_Desc(UserInputHandler.getString("Enter Item Description", 1, ".*"));
                break;
            case 5:
                item.setItem_Price(UserInputHandler.getDouble("Enter Item Price", 0, 1000000));
                break;
            case 6:
                System.out.println("Exiting...");
                break;
        }
        // Update the item details
        item.Update();
    }

    public void searchItem() {
        // Search for items based on the search field and value with a menu
        System.out.println("======== Search Item ========");
        System.out.println("1. Search by Item Name");
        System.out.println("2. Search by Item Category");
        System.out.println("3. Search by Vendor ID");
        System.out.println("4. Back to Item Management");
        System.out.println("=============================");

        switch (UserInputHandler.getInteger("Select an option", 1, 4)) {
            case 1:
                ;
                this.items = Item.Get("Item_Name", UserInputHandler.getString("Enter Item Name", 1, ".*"));
                break;
            case 2:
                //display Item category on menu
                this.items = Item.Get("Item_Category_ID", UserInputHandler.getString("Enter Item Category", 1, ".*"));
                break;
            case 3:
                this.items = Item.Get("Vendor_ID", UserInputHandler.getString("Enter Vendor ID", 1, ".*"));
                break;
            case 4:
                System.out.println("Exiting...");
                break;
        }
    }

    public static Item addNewItem() {
        // Add a new item to the list
        Item item = new Item();
        //display Item category on menu
        item.setItem_Category_ID(UserInputHandler.getInteger("Enter Item Category", 1, 100));
        item.setVendor_ID(UserInputHandler.getString("Enter Vendor ID", 1, ".*"));
        item.setItem_name(UserInputHandler.getString("Enter Item Name", 1, ".*"));
        item.setItem_Desc(UserInputHandler.getString("Enter Item Description", 1, ".*"));
        item.setItem_Quantity(UserInputHandler.getInteger("Enter Item Quantity", 1, 1000000));
        item.setItem_Price(UserInputHandler.getDouble("Enter Item Price", 0, 1000000));
        item.setItem_Created_By(user.getUserId());
        item.setItem_Modified_By(user.getUserId());
        item.Add();
        return item;
    }
}

