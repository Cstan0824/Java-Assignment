package project.view;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import project.global.MailSender;
import project.global.MailTemplate;
import project.global.UserInputHandler;
import project.modules.transaction.PurchaseOrder;
import project.modules.user.User;
import project.view.ViewPurchaseManagement.StockStatus;

public class ViewPurchaseOrder {
    private static User user;
    private final ArrayList<Integer> orderStatusList = new ArrayList<>();
    private ArrayList<PurchaseOrder> purchaseOrderList;
    private MailSender mail = null;


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

    public MailSender getMail() {
        return this.mail;
    }

    public ArrayList<PurchaseOrder> getPurchaseOrderList() {
        return this.purchaseOrderList;
    }

    public void setPurchaseOrderList() {
        this.purchaseOrderList = PurchaseOrder.GetAll();
    }

    public void setPurchaseOrderList(ArrayList<PurchaseOrder> _purchaseOrderList) {
        this.purchaseOrderList = _purchaseOrderList;
    }

    // Display and select purchase orders
    public ArrayList<PurchaseOrder> selectPurchaseOrderFromList() {
        orderStatusList.clear();
        ArrayList<PurchaseOrder> selectedOrders = new ArrayList<>();
        ArrayList<Integer> selectedStatuses = new ArrayList<>();
        Set<String> displayedOrders = new HashSet<>();

        // Print header
        System.out.println(" =========================================================== Purchase Orders ================================================================== ");
        System.out.println(String.format("| %-20s | %-20s | %-40s | %-15s | %-15s | %-15s |",
                "Order ID","Order Date", "Item Name", "Order Quantity", "Order Recipient", "Order Status"));
        System.out.println(" ============================================================================================================================================== ");

        // Display purchase orders
        purchaseOrderList.forEach(order -> {
            order.getItem().Get();
            AtomicInteger orderStatus = new AtomicInteger(); 
            //display the order details and if the PO already displayed, skip and only display its item details 
            String orderDetails = order.toString(orderStatus);
            String status = (orderStatus.get() == 2) ? "Received" : (orderStatus.get() == 1) ? "In-Process" : "Pending";

            if (displayedOrders.add(order.getDoc_No())) {
                System.out.print(orderDetails);
            } else {
                System.out.println(String.format("| %-20s   %-20s | %-40s | %-15s | %-15s | %-15s |",
                        "", "", order.getItem().getItem_Name(), order.getQuantity(), order.getTransaction_Recipient(),
                        status));
            }
            orderStatusList.add(orderStatus.get());
        });
                System.out.println(" ============================================================================================================================================== ");


        // Select a purchase order by ID
        String selectedOrderID = UserInputHandler.getString("Select Purchase Order by Order ID"
                , 7, "^PO[0-9]{5}$");

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
        ArrayList<PurchaseOrder> purchaseOrders = new ArrayList<>();
        Set<String> appearedOrders = new HashSet<>();
        for (PurchaseOrder order : this.purchaseOrderList) {
            purchaseOrders.add(order);

            order.getItem().Get();
            AtomicInteger orderStatus = new AtomicInteger();
            order.toString(orderStatus);

            if (orderStatus.get() != _OrderStatus.getValue()) {
                appearedOrders.add(order.getDoc_No());
            }
        }
        for (String order : appearedOrders) {
            purchaseOrders.removeIf(purchaseOrder -> purchaseOrder.getDoc_No().equals(order));
        }
        this.setPurchaseOrderList(purchaseOrders);


        // Print header
        System.out.println(
                " =========================================================== Purchase Orders ================================================================== ");
        System.out.println(String.format("| %-20s | %-20s | %-40s | %-15s | %-15s | %-15s |",
                "Order ID", "Order Date", "Item Name", "Order Quantity", "Order Recipient", "Order Status"));
        System.out.println(
                " ============================================================================================================================================== ");

        // Display purchase orders
        this.purchaseOrderList.forEach(order -> {
            order.getItem().Get();
            //000AH: Use AtomicInteger to store order status
            AtomicInteger orderStatus = new AtomicInteger(); // Placeholder for order status
            String orderDetails = order.toString(orderStatus);
            if (_OrderStatus.getValue() != orderStatus.get()) {
                return; //similiar to continue in for loop
            }

            String status = (orderStatus.get() == 2) ? "Received" : (orderStatus.get() == 1) ? "In-Process" : "Pending";
            if (appearedOrders.add(order.getDoc_No())) {
                System.out.print(orderDetails);
            } else {
                System.out.println(String.format("| %-20s   %-20s | %-40s | %-15s | %-15s | %-15s |",
                        "", "", order.getItem().getItem_Name(), order.getQuantity(), order.getTransaction_Recipient(),
                        status));
            }
        });
        System.out.println(
                " ============================================================================================================================================== ");

        // Select a purchase order by ID
        String selectedOrderID = UserInputHandler.getString("Select Purchase Order by Order ID", 7, "^PO[0-9]{5}$");

        purchaseOrders = PurchaseOrder.Get(selectedOrderID);
        if (purchaseOrders == null || purchaseOrders.isEmpty()) {
            System.out.println("No purchase order found with the specified ID.");
            return null;
        }
        return purchaseOrders;
    }

    // Follow up on the status of a purchase order
    public void followUpStatus(ArrayList<PurchaseOrder> _purchaseOrders) {
        Set<String> vendorID = new HashSet<>();
        _purchaseOrders.forEach(order -> {
            order.getItem().Get();
            vendorID.add(order.getTransaction_Recipient());
        });
        System.out.println("Following up on the status of the purchase order...");
        
        //Send email to vendor
        vendorID.forEach(vendor -> {
            this.mail = new MailSender(
                    "tanc8803@gmail.com",
                    "Follow Up Order Status",
                    new MailTemplate(_purchaseOrders.get(0).getDoc_No(),
                            MailTemplate.TemplateType.FOLLOW_ORDER_STATUS));
            mail.Send();
        });
    }
}
