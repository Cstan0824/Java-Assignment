package project.view;

import java.util.ArrayList;

import project.global.ConsoleUI;
import project.global.UserInputHandler;
import project.modules.item.Item;
import project.modules.transaction.AutoReplenishment;
import project.modules.user.User;

public class ViewAutoReplenishment {
    
    private static User user;
    private ArrayList<AutoReplenishment> autoReplenishments = new ArrayList<>();
    private ViewItem viewItem = null;
    //getter 
    public User getUser() {
        return user;
    }

    // Constructor to initialize the user
    public ViewAutoReplenishment(User _user) {
        user = _user;
        this.autoReplenishments = AutoReplenishment.GetAll();
        this.viewItem = new ViewItem(_user);
    }

    public void setAutoReplenishment() {
        this.autoReplenishments = AutoReplenishment.GetAll();
    }
    //Methods
    
    //Menu for Auto Replenishment
    public void menu() {
        boolean exit = true;
        while (exit) {
            ConsoleUI.clearScreen();
            System.out.println("======= Auto Replenishment =======");
            System.out.println("1. View Auto Replenishment");
            System.out.println("2. Add Auto Replenishment");
            System.out.println("3. Update Auto Replenishment");
            System.out.println("4. Delete Auto Replenishment");
            System.out.println("5. Back");
            System.out.println("==================================");

            switch (UserInputHandler.getInteger("Select an option: ", 1, 5)) {
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
        ConsoleUI.clearScreen();
        this.autoReplenishments = AutoReplenishment.GetAll();
        displayAutoReplenishmentDetails();
        ConsoleUI.pause();
    }

    //Method to add Auto Replenishment
    public void addAutoReplenishment() {
        ConsoleUI.clearScreen();
        AutoReplenishment autoReplenishment = new AutoReplenishment();
        Item item = viewItem.selectItemForReplenishment();
        if (item == null) {
            return;
        }
        autoReplenishment.setItem(item);
        autoReplenishment.setItem_Threshold(UserInputHandler.getInteger("Enter threshold level: ", 1, 100000));
        if(autoReplenishment.Add()){
            System.out.println("Auto Replenishment for " + item.getItem_Name() + " has been added successfully.");
            ConsoleUI.pause();
        }else{
            System.out.println("Error adding Auto Replenishment.");
            ConsoleUI.pause();
        }
    }

    //Method to update Auto Replenishment
    public void updateAutoReplenishment() {
        ConsoleUI.clearScreen();
        AutoReplenishment autoReplenishment = selectAutoReplenishmentFromList();
        //Update Threshold level based on selected autoReplenishment object
        autoReplenishment.setItem_Threshold(UserInputHandler.getInteger("Enter new threshold level: ", 1, 100000));
        if(autoReplenishment.Update()){
            System.out.println("Auto Replenishment for " + autoReplenishment.getItem().getItem_Name() + " has been updated successfully.");
            ConsoleUI.pause();
        }else{
            System.out.println("Error updating Auto Replenishment.");
            ConsoleUI.pause();
        }
    }

    //Method to delete Auto Replenishment
    public void deleteAutoReplenishment() {
        ConsoleUI.clearScreen();
        AutoReplenishment autoReplenishment = selectAutoReplenishmentFromList();
        //Delete Auto Replenishment based on selected autoReplenishment object
        if(autoReplenishment.Remove()){
            System.out.println("Auto Replenishment for " + autoReplenishment.getItem().getItem_Name() + " has been deleted successfully.");
            ConsoleUI.pause();
        }else{
            System.out.println("Error deleting Auto Replenishment.");
            ConsoleUI.pause();
        }
    }

    public void displayAutoReplenishmentDetails() {
        //display auto replenishment details
        //print header
        this.autoReplenishments = AutoReplenishment.GetAll();
        System.out.println("================================================= Auto Replenishment Details ==================================================");
        System.out.println(String.format("| %-5s | %-40s | %-50s | %-20s |", "No.", "Item Name",
                "Item Description", "Threshold Level"));
        System.out.println("===============================================================================================================================");
        //print data
        for (int i = 0; i < autoReplenishments.size(); i++) {
            AutoReplenishment autoReplenishment = autoReplenishments.get(i);
            System.out.println(
                    String.format("| %-5s | %-40s | %-50s | %-20s |", (i + 1) + ".",
                            autoReplenishment.getItem().getItem_Name(), autoReplenishment.getItem().getItem_Desc(),
                            autoReplenishment.getItem_Threshold()));
        }

        System.out.println("===============================================================================================================================");
    }
    
    public AutoReplenishment selectAutoReplenishmentFromList() {
        displayAutoReplenishmentDetails();
        int index = UserInputHandler.getInteger("Select an Auto Replenishment: ", 1, autoReplenishments.size()) - 1;
        return autoReplenishments.get(index);
    }
}
