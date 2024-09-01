package project.view;

import java.util.ArrayList;
import java.util.Scanner;

import project.global.MailSender;
import project.global.MailTemplate;
import project.global.UserInputHandler;
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


            switch (UserInputHandler.getInteger("Enter choice: ", 1, 5)) {
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

        String choice;
        do {
            PurchaseOrder purchaseOrder = new PurchaseOrder();
            Item item = viewItem.selectItemFromList();
            purchaseOrder.setQuantity(getValidatedQuantity());
            purchaseOrder.setItem(item);
            purchaseOrder.setDoc_No(poNo);
            purchaseOrder.setTransaction_Recipient(item.getVendor_ID());
            purchaseOrder.setTransaction_Modified_By(user.getUserId());
            purchaseOrder.setTransaction_Created_By(user.getUserId());

            purchaseOrders.add(purchaseOrder);
            // Add Stock Confirmation

        } while (UserInputHandler.getConfirmation("Do you want to continue adding stock? [Y/N]: ")
                .equalsIgnoreCase("Y"));

        // Add all purchase orders to the database
        purchaseOrders.forEach(PurchaseOrder::Add);

        // TODO: Add logic for PDF and MailSender
    }

    // Method for modifying orders
    private void orderModification() {
        // Only Pending status orders can be modified
        ArrayList<PurchaseOrder> purchaseOrders = viewPurchaseOrder.selectPurchaseOrderFromList(StockStatus.PENDING);
        if (purchaseOrders.isEmpty()) {
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

        // Menu for order modification
        int choice;
        do {
            System.out.println("1. Add Item");
            System.out.println("2. Remove Item");
            System.out.println("3. Change Order Quantity");
            System.out.println("4. Back to Purchase Management");
            System.out.print("Enter choice (1-4): ");

            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number between 1 and 4.");
                scanner.next();
            }
            choice = scanner.nextInt();
            scanner.nextLine();
        } while (choice < 1 || choice > 4);

        switch (choice) {
            case 1:
                Item item = viewItem.selectItemFromList(itemsNotInPurchaseOrders);
                if (item == null) {
                    return;
                }

                purchaseOrder.setItem(item);
                purchaseOrder.setQuantity(getValidatedQuantity());

                // Initialize other values
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

                String removeChoice;
                do {
                    System.out.print("Are you sure you want to remove this Purchase Order? [Y/N]: ");
                    removeChoice = scanner.nextLine();
                } while (!removeChoice.equalsIgnoreCase("Y") && !removeChoice.equalsIgnoreCase("N"));

                if (!removeChoice.equalsIgnoreCase("Y")) {
                    break;
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

                purchaseOrder.setQuantity(getValidatedQuantity());
                purchaseOrder.setTransaction_Modified_By(user.getUserId());
                purchaseOrder.Update();

                break;
            case 4:
                return;
            default:
                System.out.println("Invalid choice");
        }

        // TODO: Add logic for PDF and MailSender
    }

    // Method to get validated quantity input
    private int getValidatedQuantity() {
        int quantity;
        do {
            System.out.print("Enter Quantity (must be greater than 0): ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next(); // Consume the invalid input
            }
            quantity = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            if (quantity <= 0) {
                System.out.println("Quantity must be greater than 0.");
            }
        } while (quantity <= 0);
        return quantity;
    }

    private String getConfirmation(String message) {
        String choice;
        do {
            System.out.print(message + " [Y/N]: ");
            choice = scanner.nextLine().trim();
            if (!choice.equalsIgnoreCase("Y") && !choice.equalsIgnoreCase("N")) {
                System.out.println("Invalid input. Please enter 'Y' or 'N'.");
            }
        } while (!choice.equalsIgnoreCase("Y") && !choice.equalsIgnoreCase("N"));
        return choice;
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
        if (!getConfirmation("Are you sure you want to remove this Purchase Order? [Y/N]: ")
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
