package project.view;


import java.util.ArrayList;
import java.util.Scanner;

import project.modules.item.Item;
import project.modules.transaction.SalesOrder;

public class ViewSalesOrder {
    private static final Scanner sc = new Scanner(System.in);
    //action menu
    public static void ActionMenu(String selectedSO) {

        String action;

        do {
            System.out.println("Choose an action:");
            System.out.println("1. Edit Sales Order");
            System.out.println("2. Delete Sales Order");
            System.out.println("3. Return to Main Menu");

            action = sc.nextLine();

            switch (action) {
                case "1":
                    EditSalesOrder(selectedSO);
                    break;
                case "2":
                    //DeleteSalesOrder(selectedSO);
                    break;
                case "3":
                    break;
                default:
                    System.out.println("Invalid action. Please try again.");
                    break;
            }
        } while (!action.equals("3"));

    }
    
    public static void DisplayAllSalesOrder(){
        int totalSalesOrder = 0;
        
        ArrayList<SalesOrder> salesOrders = SalesOrder.GetAll();

        String[] columnNames = {"SO_No","Item_ID", "Item_Name", "Quantity", "Mode", "Date", "Recipient"};
        if (salesOrders != null && !salesOrders.isEmpty()) {
            System.out.println("Sales Order List");
            normalTableLine();
            System.out.printf("|");
            for (String columnName : columnNames) {
                System.out.printf(" %-15s ", columnName);
                System.out.printf("|");
            }
            System.out.println("");
            normalTableLine();
            for (SalesOrder salesOrder : salesOrders) {
                System.out.print(salesOrder.toString());
                totalSalesOrder++;
                normalTableLine();
            }

            System.out.println("Total Sales Order available in the database: " + totalSalesOrder);
        }else{
            System.out.println("No Sales Order Found");
        }

        
    }

    public static void DisplayDistinctSalesOrder(){
        int totalSalesOrder = 0;
        
        ArrayList<SalesOrder> salesOrders = SalesOrder.GetDistinctSalesOrder();

        String[] columnNames = {"SO_No", "Mode", "Date", "Recipient", "Creator"};
        if (salesOrders != null && !salesOrders.isEmpty()) {
            System.out.println("Distinct Sales Order List");
            distinctTableLine();
            System.out.printf("|");
            for (String columnName : columnNames) {
                System.out.printf(" %-15s ", columnName);
                System.out.printf("|");
            }
            System.out.println("");
            distinctTableLine();
            for (SalesOrder salesOrder : salesOrders) {
                System.out.print(salesOrder.distinctToString());
                totalSalesOrder++;
                distinctTableLine();
            }

            System.out.println("Total Sales Order available in the database: " + totalSalesOrder);
        }else{
            System.out.println("No Sales Order Found");
        }
        
    }

    public static String SelectSalesOrder(){
       
            System.out.println("Enter Sales Order Doc No: ");
            String docNo = sc.nextLine();
            return docNo;
        
        
    }   

    public static void DisplaySpecificSalesOrder(String SODocNo){

        ArrayList<SalesOrder> salesOrders = SalesOrder.GetAll(SODocNo);

        String[] columnNames = {"SO No","Item_ID", "Item_Name", "Quantity", "Mode", "Date", "Recipient"};
        if (salesOrders != null && !salesOrders.isEmpty()) {
            System.out.println("Sales Order List for Doc No: " + SODocNo);
            normalTableLine();
            System.out.printf("|");
            for (String columnName : columnNames) {
                System.out.printf(" %-15s ", columnName);
                System.out.printf("|");
            }
            System.out.println("");
            normalTableLine();
            for (SalesOrder salesOrder : salesOrders) {
                System.out.print(salesOrder.toString());
                normalTableLine();
            }
        }else{
            System.out.println("No Sales Order Found for Doc No: " + SODocNo);
        }

    }

