package project.modules.transaction;

import java.sql.Date;

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

    @Override
    public boolean Update() {

        SqlConnector updateSOConnector = new SqlConnector();
        updateSOConnector.Connect();
        if (!updateSOConnector.isConnected()) {
            return false;
        }

        String query = "UPDATE Transaction SET Item_ID = ?, Source_Doc_No = ?, Transaction_Date = ?, Quantity = ?, Transaction_Mode = ?, Transaction_Recipient = ?, Transaction_Created_By = ?, Transaction_Modified_By = ? WHERE Doc_No = ?";

        boolean queryExecuted = updateSOConnector.PrepareExecuteDML(query, this.getItem(), this.getSource_Doc_No(), this.getTransaction_Date(), this.getQuantity(), this.getTransaction_Mode(), this.getTransaction_Recipient(), this.getTransaction_Created_By(), this.getTransaction_Modified_By(), this.getDoc_No());

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

    @Override
    public boolean Get() {

        
        

        return true;
    }


    //Generate Document Number
    @Override
    public String GenerateDocNo() {
        return "SO" + String.format("%05d", SystemRunNo.Get("SO"));
    }

}