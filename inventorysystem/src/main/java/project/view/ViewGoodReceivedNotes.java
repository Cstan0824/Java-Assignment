package project.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import project.modules.item.Item;
import project.modules.transaction.GoodReceivedNotes;
import project.modules.transaction.Transaction;
import project.modules.user.User;

public class ViewGoodReceivedNotes {
    private static User user;
    private static final Scanner scanner = new Scanner(System.in);

    private ArrayList<GoodReceivedNotes> goodReceivedNotes = new ArrayList<>();
    private final ViewItem viewItem;

    // Constructor to initialize user and ViewItem
    public ViewGoodReceivedNotes(User _user) {
        user = _user;
        viewItem = new ViewItem(_user);
    }

    // Getter for User
    public User getUser() {
        return user;
    }

    // Method to add Goods Received Notes
    public void addGoodsReceivedNotes(String poNo, String grnNo) {
        goodReceivedNotes.clear();
        
        String choice;
        do {
            GoodReceivedNotes goodReceivedNote = new GoodReceivedNotes();
            
            Item item = viewItem.selectItemFromList(); //display item from PO
            if(item == null) {
                System.out.println("Item not selected.");
                return;
            }

            // Get Quantity
            System.out.print("Enter Quantity: ");
            goodReceivedNote.setQuantity(scanner.nextInt());
            scanner.nextLine();

            // Set the remaining properties
            goodReceivedNote.setItem(item);
            goodReceivedNote.setDoc_No(grnNo);
            goodReceivedNote.setSource_Doc_No(poNo);
            goodReceivedNote.setTransaction_Created_By(user.getUserId());
            goodReceivedNote.setTransaction_Modified_By(user.getUserId());
            goodReceivedNote.setTransaction_Date(new Date());
            goodReceivedNote.setTransaction_Recipient(item.getVendor_ID());

            // Add to the list
            this.goodReceivedNotes.add(goodReceivedNote);
            

            // Prompt user to add more items
            System.out.print("Do you want to add more items? (Y/N): ");
            viewItem.getItems().remove(item);
            choice = scanner.nextLine();
        } while (choice.equalsIgnoreCase("Y"));

        // Save the GRN to the database
        this.goodReceivedNotes.forEach(grn -> {
            if (grn != null) {
                System.out.println(grn.Add());
            } else {
                System.out.println("GoodReceivedNotes object in the list is null.");
            }
        });
    }

    // Method to edit Goods Received Notes
    public void editGoodReceivedNotes(Transaction goodReceivedNote) {
        System.out.println("Edit Good Received Notes");
        // Allow user to select the field to edit
        System.out.println("1. Quantity");
        System.out.println("2. Item");
        System.out.println("3. Back");
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                System.out.print("Enter Quantity: ");
                goodReceivedNote.setQuantity(scanner.nextInt());
                scanner.nextLine(); // Consume newline
                goodReceivedNote.setTransaction_Modified_By(user.getUserId());
                break;
            case 2:
                Item item = viewItem.selectItemFromList();
                goodReceivedNote.setItem(item);
                goodReceivedNote.setTransaction_Recipient(item.getVendor_ID());
                goodReceivedNote.setTransaction_Modified_By(user.getUserId());
                break;
            case 3:
                return;
            default:
                System.out.println("Invalid choice");
                break;
        }
        if (goodReceivedNote.Update()) {
            System.out.println("Good Received Notes updated successfully.");
        } else {
            System.out.println("Failed to update Good Received Notes.");
        }
        
    }

    // Method to remove Goods Received Notes
    public void removeGoodsReceivedNotes(Transaction goodReceivedNote) {
        // Confirmation for removal
        System.out.print("Are you sure you want to remove this Good Received Notes? (Y/N): ");
        String choice = scanner.nextLine();

        if (choice.equalsIgnoreCase("Y")) {
            GoodReceivedNotes.Get(goodReceivedNote.getDoc_No(), GoodReceivedNotes.DocumentType.GOOD_RECEIVED_NOTES)
                    .forEach(grn -> {
                        if (grn != null) {
                            grn.Remove();
                        }
                    });
        }
    }

    // Method to select a Goods Received Note from the list
    public Transaction selectGoodReceivedNotesFromList(String poNo) {
        this.goodReceivedNotes = GoodReceivedNotes.Get(poNo, GoodReceivedNotes.DocumentType.PURCHASE_ORDER);

        System.out.println(String.format("| %-20s | %-20s | %-20s |", "Item Name", "Received Quantity", "Received Date"));
        for (int i = 0; i < goodReceivedNotes.size(); i++) {
            System.out.println(i + ": " + goodReceivedNotes.get(i));
        }

        System.out.print("Select Good Received Notes (enter the number): ");
        int grnIndex = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Ensure the selected index is valid
        if (grnIndex >= 0 && grnIndex < goodReceivedNotes.size()) {
            return goodReceivedNotes.get(grnIndex);
        } else {
            System.out.println("Invalid selection.");
            return null;
        }
    }
}
