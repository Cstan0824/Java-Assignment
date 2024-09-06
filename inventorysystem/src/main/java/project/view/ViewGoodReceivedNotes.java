package project.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import project.global.UserInputHandler;
import project.modules.item.Item;
import project.modules.transaction.GoodReceivedNotes;
import project.modules.transaction.PurchaseOrder;
import project.modules.transaction.Transaction;
import project.modules.user.User;

public class ViewGoodReceivedNotes {
    private static final Scanner sc = new Scanner(System.in);
    private static User user;

    private ArrayList<GoodReceivedNotes> goodReceivedNotes = new ArrayList<>();
    private ArrayList<PurchaseOrder> purchaseOrders = new ArrayList<>();
    private final ViewItem viewItem;
    private final ArrayList<Item> items = new ArrayList<>();

    // Constructor to initialize user and ViewItem
    public ViewGoodReceivedNotes(User _user) {
        user = _user;
        viewItem = new ViewItem(_user);
    }

    // Getter for User
    public User getUser() {
        return user;
    }

    public ArrayList<Item> getItems() {
        return this.items;
    }

    // Method to add Goods Received Notes
    public void addGoodsReceivedNotes(String poNo, String grnNo) {

        HashMap<Item, Integer> OnHandStocks = new HashMap<>();
        this.items.clear();

        this.purchaseOrders = PurchaseOrder.Get(poNo);

        //get items and OnhandStock from purchaseOrders
        for (Transaction purchaseOrder : this.purchaseOrders) {
            purchaseOrder.getItem().Get();

            this.goodReceivedNotes = GoodReceivedNotes.Get(purchaseOrder.getItem(), poNo);
            int OnHandStock = 0;
            if (goodReceivedNotes == null || goodReceivedNotes.isEmpty()) {
                this.items.add(purchaseOrder.getItem());
                break;
            }
            for (GoodReceivedNotes grn : goodReceivedNotes) {
                OnHandStock += grn.getQuantity();
            }

            if (OnHandStock == purchaseOrder.getQuantity()) {
                break;
            }
            //Add to the list
            OnHandStocks.put(purchaseOrder.getItem(), OnHandStock);
            this.items.add(purchaseOrder.getItem());
        }

        if (this.purchaseOrders.isEmpty()) {
            System.out.println("No Purchase Orders available.");
            return;
        }
        if (this.goodReceivedNotes != null && !this.goodReceivedNotes.isEmpty()) {
            this.goodReceivedNotes.clear();
        } else {
            this.goodReceivedNotes = new ArrayList<>();
        }
        do {
            GoodReceivedNotes goodReceivedNote = new GoodReceivedNotes();
            viewItem.setItems(items);
            Item item = viewItem.selectItemFromList(); //display item from PO
            if (item == null) {
                System.out.println("Item not selected.");
                return;
            }
            
            // Get Virtual Stock
            Transaction purchaseOrder = new PurchaseOrder(item, poNo);
            purchaseOrder.Get();
            purchaseOrder.setItem(item); //idk why need this line but it works

            int maxQuantity = purchaseOrder.getQuantity()
                    - OnHandStocks.getOrDefault(purchaseOrder.getItem(), purchaseOrder.getQuantity());

            if (maxQuantity == 0) {
                maxQuantity = purchaseOrder.getQuantity();
            }

            //Set the GRN details
            goodReceivedNote.setQuantity(
                    UserInputHandler.validateInteger(sc, "Enter Quantity", 1,
                            maxQuantity));
            goodReceivedNote.setItem(item);
            goodReceivedNote.setDoc_No(grnNo);
            goodReceivedNote.setSource_Doc_No(poNo);
            goodReceivedNote.setTransaction_Created_By(user.getUserId());
            goodReceivedNote.setTransaction_Modified_By(user.getUserId());
            goodReceivedNote.setTransaction_Date(new Date());
            goodReceivedNote.setTransaction_Recipient(item.getVendor().getVendor_ID());

            // Add to the list
            this.goodReceivedNotes.add(goodReceivedNote);

            this.items.remove(item); //remove item from list once selected
            
            if (this.items.isEmpty()) {
                break;
            }
        } while (UserInputHandler.getConfirmation("Do you want to add more items?")
                .equalsIgnoreCase("Y"));

        // Save the GRN to the database
        this.goodReceivedNotes.forEach(_goodReceivedNote -> {
            _goodReceivedNote.Add();
        });
    }