    //for creating DO use
    public static void DisplayPendingSalesOrder(){

        ArrayList<SalesOrder> salesOrders = SalesOrder.GetPendingSalesOrder();

        String[] columnNames = {"SO No","Item_ID", "Item_Name", "Quantity", "Mode", "Date", "Recipient"};
        if (salesOrders != null && !salesOrders.isEmpty()) {
            System.out.println("Pending Sales Order List");
            normalTableLine();
            System.out.printf("|");
            for (String columnName : columnNames) {
                System.out.printf(" %-15s ", columnName);
                System.out.printf("|");
            }
            System.out.println("");
            normalTableLine();
            for (SalesOrder salesOrder : salesOrders) {
                System.out.print(salesOrder.toString());
                normalTableLine();
            }
        }else{
            System.out.println("No Pending Sales Order Found");
        }



    }

    //retailer add sales order
    public static void CreateSalesOrder() {
        
            System.out.println("Create Sales Order");
            SalesOrder SODocNo = new SalesOrder();
            String docNo = SODocNo.GenerateDocNo();
            boolean continueAddItem;
    
            do {
                try {
                    System.out.println("Enter Item ID: ");
                    int itemId = sc.nextInt();
                    sc.nextLine(); // Consume the newline character
                    Item item = new Item(itemId);
                    
                    if (item.Get()) {
                        
                        System.out.println("Enter Quantity: ");
                        int quantity = sc.nextInt();
                        sc.nextLine(); 
    
                        SalesOrder salesOrder = new SalesOrder(docNo, item);
                        salesOrder.setSource_Doc_No(docNo);
                        salesOrder.setQuantity(quantity);
                        salesOrder.setTransaction_Recipient("Customer");
                        salesOrder.setTransaction_Created_By("Admin");
                        salesOrder.setTransaction_Modified_By("Admin");
    
                        if (salesOrder.Add()) {
                            System.out.println("Sales Order added successfully.");
                        } else {
                            System.out.println("Failed to add Sales Order.");
                        }
                    } else {
                        System.out.println("Item not found. Please try again.");
                    }
                } catch (Exception e) {
                    System.out.println("An error occurred: " + e.getMessage());
                }
    
                System.out.println("Do you want to add another item to the order? (Y/N)");
                continueAddItem = sc.nextLine().equalsIgnoreCase("Y");
    
            } while (continueAddItem);
    
        
    }
    
    public static boolean EditSalesOrder(String selectedSO) {

       
            System.out.println("Edit Sales Order");
            ArrayList<SalesOrder> salesOrders = SalesOrder.GetAll(selectedSO);
    
            if (salesOrders != null && !salesOrders.isEmpty()) {
    
                boolean continueEditItem = false;
    
                do {
                    System.out.println("Enter Item ID: "); 
                    int itemId = sc.nextInt(); 
                    sc.nextLine(); 

                    Item item = new Item(itemId);
                    item.Get();

                    SalesOrder salesOrder = new SalesOrder(selectedSO, item);

                    if(salesOrder.Get()){

                        System.out.println("Enter New Item Quantity: ");
                        int quantity = sc.nextInt();
                        sc.nextLine(); // Consume the newline character
                        salesOrder.setQuantity(quantity);

                        salesOrder.setTransaction_Modified_By("Admin");

                        if (salesOrder.getQuantity() <= 0) {
                            System.out.println("Quantity is zero or negative. Do you want to delete this item from the Sales Order? (Y/N)");

                            if (sc.nextLine().equalsIgnoreCase("Y")) {
                                if (salesOrder.Remove()) {
                                    System.out.println("Item removed from Sales Order successfully.");
                                } else {
                                    System.out.println("Failed to remove item from Sales Order.");
                                }
                            }

                        } else if (salesOrder.Update()) {

                            System.out.println("Sales Order " + selectedSO + " updated successfully.");
                            System.out.println("Do you want to edit another item in the Sales Order? (Y/N)");
                            continueEditItem = sc.nextLine().equalsIgnoreCase("Y");

                        } else {
                            System.out.println("Failed to update Sales Order " + selectedSO + ".");
                        }
                    } else {
                        System.out.println("Item with ID " + itemId + " not found in Sales Order " + selectedSO + ".");
                    }
                } while (continueEditItem);
            } else {
                System.out.println("Sales Order " + selectedSO + " not found.");
            }
    
       return true;
    }
    
    
    
    
    
    
    
    //display design methods
    private static void normalTableLine(){
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------");
    }

    private static void distinctTableLine(){
        System.out.println("-------------------------------------------------------------------------------------------");
    }
}
    

