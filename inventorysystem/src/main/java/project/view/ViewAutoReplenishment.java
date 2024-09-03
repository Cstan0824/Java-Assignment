package project.view;

import java.util.ArrayList;

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
        displayASutoReplenishmentDetails();
        UserInputHandler.systemPause("Press any key to continue...");
    }

    //Method to add Auto Replenishment
    public void addAutoReplenishment() {
        AutoReplenishment autoReplenishment = new AutoReplenishment();
        this.viewItem.setItems();
        Item item = viewItem.selectItemFromList();
        if (item == null) {
            return;
        }
        autoReplenishment.setItem(item);
        autoReplenishment.setItem_Threshold(UserInputHandler.getInteger("Enter threshold level", 1, 100000));
        autoReplenishment.Add();
    }

    //Method to update Auto Replenishment
    public void updateAutoReplenishment() {
        AutoReplenishment autoReplenishment = selectAutoReplenishmentFromList();
        //Update Threshold level
        autoReplenishment.setItem_Threshold(UserInputHandler.getInteger("Enter new threshold level", 1, 100000));
    
        autoReplenishment.Update();
    }

    //Method to delete Auto Replenishment
    public void deleteAutoReplenishment() {
        ArrayList<Item> items = new ArrayList<>();
        autoReplenishments.forEach(autoReplenishment -> {
            items.add(autoReplenishment.getItem());
        });
        
        this.viewItem.setItems(items);
        Item item = viewItem.selectItemFromList();

        AutoReplenishment ar = new AutoReplenishment();
        ar.setItem(item);
        ar.Remove();
    }

    public void displayASutoReplenishmentDetails() {
        //display auto replenishment details
        //print header
        System.out.println("===============Auto Replenishment Details================");
        System.out.println(String.format("| %-5s | %-20s | %-20s | %-50s | %-20s |", "No.", "Item ID", "Item Name",
                "Item Description", "Threshold Level"));
        System.out.println("=========================================================");

        //print data
        for (int i = 0; i < autoReplenishments.size(); i++) {
            AutoReplenishment autoReplenishment = autoReplenishments.get(i);
            System.out.println(
                    String.format("| %-5s | %-20s | %-20s | %-50s | %-20s |", (i + 1) + ".",
                            autoReplenishment.getItem().getItem_ID(),
                            autoReplenishment.getItem().getItem_Name(), autoReplenishment.getItem().getItem_Desc(),
                            autoReplenishment.getItem_Threshold()));
        }

        System.out.println("=========================================================");
    }
    
    public AutoReplenishment selectAutoReplenishmentFromList() {
        displayASutoReplenishmentDetails();
        int index = UserInputHandler.getInteger("Select an Auto Replenishment", 1, autoReplenishments.size()) - 1;
        return autoReplenishments.get(index);
    }
}
