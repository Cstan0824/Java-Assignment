package project.view;

import java.util.ArrayList;
import java.util.Scanner;

import project.global.ConsoleUI;
import project.modules.transaction.DeliveryOrder;
import project.modules.user.Admin;
import project.modules.user.Retailer;
import project.modules.user.User;

public class ViewDeliveryOrder {
    
    private static final Scanner sc = new Scanner(System.in);  
    private static User user;
    private ArrayList<DeliveryOrder> deliveryOrderList;
    private ArrayList<DeliveryOrder> pendingDeliveryOrderList;

    public ViewDeliveryOrder(User user) {
        ViewDeliveryOrder.user = user;
        if (user instanceof Admin) {
            deliveryOrderList = DeliveryOrder.GetDistinctDeliveryOrder();
            pendingDeliveryOrderList = DeliveryOrder.GetDistinctPendingDeliveryOrder();
        } else if (user instanceof Retailer) {
            deliveryOrderList = DeliveryOrder.GetDistinctDeliveryOrder(user.getUserId());
            pendingDeliveryOrderList = null;
        } else {
            deliveryOrderList = null;
            pendingDeliveryOrderList = null;
        }
        
    }

    public User getUser() {
        return user;
    }

    public ArrayList<DeliveryOrder> getDeliveryOrderList() {
        return deliveryOrderList;
    }

    public ArrayList<DeliveryOrder> selectDeliveryOrderFromList() {
        if (user instanceof Admin) {
            this.deliveryOrderList = DeliveryOrder.GetDistinctDeliveryOrder();
        } else if (user instanceof Retailer) {
            this.deliveryOrderList = DeliveryOrder.GetDistinctDeliveryOrder(user.getUserId());
        }

        int totalSalesOrder = 0;

        String[] columnNames = {"DO_No", "Mode", "Date", "Recipient", "Creator"};

        if (deliveryOrderList != null && !deliveryOrderList.isEmpty() ){

            System.out.println("Delivery Order List");
            distinctTableLine();
            System.out.printf("|");
            for (String columnName : columnNames) {
                System.out.printf(" %-15s ", columnName);
                System.out.printf("|");
            }
            System.out.println("");
            distinctTableLine();
            for (DeliveryOrder DO : deliveryOrderList) {
                System.out.print(DO.distinctToString());
                totalSalesOrder++;
                distinctTableLine();
            }

            System.out.println("Total Delivery Order available in the database: " + totalSalesOrder);

            ArrayList<DeliveryOrder> selectedDeliveryOrderList;

            do{
                System.out.print("Enter Delivery Order Doc No: ");
                String docNo = sc.nextLine();

                selectedDeliveryOrderList = DeliveryOrder.GetAll(docNo);

                if (selectedDeliveryOrderList != null && !selectedDeliveryOrderList.isEmpty()) {

                    if (user instanceof Retailer) {
                        if (!selectedDeliveryOrderList.get(0).getTransaction_Recipient().equals(user.getUserId())) {
                            return null;
                        }
                    }
                    
                    //display and save into array list
                    String[] selectedColumnNames = {"DO No","Item_ID", "Item_Name", "Quantity", "Mode", "Date", "Recipient"};
                    System.out.println("\n\nSales Order List found for Doc No: " + docNo);
                    normalTableLine();
                    System.out.printf("|");
                    for (String columnName : selectedColumnNames) {
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
                    for (DeliveryOrder DO : selectedDeliveryOrderList) {
                        System.out.print(DO.toString());
                        normalTableLine();
                    }
                    ConsoleUI.pause();
                    return selectedDeliveryOrderList;
                } else {
                    System.out.println("No Delivery Order found with the given Doc No. Please try again.");
                    
                }
            } while (selectedDeliveryOrderList == null ||selectedDeliveryOrderList.isEmpty());
        }

        return null;
    }
    
    public ArrayList<DeliveryOrder> selectPendingDeliveryOrder(){

        this.pendingDeliveryOrderList = DeliveryOrder.GetDistinctPendingDeliveryOrder();

        int totalPendingOrder = 0;

        String[] columnNames = {"DO_No", "Mode", "Date", "Recipient", "Creator"};

        if (pendingDeliveryOrderList != null && !pendingDeliveryOrderList.isEmpty() ){

            System.out.println("Pending Delivery Order List");
            distinctTableLine();
            System.out.printf("|");
            for (String columnName : columnNames) {
                System.out.printf(" %-15s ", columnName);
                System.out.printf("|");
            }
            System.out.println("");
            distinctTableLine();
            for (DeliveryOrder DO : pendingDeliveryOrderList) {
                System.out.print(DO.distinctToString());
                totalPendingOrder++;
                distinctTableLine();
            }

            System.out.println("Total Pending Delivery Order available in the database: " + totalPendingOrder);

            ArrayList<DeliveryOrder> selectedDeliveryOrderList;

            do{
                System.out.print("Enter Delivery Order Doc No: ");
                String docNo = sc.nextLine();

                selectedDeliveryOrderList = DeliveryOrder.GetAll(docNo);

                if (selectedDeliveryOrderList != null && !selectedDeliveryOrderList.isEmpty()) {
                    //display and save into array list
                    String[] selectedColumnNames = {"DO No","Item_ID", "Item_Name", "Quantity", "Mode", "Date", "Recipient"};
                    System.out.println("\n\nDelivery Order List found for Doc No: " + docNo);
                    normalTableLine();
                    System.out.printf("|");
                    for (String columnName : selectedColumnNames) {
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
                    for (DeliveryOrder DO : selectedDeliveryOrderList) {
                        System.out.print(DO.toString());
                        normalTableLine();
                    }
                    ConsoleUI.pause();
                    return selectedDeliveryOrderList;
                } else {
                    System.out.println("No Delivery Order found with the given Doc No. Please try again.");
                }
            } while (selectedDeliveryOrderList == null ||selectedDeliveryOrderList.isEmpty());
        }else{
            System.out.println("No pending Delivery Order found.");
            ConsoleUI.pause();
        }

        return null;

    }

    
    //display design methods
    private static void normalTableLine(){
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    private static void distinctTableLine(){
        System.out.println("-------------------------------------------------------------------------------------------");
    }
}
