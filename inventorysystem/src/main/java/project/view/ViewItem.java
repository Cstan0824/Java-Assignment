package project.view;

import java.util.ArrayList;
import java.util.Scanner;

import project.modules.item.Item;
import project.modules.user.User;

public class ViewItem {
    private static User user;
    private static final Scanner scanner = new Scanner(System.in);

    //getter
    public User getUser() {
        return user;
    }

    public Item SelectItemFromList() {
        ArrayList<Item> items = Item.GetAll();
        System.out.println(String.format("| %-20s | %-20s | %-20s |", "Item ID", "Item Name", "Item Description"));
        items.forEach(_item -> {
            System.out.println(_item.toString());
        });

        System.out.print("Select Item: ");
        int itemID = scanner.nextInt();
        scanner.nextLine();

        Item item = new Item(itemID);
        item.Get();
        
        return item;
    }

    public ViewItem(User _user) {
        user = _user;
    }
}
