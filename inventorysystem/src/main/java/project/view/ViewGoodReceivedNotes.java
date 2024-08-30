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

    private ViewItem viewItem = null;

    
    public User getUser() {
        return user;
    }
    public void AddGoodsReceivedNotes(String _PoNo, String _GrnNo) {
        String choice;
        do {
            GoodReceivedNotes goodReceivedNote = new GoodReceivedNotes();
            Item item = viewItem.SelectItemFromList();

            System.out.print("Enter Quantity: ");
            goodReceivedNote.setQuantity(scanner.nextInt());
            scanner.nextLine();

            goodReceivedNote.setItem(item);
            goodReceivedNote.setDoc_No(_GrnNo);
            goodReceivedNote.setSource_Doc_No(_PoNo);
            goodReceivedNote.setTransaction_Created_By(user.getUserId());
            goodReceivedNote.setTransaction_Modified_By(user.getUserId());
            goodReceivedNote.setTransaction_Date(new Date());
            goodReceivedNote.setTransaction_Recipient(item.getVendor_ID());
            this.goodReceivedNotes.add(goodReceivedNote);

            //Continue or stop scanner
            System.out.println("Do you want to add more items? (Y/N)");
            choice = scanner.nextLine();
        } while (choice.equalsIgnoreCase("Y"));

        //Save the GRN
        this.goodReceivedNotes.forEach(_goodReceivedNote -> _goodReceivedNote.Add());
    }

    @SuppressWarnings("deprecation")
    public void EditGoodReceivedNotes(Transaction goodReceivedNotes) {

        System.out.println(goodReceivedNotes.toString());
        //allow user to select the GRN field to edit
        System.out.println("Edit Good Received Notes");

        System.out.println("1. Quantity");
        System.out.println("2. Item");
        System.out.println("3. Date");
        System.out.println("4. Back");

        System.out.print("Enter choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                System.out.print("Enter Quantity: ");
                goodReceivedNotes.setQuantity(scanner.nextInt());
                scanner.nextLine();
                break;
            case 2:
                Item item = viewItem.SelectItemFromList();
                goodReceivedNotes.setItem(item);
                break;
            case 3:
                System.out.print("Enter Date: ");
                goodReceivedNotes.setTransaction_Date(new Date(scanner.nextLine()));
                break;
            case 4:
                break;
            default:
                System.out.println("Invalid choice");
                break;
        }

        

    }
    
    public void RemoveGoodReceivedNotes(Transaction goodReceivedNotes) {
        //Remove confirmation

        System.out.println("Are you sure you want to remove this Good Received Notes? (Y/N)");
        String choice = scanner.nextLine();

        if (choice.equalsIgnoreCase("Y")) {
            goodReceivedNotes.Remove();
        }
    }

    public Transaction SelectGoodReceivedNotesFromList(String _PoNo) {
        this.goodReceivedNotes = GoodReceivedNotes.Get( _PoNo,
                GoodReceivedNotes.DocumentType.PURCHASE_ORDER);
        System.out.println(String.format("| %-20s | %-20s | %-20s |", "Item Name", "Received Quantity", "Received Date"));
        goodReceivedNotes.forEach(_goodReceivedNote -> {
            System.out.println(_goodReceivedNote.toString());
        });

        System.out.print("Select Good Received Notes: ");
        int grnID = scanner.nextInt();
        scanner.nextLine();

        return goodReceivedNotes.get(grnID);
    }

    

    public ViewGoodReceivedNotes(User _user) {
        user = _user;
        viewItem = new ViewItem(_user);
    }
}
