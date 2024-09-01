package project.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import project.global.MailSender;
import project.global.MailTemplate;
import project.global.UserInputHandler;
import project.modules.item.Item;
import project.modules.transaction.GoodReceivedNotes;
import project.modules.transaction.PurchaseOrder;
import project.modules.transaction.Transaction;
import project.modules.user.User;


public class ViewPurchaseManagement {
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
        boolean exit = false;
        while (!exit) {
            System.out.println("\n=============================");
            System.out.println("       Purchase Management       ");
            System.out.println("=============================");
            System.out.println("1. Order Restock");
            System.out.println("2. Order Modification");
            System.out.println("3. Order Cancellation");
            System.out.println("4. View Purchase Records");
            System.out.println("5. Back to Main Menu");
            System.out.println("=============================");


            switch (UserInputHandler.getInteger("Enter choice", 1, 5)) {
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
                    exit = true; // Exit the loop
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Method for restocking orders
    private void orderRestock() {
        ArrayList<PurchaseOrder> purchaseOrders = new ArrayList<>();
        String poNo = new PurchaseOrder().GenerateDocNo();
        viewItem.setItems();
        do {
            PurchaseOrder purchaseOrder = new PurchaseOrder();
            if (viewItem.getItems().isEmpty()) {
                break;
            }
            Item item = viewItem.selectItemFromList();
            purchaseOrder.setQuantity(
                    UserInputHandler.getInteger("Enter Quantity", 1, 100000));
            purchaseOrder.setItem(item);
            purchaseOrder.setDoc_No(poNo);
            purchaseOrder.setTransaction_Recipient(item.getVendor_ID());
            purchaseOrder.setTransaction_Modified_By(user.getUserId());
            purchaseOrder.setTransaction_Created_By(user.getUserId());

            purchaseOrders.add(purchaseOrder);
            // Add Stock Confirmation
            ArrayList<Item> items = viewItem.getItems();
            items.remove(item);
            viewItem.setItems(items);
            if(items.isEmpty()) {
                System.out.println("No more items available for restocking.");
                break;
            }
        } while (UserInputHandler.getConfirmation("Do you want to continue adding stock?")
                .equalsIgnoreCase("Y"));

        // Add all purchase orders to the database
        purchaseOrders.forEach(PurchaseOrder::Add);

        // TODO: Add logic for PDF and MailSender
    }

    // Method for modifying orders
    private void orderModification() {
        // Only Pending status orders can be modified
        viewPurchaseOrder.setPurchaseOrderList();
        ArrayList<PurchaseOrder> purchaseOrders = viewPurchaseOrder.selectPurchaseOrderFromList(StockStatus.PENDING);
        if (purchaseOrders == null || purchaseOrders.isEmpty()) {
            System.out.println("No pending orders available for modification.");
            return;
        }

        // SELECT item to modify
        Transaction purchaseOrder = new PurchaseOrder(purchaseOrders.get(0).getDoc_No());

        ArrayList<Item> itemsInPurchaseOrders = new ArrayList<>();
        ArrayList<Item> itemsNotInPurchaseOrders = Item.GetAll();

        for (PurchaseOrder po : purchaseOrders) {
            itemsInPurchaseOrders.add(po.getItem());
        }

        for (PurchaseOrder po : purchaseOrders) {
            itemsNotInPurchaseOrders.removeIf(item -> po.getItem().getItem_ID() == item.getItem_ID());
        }

        displayOrderDetails(purchaseOrders);
        boolean backToPurchaseManagement = false;

        // Menu for order modification
        while (!backToPurchaseManagement) {
            System.out.println("\n===============================");
            System.out.println("Order Modification");
            System.out.println("===============================");
            System.out.println("1. Add Item");
            System.out.println("2. Remove Item");
            System.out.println("3. Change Order Quantity");
            System.out.println("4. Back to Purchase Management");
            System.out.println("===============================");

            switch (UserInputHandler.getInteger("Enter choice", 1, 4)) {
                case 1:
                    viewItem.setItems(itemsNotInPurchaseOrders);
                    Item item = viewItem.selectItemFromList();
                    if (item == null) {
                        break;
                    }

                    purchaseOrder.setItem(item);

                    purchaseOrder.setQuantity(
                            UserInputHandler.getInteger("Enter Quantity", 1,
                                    100000));

                    // Initialize other values
                    purchaseOrder.setTransaction_Recipient(item.getVendor_ID());
                    purchaseOrder.setTransaction_Modified_By(user.getUserId());
                    purchaseOrder.setTransaction_Created_By(user.getUserId());

                    purchaseOrder.Add();
                    itemsNotInPurchaseOrders.remove(item);
                    itemsInPurchaseOrders.add(item);
                    break;
                case 2:
                    viewItem.setItems(itemsInPurchaseOrders);
                    Item itemToRemove = viewItem.selectItemFromList();
                    if (itemToRemove == null) {
                        System.out.println("Item not selected.");
                        return;
                    }

                    purchaseOrder.setItem(itemToRemove);

                    if (!UserInputHandler
                            .getConfirmation("Are you sure you want to remove this Purchase Order?")
                            .equalsIgnoreCase("Y")) {
                        break;
                    }

                    if (purchaseOrder.Remove()) {
                        System.out.println("Item removed from Purchase Order successfully.");
                        if (itemsInPurchaseOrders.size() == 1) {
                            System.out.println("Purchase Order is empty. Removing Purchase Order.");
                        }
                        itemsInPurchaseOrders.remove(itemToRemove);
                        itemsNotInPurchaseOrders.add(itemToRemove);
                        
                    } else {
                        System.out.println("Failed to remove Purchase Order.");
                    }
                    break;
                case 3:
                    viewItem.setItems(itemsInPurchaseOrders);
                    Item itemToChange = viewItem.selectItemFromList();
                    if (itemToChange == null) {
                        System.out.println("Item not selected.");
                        return;
                    }
                    purchaseOrder.setItem(itemToChange);
                    purchaseOrder.Get();
                    System.out.println("Current Quantity: " + purchaseOrder.getQuantity());

                    purchaseOrder.setQuantity(
                            UserInputHandler.getInteger("Enter Quantity", 1,
                                    100000));
                    purchaseOrder.setTransaction_Modified_By(user.getUserId());
                    System.out.println(purchaseOrder.Update());

                    break;
                case 4:
                    backToPurchaseManagement = true;
                default:
                    System.out.println("Invalid choice");
            }
        }

        // TODO: Add logic for PDF and MailSender
    }

    // Method to get validated quantity input

    // Method for canceling orders
    private void orderCancellation() {
        viewPurchaseOrder.setPurchaseOrderList();
        ArrayList<PurchaseOrder> purchaseOrders = viewPurchaseOrder.selectPurchaseOrderFromList(StockStatus.PENDING);
        if (purchaseOrders == null || purchaseOrders.isEmpty()) {
            return;
        }

        PurchaseOrder purchaseOrder = purchaseOrders.get(0);
        displayOrderDetails(purchaseOrders);

        // Remove confirmation
        if (!UserInputHandler.getConfirmation("Are you sure you want to remove this Purchase Order?")
                .equalsIgnoreCase("Y")) {
            return;
        }

        purchaseOrders.forEach(PurchaseOrder::Remove);

        // Send cancellation email
        MailSender mail = new MailSender(
                "tancs8803@gmail.com",
                "Order Cancelled",
                new MailTemplate(purchaseOrder.getDoc_No(), MailTemplate.TemplateType.ORDER_CANCELLATION));
        mail.Send();
    }

    // Method for viewing order records
    private void viewOrderRecords() {
        boolean PendingStatus = false;
        viewPurchaseOrder.setPurchaseOrderList();
        ArrayList<PurchaseOrder> purchaseOrders = viewPurchaseOrder.selectPurchaseOrderFromList();
        if (purchaseOrders == null || purchaseOrders.isEmpty()) {
            return;
        }

        PurchaseOrder purchaseOrder = purchaseOrders.get(0);
        displayOrderDetails(purchaseOrders);

        // Display Goods Received Notes
        ArrayList<GoodReceivedNotes> goodReceivedNotesList = GoodReceivedNotes.Get(purchaseOrder.getDoc_No(),
                GoodReceivedNotes.DocumentType.PURCHASE_ORDER);
        if (!(goodReceivedNotesList == null || goodReceivedNotesList.isEmpty())) {
            displayGoodReceivedNotes(goodReceivedNotesList);
            //display those item that havent fully received with its amount
            for (PurchaseOrder order : purchaseOrders) {
                int totalReceived = 0;

                // Calculate the total quantity received for this order's item
                for (GoodReceivedNotes notes : goodReceivedNotesList) {
                    if (order.getItem().getItem_ID() == notes.getItem().getItem_ID()) {
                        totalReceived += notes.getQuantity();
                    }
                }

                // Check if the total received is less than the ordered quantity
                if (totalReceived < order.getQuantity()) {
                    int remainingQuantity = order.getQuantity() - totalReceived;
                    System.out.println("Item " + order.getItem().getItem_Name() + " has not been fully received.");
                    System.out.println("Amount left to receive: " + remainingQuantity);
                }
            }
        } else {
            System.out.println("No Goods Received Notes found for this order.");
        }

        // Allow user to follow up or manage stock status - only display if the order is PENDING status
        //for loop 
        
        for (Integer orderStatus : viewPurchaseOrder.getOrderStatusList()) {           
            if (orderStatus != StockStatus.RECEIVED.getValue()) {
                
                PendingStatus = true;
            }
        }
        if (PendingStatus) {
            viewStockStatusMenu(purchaseOrder);
        } else {
            try {
                System.in.read();

            } catch (IOException e) {
                System.out.println("Pause Error: " + e.getMessage());
            }
        }
    }

    // Helper method to display order details
    private void displayOrderDetails(ArrayList<PurchaseOrder> purchaseOrders) {
        if (purchaseOrders.isEmpty()) {
            System.out.println("No purchase orders to display.");
            return;
        }

        PurchaseOrder purchaseOrder = purchaseOrders.get(0);
        System.out.println(" ========================================= ");
        System.out.println("| Purchase Order Details                  |");
        System.out.println(" ========================================= ");
        System.out.println(String.format("| %-20s : %-16s |", "Order ID", purchaseOrder.getDoc_No()));
        System.out.println(String.format("| %-20s : %-16s |", "Order Date", purchaseOrder.getTransaction_Date()));
        System.out.println(
                String.format("| %-20s : %-16s |", "Order Recipient", purchaseOrder.getTransaction_Recipient()));
        System.out.println(" ========================================= \n");

        // Display item details
        System.out.println("\n ==================== Items in Purchase Order ======================= ");
        System.out.println(String.format("| %-20s | %-20s | %-20s |", "Item Name", "Order Quantity", "Item Price"));
        System.out.println(" ==================================================================== ");

        double totalPrice = 0;

        for (PurchaseOrder order : purchaseOrders) {
            order.getItem().Get();
            double orderPrice = order.getItem().getItem_Price() * order.getQuantity();
            totalPrice += orderPrice;
            System.out.println(String.format("| %-20s | %-20d | %-20.2f |",
                    order.getItem().getItem_Name(),
                    order.getQuantity(),
                    order.getItem().getItem_Price()));
        }

        System.out.println(" ==================================================================== ");
        System.out.println(String.format("| %56s %7.2f |", "Total Price: ", totalPrice));
        System.out.println(" ==================================================================== \n");
    }

    // Helper method to display Goods Received Notes
    private void displayGoodReceivedNotes(ArrayList<GoodReceivedNotes> goodReceivedNotesList) {
        Set<String> displayedGRN = new HashSet<>();
        System.out.println(
                "\n ================================== Goods Received Notes =================================== ");

        System.out.println(String.format("| %-20s | %-20s | %-20s | %-20s |", "Received Notes", "Item Name",
                "Item Price", "Received Quantity"));
        System.out.println(
                " =========================================================================================== ");

        for (GoodReceivedNotes notes : goodReceivedNotesList) {
            notes.getItem().Get();
            if (!displayedGRN.add(notes.getDoc_No())) {
                System.out.println(String.format("| %-20s | %-20s | %-20s | %-20s |",
                        "",
                        notes.getItem().getItem_Name(),
                        notes.getItem().getItem_Price(),
                        notes.getQuantity()));
            } else {
                System.out.println(String.format("| %-20s | %-20s | %-20s | %-20s |",
                        notes.getDoc_No(),
                        notes.getItem().getItem_Name(),
                        notes.getItem().getItem_Price(),
                        notes.getQuantity()));
            }

        }
        System.out.println(
                " =========================================================================================== \n");
    }

    // Submenu for managing stock status - 
    private void viewStockStatusMenu(PurchaseOrder purchaseOrder) {
        boolean backToPurchaseManagement = false;

        while (!backToPurchaseManagement) {
            System.out.println("\n===============================");
            System.out.println("       Stock Status");
            System.out.println("===============================");
            System.out.println("1. Follow Up Status");
            System.out.println("2. Add Goods Received Notes");
            System.out.println("3. Edit Goods Received Notes");
            System.out.println("4. Remove Goods Received Notes");
            System.out.println("5. Back to Purchase Management");
            System.out.println("===============================");

            Transaction goodReceivedNotes = new GoodReceivedNotes();

            switch (UserInputHandler.getInteger("Enter choice", 1, 5)) {
                case 1:
                    viewPurchaseOrder.followUpStatus(purchaseOrder);
                    break;
                case 2:
                    String grnNo = goodReceivedNotes.GenerateDocNo();
                    this.viewGoodReceivedNotes.addGoodsReceivedNotes(purchaseOrder.getDoc_No(), grnNo);
                    break;
                case 3:
                    goodReceivedNotes = this.viewGoodReceivedNotes
                            .selectGoodReceivedNotesFromList(purchaseOrder.getDoc_No());
                    if (goodReceivedNotes == null) {
                        System.out.println("No Goods Received Notes found for this order.");
                        break;
                    }
                    this.viewGoodReceivedNotes.editGoodReceivedNotes(goodReceivedNotes);
                    break;
                case 4:
                    goodReceivedNotes = this.viewGoodReceivedNotes
                            .selectGoodReceivedNotesFromList(purchaseOrder.getDoc_No());
                    if (goodReceivedNotes == null) {
                        System.out.println("No Goods Received Notes found for this order.");
                        break;
                    }
                    this.viewGoodReceivedNotes.removeGoodsReceivedNotes(goodReceivedNotes);
                    break;
                case 5:
                    backToPurchaseManagement = true;
                    break;
                default:
                    break;
            }
            if (this.viewGoodReceivedNotes.getItems().isEmpty()) {
                System.out.println("No more items available.");
                break;
            }
        }
    }
}
