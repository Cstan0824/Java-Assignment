package project.view;

import java.util.ArrayList;

import project.global.UserInputHandler;
import project.modules.item.Item;
import project.modules.transaction.AutoReplenishment;
import project.modules.user.User;

public class ViewAutoReplenishment {
    private static User user;
    private ArrayList<AutoReplenishment> autoReplenishments = new ArrayList<>();
    //getter 
    public User getUser() {
        return user;
    }

    // Constructor to initialize the user
    public ViewAutoReplenishment(User _user) {
        user = _user;
        this.autoReplenishments = AutoReplenishment.GetAll();
    }

    public void setAutoReplenishment() {
        this.autoReplenishments = AutoReplenishment.GetAll();
    }

    //Menu for Auto Replenishment
    public void menu() {
        boolean exit = true;
        while (exit) {
            System.out.println("=== Auto Replenishment ===");
            System.out.println("1. View Auto Replenishment");
            System.out.println("2. Add Auto Replenishment");
            System.out.println("3. Update Auto Replenishment");
            System.out.println("4. Delete Auto Replenishment");
            System.out.println("5. Back");
            System.out.println("=============================");

            switch (UserInputHandler.getInteger("Select an option", 1, 5)) {
                case 1:
                    viewAutoReplenishment();
                    break;
                case 2:
                    addAutoReplenishment();
                    break;
                case 3:
                    updateAutoReplenishment();
                    break;
                case 4:
                    deleteAutoReplenishment();
                    break;
                case 5:
                    exit = false;
                    break;
            }
        }
    }

    //Method to view Auto Replenishment
    public void viewAutoReplenishment() {
        System.out.println("View Auto Replenishment");
    }

    //Method to add Auto Replenishment
    public void addAutoReplenishment() {
        System.out.println("Add Auto Replenishment");
    }

    //Method to update Auto Replenishment
    public void updateAutoReplenishment() {
        System.out.println("Update Auto Replenishment");
    }

    //Method to delete Auto Replenishment
    public void deleteAutoReplenishment() {
        ArrayList<Item> items = new ArrayList<>();
        autoReplenishments.forEach(autoReplenishment -> {
            items.add(autoReplenishment.getItem());
        });
        ViewItem viewItem = new ViewItem(user);
        viewItem.setItems(items);
        Item item = viewItem.selectItemFromList();

        AutoReplenishment ar = new AutoReplenishment();
        ar.setItem(item);
        ar.Remove();
    }

    public void displayASutoReplenishmentDetails() {
        //display auto replenishment details


    }
}
