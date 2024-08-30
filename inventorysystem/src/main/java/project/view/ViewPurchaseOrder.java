package project.view;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import project.global.MailSender;
import project.global.MailTemplate;
import project.modules.transaction.PurchaseOrder;
import project.modules.user.User;

public class ViewPurchaseOrder {
    private static User user;
    private static final Scanner scanner = new Scanner(System.in);
    private final ArrayList<Integer> OrderStatusList = new ArrayList<>();
    private final ArrayList<PurchaseOrder> purchaseOrderList = PurchaseOrder.GetAll();

    //getter
    public User getUser() {
        return user;
    }

    public ArrayList<Integer> getOrderStatusList() {
        return this.OrderStatusList;
    }

    public ArrayList<PurchaseOrder> getPurchaseOrderList() {
        return this.purchaseOrderList;
    }

    public ViewPurchaseOrder(User _user) {
        user = _user;
    }

    public ArrayList<PurchaseOrder> SelectPurchaseOrderFromList() {
        ArrayList<PurchaseOrder> purchaseOrders = new ArrayList<>();
        ArrayList<Integer> orderStatuses = new ArrayList<>();
        AtomicInteger i = new AtomicInteger(1);
        AtomicInteger j = new AtomicInteger(1);

        //Header in String.format
        System.out.println(
                String.format("| %-20s | %-20s | %-20s | %-15s | %-15s | %-15s | %-15s | %-15s | %-15s |", "Order ID",
                        "Item Name", "Item Description", "Item Quantity", "Item Price", "Order Date", "Order Mode",
                        "Order Recipient", "Order Created By"));

        //Display Purchase Order List - display distinct
        this.purchaseOrderList.forEach((PurchaseOrder _purchaseOrder) -> {
            _purchaseOrder.getItem().Get();
            System.out.println(i.getAndIncrement());
            Integer OrderStatus = 0; // temp value
            System.out.println(_purchaseOrder.toString(OrderStatus));
            OrderStatusList.add(OrderStatus);
        });

        //Select Purchase Order
        System.out.print("Select Purchase Order: ");
        String orderID = scanner.nextLine();

        //Display Purchase Order Details
        this.purchaseOrderList.forEach((PurchaseOrder _purchaseOrder) -> {
            j.getAndIncrement();
            if (_purchaseOrder.getDoc_No().equals(orderID)) {
                purchaseOrders.add(_purchaseOrder);
                orderStatuses.add(OrderStatusList.get(j.get()));
            }
        });

        this.OrderStatusList.clear();
        this.OrderStatusList.addAll(orderStatuses);
        //return the purchase order ***
        return purchaseOrders;
    }

    public void FollowUpStatus(PurchaseOrder _purchaseOrder) {
        MailSender mail = new MailSender("tancs8803@gmail.com", "Follow Up Order Status",
                new MailTemplate(_purchaseOrder.getDoc_No(), MailTemplate.TemplateType.FOLLOW_ORDER_STATUS));
        mail.Send();
    }
}
