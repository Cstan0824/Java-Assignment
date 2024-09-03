package project.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import project.global.UserInputHandler;
import project.modules.item.Item;
import project.modules.transaction.DeliveryOrder;
import project.modules.transaction.SalesOrder;
import project.modules.user.User;



//use by retailer to order stock, check their order records
//use by admin to view SO list, receive SO and Create DO
public class ViewSalesManagement {

    private static final Scanner sc = new Scanner(System.in);
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
            System.out.println("\n\nSales Management");
            System.out.println("1. View Sales Order Records");
            System.out.println("2. Create Delivery Order");
            System.out.println("3. Modify Sales Order");
            System.out.println("4. Cancel Sales Order");
            System.out.println("5. Exit");
            System.out.println("Choose your actions: ");
            int choice = sc.nextInt();
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
                    System.exit(1);
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
            System.out.println("\n\nOrder Management");
            System.out.println("1. Order Stock");
            System.out.println("2. View Order Records");
            System.out.println("3. Exit");
            System.out.println("Choose your actions: ");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    createSalesOrder();
                    break;
                case 2:
                    viewOrderRecords();
                    break;
                case 3:
                    System.exit(1);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }




    }

    private void viewOrderRecords() {
        if (user.getUserType().equals("Admin") || user.getUserType().equals("Retailer")) {
            boolean error = false;
            do {
                ArrayList<SalesOrder> salesOrders = viewSalesOrder.selectSalesOrderFromList();
                if (salesOrders == null || salesOrders.isEmpty()){
                    System.out.println("No Sales Order found.");
                    error = true;
                }
            }while (error);

        }
         
    }


    //for admin to create DO
    private void createDeliveryOrder() {

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
                    System.out.println("Delivery Order for " + deliveryOrder.getItem().getItem_Name() + " is added.");
                } else {
                    System.out.println("Error adding Delivery Order.");
                }


            }

            //display created DO
            ArrayList<DeliveryOrder> deliveryOrders = DeliveryOrder.GetAll(docNo);

            String[] columnNames = {"DO No","Item_ID", "Item_Name", "Quantity", "Mode", "Date", "Recipient"};
            if (deliveryOrders != null && !deliveryOrders.isEmpty()) {
                System.out.println("Delivery Order List for Doc No: " + docNo);
                normalTableLine();
                System.out.printf("|");
                for (String columnName : columnNames) {
                    System.out.printf(" %-15s ", columnName);
                    System.out.printf("|");
                }
                System.out.println("");
                normalTableLine();
                for (DeliveryOrder deliveryOrder : deliveryOrders) {
                    System.out.print(deliveryOrder.toString());
                    normalTableLine();
                }
            }

        }

        

    }

    //for retailer to order stock
    private void createSalesOrder() {
        if (user.getUserType().equals("Retailer")) {
            
            ArrayList<SalesOrder> newOrders = new ArrayList<>();
            String docNo = new SalesOrder().GenerateDocNo();

            do {
            Item item = viewItem.selectItemFromList();
            SalesOrder salesOrder = new SalesOrder(docNo, item);
            salesOrder.setQuantity(getValidatedQuantity());
            salesOrder.setTransaction_Recipient(user.getUserId());
            salesOrder.setTransaction_Modified_By(user.getUserId());
            salesOrder.setTransaction_Created_By(user.getUserId());

            newOrders.add(salesOrder);
            

            } while (UserInputHandler.getConfirmation("Do you want to continue adding item? [Y/N]: ")
                .equalsIgnoreCase("Y"));

        // Add all purchase orders to the database
        newOrders.forEach(SalesOrder::Add);

        }
    }
    
    //for admin to modify pending SO
    private void orderModification() {
        if (user.getUserType().equals("Admin")) {
            boolean error = false;
            ArrayList<SalesOrder> salesOrders = viewSalesOrder.selectPendingSalesOrder("Modify");

            if (salesOrders == null || salesOrders.isEmpty()){
                return;
            }
            //modify SO
            do {
                do { 
                    System.out.println("Enter Item ID: "); 
                    int itemId = sc.nextInt(); 
                    sc.nextLine(); 

                    Item item = new Item(itemId);
                    item.Get();

                    SalesOrder salesOrder = new SalesOrder(salesOrders.get(0).getDoc_No(), item);

                    if(salesOrder.Get()){
                        String choice;
                        do{
                            System.out.println("Enter New Item Quantity: ");
                            int quantity = sc.nextInt();
                            sc.nextLine(); // Consume the newline character
                            salesOrder.setQuantity(quantity);

                            salesOrder.setTransaction_Modified_By(user.getUserId());

                            if (salesOrder.getQuantity() <= 0) {
                                System.out.println("Quantity is zero or negative. Do you want to delete this item from the Sales Order? (Y/N)");

                                choice = sc.nextLine();

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

                            System.out.println("Sales Order " + salesOrders.get(0).getDoc_No() + " with item " + salesOrder.getItem().getItem_Name() + " updated successfully.");

                            //add mail notification here
                        }

                    } else {
                        System.out.println("Item with ID " + itemId + " not found in Sales Order " + salesOrders.get(0).getDoc_No() + ".");
                        System.out.println("Please re-enter the item ID.");
                        error = true;

                    }
                } while (error);
            } while (UserInputHandler.getConfirmation("Do you want to continue edit sales order? [Y/N]: ").equalsIgnoreCase("Y"));
        }
    }

    //for admin to cancel pending SO
    private void orderCancellation() {

        if (user.getUserType().equals("Admin")) {
            
            ArrayList<SalesOrder> salesOrders = viewSalesOrder.selectPendingSalesOrder("Remove");

            if (salesOrders == null || salesOrders.isEmpty()){
                return;
            }

            System.out.println("Do you want to cancel the Sales Order? [Y/N]: ");
            sc.nextLine();
            String choice = sc.nextLine();

            if (choice.equalsIgnoreCase("Y")) {
                for (SalesOrder salesOrder : salesOrders) {
                    if (salesOrder.Remove()) {
                        System.out.println("Sales Order " + salesOrder.getDoc_No() + " with Item " + salesOrder.getItem().getItem_Name() +" cancelled successfully.");
                    } else {
                        System.out.println("Failed to cancel Sales Order " + salesOrder.getDoc_No() + ". Please try again.");
                    }
                }
            } else {
                System.out.println("Sales Order " + salesOrders.get(0).getDoc_No() + " not cancelled.");
            }


        }



    }

    //display design methods
    private static void normalTableLine(){
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------");
    }

    private int getValidatedQuantity() {
        int quantity;
        do {
            System.out.print("Enter Quantity (must be greater than 0): ");
            while (!sc.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid number.");
                sc.next(); // Consume the invalid input
            }
            quantity = sc.nextInt();
            sc.nextLine(); // Consume newline
            if (quantity <= 0) {
                System.out.println("Quantity must be greater than 0.");
            }
        } while (quantity <= 0);
        return quantity;
    }

    
    
}
