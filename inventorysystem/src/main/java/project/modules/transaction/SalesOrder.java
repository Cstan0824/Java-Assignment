package project.modules.transaction;

import java.sql.Date;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import project.global.SqlConnector;
import project.global.SystemRunNo;
import project.modules.item.Item;
import project.view.ViewSalesManagement.OrderStatus;


public class SalesOrder extends Transaction {

    //Constructor
    public SalesOrder() {
        this.setTransaction_Mode(TransactionMode.STOCK_OUT);
        //Get current date
        this.setTransaction_Date(new Date(System.currentTimeMillis()));
    }

    public SalesOrder(String _DocNo, Item _item) {
        this.setTransaction_Mode(TransactionMode.STOCK_OUT);
        this.setDoc_No(_DocNo);
        this.setItem(_item);
        this.setSource_Doc_No(_DocNo);
        this.setTransaction_Date(new Date(System.currentTimeMillis()));
    }
    
    public SalesOrder(Item _item, String _Doc_No, Date _Transaction_Date, int _Quantity,
            String _Transaction_Recipient, String _Transaction_Created_By, String _Transaction_Modified_By) {
        super(_item, _Doc_No, _Transaction_Date, _Quantity, TransactionMode.STOCK_OUT,
                _Transaction_Recipient,
                _Transaction_Created_By, _Transaction_Modified_By);
    }

    //Setters and Getters inherited

    //Implementations of CRUD Operations
    @Override
    public boolean Add(){

        SqlConnector addSOConnector = new SqlConnector();
        addSOConnector.Connect();
        if (!addSOConnector.isConnected()) {
            return false;
        }

        String query = "INSERT INTO Transaction(Item_ID, Doc_No, Source_Doc_No, Transaction_Date, Quantity, Transaction_Mode, Transaction_Recipient, Transaction_Created_By, Transaction_Modified_By) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        boolean queryExecuted = addSOConnector.PrepareExecuteDML(query, this.getItem().getItem_ID(), this.getDoc_No(), this.getSource_Doc_No(), this.getTransaction_Date(), this.getQuantity(), this.getTransaction_Mode(), this.getTransaction_Recipient(), this.getTransaction_Created_By(), this.getTransaction_Modified_By());

        addSOConnector.Disconnect();

        return queryExecuted;
    }
        
    @Override
    public boolean Update() {

        SqlConnector updateSOConnector = new SqlConnector();
        updateSOConnector.Connect();
        if (!updateSOConnector.isConnected()) {
            return false;
        }

        String query = "UPDATE Transaction SET Quantity = ?, Transaction_Modified_By = ? WHERE Doc_No = ? AND Item_ID = ?";

        boolean queryExecuted = updateSOConnector.PrepareExecuteDML(query, this.getQuantity(), this.getTransaction_Modified_By(), this.getDoc_No(), this.getItem().getItem_ID());

        return queryExecuted;
    }

    @Override
    public boolean Remove() {

        SqlConnector removeSOConnector = new SqlConnector();
        removeSOConnector.Connect();
        if (!removeSOConnector.isConnected()) {
            return false;
        }

        String query = "DELETE FROM Transaction WHERE Doc_No = ? AND Item_ID = ?";
        boolean queryExecuted = removeSOConnector.PrepareExecuteDML(query, this.getDoc_No(), this.getItem().getItem_ID());


        return queryExecuted;
    }

    //search SO
    @Override
    public boolean Get() {

        Transaction salesOrder = SalesOrder.Get(this.getDoc_No(), this.getItem().getItem_ID());

        if (salesOrder == null) {
            return false;
        }

        this.setDoc_No(salesOrder.getDoc_No());
        this.setSource_Doc_No(salesOrder.getSource_Doc_No());
        this.setTransaction_Date(salesOrder.getTransaction_Date());
        this.setQuantity(salesOrder.getQuantity());
        this.setTransaction_Mode(salesOrder.getTransaction_Mode());
        this.setTransaction_Recipient(salesOrder.getTransaction_Recipient());
        this.setTransaction_Created_By(salesOrder.getTransaction_Created_By());
        this.setTransaction_Modified_By(salesOrder.getTransaction_Modified_By());


        return true;
    }

