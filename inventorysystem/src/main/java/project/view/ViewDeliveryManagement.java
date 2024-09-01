package project.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import project.modules.schedule.Schedule;
import project.modules.transaction.DeliveryOrder;
import project.modules.transaction.SalesOrder;

public class ViewDeliveryManagement {
    
    private static final Scanner sc = new Scanner(System.in);

    public static void DisplayDistinctDeliveryOrder(){
        int totalDeliveryOrder = 0;
        
        ArrayList<DeliveryOrder> deliveryOrders = DeliveryOrder.GetDistinctDeliveryOrder();

        String[] columnNames = {"SO_No", "Mode", "Date", "Recipient", "Creator"};
        if (deliveryOrders != null && !deliveryOrders.isEmpty()) {
            System.out.println("Distinct Sales Order List");
            distinctTableLine();
            System.out.printf("|");
            for (String columnName : columnNames) {
                System.out.printf(" %-15s ", columnName);
                System.out.printf("|");
            }
            System.out.println("");
            distinctTableLine();
            for (DeliveryOrder deliveryOrder : deliveryOrders) {
                System.out.print(deliveryOrder.distinctToString());
                totalDeliveryOrder++;
                distinctTableLine();
            }

            System.out.println("Total Delivery Order(s) available in the database: " + totalDeliveryOrder);
        }else{
            System.out.println("No Delivery Order Found");
        }
        
    }
    
    public static void CreateDeliveryOrder() {
        
        

        DeliveryOrder DODocNo = new DeliveryOrder();
        String docNo = DODocNo.GenerateDocNo();

        try {
            System.out.println("Create Delivery Order");
            System.out.println("Enter Sales Order Doc No: ");
            String soDocNo = sc.nextLine();
            ArrayList<SalesOrder> salesOrders = SalesOrder.GetAll(soDocNo);
            
            if (salesOrders != null && !salesOrders.isEmpty()) { 

                for (SalesOrder salesOrder : salesOrders) {
                    DeliveryOrder deliveryOrder = new DeliveryOrder(docNo, salesOrder.getItem());
                    
                    deliveryOrder.setSource_Doc_No(soDocNo);
                    deliveryOrder.setTransaction_Date(new Date(System.currentTimeMillis()));
                    deliveryOrder.setQuantity(salesOrder.getQuantity());
                    deliveryOrder.setTransaction_Recipient("Customer");  // Customize as needed
                    deliveryOrder.setTransaction_Created_By("Admin");  // Customize as needed
                    deliveryOrder.setTransaction_Modified_By("Admin");  // Customize as needed
                    if (deliveryOrder.Add()) {
                        System.out.println("Delivery Order Added.");

                    } else {
                        System.out.println("Error adding Delivery Order.");
                    }
                }
            } else {
                System.out.println("Item not found. Please try again.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    
        DisplaySpecificDeliveryOrder(docNo);
        
    }

    public static void CreateSchedule(String _DocNo) {

        DeliveryOrder deliveryOrder = DeliveryOrder.Get(_DocNo);
        if (deliveryOrder != null) {

            Schedule schedule = new Schedule();
            schedule.setDeliveryOrder(deliveryOrder);
            

           
        } else {
            System.out.println("Delivery Order not found. Please try again.");
        }

    }
    
    public static void DisplaySpecificDeliveryOrder(String DODocNo){

        ArrayList<DeliveryOrder> deliveryOrders = DeliveryOrder.GetAll(DODocNo);

        String[] columnNames = {"DO No","Item_ID", "Item_Name", "Quantity", "Mode", "Date", "Recipient"};
        if (deliveryOrders != null && !deliveryOrders.isEmpty()) {
            System.out.println("Sales Order List for Doc No: " + DODocNo);
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
        }else{
            System.out.println("No Sales Order Found for Doc No: " + DODocNo);
        }

    }

    
    
    
    //display design methods
    private static void normalTableLine(){
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------");
    }

    private static void distinctTableLine(){
        System.out.println("-------------------------------------------------------------------------------------------");
    }
}
