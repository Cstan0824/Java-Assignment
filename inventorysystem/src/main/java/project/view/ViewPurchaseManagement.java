package project.view;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import project.global.MailSender;
import project.global.MailTemplate;
import project.global.PdfConverter;
import project.global.PdfTemplate;
import project.global.UserInputHandler;
import project.modules.item.Item;
import project.modules.transaction.GoodReceivedNotes;
import project.modules.transaction.PurchaseOrder;
import project.modules.transaction.Transaction;
import project.modules.user.User;
import project.modules.vendor.Vendor;


public class ViewPurchaseManagement {
    private static User user; //Session

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
    public ViewPurchaseManagement(User _user) {
        ViewPurchaseManagement.user = _user;
        this.viewGoodReceivedNotes = new ViewGoodReceivedNotes(_user);
        this.viewPurchaseOrder = new ViewPurchaseOrder(_user);
        this.viewItem = new ViewItem(_user);
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
        Map<Vendor, ArrayList<Transaction>> orders = new HashMap<>();
        File file;
        MailSender mail;
        PdfConverter pdf;

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
            purchaseOrder.setTransaction_Recipient(item.getVendor().getVendor_ID());
            purchaseOrder.setTransaction_Modified_By(user.getUserId());
            purchaseOrder.setTransaction_Created_By(user.getUserId());

            purchaseOrders.add(purchaseOrder);
            // Add Stock Confirmation
            ArrayList<Item> items = viewItem.getItems();
            items.remove(item);
            viewItem.setItems(items);
            if (items.isEmpty()) {
                System.out.println("No more items available for restocking.");
                break;
            }
        } while (UserInputHandler.getConfirmation("Do you want to continue adding stock?")
                .equalsIgnoreCase("Y"));

        // Add all purchase orders to the database
        purchaseOrders.forEach(_purchaseOrder -> {
            _purchaseOrder.Add();
            if (!orders.containsKey(_purchaseOrder.getItem().getVendor())) {
                orders.put(_purchaseOrder.getItem().getVendor(), new ArrayList<>());
            }
            orders.get(_purchaseOrder.getItem().getVendor()).add(_purchaseOrder);
        });