    public static SalesOrder Get(String _DocNo) {
        SqlConnector connector = new SqlConnector();
        connector.Connect();
        if (!connector.isConnected()) {
            return null;
        }

        String query = "SELECT * FROM Transaction WHERE Doc_No = ?";
        ArrayList<SalesOrder> salesOrders = connector.PrepareExecuteRead(query, SalesOrder.class, _DocNo);

        if (salesOrders != null && !salesOrders.isEmpty()) {
            SalesOrder salesOrder = salesOrders.get(0);
            connector.Disconnect();
            return salesOrder;
        }else {
            connector.Disconnect();
            return null;
        }

    }

    public static SalesOrder Get(String _DocNo, int _Item_ID) {
        SqlConnector connector = new SqlConnector();
        connector.Connect();
        if (!connector.isConnected()) {
            return null;
        }

        String query = "SELECT * FROM Transaction WHERE Doc_No = ? AND Item_ID = ?";
        ArrayList<SalesOrder> salesOrders = connector.PrepareExecuteRead(query, SalesOrder.class, _DocNo, _Item_ID);

        if (salesOrders != null && !salesOrders.isEmpty()) {
            SalesOrder salesOrder = salesOrders.get(0);
            connector.Disconnect();
            return salesOrder;
        }else {
            connector.Disconnect();
            return null;
        }

    }

    public static ArrayList<SalesOrder> GetAll(String _DocNo){


        SqlConnector getAllSOConnector = new SqlConnector();
        try {
            getAllSOConnector.Connect();
            if (!getAllSOConnector.isConnected()) {
                return null;
            }
            
            String query = "SELECT * FROM TRANSACTION WHERE DOC_NO = ?";
            ArrayList<SalesOrder> salesOrders = getAllSOConnector.PrepareExecuteRead(query, SalesOrder.class, _DocNo);
            if (salesOrders == null) {
                return new ArrayList<>();
            }
            return salesOrders;
        } finally {
            getAllSOConnector.Disconnect();
        }

    }
    //Get all SO (for display)
    public static ArrayList<SalesOrder> GetAll() {
        SqlConnector getAllSOConnector = new SqlConnector();
        try {
            getAllSOConnector.Connect();
            if (!getAllSOConnector.isConnected()) {
                return null;
            }
            
            String query = "SELECT * FROM TRANSACTION WHERE DOC_NO LIKE 'SO%';";
            ArrayList<SalesOrder> salesOrders = getAllSOConnector.PrepareExecuteRead(query, SalesOrder.class);
            if (salesOrders == null) {
                return new ArrayList<>();
            }
            return salesOrders;
        } finally {
            getAllSOConnector.Disconnect();
        }
        
    }

    public static ArrayList<SalesOrder> GetDistinctPendingSalesOrder(){

        SqlConnector connector = new SqlConnector();
        ArrayList<SalesOrder> salesOrders = new ArrayList<>();
        try {
            connector.Connect();
            if (!connector.isConnected()) {
                return null;
            }
            String[] DocNos = connector.getDistinctPendingDocNos("SO", "DO");
        
            for (String DocNo : DocNos) {
                SalesOrder salesOrder = SalesOrder.Get(DocNo);
                salesOrders.add(salesOrder);
            }
        } finally {
            connector.Disconnect();
        }
        return salesOrders;

    }
    
