package project.modules.transaction;

import java.sql.Date;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import project.global.SqlConnector;
import project.global.SystemRunNo;
import project.modules.item.Item;





public class PurchaseOrder extends Transaction {

    @Override
    //Done
    public boolean Add() {

        SqlConnector connector = new SqlConnector();
        connector.Connect();
        if (!connector.isConnected()) {
            return false;
        }

        String query = "INSERT INTO Transaction(Item_ID, Doc_No, Transaction_Date, Quantity, Transaction_Mode, Transaction_Recipient, Transaction_Created_By, Transaction_Modified_By) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        boolean QueryExecuted = connector.PrepareExecuteDML(query,
                this.getItem().getItem_ID(), this.getDoc_No(),
                this.getTransaction_Date(), this.getQuantity(), this.getTransaction_Mode(),
                this.getTransaction_Recipient(),
                this.getTransaction_Created_By(), this.getTransaction_Modified_By());

        connector.Disconnect();

        return QueryExecuted;
    }

    //Only change vendor or item will use this method
    //Item_ID, Transaction_date, Quantity, Transaction_Receipent
    //Done
    @Override
    public boolean Update() {

        ArrayList<GoodReceivedNotes> goodReceiveNotes = GoodReceivedNotes.Get(this.getDoc_No(),
                GoodReceivedNotes.DocumentType.PURCHASE_ORDER);

        //Check whether the good receive note is exist or not
        if (goodReceiveNotes != null && !goodReceiveNotes.isEmpty()) {
            //The stock is already reiceived
            return false;
        }
        Transaction oldPurchaseOrder = new PurchaseOrder(this.getItem(), this.getDoc_No());

        if (!oldPurchaseOrder.Get()) {
            return false;
        }

        SqlConnector connector = new SqlConnector();
        connector.Connect();

        if (!connector.isConnected()) {
            return false;
        }
        String query;
        boolean QueryExecuted;
        if (this.getItem().getItem_ID() == oldPurchaseOrder.getItem().getItem_ID()) {
            query = "UPDATE Transaction SET  Transaction_Date = ?, Quantity = ?, Transaction_Recipient = ?, Transaction_Modified_By = ? WHERE Doc_No = ?";
            QueryExecuted = connector.PrepareExecuteDML(query,
                    this.getTransaction_Date(),
                    this.getQuantity(),
                    this.getTransaction_Recipient(),
                    this.getTransaction_Modified_By(),
                    this.getDoc_No());
        } else {
            query = "UPDATE Transaction SET Item_ID = ?, Transaction_Date = ?, Quantity = ?, Transaction_Recipient = ?, Transaction_Modified_By = ? WHERE Doc_No = ?";
            QueryExecuted = connector.PrepareExecuteDML(query,
                    this.getItem().getItem_ID(), this.getTransaction_Date(),
                    this.getQuantity(),
                    this.getTransaction_Recipient(),
                    this.getTransaction_Modified_By(),
                    this.getDoc_No());
        }
        connector.Disconnect();
        return QueryExecuted;
    }

    @Override
    //Done
    //need to check if already received the stock or not
    public boolean Remove() {
        ArrayList<GoodReceivedNotes> goodReceiveNotes = GoodReceivedNotes.Get(this.getDoc_No(),
                GoodReceivedNotes.DocumentType.PURCHASE_ORDER);

        //Check whether the good receive note is exist or not
        if (goodReceiveNotes != null && !goodReceiveNotes.isEmpty()) {
            //The stock is already reiceived
            return false;
        }

        SqlConnector connector = new SqlConnector();
        connector.Connect();

        if (!connector.isConnected()) {
            return false;
        }
        String query = "DELETE FROM Transaction WHERE Doc_No = ? and Item_ID = ?";

        boolean QueryExecuted = connector.PrepareExecuteDML(query, this.getDoc_No(), this.getItem().getItem_ID());

        connector.Disconnect();

        return QueryExecuted;
    }

    @Override
    //Check the stock status and ask user whether they want to proceed with the stock[send mail to the vendor for follow up status]
    //Done
    public boolean Get() {
        SqlConnector connector = new SqlConnector();
        connector.Connect();
        if (!connector.isConnected()) {
            return false;
        }

        String query = "SELECT * FROM Transaction WHERE Doc_No = ? AND Item_ID = ? ORDER BY Doc_No";
        ArrayList<PurchaseOrder> purchaseOrders = connector.PrepareExecuteRead(query, PurchaseOrder.class,
                this.getDoc_No(),
                this.getItem().getItem_ID());
        connector.Disconnect();

        if (purchaseOrders == null || purchaseOrders.isEmpty()) {
            return false;
        }
        Transaction purchaseOrder = purchaseOrders.get(0);

        //initialize the value
        purchaseOrder.getItem().Get();
        this.setItem(purchaseOrder.getItem());
        this.setDoc_No(purchaseOrder.getDoc_No());
        this.setSource_Doc_No(purchaseOrder.getSource_Doc_No());
        this.setTransaction_Date(purchaseOrder.getTransaction_Date());
        this.setQuantity(purchaseOrder.getQuantity());
        this.setTransaction_Mode(purchaseOrder.getTransaction_Mode());
        this.setTransaction_Recipient(purchaseOrder.getTransaction_Recipient());
        this.setTransaction_Created_By(purchaseOrder.getTransaction_Created_By());
        this.setTransaction_Modified_By(purchaseOrder.getTransaction_Modified_By());

        return true;
    }

    @Override
    public String GenerateDocNo() {
        return "PO" + String.format("%05d", SystemRunNo.Get("PO"));
    }

    public String toString(AtomicInteger StockStatus_) {
        //000AH: Use AtomicInteger to store order status
        super.getItem().Get();

        // Determine the stock status by checking GoodReceivedNotes
        ArrayList<GoodReceivedNotes> goodReceiveNotes = GoodReceivedNotes.Get(this.getDoc_No(),
                GoodReceivedNotes.DocumentType.PURCHASE_ORDER);
        int VirtualStock = this.getQuantity();
        int OnHandStock = 0;

        if (goodReceiveNotes != null && !goodReceiveNotes.isEmpty()) {
            for (Transaction goodReceiveNote : goodReceiveNotes) {
                goodReceiveNote.getItem().Get();
                if(goodReceiveNote.getItem().getItem_ID() != this.getItem().getItem_ID()){
                    continue;
                }
                OnHandStock += goodReceiveNote.getQuantity();
            }
        }

        if (OnHandStock >= VirtualStock) {
            StockStatus_.set(2); // Received
        } else if (OnHandStock > 0 && OnHandStock < VirtualStock) {
            StockStatus_.set(1); // In-Process
        } else {
            StockStatus_.set(0); // Pending
        }

        // Use StockStatus_ to generate the status string
        String status = (StockStatus_.get() == 2) ? "Received" : (StockStatus_.get() == 1) ? "In-Process" : "Pending";


        // Define a format string with placeholders for the important column values
        String format = "| %-20s | %-20s | %-40s | %-15s | %-15s | %-15s |\n";
        String value = String.format(format,
                this.getDoc_No(),
                this.getTransaction_Date(),
                this.getItem().getItem_Name(),
                this.getQuantity(),
                this.getTransaction_Recipient(),
                status);
        return value;
    }

    


    public static Transaction Get(Item _item, String _DocNo) {
        SqlConnector connector = new SqlConnector();
        connector.Connect();
        if (!connector.isConnected()) {
            return null;
        }

        String query = "SELECT * FROM Transaction WHERE Doc_No = ? AND Item_ID = ? ORDER BY Doc_No";
        ArrayList<PurchaseOrder> purchaseOrders = connector.PrepareExecuteRead(query, PurchaseOrder.class, _DocNo,
                _item.getItem_ID());
        connector.Disconnect();

        if (purchaseOrders == null || purchaseOrders.isEmpty()) {
            return null;
        }
        purchaseOrders.get(0).setItem(_item);
        return purchaseOrders.get(0);
    }

    public static ArrayList<PurchaseOrder> Get(String _DocNo) {
        SqlConnector connector = new SqlConnector();
        connector.Connect();
        if (!connector.isConnected()) {
            return null;
        }

        String query = "SELECT * FROM Transaction WHERE Doc_No = ? ORDER BY Doc_No";
        ArrayList<PurchaseOrder> purchaseOrders = connector.PrepareExecuteRead(query, PurchaseOrder.class, _DocNo);
        connector.Disconnect();

        return purchaseOrders;
    }

    public static ArrayList<PurchaseOrder> GetAll() {
        SqlConnector connector = new SqlConnector();
        connector.Connect();
        if (!connector.isConnected()) {
            return null;
        }

        String query = "SELECT * FROM Transaction WHERE DOC_NO LIKE 'PO%' ORDER BY Doc_No";
        ArrayList<PurchaseOrder> purchaseOrders = connector.ExecuteRead(query, PurchaseOrder.class);

        if (purchaseOrders == null || purchaseOrders.isEmpty()) {
            return null;
        }

        purchaseOrders.forEach(purchaseOrder -> {
            purchaseOrder.getItem().Get();
        });

        connector.Disconnect();
        return purchaseOrders;
    }
    
    @Override
    public String toString() {
        super.getItem().Get();

        //I want to print the status whether the stock is received or not
        ArrayList<GoodReceivedNotes> goodReceiveNotes = GoodReceivedNotes.Get(this.getDoc_No(),
                GoodReceivedNotes.DocumentType.PURCHASE_ORDER);
        int VirtualStock = this.getQuantity();
        int OnHandStock = 0;

        if (goodReceiveNotes != null && !goodReceiveNotes.isEmpty()) {
            for (Transaction goodReceiveNote : goodReceiveNotes) {
                OnHandStock += goodReceiveNote.getQuantity();
            }
        }

        String status = (OnHandStock == VirtualStock) ? "Received" : (OnHandStock > 0) ? "In-Process" : "Pending";

        // Define a format string with placeholders for the important column values
        String format = "| %-15s | %-15s | %-10s | %-10s | %-10s |\n";
        String value = String.format(format,
                this.getItem().getItem_Name(),
                this.getDoc_No(),
                this.getTransaction_Date(),
                this.getQuantity(),
                status);
        // Format the fields according to the placeholders
        return value;
    }

    //Constructor
    public PurchaseOrder() {
        super.setTransaction_Mode(TransactionMode.STOCK_IN);
        //Get current date
        super.setTransaction_Date(new Date(System.currentTimeMillis()));

    }

    public PurchaseOrder(String _DocNo) {
        super.setTransaction_Mode(TransactionMode.STOCK_IN);
        super.setDoc_No(_DocNo);
        super.setTransaction_Date(new Date(System.currentTimeMillis()));

    }

    public PurchaseOrder(Item _item, String _DocNo) {
        super.setTransaction_Mode(TransactionMode.STOCK_IN);
        super.setDoc_No(_DocNo);
        super.setItem(_item);
        super.getItem().Get();
        super.setTransaction_Date(new Date(System.currentTimeMillis()));

    }

    public PurchaseOrder(Item _item, String _Doc_No, Date _Transaction_Date, int _Quantity,
            String _Transaction_Recipient, String _Transaction_Created_By, String _Transaction_Modified_By) {
        super(_item, _Doc_No, _Transaction_Date, _Quantity, TransactionMode.STOCK_IN,
                _Transaction_Recipient,
                _Transaction_Created_By, _Transaction_Modified_By);
    }

}
