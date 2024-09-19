package project.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import project.global.UserInputHandler;
import project.modules.item.Item;
import project.modules.transaction.GoodReceivedNotes;
import project.modules.transaction.PurchaseOrder;
import project.modules.transaction.Transaction;
import project.modules.user.User;

public class ViewGoodReceivedNotes {
    
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
    public void addGoodsReceivedNotes(String _poNo, String _grnNo) {

        HashMap<Item, Integer> OnHandStocks = new HashMap<>();
        this.items.clear();

        this.purchaseOrders = PurchaseOrder.Get(_poNo);

        //get items and OnhandStock from purchaseOrders
        //calculate the On hand stock
        for (Transaction purchaseOrder : this.purchaseOrders) {
            purchaseOrder.getItem().Get();
            this.goodReceivedNotes = GoodReceivedNotes.Get(purchaseOrder.getItem(), _poNo);
            int OnHandStock = 0;
            if (goodReceivedNotes == null || goodReceivedNotes.isEmpty()) {
                this.items.add(purchaseOrder.getItem());
                continue;
            }
            for (GoodReceivedNotes grn : goodReceivedNotes) {
                OnHandStock += grn.getQuantity();
            }
            //Check if the On Hand Stock is equal to the quantity of the purchase order - order status
            if (OnHandStock == purchaseOrder.getQuantity()) {
                continue;
            }
            //Add to the list of On Hand Stocks
            OnHandStocks.put(purchaseOrder.getItem(), OnHandStock);
            this.items.add(purchaseOrder.getItem());
        }
        //Check if there is no purchase order
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
            Transaction purchaseOrder = new PurchaseOrder(item, _poNo);
            purchaseOrder.Get();
            purchaseOrder.setItem(item); //idk why need this line but it works

            int maxQuantity = purchaseOrder.getQuantity()
                    - OnHandStocks.getOrDefault(purchaseOrder.getItem(), purchaseOrder.getQuantity());

            if (maxQuantity == 0) {
                maxQuantity = purchaseOrder.getQuantity();
            }

            //Set the GRN details
            goodReceivedNote.setQuantity(
                    UserInputHandler.getInteger("Enter Quantity: ", 1,
                            maxQuantity));
            goodReceivedNote.setItem(item);
            goodReceivedNote.setDoc_No(_grnNo);
            goodReceivedNote.setSource_Doc_No(_poNo);
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
    public void editGoodReceivedNotes(Transaction _goodReceivedNote) {
        int OnHandStock = 0;
        Transaction purchaseOrder = new PurchaseOrder(_goodReceivedNote.getItem(),
                _goodReceivedNote.getSource_Doc_No());
        purchaseOrder.Get();
        this.goodReceivedNotes = GoodReceivedNotes.Get(purchaseOrder.getItem(),
                _goodReceivedNote.getSource_Doc_No());
        
        if (goodReceivedNotes == null || goodReceivedNotes.isEmpty()) {
            return;
        }
        //Get On hand stock
        for (GoodReceivedNotes grn : goodReceivedNotes) {
            OnHandStock += grn.getQuantity();
        }
        //Set the new Quantity based on the purchase order quantity 
        //Range from 1 to the difference of the purchase order quantity and the On Hand Stock
        _goodReceivedNote.setQuantity(
                UserInputHandler.getInteger("Enter Quantity: ", 1,
                        purchaseOrder.getQuantity() - (OnHandStock - _goodReceivedNote.getQuantity())));
        _goodReceivedNote.setTransaction_Modified_By(user.getUserId());

        //Update
        if (!_goodReceivedNote.Update()) {
            System.out.println("Failed to update Good Received Notes.");
            return;
        }
        System.out.println("Good Received Notes updated successfully.");
        
    }

    // Method to remove Goods Received Notes
    public void removeGoodsReceivedNotes(Transaction _goodReceivedNote) {
        if (!UserInputHandler.getConfirmation("Are you sure you want to remove this Good Received Notes?")
                .equalsIgnoreCase("Y")) {
            return;
        }
        //Remove the GRN from database based on PO no
        GoodReceivedNotes.Get(_goodReceivedNote.getDoc_No(), GoodReceivedNotes.DocumentType.GOOD_RECEIVED_NOTES)
                    .forEach(grn -> {
                        if (grn != null) {
                            grn.Remove();
                        }
                    });
    }

    // Method to select a Goods Received Note from the list
    public Transaction selectGoodReceivedNotesFromList(String _poNo) {
        this.goodReceivedNotes = GoodReceivedNotes.Get(_poNo, GoodReceivedNotes.DocumentType.PURCHASE_ORDER);
        if (this.goodReceivedNotes == null || this.goodReceivedNotes.isEmpty()) {
            return null;
        }
        System.out.println(
                " =================================== Good Received Notes =========================================== ");

        System.out
                .println(String.format("| %-5s | %-20s | %-20s | %-20s | %-20s |", "No.", "Received Notes", "Item Name",
                        "Item Price", "Received Quantity"));
        System.out.println(
                " ================================================================================================== ");

        for (int i = 0; i < goodReceivedNotes.size(); i++) {
            goodReceivedNotes.get(i).getItem().Get();
            //Make sure the Doc no will not displayed repeatly

            System.out.println(String.format("| %-5s | %-20s | %-20s | %-20s | %-20s |",
                    (i + 1) + ". ",
                    goodReceivedNotes.get(i).getDoc_No(),
                    goodReceivedNotes.get(i).getItem().getItem_Name(),
                    goodReceivedNotes.get(i).getItem().getItem_Price(),
                    goodReceivedNotes.get(i).getQuantity()));

        }
        System.out.println(
                " ================================================================================================== ");

        return goodReceivedNotes.get(UserInputHandler.getInteger("Select Good Received Notes: ", 1,
                goodReceivedNotes.size()) - 1);
    }
}