package project.view;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import project.global.MailSender;
import project.global.MailTemplate;
import project.modules.transaction.PurchaseOrder;
import project.modules.user.User;
import project.view.ViewPurchaseManagement.StockStatus;

public class ViewPurchaseOrder {
    private static User user;
    private static final Scanner scanner = new Scanner(System.in);
    private final ArrayList<Integer> orderStatusList = new ArrayList<>();
    private final ArrayList<PurchaseOrder> purchaseOrderList;


    // Constructor
    public ViewPurchaseOrder(User user) {
        ViewPurchaseOrder.user = user;
        this.purchaseOrderList = PurchaseOrder.GetAll();
    }

    // Getters
    public User getUser() {
        return user;
    }

    public ArrayList<Integer> getOrderStatusList() {
        return this.orderStatusList;
    }

    public ArrayList<PurchaseOrder> getPurchaseOrderList() {
        return this.purchaseOrderList;
    }

    // Display and select purchase orders
    public ArrayList<PurchaseOrder> selectPurchaseOrderFromList() {
        ArrayList<PurchaseOrder> selectedOrders = new ArrayList<>();
        ArrayList<Integer> selectedStatuses = new ArrayList<>();
        AtomicInteger index = new AtomicInteger(1);

        // Print header
        System.out.println(String.format("| %-20s | %-20s | %-20s | %-15s | %-15s | %-15s | %-15s | %-15s | %-15s |",
                "Order ID", "Item Name", "Item Description", "Item Quantity", "Item Price", "Order Date",
                "Order Mode", "Order Recipient", "Order Created By"));

        // Display purchase orders
        purchaseOrderList.forEach(order -> {
            order.getItem().Get();
            System.out.print(index.getAndIncrement() + " ");
            AtomicInteger orderStatus = new AtomicInteger(); // Placeholder for order status
            System.out.print(order.toString(orderStatus));
            System.out.println("Order Status: " + orderStatus);
            orderStatusList.add(orderStatus.get());
        });

        // Select a purchase order by ID
        System.out.print("Select Purchase Order by Order ID: ");
        String selectedOrderID = scanner.nextLine();

        // Filter selected order(s)
        AtomicInteger orderIndex = new AtomicInteger(0);
        purchaseOrderList.forEach(order -> {
            if (order.getDoc_No().equals(selectedOrderID)) {
                selectedOrders.add(order);
                selectedStatuses.add(orderStatusList.get(orderIndex.get()));
            }
            orderIndex.getAndIncrement();
        });

        // Update order status list with selected statuses
        orderStatusList.clear();
        orderStatusList.addAll(selectedStatuses);

        return selectedOrders;
    }
    
    public ArrayList<PurchaseOrder> selectPurchaseOrderFromList(StockStatus _OrderStatus) {
        ArrayList<PurchaseOrder> selectedOrders = new ArrayList<>();
        ArrayList<Integer> selectedStatuses = new ArrayList<>();
        AtomicInteger index = new AtomicInteger(1);

        // Print header
        System.out.println(String.format("| %-20s | %-20s | %-20s | %-15s | %-15s | %-15s | %-15s | %-15s | %-15s |",
                "Order ID", "Item Name", "Item Description", "Item Quantity", "Item Price", "Order Date", 
                "Order Mode", "Order Recipient", "Order Created By"));

        // Display purchase orders
        purchaseOrderList.forEach(order -> {
            order.getItem().Get();
            
            AtomicInteger orderStatus = new AtomicInteger(); // Placeholder for order status
            String orderDetails = order.toString(orderStatus);
            if (orderStatus.get() == _OrderStatus.getValue()) {
                System.out.print(index.getAndIncrement() + " ");
                System.out.print(orderDetails);
                orderStatusList.add(orderStatus.get());
            }
        });

        // Select a purchase order by ID
        System.out.print("Select Purchase Order by Order ID: ");
        String selectedOrderID = scanner.nextLine();

        // Filter selected order(s)
        AtomicInteger orderIndex = new AtomicInteger(0);
        ArrayList<PurchaseOrder> purchaseOrders = PurchaseOrder.Get(selectedOrderID);
        if (purchaseOrders == null || purchaseOrders.isEmpty()) {
            System.out.println("No purchase order found with the specified ID.");
            return null;
        }

        purchaseOrders.forEach(order -> {
            order.getItem().Get();
            if (order.getDoc_No().equals(selectedOrderID)) {
                selectedOrders.add(order);
                selectedStatuses.add(orderStatusList.get(orderIndex.get()));
            }
            orderIndex.getAndIncrement();
        });

        // Update order status list with selected statuses
        orderStatusList.clear();
        orderStatusList.addAll(selectedStatuses);

        return selectedOrders;
    }

    // Follow up on the status of a purchase order
    public void followUpStatus(PurchaseOrder order) {
        MailSender mail = new MailSender(
                "tancs8803@gmail.com",
                "Follow Up Order Status",
                new MailTemplate(order.getDoc_No(), MailTemplate.TemplateType.FOLLOW_ORDER_STATUS)
        );
        mail.Send();
    }
}
