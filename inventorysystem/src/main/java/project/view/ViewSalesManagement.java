package project.view;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import project.global.ConsoleUI;
import project.global.MailSender;
import project.global.MailTemplate;
import project.global.PdfConverter;
import project.global.PdfTemplate;
import project.global.UserInputHandler;
import project.modules.item.Item;
import project.modules.transaction.DeliveryOrder;
import project.modules.transaction.SalesOrder;
import project.modules.transaction.Transaction;
import project.modules.user.Retailer;
import project.modules.user.User;



//use by retailer to order stock, check their order records
//use by admin to view SO list, receive SO and Create DO
public class ViewSalesManagement {
    private ViewSalesOrder viewSalesOrder;
    private ViewItem viewItem;
    private static User user;

    public enum OrderStatus {
        PENDING(0),
        IN_PROCESS(1),
        DELIVERED(2);

        private final int value;

        OrderStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    
    public ViewSalesManagement(User user){

        ViewSalesManagement.user = user;
        this.viewSalesOrder = new ViewSalesOrder(user);
        this.viewItem = new ViewItem(user);


    }

    public ViewSalesOrder getViewSalesOrder() {
        return this.viewSalesOrder;
    }

    public void setViewSalesOrder(ViewSalesOrder viewSalesOrder) {
        this.viewSalesOrder = viewSalesOrder;
    }

    public ViewItem getViewItem() {
        return this.viewItem;
    }

    public void setViewItem(ViewItem viewItem) {
        this.viewItem = viewItem;
    }

    //menu for user
    public void userMenu() {
        if (user.getUserType().equals("Admin")) {
            adminMenu();
        } else if (user.getUserType().equals("Retailer")) {
            retailerMenu();
        }
    }

