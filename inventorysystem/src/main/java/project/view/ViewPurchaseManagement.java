package project.view;

import java.util.ArrayList;
import java.util.Scanner;

import project.modules.transaction.GoodReceivedNotes;
import project.modules.transaction.PurchaseOrder;
import project.modules.transaction.Transaction;
import project.modules.user.User;

public class ViewPurchaseManagement {
    private final Scanner scanner = new Scanner(System.in);

    private static User user = null;

    private ViewGoodReceivedNotes viewGoodReceivedNotes = null;
    private ViewPurchaseOrder viewPurchaseOrder = null;

    public enum StockStatus {
        PENDING(0),
        IN_PROCESS(1),
        RECEIVED(2);

        private final int value;

        StockStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public ViewPurchaseManagement(User _user) {
        user = _user;
        this.viewGoodReceivedNotes = new ViewGoodReceivedNotes(_user);
        this.viewPurchaseOrder = new ViewPurchaseOrder(_user);
    }

    public User getUser() {
        return user;
    }

    public void Menu() {
        System.out.println("Purchase Management");
        System.out.println("1. Order Restock");
        System.out.println("2. Order Modification");
        System.out.println("3. Order Cancellation");
        System.out.println("4. View Purchase Records");
        System.out.println("5. Back to Main Menu");
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                OrderRestock();
                break;
            case 2:
                OrderModification();
                break;
            case 3:
                OrderCancellation();
                break;
            case 4:
                ViewOrderRecords();
                break;
            case 5:
                break;
            default:
                System.out.println("Invalid choice");
                break;
        }
    }

    private void OrderRestock() {

    }
    
    private void OrderModification() {
        
    }

    private void OrderCancellation() {
        ArrayList<PurchaseOrder> purchaseOrders = this.viewPurchaseOrder.SelectPurchaseOrderFromList(); //Display the selected Purchase Order Document
        PurchaseOrder purchaseOrder = purchaseOrders.get(0);
        //Display Purchase Order Details
        System.out.println("Purchase Order Details");
        System.out.println("Order ID: " + purchaseOrder.getDoc_No());
        System.out.println("Order Date: " + purchaseOrder.getTransaction_Date());
        System.out.println("Order Recipient: " + purchaseOrder.getTransaction_Recipient());

        //Display Purchase Order's Item Details 
        System.out.println(String.format("| %-20s | %-20s |", "Item Name",
                "Order Quantity"));

        for (PurchaseOrder _purchaseOrder : purchaseOrders) {
            System.out.println(String.format("| %-20s | %-20s |",
                    _purchaseOrder.getItem().getItem_Name(),
                    _purchaseOrder.getQuantity()));
        }

        //Remove confirmation
        System.out.println("Are you sure you want to remove this Purchase Order? (Y/N)");
        String choice = scanner.nextLine();

        if (!choice.equalsIgnoreCase("Y")) {
            return;
        }
        
        for (PurchaseOrder _purchaseOrder : purchaseOrders) {
            _purchaseOrder.Remove();
        }

    }
    
    private void ViewOrderRecords() {
        ArrayList<PurchaseOrder> purchaseOrders = this.viewPurchaseOrder.SelectPurchaseOrderFromList(); //Display the selected Purchase Order Document
        PurchaseOrder purchaseOrder = purchaseOrders.get(0);
        //Display Purchase Order Details
        System.out.println("Purchase Order Details");
        System.out.println("Order ID: " + purchaseOrder.getDoc_No());
        System.out.println("Order Date: " + purchaseOrder.getTransaction_Date());
        System.out.println("Order Recipient: " + purchaseOrder.getTransaction_Recipient());

        //Display Purchase Order's Item Details 
        System.out.println("Item Details");
        System.out.println(String.format("| %-20s | %-20s | %-20s | %-15s |", "Item Name", "Item Price",
                "Order Quantity", "Order Price"));

        double TotalPrice = 0;

        for (PurchaseOrder _purchaseOrder : purchaseOrders) {
            double OrderPrice = _purchaseOrder.getItem().getItem_Price() * _purchaseOrder.getQuantity();
            TotalPrice += OrderPrice;

            System.out.println(String.format("| %-20s | %-20s | %-20s | %-15s |",
                    _purchaseOrder.getItem().getItem_Name(),
                    _purchaseOrder.getItem().getItem_Price(),
                    _purchaseOrder.getQuantity(),
                    String.valueOf(OrderPrice)));
        }
        //Display total price in format
        System.out.println(String.format("| %60s | %-15s |", "Total Price: ", String.valueOf(TotalPrice)));

        //Display GRN
        System.out.println("Goods Received Notes");
        ArrayList<GoodReceivedNotes> goodReceivedNotes = GoodReceivedNotes.Get(purchaseOrder.getDoc_No(),
                GoodReceivedNotes.DocumentType.PURCHASE_ORDER);
        //Transaction goodReceivedNote = goodReceivedNotes.get(0);

        //Display GRN Header
        System.out.println(String.format("| %-20s | %-20s | %-20s |", "Item Name", "Item Price",
                "Received Quantity"));

        //Display GRN Details
        for (GoodReceivedNotes _goodReceivedNote : goodReceivedNotes) {
            System.out.println(String.format("| %-20s | %-20s | %-20s |",
                    _goodReceivedNote.getItem().getItem_Name(),
                    _goodReceivedNote.getItem().getItem_Price(),
                    _goodReceivedNote.getQuantity()));
        }
        //Only for Pending, In-process status
        ViewStockStatusMenu(purchaseOrder);
    }
    

    private void ViewStockStatusMenu(PurchaseOrder _purchaseOrder) {
        Transaction goodReceivedNotes = new GoodReceivedNotes();

        //Sub Menu allow user to follow up status or add GRN
        System.out.println("1. Follow Up Status");
        System.out.println("2. Add Goods Received Notes");
        System.out.println("3. Edit Goods Received Notes");
        System.out.println("4. Remove GoodReceived Notes");
        System.out.println("5. Back to Purchase Management");

        System.out.print("Enter choice: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                this.viewPurchaseOrder.FollowUpStatus(_purchaseOrder);
                break;
            case 2:
                String GrnNo = goodReceivedNotes.GenerateDocNo();
                this.viewGoodReceivedNotes.AddGoodsReceivedNotes(_purchaseOrder.getDoc_No(), GrnNo);
                break;
            case 3:
                //prompt user to select GRN
                goodReceivedNotes = this.viewGoodReceivedNotes
                        .SelectGoodReceivedNotesFromList(_purchaseOrder.getDoc_No());
                this.viewGoodReceivedNotes.EditGoodReceivedNotes(goodReceivedNotes);
                break;
            case 4:
                //prompt user to select GRN
                goodReceivedNotes = this.viewGoodReceivedNotes
                        .SelectGoodReceivedNotesFromList(_purchaseOrder.getDoc_No());
                this.viewGoodReceivedNotes.RemoveGoodReceivedNotes(goodReceivedNotes);
                break;
            case 5:
                break;
            default:
                System.out.println("Invalid choice");
                break;
        }
    }
}
