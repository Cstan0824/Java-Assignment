package project.view;

import java.util.ArrayList;

import project.global.ConsoleUI;
import project.global.UserInputHandler;
import project.modules.item.Item;
import project.modules.item.ItemCategory;
import project.modules.user.User;

public class ViewItem {
    
    private static User user;
    private ArrayList<Item> items = new ArrayList<>();
    private final ViewVendor viewVendor;

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
        this.viewVendor = new ViewVendor(user);
    }

    // Getter for User
    public User getUser() {
        return user;
    }

    public void menu() {
        boolean exit = true;
        while (exit) {
            ConsoleUI.clearScreen();
            System.out.println("========= Item Management =========");
            System.out.println("1. Add Item");
            System.out.println("2. View Item");
            System.out.println("3. Update Item");
            System.out.println("4. Delete Item");
            System.out.println("5. View All Items");
            System.out.println("6. Auto Replenishment Management");
            System.out.println("7. Back");
            System.out.println("===================================");

            switch (UserInputHandler.getInteger("Select an option: ", 1, 7)) {
                case 1:
                //only able to add item while adding the vendor
                    Item newItem = addNewItem();
                    this.items.add(newItem);
                    break;
                case 2:
                    searchItem();
                    break;
                case 3:
                    editItemDetails();
                    break;
                case 4:
                    setItems();
                    Item item = selectItemFromList();
                    if (item != null && UserInputHandler.getConfirmation("Are you sure you want to delete this item?")
                            .equalsIgnoreCase("Y")) {      
                        if(item.Remove()){
                            System.out.println("Item deleted successfully.");
                            this.items.remove(item);
                            ConsoleUI.pause();
                        } else {
                            System.out.println("Error deleting item.");
                            ConsoleUI.pause();
                        }
                    }
                    break;
                case 5:
                    setItems();
                    displayItemDetails();
                    ConsoleUI.pause();
                    break;
                case 6:
                    ViewAutoReplenishment viewAutoReplenishment = new ViewAutoReplenishment(user);
                    viewAutoReplenishment.menu();
                    break;
                case 7:
                    exit = false;
                    break;
            }
        }
    }

    private void displayItemDetails() {
        // Display the list of items
        System.out.println(
                " ========================================================== Items ================================================================ ");
        System.out.println(
                String.format("| %-5s | %-20s | %-20s | %-20s | %-50s |", "No.", "Item Category", "Vendor", "Item Name",
                        "Item Description"));
        System.out.println(
                " ================================================================================================================================= ");
        for (int i = 0; i < items.size(); i++) {
            System.out.println(String.format("| %-5s %-110s", (i + 1) + ". ", items.get(i))); // Display items with index
        }
        System.out.println(
                " ================================================================================================================================= ");
    }

    // Method to select an item from the list
    public Item selectItemFromList() {
        // Fetch all items
        setItems();
        if (this.items.isEmpty()) {
            System.out.println("No items available.");
            ConsoleUI.pause();
            return null; // Return null if no items are available
        }

        displayItemDetails();
        Item selectedItem = items
                .get(UserInputHandler.getInteger("Select Item by No: ", 1, items.size()) - 1);
        selectedItem.Get();
        selectedItem.getItemCategory().Get();
        return selectedItem;
    }

    public Item selectItemForReplenishment() {

        this.items = Item.GetAllForNewReplenishment();
        if (this.items == null || this.items.isEmpty()) {
            System.out.println("All items have auto replenishment.");
            ConsoleUI.pause();
            return null;
        }

        displayItemDetails();
        Item selectedItem = items
                .get(UserInputHandler.getInteger("Select Item by No: ", 1, items.size()) - 1);
        selectedItem.Get();
        selectedItem.getItemCategory().Get();
        return selectedItem;
    }

    // Method to edit item details (implementation placeholder)
    private void editItemDetails() {
        ConsoleUI.clearScreen();
        // Select an item from the list
        Item item = selectItemFromList();
        if (item == null) {
            return;
        }

        
        

        //Select details to edit from menu
        boolean exit = false;
        while (!exit) {
            //display current item details - in format
            System.out.println("\n");
            System.out.println(" =================== Current Item Details =================== ");
            System.out.println(" Item Category: " + item.getItemCategory().getItem_Type());
            System.out.println(" Vendor: " + item.getVendor().getVendor_Name());
            System.out.println(" Item Name: " + item.getItem_Name());
            System.out.println(" Item Description: " + item.getItem_Desc());
            System.out.println(" Item Price: " + item.getItem_Price());
            System.out.println(" ============================================================ \n");


            System.out.println("========== Edit Item ==========");
            System.out.println("1. Edit Item Category");
            System.out.println("2. Edit Vendor ID");
            System.out.println("3. Edit Item Name");
            System.out.println("4. Edit Item Description");
            System.out.println("5. Edit Item Price");
            System.out.println("6. Back to Item Management");
            System.out.println("===============================");

            switch (UserInputHandler.getInteger("Select an option: ", 1, 6)) {
                case 1:
                    ConsoleUI.clearScreen();
                    item.getItemCategory().setItem_Category_ID(selectItemCategoryFromList().getItem_Category_ID());
                    // Update the item details
                    if (item.Update()) {
                        System.out.println("Item updated successfully.");
                        ConsoleUI.pause();
                        ConsoleUI.clearScreen();
                    } else {
                        System.out.println("Error updating item.");
                        ConsoleUI.pause();
                        ConsoleUI.clearScreen();
                    }
                    break;
                case 2:
                    ConsoleUI.clearScreen();
                    this.viewVendor.getVendors().remove(item.getVendor());
                    item.getVendor().setVendor_ID(this.viewVendor.selectVendorFromList().getVendor_ID());
                    // Update the item details
                    if (item.Update()) {
                        System.out.println("Item updated successfully.");
                        ConsoleUI.pause();
                        ConsoleUI.clearScreen();
                    } else {
                        System.out.println("Error updating item.");
                        ConsoleUI.pause();
                        ConsoleUI.clearScreen();
                    }
                    break;
                case 3:
                    ConsoleUI.clearScreen();
                    item.setItem_name(UserInputHandler.getString("Enter Item Name: ", 1, 50));
                    // Update the item details
                    if (item.Update()) {
                        System.out.println("Item updated successfully.");
                        ConsoleUI.pause();
                        ConsoleUI.clearScreen();
                    } else {
                        System.out.println("Error updating item.");
                        ConsoleUI.pause();
                        ConsoleUI.clearScreen();
                    }
                    break;
                case 4:
                    ConsoleUI.clearScreen(); 
                    item.setItem_Desc(UserInputHandler.getString("Enter Item Description: ", 1, 100));
                    // Update the item details
                    if (item.Update()) {
                        System.out.println("Item updated successfully.");
                        ConsoleUI.pause();
                        ConsoleUI.clearScreen();
                    } else {
                        System.out.println("Error updating item.");
                        ConsoleUI.pause();
                        ConsoleUI.clearScreen();
                    }
                    break;
                case 5:
                    ConsoleUI.clearScreen();
                    item.setItem_Price(UserInputHandler.getDouble("Enter Item Price: ", 0, 1000000));
                    // Update the item details
                    if (item.Update()) {
                        System.out.println("Item updated successfully.");
                        ConsoleUI.pause();
                        ConsoleUI.clearScreen();
                    } else {
                        System.out.println("Error updating item.");
                        ConsoleUI.pause();
                        ConsoleUI.clearScreen();
                    }
                    break;
                case 6:
                    exit = true;
                    break;
            }
            
        }
    }

    private void searchItem() {
        // Search for items based on the search field and value with a menu
        ConsoleUI.clearScreen();
        System.out.println("========== Search Item ==========");
        System.out.println("1. Search by Item Name");
        System.out.println("2. Search by Item Category");
        System.out.println("3. Search by Vendor");
        System.out.println("4. Back to Item Management");
        System.out.println("=================================");

        switch (UserInputHandler.getInteger("Select an option: ", 1, 4)) {
            case 1:
                this.items = Item.Get("Item_Name", UserInputHandler.getString("\nEnter Item Name: ",  1,50));

                if (this.items == null || this.items.isEmpty()) {
                    System.out.println("No items found.");
                    ConsoleUI.pause();
                    break;
                }
                displayItemDetails();
                ConsoleUI.pause();
                break;
            case 2:
                this.items = Item.Get("Item_Category_ID", selectItemCategoryFromList().getItem_Category_ID() + "");
                if (this.items == null || this.items.isEmpty()) {
                    System.out.println("No items found.");
                    ConsoleUI.pause();
                    break;
                }
                displayItemDetails();
                ConsoleUI.pause();
                break;
            case 3:
                this.viewVendor.setVendors();
                this.items = Item.Get("Vendor_ID", this.viewVendor.selectVendorFromList().getVendor_ID());
                if (this.items == null || this.items.isEmpty()) {
                    System.out.println("No items found.");
                    ConsoleUI.pause();
                    break;
                }
                displayItemDetails();
                ConsoleUI.pause();
                break;
            case 4:
                System.out.println("Exiting...");
                break;
        }
    }

    private Item addNewItem() {
        // Add a new item to the list
        Item item = new Item();
        this.viewVendor.setVendors();
        //display Item category on menu
        item.setItemCategory(selectItemCategoryFromList());
        
        this.viewVendor.setVendors();
        item.setVendor(this.viewVendor.selectVendorFromList());
        item.setItem_name(UserInputHandler.getString("Enter Item Name: ", 1, 50));
        item.setItem_Desc(UserInputHandler.getString("Enter Item Description: ", 1, 100));
        item.setItem_Quantity(UserInputHandler.getInteger("Enter Item Quantity: ", 1, 1000000));
        item.setItem_Price(UserInputHandler.getDouble("Enter Item Price: ", 0, 1000000));
        item.setItem_Created_By(user.getUserId());
        item.setItem_Modified_By(user.getUserId());
        if(item.Add()) {
            System.out.println("Item added successfully.");
            return item;
        } else {
            System.out.println("Error adding item.");
            return null;
        }
    }

    public ItemCategory selectItemCategoryFromList() {
        ArrayList<ItemCategory> itemCategories = ItemCategory.GetAll();
        
        System.out.println(" ================== Item Category ================== ");
        System.out.println(String.format("| %-5s | %-40s |", "No.", "Item Category"));
        System.out.println(" =================================================== ");
        for (int i = 0; i < itemCategories.size(); i++) {
            System.out.println(String.format("| %-5s | %-40s |", (i + 1) + ". ", itemCategories.get(i)));
        }

        System.out.println(" =================================================== ");

        return itemCategories
                .get(UserInputHandler.getInteger("Select Item Category by No: ", 1, itemCategories.size()) - 1);
    }
}

