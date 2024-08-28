package project.modules.transaction;

import java.sql.Date;
import java.util.ArrayList;

import project.global.SqlConnector;
import project.global.SystemRunNo;
import project.modules.item.Item;


public class DeliveryOrder extends Transaction {

    public DeliveryOrder() {
        this.setTransaction_Mode(TransactionMode.STOCK_OUT);
        this.setTransaction_Date(new Date(System.currentTimeMillis()));
    }

    public DeliveryOrder(String _DocNo) {
        this.setTransaction_Mode(TransactionMode.STOCK_OUT);
        this.setTransaction_Date(new Date(System.currentTimeMillis()));
        this.setDoc_No(_DocNo);
    }

    public DeliveryOrder(Item _item, String _Doc_No, Date _Transaction_Date, int _Quantity,
            String _Transaction_Recipient, String _Transaction_Created_By, String _Transaction_Modified_By) {
        super(_item, _Doc_No, _Transaction_Date, _Quantity, TransactionMode.STOCK_OUT,
                _Transaction_Recipient,
                _Transaction_Created_By, _Transaction_Modified_By);
    }

    //Setter and Getters inherited

    //Implementations of CRUD Operations
    @Override
    public boolean Add() {

        SqlConnector connector = new SqlConnector();
        connector.Connect();
        if (!connector.isConnected()) {
            return false;
        }

        String query = "INSERT INTO Transaction(Item_ID, Doc_No, Source_Doc_No, Transaction_Date, Quantity, Transaction_Mode, Transaction_Recipient, Transaction_Created_By, Transaction_Modified_By) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        boolean queryExecuted = connector.PrepareExecuteDML(query, this.getItem().getItem_ID(), this.getDoc_No(), this.getSource_Doc_No(), this.getTransaction_Date(), this.getQuantity(), this.getTransaction_Mode(), this.getTransaction_Recipient(), this.getTransaction_Created_By(), this.getTransaction_Modified_By());

        connector.Disconnect();

        return queryExecuted;

        
    }


    @Override
    public boolean Update() {

        SqlConnector updateSOConnector = new SqlConnector();
        updateSOConnector.Connect();
        if (!updateSOConnector.isConnected()) {
            return false;
        }

        String query = "UPDATE Transaction SET Item_ID = ?, Source_Doc_No = ?, Transaction_Date = ?, Quantity = ?, Transaction_Mode = ?, Transaction_Recipient = ?, Transaction_Created_By = ?, Transaction_Modified_By = ? WHERE Doc_No = ?";

        boolean queryExecuted = updateSOConnector.PrepareExecuteDML(query, this.getItem().getItem_ID(), this.getSource_Doc_No(), this.getTransaction_Date(), this.getQuantity(), this.getTransaction_Mode(), this.getTransaction_Recipient(), this.getTransaction_Created_By(), this.getTransaction_Modified_By(), this.getDoc_No());

        return queryExecuted;

    }

    @Override
    public boolean Remove() {

        SqlConnector connector = new SqlConnector();
        connector.Connect();
        if (!connector.isConnected()) {
            return false;
        }

        String query = "DELETE FROM Transaction WHERE Doc_No = ?";
        boolean queryExecuted = connector.PrepareExecuteDML(query, this.getDoc_No());


        return queryExecuted;

    }

    @Override
    public boolean Get() {

        Transaction deliveryOrder = DeliveryOrder.Get(this.getDoc_No());

        if (deliveryOrder == null) {
            return false;
        }

        this.setItem(deliveryOrder.getItem());
        this.setDoc_No(deliveryOrder.getDoc_No());
        this.setSource_Doc_No(deliveryOrder.getSource_Doc_No());
        this.setTransaction_Date(deliveryOrder.getTransaction_Date());
        this.setQuantity(deliveryOrder.getQuantity());
        this.setTransaction_Mode(deliveryOrder.getTransaction_Mode());
        this.setTransaction_Recipient(deliveryOrder.getTransaction_Recipient());
        this.setTransaction_Created_By(deliveryOrder.getTransaction_Created_By());
        this.setTransaction_Modified_By(deliveryOrder.getTransaction_Modified_By());


        return true;
    }

    public static DeliveryOrder Get(String _DocNo) {

        SqlConnector connector = new SqlConnector();
        connector.Connect();
        if (!connector.isConnected()) {
            return null;
        }

        String query = "SELECT * FROM Transaction WHERE Doc_No = ?";
        ArrayList<DeliveryOrder> deliveryOrders = connector.PrepareExecuteRead(query, DeliveryOrder.class, _DocNo);

        if (deliveryOrders != null && !deliveryOrders.isEmpty()) {
            DeliveryOrder deliveryOrder = deliveryOrders.get(0);
            connector.Disconnect();
            return deliveryOrder;
        }else {
            connector.Disconnect();
            return null;
        }

        
    }

    
    public ArrayList<DeliveryOrder> GetAll() {

        SqlConnector connector = new SqlConnector();
        try {
            connector.Connect();
            if (!connector.isConnected()) {
                return null;
            }
            
            String query = "SELECT * FROM TRANSACTION WHERE DOC_NO LIKE 'DO%';";
            ArrayList<DeliveryOrder> deliveryOrders = connector.PrepareExecuteRead(query, DeliveryOrder.class);
            if (deliveryOrders == null) {
                return new ArrayList<>();
            }
            return deliveryOrders;
        } finally {
            connector.Disconnect();
        }
    }


    //Generate Doc No
    @Override
    public String GenerateDocNo() {
        return "DO" + String.format("%05d", SystemRunNo.Get("DO"));
    }
}