    // Method to edit Goods Received Notes
    public void editGoodReceivedNotes(Transaction goodReceivedNote) {
        int OnHandStock = 0;
        Transaction purchaseOrder = new PurchaseOrder(goodReceivedNote.getItem(),
                goodReceivedNote.getSource_Doc_No());
        purchaseOrder.Get();
        this.goodReceivedNotes = GoodReceivedNotes.Get(purchaseOrder.getItem(),
                goodReceivedNote.getSource_Doc_No());
        
        if (goodReceivedNotes == null || goodReceivedNotes.isEmpty()) {
            return;
        }
        //Get On hand stock
        for (GoodReceivedNotes grn : goodReceivedNotes) {
            OnHandStock += grn.getQuantity();
        }

        goodReceivedNote.setQuantity(
                UserInputHandler.validateInteger(sc, "Enter Quantity", 1,
                        purchaseOrder.getQuantity() - (OnHandStock - goodReceivedNote.getQuantity())));
        goodReceivedNote.setTransaction_Modified_By(user.getUserId());

        //Update
        if (!goodReceivedNote.Update()) {
            System.out.println("Failed to update Good Received Notes.");
            return;
        }
        System.out.println("Good Received Notes updated successfully.");
        
    }

    // Method to remove Goods Received Notes
    public void removeGoodsReceivedNotes(Transaction goodReceivedNote) {
        if (!UserInputHandler.getConfirmation("Are you sure you want to remove this Good Received Notes?")
                .equalsIgnoreCase("Y")) {
            return;
        }
        GoodReceivedNotes.Get(goodReceivedNote.getDoc_No(), GoodReceivedNotes.DocumentType.GOOD_RECEIVED_NOTES)
                    .forEach(grn -> {
                        if (grn != null) {
                            grn.Remove();
                        }
                    });
    }

    // Method to select a Goods Received Note from the list
    public Transaction selectGoodReceivedNotesFromList(String poNo) {
        Set<String> displayedGRN = new HashSet<>();
        this.goodReceivedNotes = GoodReceivedNotes.Get(poNo, GoodReceivedNotes.DocumentType.PURCHASE_ORDER);
        if(this.goodReceivedNotes == null || this.goodReceivedNotes.isEmpty()) {
            return null;
        }
        System.out.println(
                " =================================== Good Received Notes ============================================ ");

        System.out.println(String.format("| %-5s | %-20s | %-20s | %-20s | %-20s |","No.","Received Notes", "Item Name",
                "Item Price", "Received Quantity"));
        System.out.println(
                " ===================================================================================================== ");

        for (int i = 0; i < goodReceivedNotes.size(); i++) {
            goodReceivedNotes.get(i).getItem().Get();
            //Make sure the Doc no will not displayed repeatly
            if (!displayedGRN.add(goodReceivedNotes.get(i).getDoc_No())) {
                System.out.println(String.format("| %-20s | %-20s | %-20s | %-20s |",
                        "",
                        goodReceivedNotes.get(i).getItem().getItem_Name(),
                        goodReceivedNotes.get(i).getItem().getItem_Price(),
                        goodReceivedNotes.get(i).getQuantity()));
            } else {
                System.out.println(String.format("| %-5s | %-20s | %-20s | %-20s | %-20s |",
                        (i + 1) + ". ",
                        goodReceivedNotes.get(i).getDoc_No(),
                        goodReceivedNotes.get(i).getItem().getItem_Name(),
                        goodReceivedNotes.get(i).getItem().getItem_Price(),
                        goodReceivedNotes.get(i).getQuantity()));
            }
        }
        System.out.println(
                " ===================================================================================================== ");

        return goodReceivedNotes.get(UserInputHandler.validateInteger(sc, "Select Good Received Notes", 1,
                goodReceivedNotes.size()) - 1);
    }
}