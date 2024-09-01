package project.view;

import java.util.ArrayList;
import java.util.Scanner;

import project.modules.transaction.DeliveryOrder;
import project.modules.user.User;

public class ViewDeliveryOrder {
    
    private static final Scanner sc = new Scanner(System.in);  
    private static User user;
    private final ArrayList<DeliveryOrder> deliveryOrderList;

    public ViewDeliveryOrder(User user) {
        ViewDeliveryOrder.user = user;
        deliveryOrderList = DeliveryOrder.GetDistinctDeliveryOrder();
    }

    public User getUser() {
        return user;
    }

    public ArrayList<DeliveryOrder> getDeliveryOrderList() {
        return deliveryOrderList;
    }

    public ArrayList<DeliveryOrder> selectDeliveryOrderFromList() {

        int totalSalesOrder = 0;

        String[] columnNames = {"DO_No", "Mode", "Date", "Recipient", "Creator"};

        if (deliveryOrderList != null && !deliveryOrderList.isEmpty() ){

            System.out.println("Distinct Delivery Order List");
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
               
                distinctTableLine();
            }

            System.out.println("Total Sales Order available in the database: " + totalSalesOrder);

            ArrayList<DeliveryOrder> selectedDeliveryOrderList;

            do{
                System.out.println("Enter Sales Order Doc No: ");
                String docNo = sc.nextLine();

                selectedDeliveryOrderList = DeliveryOrder.GetAll(docNo);

                if (selectedDeliveryOrderList != null && !selectedDeliveryOrderList.isEmpty()) {
                    //display and save into array list
                    String[] selectedColumnNames = {"SO No","Item_ID", "Item_Name", "Quantity", "Mode", "Date", "Recipient"};
                    System.out.println("Sales Order List found for Doc No: " + docNo);
                    normalTableLine();
                    System.out.printf("|");
                    for (String columnName : selectedColumnNames) {
                        System.out.printf(" %-15s ", columnName);
                        System.out.printf("|");
                    }
                    System.out.println("");
                    normalTableLine();
                    for (DeliveryOrder DO : selectedDeliveryOrderList) {
                        System.out.print(DO.toString());
                        normalTableLine();
                    }

                    return selectedDeliveryOrderList;
                } else {
                    System.out.println("No Delivery Order found with the given Doc No. Please try again.");
                }
            } while (selectedDeliveryOrderList == null ||selectedDeliveryOrderList.isEmpty());
        }

        return null;
    }
    
    


    //display design methods
    private static void normalTableLine(){
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------");
    }

    private static void distinctTableLine(){
        System.out.println("-------------------------------------------------------------------------------------------");
    }
}