        // Send email to vendors
        for (Map.Entry<Vendor, ArrayList<Transaction>> entry : orders.entrySet()) {
            file = new File(
                    "C:/Cstan/TARUMT Course/DIPLOMA IN INFORMATION TECHNOLOGY/YEAR2/Y2S1/Object Oriented Programming/Java-Assignment/inventorysystem/src/main/java/project/global/Pdf",
                    entry.getValue().get(0).getDoc_No() + ".pdf");
            pdf = new PdfConverter(file,
                    new PdfTemplate(entry.getValue(), PdfTemplate.TemplateType.PURCHASE_ORDER));
            pdf.Save();

            mail = new MailSender(
                    entry.getKey().getVendor_Email(),
                    "Purchase Order",
                    new MailTemplate(poNo, MailTemplate.TemplateType.PURCHASE_ORDER));
            mail.AttachFile(file);
            mail.Send();
        }
    }

    // Method for modifying orders
    private void orderModification() {
        // Only Pending status orders can be modified
        boolean backToPurchaseManagement = false;
        boolean orderModified = false;
        Map<Vendor, ArrayList<Transaction>> orders = new HashMap<>();
        ArrayList<Item> itemsInPurchaseOrders = new ArrayList<>();
        ArrayList<Item> itemsNotInPurchaseOrders = Item.GetAll();

        viewPurchaseOrder.setPurchaseOrderList();
        ArrayList<PurchaseOrder> purchaseOrders = viewPurchaseOrder.selectPurchaseOrderFromList(StockStatus.PENDING);
        if (purchaseOrders == null || purchaseOrders.isEmpty()) {
            System.out.println("No pending orders available for modification.");
            return;
        }

        // SELECT item to modify
        Transaction purchaseOrder = new PurchaseOrder(purchaseOrders.get(0).getDoc_No());

        for (PurchaseOrder po : purchaseOrders) {
            itemsInPurchaseOrders.add(po.getItem());
        }

        for (PurchaseOrder po : purchaseOrders) {
            itemsNotInPurchaseOrders.removeIf(item -> po.getItem().getItem_ID() == item.getItem_ID());
        }

        displayOrderDetails(purchaseOrders);

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
                    purchaseOrder.setTransaction_Recipient(item.getVendor().getVendor_ID());
                    purchaseOrder.setTransaction_Modified_By(user.getUserId());
                    purchaseOrder.setTransaction_Created_By(user.getUserId());

                    purchaseOrder.Add();
                    itemsNotInPurchaseOrders.remove(item);
                    itemsInPurchaseOrders.add(item);
                    orderModified = true;
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
                    orderModified = true;
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
                    orderModified = true;
                    break;
                case 4:
                    backToPurchaseManagement = true;
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }

        }

        if (!orderModified) {
            return;
        }

        PurchaseOrder.Get(purchaseOrders.get(0).getDoc_No()).forEach(po -> {
            po.getItem().Get();
            if (!orders.containsKey(po.getItem().getVendor())) {
                orders.put(po.getItem().getVendor(), new ArrayList<>());
            }
            orders.get(po.getItem().getVendor()).add(po);
        });

        // Send email to vendors
        MailSender mail;
        File file;
        PdfConverter pdf;
        for (Map.Entry<Vendor, ArrayList<Transaction>> entry : orders.entrySet()) {
            file = new File(
                    "C:/Cstan/TARUMT Course/DIPLOMA IN INFORMATION TECHNOLOGY/YEAR2/Y2S1/Object Oriented Programming/Java-Assignment/inventorysystem/src/main/java/project/global/Pdf",
                    entry.getValue().get(0).getDoc_No() + ".pdf");
            pdf = new PdfConverter(file,
                    new PdfTemplate(entry.getValue(), PdfTemplate.TemplateType.PURCHASE_ORDER));
            pdf.Save();

            mail = new MailSender(
                    entry.getKey().getVendor_Email(),
                    "Reordering",
                    new MailTemplate(purchaseOrders.get(0).getDoc_No(), MailTemplate.TemplateType.REORDERING));
            mail.AttachFile(file);
            mail.Send();
        }
    }

    // Method to get validated quantity input

    // Method for canceling orders
    private void orderCancellation() {
        Set<Vendor> vendors = new HashSet<>();
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

        purchaseOrders.forEach(_purchaseOrder -> {
            _purchaseOrder.Remove();
            vendors.add(_purchaseOrder.getItem().getVendor());
        });

        // Send cancellation email
        MailSender mail;
        for (Vendor vendor : vendors) {
            mail = new MailSender(
                    vendor.getVendor_Email(),
                    "Order Cancelled",
                    new MailTemplate(purchaseOrder.getDoc_No(), MailTemplate.TemplateType.ORDER_CANCELLATION));
            mail.Send();
        }
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
            viewStockStatusMenu(purchaseOrders);
        } else {
            UserInputHandler.systemPause("Press any key to continue...");
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
    private void viewStockStatusMenu(ArrayList<PurchaseOrder> _purchaseOrders) {
        boolean backToPurchaseManagement = false;
        boolean orderManaged = false;

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
                    viewPurchaseOrder.followUpStatus(_purchaseOrders);
                    break;
                case 2:
                    String grnNo = goodReceivedNotes.GenerateDocNo();
                    this.viewGoodReceivedNotes.addGoodsReceivedNotes(_purchaseOrders.get(0).getDoc_No(), grnNo);
                    orderManaged = true;
                    if (this.viewGoodReceivedNotes.getItems().isEmpty()) {
                        backToPurchaseManagement = true;
                        System.out.println("No more items available.");
                        break;
                    }
                    break;
                case 3:
                    goodReceivedNotes = this.viewGoodReceivedNotes
                            .selectGoodReceivedNotesFromList(_purchaseOrders.get(0).getDoc_No());
                    if (goodReceivedNotes == null) {
                        System.out.println("No Goods Received Notes found for this order.");
                        backToPurchaseManagement = true;
                        break;
                    }
                    this.viewGoodReceivedNotes.editGoodReceivedNotes(goodReceivedNotes);
                    orderManaged = true;
                    break;
                case 4:
                    goodReceivedNotes = this.viewGoodReceivedNotes
                            .selectGoodReceivedNotesFromList(_purchaseOrders.get(0).getDoc_No());
                    if (goodReceivedNotes == null) {
                        System.out.println("No Goods Received Notes found for this order.");
                        backToPurchaseManagement = true;
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

        }

        if (!orderManaged) {
            return;
        }

        //Send email to vendors
        ArrayList<PurchaseOrder> purchaseOrders = PurchaseOrder.Get(_purchaseOrders.get(0).getDoc_No());
        Map<Vendor, ArrayList<Transaction>> orders = new HashMap<>();
        for (PurchaseOrder order : purchaseOrders) {
            order.getItem().Get();
            if (!orders.containsKey(order.getItem().getVendor())) {
                orders.put(order.getItem().getVendor(), new ArrayList<>());
            }
            orders.get(order.getItem().getVendor()).add(order);
        }

        //check if the good are all received
        for (Map.Entry<Vendor, ArrayList<Transaction>> entry : orders.entrySet()) {
            boolean isFullyReceived = true;
            for (Transaction order : entry.getValue()) {
                int totalReceived = 0;
                ArrayList<GoodReceivedNotes> goodReceivedNotesList = GoodReceivedNotes.Get(order.getItem(),
                        order.getDoc_No());
                if (goodReceivedNotesList == null || goodReceivedNotesList.isEmpty()) {
                    isFullyReceived = false;
                    break;
                }
                for (GoodReceivedNotes notes : goodReceivedNotesList) {
                    totalReceived += notes.getQuantity();
                }
                if (totalReceived < order.getQuantity()) {
                    isFullyReceived = false;
                    break;
                }
            }
            if (isFullyReceived) {
                MailSender mail = new MailSender(
                        entry.getKey().getVendor_Email(),
                        "Order Confirmation",
                        new MailTemplate(purchaseOrders.get(0).getDoc_No(),
                                MailTemplate.TemplateType.ORDER_CONFIRMATION));
                mail.Send();
            }
        }
    }
}