    //menu for admin
    public void adminMenu() {
        boolean exit = false;
        while(!exit) {
            ConsoleUI.clearScreen();
            System.out.println("======= Sales Management =======");
            System.out.println("1. View Sales Order Records");
            System.out.println("2. Create Delivery Order");
            System.out.println("3. Modify Sales Order");
            System.out.println("4. Cancel Sales Order");
            System.out.println("5. Exit");
            System.out.println("================================");
            int choice = UserInputHandler.getInteger("Choose your actions: ", 1, 5);
            switch (choice) {
                case 1:
                    viewOrderRecords();
                    break;
                case 2:
                    createDeliveryOrder();
                    break;
                case 3:
                    orderModification();
                    break;
                case 4:
                    orderCancellation();
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    //menu for retailer
    public void retailerMenu(){

        boolean exit = false;
        while(!exit) {
            ConsoleUI.clearScreen();
            System.out.println("===== Order Management ======");
            System.out.println("1. Order Stock");
            System.out.println("2. View Order Records");
            System.out.println("3. Exit");
            System.out.println("=============================");
            int choice = UserInputHandler.getInteger("Choose your actions: ", 1, 3);
            switch (choice) {
                case 1:
                    createSalesOrder();
                    break;
                case 2:
                    viewOrderRecords();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }




    }

    private void viewOrderRecords() {
        
        if (user.getUserType().equals("Admin") || user.getUserType().equals("Retailer")) {
            
            ConsoleUI.clearScreen();
            ArrayList<SalesOrder> salesOrders = viewSalesOrder.selectSalesOrderFromList();
            if (salesOrders == null || salesOrders.isEmpty()){
                if (user.getUserType().equals("Admin")) {
                    System.out.println("No Sales Order found.");
                    ConsoleUI.pause();
                }
                else if (user.getUserType().equals("Retailer")) {
                    System.out.println("No Sales Order found.");
                    ConsoleUI.pause();
                }
            }

        }
         
    }


    //for admin to create DO
    private void createDeliveryOrder() {
        ConsoleUI.clearScreen();
        if (user.getUserType().equals("Admin")) {
            ArrayList<SalesOrder> salesOrders = viewSalesOrder.selectPendingSalesOrder("createDO");

            if (salesOrders == null || salesOrders.isEmpty()){
                return;
            }
            
            //create DO
            DeliveryOrder DODocNo = new DeliveryOrder();
            String docNo = DODocNo.GenerateDocNo();
            for (SalesOrder salesOrder : salesOrders) {
                //create DO
                 DeliveryOrder deliveryOrder = new DeliveryOrder(docNo, salesOrder.getItem());
                    
                deliveryOrder.setSource_Doc_No(salesOrder.getDoc_No());
                deliveryOrder.setTransaction_Date(new Date(System.currentTimeMillis()));
                deliveryOrder.setQuantity(salesOrder.getQuantity());
                deliveryOrder.setTransaction_Recipient(salesOrder.getTransaction_Recipient());  // Customize as needed
                deliveryOrder.setTransaction_Created_By(user.getUserId());  // Customize as needed
                deliveryOrder.setTransaction_Modified_By(user.getUserId());  // Customize as needed
                if (deliveryOrder.Add()) {
                    System.out.println("Delivery Order for " + deliveryOrder.getQuantity() + " " + deliveryOrder.getItem().getItem_Name() + " is added.");

                    //add mail notification here
                    MailSender mail;
                    Retailer retailer = new Retailer();
                    retailer.setUserId(salesOrder.getTransaction_Recipient());
                    retailer.Get();
                    mail = new MailSender(
                    retailer.getUserEmail(),
                    "Order Confirmed",
                    new MailTemplate(salesOrder.getDoc_No() + " - " + deliveryOrder.getQuantity() + " " + deliveryOrder.getItem().getItem_Name(), MailTemplate.TemplateType.ORDER_CONFIRMATION));
                    mail.Send();

                } else {
                    System.out.println("Error adding Delivery Order.");
                }


            }

            //display created DO
            ArrayList<DeliveryOrder> deliveryOrders = DeliveryOrder.GetAll(docNo);

            String[] columnNames = {"DO No","Item_ID", "Item_Name", "Quantity", "Mode", "Date", "Recipient"};
            if (deliveryOrders != null && !deliveryOrders.isEmpty()) {
                System.out.println("\n\nDelivery Order List for Doc No: " + docNo);
                normalTableLine();
                System.out.printf("|");
                for (String columnName : columnNames) {
                    if (columnName.equals("Item_Name")) {
                        System.out.printf(" %-40s ", columnName);
                        System.out.printf("|");
                    } else {
                        System.out.printf(" %-15s ", columnName);
                        System.out.printf("|");
                    }
                }
                System.out.println("");
                normalTableLine();
                for (DeliveryOrder deliveryOrder : deliveryOrders) {
                    System.out.print(deliveryOrder.toString());
                    normalTableLine();
                }
            }

            ConsoleUI.pause();

        }

        

    }

    //for retailer to order stock
    private void createSalesOrder() {
        if (user.getUserType().equals("Retailer")) {
            File file;
            MailSender mail;
            PdfConverter pdf;
            ArrayList<Transaction> transactions = new ArrayList<>();
            ArrayList<SalesOrder> newOrders = new ArrayList<>();
            String docNo = new SalesOrder().GenerateDocNo();

            do {
            ConsoleUI.clearScreen();
            Item item = viewItem.selectItemForRetailer();
            SalesOrder salesOrder = new SalesOrder(docNo, item);
            salesOrder.setQuantity(UserInputHandler.getInteger("Enter Quantity (must be greater than 0): ", 1, 1000000));
            salesOrder.setTransaction_Recipient(user.getUserId());
            salesOrder.setTransaction_Modified_By(user.getUserId());
            salesOrder.setTransaction_Created_By(user.getUserId());
            newOrders.add(salesOrder);
            
            } while (UserInputHandler.getConfirmation("Do you want to continue adding item? ")
                .equalsIgnoreCase("Y"));

                
                //display created SO
                String[] columnNames = {"SO No","Item_ID", "Item_Name", "Quantity", "Mode", "Date", "Recipient"};
                System.out.println("\n\nPurchase Order List for Doc No: " + docNo);
                normalTableLine();
                System.out.printf("|");
                for (String columnName : columnNames) {
                    if (columnName.equals("Item_Name")) {
                        System.out.printf(" %-40s ", columnName);
                        System.out.printf("|");
                    } else {
                        System.out.printf(" %-15s ", columnName);
                        System.out.printf("|");
                    }
                }
                System.out.println("");
                normalTableLine();
                for (SalesOrder salesOrder : newOrders) {
                    System.out.print(salesOrder.toString());
                    normalTableLine();
                }
            ConsoleUI.pause();    
            System.out.println();
            // Add all purchase orders to the database
            for (SalesOrder salesOrder : newOrders) {
                if (salesOrder.Add()) {
                    System.out.println("Your Order - " + salesOrder.getDoc_No() +" for " + salesOrder.getQuantity() + " " + salesOrder.getItem().getItem_Name() + " is added.");
                } else {
                    System.out.println("Error adding Sales Order.");
                }
                transactions.add(salesOrder);
            }

            //mail sender
             URL resource = getClass().getClassLoader()
                    .getResource("project/global/Pdf");
            file = new File(
            resource.getPath().replace("%20", " "), docNo + ".pdf");

            pdf = new PdfConverter(file,
                    new PdfTemplate(transactions, PdfTemplate.TemplateType.SALES_ORDER));
            pdf.Save();

            Retailer retailer = new Retailer();
            retailer.setUserId(user.getUserId());
            retailer.Get();

            mail = new MailSender(
                    retailer.getUserEmail(),
                    "Sales Order",
                    new MailTemplate(docNo, MailTemplate.TemplateType.SALES_ORDER));
            mail.AttachFile(file);
            mail.Send();
        }
    }
    
    //for admin to modify pending SO
    private void orderModification() {
        ConsoleUI.clearScreen();
        if (user.getUserType().equals("Admin")) {
            boolean error = false;
            ArrayList<SalesOrder> salesOrders = viewSalesOrder.selectPendingSalesOrder("Modify");

            if (salesOrders == null || salesOrders.isEmpty()){
                return;
            }
            //modify SO
            do {
                do {  
                    int itemId = UserInputHandler.getInteger("\nEnter Item ID: ", 1, 1000000);

                    Item item = new Item(itemId);
                    item.Get();

                    SalesOrder salesOrder = new SalesOrder(salesOrders.get(0).getDoc_No(), item);

                    if(salesOrder.Get()){
                        String choice;
                        do{
                            int quantity = UserInputHandler.getInteger("Enter New Item Quantity: ", 0, 1000000);
                            salesOrder.setQuantity(quantity);

                            salesOrder.setTransaction_Modified_By(user.getUserId());

                            if (salesOrder.getQuantity() <= 0) {

                                choice = UserInputHandler.getConfirmation("Quantity is zero or negative. Do you want to delete this item from the Sales Order? ");

                                if (choice.equalsIgnoreCase("Y")) {
                                    if (salesOrder.Remove()) {
                                        System.out.println("Item removed from Sales Order successfully.");
                                    } else {
                                        System.out.println("Failed to remove item from Sales Order. Please try again.");
                                    }
                                } else {
                                    System.out.println("Please re-enter item quantity.");
                                }
                            }
                        } while (salesOrder.getQuantity() <= 0);

                        if(salesOrder.Update()){

                            System.out.println("\nSales Order " + salesOrders.get(0).getDoc_No() + " with item " + salesOrder.getItem().getItem_Name() + " updated successfully.");

                            //add mail notification here
                            MailSender mail;
                            Retailer retailer = new Retailer();
                            retailer.setUserId(salesOrder.getTransaction_Recipient());
                            retailer.Get();
                            mail = new MailSender(retailer.getUserEmail(), "Order Modification for " + salesOrder.getDoc_No(),
                            new MailTemplate(salesOrder.getDoc_No() + " - " + salesOrder.getItem().getItem_Name(), MailTemplate.TemplateType.ORDER_MODIFICATION));
                            mail.Send();
                        }

                    } else {
                        System.out.println("Item with ID " + itemId + " not found in Sales Order " + salesOrders.get(0).getDoc_No() + ".");
                        System.out.println("Please re-enter the item ID.");
                        error = true;
                    }
                } while (error);
            } while (UserInputHandler.getConfirmation("\nDo you want to continue edit sales order? ").equalsIgnoreCase("Y"));
        }
    }

    //for admin to cancel pending SO
    private void orderCancellation() {
        ConsoleUI.clearScreen();
        if (user.getUserType().equals("Admin")) {
            
            ArrayList<SalesOrder> salesOrders = viewSalesOrder.selectPendingSalesOrder("Remove");

            if (salesOrders == null || salesOrders.isEmpty()){
                return;
            }

            String choice = UserInputHandler.getConfirmation("Do you want to cancel the Sales Order? ");

            if (!choice.equalsIgnoreCase("Y")) {
                return;
            }
            System.out.println();
            for (SalesOrder salesOrder : salesOrders) {
                if (salesOrder.Remove()) {
                    System.out.println("Sales Order " + salesOrder.getDoc_No() + " with Item " + salesOrder.getItem().getItem_Name() +" cancelled successfully.");
                    //add mail notification here
                    MailSender mail;
                    Retailer retailer = new Retailer();
                    retailer.setUserId(salesOrder.getTransaction_Recipient());
                    retailer.Get();
                    mail = new MailSender(
                    retailer.getUserEmail(),
                    "Order Cancelled",
                    new MailTemplate(salesOrder.getItem().getItem_Name() + "in " + salesOrder.getDoc_No(), MailTemplate.TemplateType.SALES_ORDER_CANCELLATION));
                    mail.Send();
                } else {
                    System.out.println("Failed to cancel Sales Order " + salesOrder.getDoc_No() + ". Please try again.");
                }
                
            }
            ConsoleUI.pause();
        }
    }

    //display design methods
    private static void normalTableLine(){
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    
    
}
