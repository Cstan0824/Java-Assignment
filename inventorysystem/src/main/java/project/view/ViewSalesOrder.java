package project.view;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import project.global.ConsoleUI;
import project.modules.transaction.SalesOrder;
import project.modules.user.Admin;
import project.modules.user.Retailer;
import project.modules.user.User;


public class ViewSalesOrder {
    
    private static final Scanner sc = new Scanner(System.in);  
    private static User user;
    private final ArrayList<Integer> orderStatusList = new ArrayList<>();
    private ArrayList<SalesOrder> salesOrderList;
    private ArrayList<SalesOrder> pendingSalesOrderList;

    public ViewSalesOrder(User user) {
        ViewSalesOrder.user = user;
        if (user instanceof Admin) {
            salesOrderList = SalesOrder.GetDistinctSalesOrder();
            pendingSalesOrderList = SalesOrder.GetDistinctPendingSalesOrder();
        } else if (user instanceof Retailer) {
            salesOrderList = SalesOrder.GetDistinctSalesOrder(user.getUserId());
            pendingSalesOrderList = null;
        } else {
            salesOrderList = null;
            pendingSalesOrderList = null;
        }
    }

    public User getUser() {
        return user;
    }

    public ArrayList<SalesOrder> getSalesOrderList() {
        return salesOrderList;
    }

    

    public ArrayList<SalesOrder> selectSalesOrderFromList() {
        orderStatusList.clear();
        int totalSalesOrder = 0;
        if (user instanceof Admin) {
            this.salesOrderList = SalesOrder.GetDistinctSalesOrder();
        }
        else if (user instanceof Retailer) {
            this.salesOrderList = SalesOrder.GetDistinctSalesOrder(user.getUserId());
        }
        String[] columnNames = {"SO_No", "Mode", "Date", "Recipient", "Creator", "Order Status"};

        if (salesOrderList != null && !salesOrderList.isEmpty() ){

            System.out.println("Sales Order List");
            distinctTableLine();
            System.out.printf("|");
            for (String columnName : columnNames) {
                System.out.printf(" %-15s ", columnName);
                System.out.printf("|");
            }
            System.out.println("");
            distinctTableLine();
            for (SalesOrder salesOrder : salesOrderList) {
                AtomicInteger orderStatus = new AtomicInteger(); 
                System.out.print(salesOrder.distinctToString(orderStatus));
                totalSalesOrder++;
                distinctTableLine();
                orderStatusList.add(orderStatus.get());
            }

            System.out.println("Total Sales Order available in the database: " + totalSalesOrder);

            ArrayList<SalesOrder> selectedSalesOrderList;

            do{
                System.out.print("Enter Sales Order Doc No: ");
                String docNo = sc.nextLine();

                selectedSalesOrderList = SalesOrder.GetAll(docNo);

                if (selectedSalesOrderList != null && !selectedSalesOrderList.isEmpty()) {
                    //display and save into array list
                    String[] selectedColumnNames = {"SO No","Item_ID", "Item_Name", "Quantity", "Mode", "Date", "Recipient"};
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
                    for (SalesOrder salesOrder : selectedSalesOrderList) {
                        System.out.print(salesOrder.toString());
                        normalTableLine();
                    }
                    ConsoleUI.pause();
                    return selectedSalesOrderList;
                } else {
                    System.out.println("No Sales Order found with the given Doc No. Please try again.");
                }
            } while (selectedSalesOrderList == null ||selectedSalesOrderList.isEmpty());
        }

        return null;
    }
    
    public ArrayList<SalesOrder> selectPendingSalesOrder(String mode) {
        orderStatusList.clear();
        this.pendingSalesOrderList = SalesOrder.GetDistinctPendingSalesOrder();
        int totalSalesOrder = 0;

        String[] columnNames = {"SO_No", "Mode", "Date", "Recipient", "Creator", "Order Status"};

        if (pendingSalesOrderList != null && !pendingSalesOrderList.isEmpty() ){

            System.out.println("Pending Sales Order List");
            distinctTableLine();
            System.out.printf("|");
            for (String columnName : columnNames) {
                System.out.printf(" %-15s ", columnName);
                System.out.printf("|");
            }
            System.out.println("");
            distinctTableLine();
            for (SalesOrder salesOrder : pendingSalesOrderList) {
                AtomicInteger orderStatus = new AtomicInteger(); 
                System.out.print(salesOrder.distinctToString(orderStatus));
                totalSalesOrder++;
                distinctTableLine();
                orderStatusList.add(orderStatus.get());
            }

            System.out.println("Total Pending Sales Order available in the database: " + totalSalesOrder);

            ArrayList<SalesOrder> selectedSalesOrderList;

            do{
                switch (mode) {
                    case "createDO":
                        System.out.print("\nEnter Pending Sales Order Doc_No to create Delivery Order: ");
                        break;
                    case "Modify":
                        System.out.print("\nEnter Pending Sales Order Doc_No to Modify: ");
                        break;
                    case "Remove":
                        System.out.print("\nEnter Pending Sales Order Doc_No to Cancel: ");
                        break;
                    default:
                        break;
                }
                
                String docNo = sc.nextLine();

                selectedSalesOrderList = SalesOrder.GetAll(docNo);

                if (selectedSalesOrderList != null && !selectedSalesOrderList.isEmpty()) {
                    //display and save into array list
                    String[] selectedColumnNames = {"SO No","Item_ID", "Item_Name", "Quantity", "Mode", "Date", "Recipient"};
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
                    for (SalesOrder salesOrder : selectedSalesOrderList) {
                        System.out.print(salesOrder.toString());
                        normalTableLine();
                    }
                    ConsoleUI.pause();
                    return selectedSalesOrderList;
                } else {
                    System.out.println("No Pending Sales Order found with the given Doc_No. Please try again.");
                    
                }
            } while (selectedSalesOrderList == null ||selectedSalesOrderList.isEmpty());
        }else {
            System.out.println("No Pending Sales Order found.");
            ConsoleUI.pause();
        }

        return null;

    }

    //display design methods
    private static void normalTableLine(){
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    private static void distinctTableLine(){
        System.out.println("-------------------------------------------------------------------------------------------------------------");
    }
}
