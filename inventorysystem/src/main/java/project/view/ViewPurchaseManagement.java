package project.view;

import java.util.ArrayList;
import java.util.Scanner;

import project.global.MailSender;
import project.global.MailTemplate;
import project.modules.item.Item;
import project.modules.transaction.GoodReceivedNotes;
import project.modules.transaction.PurchaseOrder;
import project.modules.transaction.Transaction;
import project.modules.user.User;


public class ViewPurchaseManagement {
    private final Scanner scanner = new Scanner(System.in);

    private static User user;

    private final ViewGoodReceivedNotes viewGoodReceivedNotes;
    private final ViewPurchaseOrder viewPurchaseOrder;
    private final ViewItem viewItem;

    // Enum for stock status
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

    // Constructor
    public ViewPurchaseManagement(User user) {
        ViewPurchaseManagement.user = user;
        this.viewGoodReceivedNotes = new ViewGoodReceivedNotes(user);
        this.viewPurchaseOrder = new ViewPurchaseOrder(user);
        this.viewItem = new ViewItem(user);
    }

    // Getter for User
    public User getUser() {
        return user;
    }

    // Main menu for purchase management
    public void menu() {
        System.out.println("Purchase Management");
        System.out.println("1. Order Restock");
        System.out.println("2. Order Modification");
        System.out.println("3. Order Cancellation");
        System.out.println("4. View Purchase Records");
        System.out.println("5. Back to Main Menu");
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                orderRestock();
                break;
            case 2:
                orderModification();
                break;
            case 3:
                orderCancellation();
                break;
            case 4:
                viewOrderRecords();
                break;
            case 5:
                break;
            default:
                System.out.println("Invalid choice");
                break;
        }
    }

    // Method for restocking orders
    private void orderRestock() {
        ArrayList<PurchaseOrder> purchaseOrders = new ArrayList<>();
        String poNo = new PurchaseOrder().GenerateDocNo();

        String choice;
        do {
            Item item = viewItem.selectItemFromList();
            PurchaseOrder purchaseOrder = new PurchaseOrder();
            
            // Get Quantity
            System.out.print("Enter Quantity: ");
            purchaseOrder.setQuantity(scanner.nextInt());
            scanner.nextLine(); // Consume newline

            purchaseOrder.setItem(item);
            purchaseOrder.setDoc_No(poNo);
            purchaseOrder.setTransaction_Recipient(item.getVendor_ID());
            purchaseOrder.setTransaction_Modified_By(user.getUserId());
            purchaseOrder.setTransaction_Created_By(user.getUserId());

            purchaseOrders.add(purchaseOrder);

            // Add Stock Confirmation
            System.out.print("Do you want to continue adding stock? [Y/N]: ");
            choice = scanner.nextLine();
        } while (choice.equalsIgnoreCase("Y"));

        // Add all purchase orders to the database
        purchaseOrders.forEach(PurchaseOrder::Add);

        // TODO: Add logic for PDF and MailSender
    }

    // Method for modifying orders
    private void orderModification() {
        // Only Pending status orders can be modified
        ArrayList<PurchaseOrder> purchaseOrders = viewPurchaseOrder.selectPurchaseOrderFromList(StockStatus.PENDING);
        if (purchaseOrders.isEmpty()) {
            return;
        }

        //SELECT item to modify

        Transaction purchaseOrder = new PurchaseOrder(purchaseOrders.get(0).getDoc_No());

        ArrayList<Item> itemsInPurchaseOrders = new ArrayList<>();
        ArrayList<Item> itemsNotInPurchaseOrders = Item.GetAll();

        for (PurchaseOrder po : purchaseOrders) {
            itemsInPurchaseOrders.add(po.getItem());
        }

        // ArrayList for Items that are not inside the purchase orders
        
        for (PurchaseOrder po : purchaseOrders) {
            for(Item item : itemsNotInPurchaseOrders) {
                if (po.getItem().getItem_ID() == item.getItem_ID()) {
                    itemsNotInPurchaseOrders.remove(item);
                    break;
                }
            }
        }
        

        displayOrderDetails(purchaseOrders);

        // Allow user to change item or order quantity
        //remove item from PO
        //add new item to PO
        System.out.println("1. Add Item"); //select new item from list
        System.out.println("2. Remove Item"); //select item from list
        System.out.println("3. Change Order Quantity"); //select item from list and enter new quantity
        System.out.println("4. Back to Purchase Management");
        System.out.print("Enter choice: ");

        int choice;
        choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                Item item = viewItem.selectItemFromList(itemsNotInPurchaseOrders);
                if (item == null) {
                    return;
                }

                purchaseOrder.setItem(item);

                System.out.print("Enter Quantity: ");
                purchaseOrder.setQuantity(scanner.nextInt());
                scanner.nextLine(); // Consume newline

                //initialise the value
                purchaseOrder.setTransaction_Recipient(item.getVendor_ID());
                purchaseOrder.setTransaction_Modified_By(user.getUserId());
                purchaseOrder.setTransaction_Created_By(user.getUserId());

                purchaseOrder.Add();

                break;
            case 2:
                Item itemToRemove = viewItem.selectItemFromList(itemsInPurchaseOrders);
                if (itemToRemove == null) {
                    System.out.println("Item not selected.");
                    return;
                }

                purchaseOrder.setItem(itemToRemove);

                //remove confirmation
                System.out.print("Are you sure you want to remove this Purchase Order? [Y/N]: ");
                String removeChoice = scanner.nextLine();

                if (!removeChoice.equalsIgnoreCase("Y")) {
                    return;
                }
                if (purchaseOrder.Remove()) {
                    System.out.println("Item removed from Purchase Order successfully.");
                    if (itemsInPurchaseOrders.size() == 1) {
                        System.out.println("Purchase Order is empty. Removing Purchase Order.");
                    }
                } else {
                    System.out.println("Failed to remove Purchase Order.");
                }
                break;
            case 3:
                Item itemToChange = viewItem.selectItemFromList(itemsInPurchaseOrders);
                if (itemToChange == null) {
                    System.out.println("Item not selected.");
                    return;
                }
                purchaseOrder.setItem(itemToChange);
                purchaseOrder.Get();

                System.out.print("Enter Quantity: ");
                purchaseOrder.setQuantity(scanner.nextInt());
                scanner.nextLine();

                purchaseOrder.setTransaction_Modified_By(user.getUserId());
                purchaseOrder.Update();

                break;
            case 4:
                return;
            default:
                System.out.println("Invalid choice");
        }
        
        //TODO: Add logic for PDF and MailSender
    }

    // Method for canceling orders
    private void orderCancellation() {
        ArrayList<PurchaseOrder> purchaseOrders = viewPurchaseOrder.selectPurchaseOrderFromList(StockStatus.PENDING);
        if (purchaseOrders == null || purchaseOrders.isEmpty()) {
            return;
        }

        PurchaseOrder purchaseOrder = purchaseOrders.get(0);
        displayOrderDetails(purchaseOrders);

        // Remove confirmation
        System.out.print("Are you sure you want to remove this Purchase Order? [Y/N]: ");
        String choice = scanner.nextLine();

        if (!choice.equalsIgnoreCase("Y")) {
            return;
        }

        purchaseOrders.forEach(PurchaseOrder::Remove);

        // Send cancellation email
        MailSender mail = new MailSender(
                "tancs8803@gmail.com", 
                "Order Cancelled",
                new MailTemplate(purchaseOrder.getDoc_No(), MailTemplate.TemplateType.ORDER_CANCELLATION)
        );
        mail.Send();
    }

    // Method for viewing order records
    private void viewOrderRecords() {
        ArrayList<PurchaseOrder> purchaseOrders = viewPurchaseOrder.selectPurchaseOrderFromList();
        if (purchaseOrders.isEmpty()) {
            return;
        }

        PurchaseOrder purchaseOrder = purchaseOrders.get(0);
        displayOrderDetails(purchaseOrders);

        // Display Goods Received Notes
        ArrayList<GoodReceivedNotes> goodReceivedNotesList = GoodReceivedNotes.Get(purchaseOrder.getDoc_No(),
                GoodReceivedNotes.DocumentType.PURCHASE_ORDER);
        if (!(goodReceivedNotesList == null || goodReceivedNotesList.isEmpty())) {
            displayGoodReceivedNotes(goodReceivedNotesList);
        } else {
            System.out.println("No Goods Received Notes found for this order.");
        }


        // Allow user to follow up or manage stock status - only display if the order is PENDING status
        if (viewPurchaseOrder.getOrderStatusList().get(0) != StockStatus.RECEIVED.getValue()) {
            viewStockStatusMenu(purchaseOrder);
        }
    }

    // Helper method to display order details
    private void displayOrderDetails(ArrayList<PurchaseOrder> purchaseOrders) {
        PurchaseOrder purchaseOrder = purchaseOrders.get(0);
        System.out.println("Purchase Order Details");
        System.out.println("Order ID: " + purchaseOrder.getDoc_No());
        System.out.println("Order Date: " + purchaseOrder.getTransaction_Date());
        System.out.println("Order Recipient: " + purchaseOrder.getTransaction_Recipient());

        // Display item details
        System.out.println(String.format("| %-20s | %-20s |", "Item Name", "Order Quantity"));
        double totalPrice = 0;

        for (PurchaseOrder order : purchaseOrders) {
            double orderPrice = order.getItem().getItem_Price() * order.getQuantity();
            totalPrice += orderPrice;
            System.out.println(String.format("| %-20s | %-20s |", order.getItem().getItem_Name(), order.getQuantity()));
        }

        System.out.println(String.format("Total Price: %.2f", totalPrice));
    }

    // Helper method to display Goods Received Notes
    private void displayGoodReceivedNotes(ArrayList<GoodReceivedNotes> goodReceivedNotesList) {
        System.out.println("Goods Received Notes");
        System.out.println(String.format("| %-20s | %-20s | %-20s |", "Item Name", "Item Price", "Received Quantity"));

        for (GoodReceivedNotes notes : goodReceivedNotesList) {
            notes.getItem().Get();
            System.out.println(String.format("| %-20s | %-20s | %-20s |",
                    notes.getItem().getItem_Name(),
                    notes.getItem().getItem_Price(),
                    notes.getQuantity()));
        }
    }

    // Submenu for managing stock status
    private void viewStockStatusMenu(PurchaseOrder purchaseOrder) {
        System.out.println("1. Follow Up Status");
        System.out.println("2. Add Goods Received Notes");
        System.out.println("3. Edit Goods Received Notes");
        System.out.println("4. Remove Goods Received Notes");
        System.out.println("5. Back to Purchase Management");
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt();

        scanner.nextLine();

        Transaction goodReceivedNotes;

        switch (choice) {
            case 1:
                viewPurchaseOrder.followUpStatus(purchaseOrder);
                break;
            case 2:
                goodReceivedNotes = new GoodReceivedNotes();
                String grnNo = goodReceivedNotes.GenerateDocNo();
                viewGoodReceivedNotes.addGoodsReceivedNotes(purchaseOrder.getDoc_No(), grnNo);
                break;
            case 3:
                goodReceivedNotes = viewGoodReceivedNotes.selectGoodReceivedNotesFromList(purchaseOrder.getDoc_No());
                viewGoodReceivedNotes.editGoodReceivedNotes(goodReceivedNotes);
                break;
            case 4:
                goodReceivedNotes = viewGoodReceivedNotes.selectGoodReceivedNotesFromList(purchaseOrder.getDoc_No());
                viewGoodReceivedNotes.removeGoodsReceivedNotes(goodReceivedNotes);
                break;
            case 5:
                break;
            default:
                System.out.println("Invalid choice");
                break;
        }
    }
}
