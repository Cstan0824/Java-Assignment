package project.modules.transaction;

import java.util.ArrayList;

import project.global.SqlConnector;
import project.global.SystemRunNo;

public class GoodReceivedNotes extends Transaction {
    @Override
    public boolean Add() {
        SqlConnector connector = new SqlConnector();

        connector.Connect();

        if (!connector.isConnected()) {
            return false;
        }

        String query = "INSERT INTO Transaction (Item_ID, Doc_No, Transaction_Date, Quantity, Transaction_Mode, Transaction_Receipient, Transaction_Created_By, Transaction_Modified_By) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

        boolean QueryExecuted = connector.PrepareExecuteDML(query,
                super.getItem().getItem_ID(), super.getDoc_No(),
                super.getTransaction_Date(), super.getQuantity(), super.getTransaction_Mode(),
                super.getTransaction_Receipient(),
                super.getTransaction_Created_By(), super.getTransaction_Modified_By());

        connector.Disconnect();

        return QueryExecuted;
    }

    @Override //Human Error[wrong item quantity]
    public boolean Update() {
        SqlConnector connector = new SqlConnector();

        connector.Connect();

        if (!connector.isConnected()) {
            return false;
        }

        String query = "UPDATE Transaction SET Item_ID = ?, Transaction_Date = ?, Quantity = ?, Transaction_Mode = ?, Transaction_Receipient = ?, Transaction_Created_By = ?, Transaction_Modified_By = ? WHERE Doc_No = ?;";

        boolean QueryExecuted = connector.PrepareExecuteDML(query,
                super.getItem().getItem_ID(),
                super.getTransaction_Date(), super.getQuantity(), super.getTransaction_Mode(),
                super.getTransaction_Receipient(),
                super.getTransaction_Created_By(), super.getTransaction_Modified_By(),
                super.getDoc_No());

        connector.Disconnect();
        return QueryExecuted;
    }
    
    @Override//Human Error[]
    public boolean Remove() {
        SqlConnector connector = new SqlConnector();

        connector.Connect();

        if (!connector.isConnected()) {
            return false;
        }

        String query = "DELETE FROM Transaction WHERE Doc_No = ?;";

        boolean QueryExecuted = connector.PrepareExecuteDML(query, super.getDoc_No());

        connector.Disconnect();

        return QueryExecuted;
    }

    @Override
    public boolean Get() {
        SqlConnector connector = new SqlConnector();

        connector.Connect();

        if (!connector.isConnected()) {
            return false;
        }

        String query = "SELECT * FROM Transaction WHERE Doc_No = ?;";

        ArrayList<Transaction> purchaseOrder = connector.PrepareExecuteRead(query, Transaction.class,
                super.getDoc_No());

        if (purchaseOrder == null || purchaseOrder.isEmpty()) {
            return false;
        }

        Transaction transaction = purchaseOrder.get(0);
        super.setItem(transaction.getItem());
        super.setTransaction_Date(transaction.getTransaction_Date());
        super.setQuantity(transaction.getQuantity());
        super.setTransaction_Mode(transaction.getTransaction_Mode());
        super.setTransaction_Receipient(transaction.getTransaction_Receipient());
        super.setTransaction_Created_By(transaction.getTransaction_Created_By());
        super.setTransaction_Modified_By(transaction.getTransaction_Modified_By());

        return true;
    }
    
    @Override 
    public String GenerateDocNo() {
        return "GRN" + SystemRunNo.Get("GRN");
    }
    
    //Constructor
    public GoodReceivedNotes() {
        super.setTransaction_Mode(TransactionMode.STOCK_IN);
    }

    public GoodReceivedNotes(String _Doc_No) {
        super.setDoc_No(_Doc_No);
        super.setTransaction_Mode(1);
    }

    
}
