package project.view;

import java.util.ArrayList;
import java.util.Scanner;

import project.modules.item.Item;
import project.modules.transaction.PurchaseOrder;
import project.modules.transaction.SalesOrder;


public class ViewTransaction {

    private final Scanner scanner = new Scanner(System.in);

    public void Menu() {
        int choice;
        do {
            System.out.println("1. Restock Item");
            System.out.println("2. View Purchase Record");
            System.out.println("3. View Sales Record");
            System.out.println("4. Back");

            //Accept user input
            System.out.print("Enter choice: ");
            //Scanner scanner = new Scanner(System.in);
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    RestockItem();
                    break;
                case 2:
                    ViewPurchaseHistory();
                    break;
                case 3:
                    //View Sales Record
                    ViewSalesRecord();
                    break;
                case 4:
                    System.out.println("Back");
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        } while (choice != 4);
    }

    private void RestockItem() {
        System.out.println("Restock Item");
        ArrayList<Item> items = Item.GetAll();
        items.get(0).getItem_ID();//display

        System.out.print("Enter item ID: ");
        int itemId = scanner.nextInt();
        scanner.nextLine();

        Item item = new Item(itemId);

        if (item.getItem_ID() == 0) {
            System.out.println("Item not found");
            return;
        }

        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setDoc_No(purchaseOrder.GenerateDocNo());
        purchaseOrder.setItem(item);
        purchaseOrder.setQuantity(quantity);

        //Double confirm with user
        System.out.println(purchaseOrder.toString());

        //assume that the Goods will received after 3 days
    }

    private void ViewPurchaseHistory() {
        //View Purchase Record
        ArrayList<PurchaseOrder> purchaseOrderList = PurchaseOrder.GetAll();
        if (purchaseOrderList == null || purchaseOrderList.isEmpty()) {
            return;
        }
        purchaseOrderList.forEach(_purchaseOrder -> {
            System.out.println(_purchaseOrder.toString());
        });
        //Display purchase order with status "Pending" or "Received"
    }

    private void ViewSalesRecord() {
        //View Sales Record
        //Display sales record with status "Pending" or "Received"
        ArrayList<SalesOrder> salesOrderList = SalesOrder.GetAll();
        if (salesOrderList == null || salesOrderList.isEmpty()) {
            return;
        }
        salesOrderList.forEach(_salesOrder -> {
            System.out.println(_salesOrder.toString());
        });
    }
    
    

}