    public static ArrayList<SalesOrder> GetDistinctSalesOrder(){

        SqlConnector connector = new SqlConnector();
        ArrayList<SalesOrder> salesOrders = new ArrayList<>();
        try {
            connector.Connect();
            if (!connector.isConnected()) {
                return null;
            }
            String[] DocNos = connector.getDistinctDocNos("SO");
        
            for (String DocNo : DocNos) {
                SalesOrder salesOrder = SalesOrder.Get(DocNo);
                salesOrders.add(salesOrder);
            }
        } finally {
            connector.Disconnect();
        }
        return salesOrders;
    }
    
    public static ArrayList<SalesOrder> GetDistinctSalesOrder(String _Transaction_Recipient){

        SqlConnector connector = new SqlConnector();
        ArrayList<SalesOrder> salesOrders = new ArrayList<>();
        try {
            connector.Connect();
            if (!connector.isConnected()) {
                return null;
            }
            String[] DocNos = connector.getDistinctDocNos("SO", _Transaction_Recipient);
        
            for (String DocNo : DocNos) {
                SalesOrder salesOrder = SalesOrder.Get(DocNo);
                salesOrders.add(salesOrder);
            }
        } finally {
            connector.Disconnect();
        }
        return salesOrders;
    }

    public static ArrayList<SalesOrder> GetPassedSalesOrder(){

        SqlConnector connector = new SqlConnector();
        ArrayList<SalesOrder> salesOrders = new ArrayList<>();
        try {
            connector.Connect();
            if (!connector.isConnected()) {
                return null;
            }
            String[] DocNos = connector.getDistinctPassedSODocNos();
        
            for (String DocNo : DocNos) {
                SalesOrder salesOrder = SalesOrder.Get(DocNo);
                salesOrders.add(salesOrder);
            }
        } finally {
            connector.Disconnect();
        }
        return salesOrders;
    }
    

    //Generate Document Number
    @Override
    public String GenerateDocNo() {
        return "SO" + String.format("%05d", SystemRunNo.Get("SO"));
    }


    //Display SO (to String)
    @Override
    public String toString() {

        String format = "| %-15s | %-15s | %-15s | %-15s | %-15s | %-15s | %-15s |%n";
        this.getItem().Get();
        // Format the fields according to the placeholders
        return String.format(format,
                this.getDoc_No(),
                this.getItem().getItem_ID(),
                this.getItem().getItem_Name(),
                this.getQuantity(),
                this.getTransaction_Mode() + " (" + (this.getTransaction_Mode() == TransactionMode.STOCK_IN ? "IN" : "OUT") + ")",
                this.getTransaction_Date(),
                this.getTransaction_Recipient()
                );
    }

    public String distinctToString(AtomicInteger orderStatus){

        ArrayList<DeliveryOrder> deliveryOrders = DeliveryOrder.GetForStatus(this.getDoc_No());

        if (deliveryOrders != null && !deliveryOrders.isEmpty()) {
           orderStatus.set(OrderStatus.IN_PROCESS.getValue());
        }else{
            orderStatus.set(OrderStatus.PENDING.getValue());
        }

        ArrayList<SalesOrder> passedSalesOrders = SalesOrder.GetPassedSalesOrder();

        if (passedSalesOrders != null && !passedSalesOrders.isEmpty()) {
            for (SalesOrder salesOrder : passedSalesOrders) {
                if (salesOrder.getDoc_No().equals(this.getDoc_No())) {
                    orderStatus.set(2);
                    break;
                }
            }
        }
        
        String status = (orderStatus.get() == 0 ? "Pending" : (orderStatus.get() == 1 ? "In-Process" : "Delivered"));


        String format = "| %-15s | %-15s | %-15s | %-15s | %-15s | %-15s |%n";
        this.getItem().Get();
        // Format the fields according to the placeholders
        return String.format(format,
                this.getDoc_No(),
                this.getTransaction_Mode() + " (" + (this.getTransaction_Mode() == TransactionMode.STOCK_IN ? "IN" : "OUT") + ")",
                this.getTransaction_Date(),
                this.getTransaction_Recipient(),
                this.getTransaction_Created_By(), status
                );
    }
}