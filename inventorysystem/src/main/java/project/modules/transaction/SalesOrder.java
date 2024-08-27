package project.modules.transaction;

import java.sql.Date;
import java.util.ArrayList;

import project.global.SqlConnector;
import project.global.SystemRunNo;
import project.modules.item.Item;
public class SalesOrder extends Transaction {

    //Constructor
    public SalesOrder() {
        this.setTransaction_Mode(TransactionMode.STOCK_OUT);
        //Get current date
        this.setTransaction_Date(new Date(System.currentTimeMillis()));
    }

    public SalesOrder(String _DocNo) {
        this.setTransaction_Mode(TransactionMode.STOCK_OUT);
        this.setDoc_No(_DocNo);
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

    public void addMultipleItemSO(ArrayList<Transaction> _salesOrders) {
        

        _salesOrders.forEach(_salesOrder -> {
            _salesOrder.Add();    

        });

        

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

        SqlConnector removeSOConnector = new SqlConnector();
        removeSOConnector.Connect();
        if (!removeSOConnector.isConnected()) {
            return false;
        }

        String query = "DELETE FROM Transaction WHERE Doc_No = ?";
        boolean queryExecuted = removeSOConnector.PrepareExecuteDML(query, this.getDoc_No());


        return queryExecuted;
    }

    //search SO
    @Override
    public boolean Get() {

        Transaction salesOrder = SalesOrder.Get(this.getDoc_No());

        if (salesOrder == null) {
            return false;
        }

        this.setItem(salesOrder.getItem());
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


    //Generate Document Number
    @Override
    public String GenerateDocNo() {
        return "SO" + String.format("%05d", SystemRunNo.Get("SO"));
    }

}