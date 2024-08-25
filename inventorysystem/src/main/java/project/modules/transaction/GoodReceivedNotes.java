package project.modules.transaction;

import java.util.ArrayList;

import project.global.MailSender;
import project.global.MailTemplate;
import project.global.SqlConnector;
import project.global.SystemRunNo;

public class GoodReceivedNotes extends Transaction {
    @Override
    //update PO row - OnHandStock
    //if the amount not same then send email to update the status with vendor
    //Done
    public boolean Add() {
        //Connect to Sql
        SqlConnector connector = new SqlConnector();
        connector.Connect();

        if (!connector.isConnected()) {
            return false;
        }

        String query = "INSERT INTO Transaction (Item_ID, Doc_No, Transaction_Date, Quantity, Transaction_Mode, Transaction_Receipient, Transaction_Created_By, Transaction_Modified_By) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

        boolean QueryExecuted = connector.PrepareExecuteDML(query,
                this.getItem().getItem_ID(), this.getDoc_No(),
                this.getTransaction_Date(), this.getVirtualStock(),
                this.getOnHandStock(), this.getTransaction_Mode(),
                this.getTransaction_Receipient(),
                this.getTransaction_Created_By(), this.getTransaction_Modified_By());
        connector.Disconnect();

        if (QueryExecuted == false) {
            return false;
        }
        

        //IDK HOW TO GET DOCNO
        String _DocNo = "";
        //Update PO row
        Transaction purchaseOrder = new PurchaseOrder(_DocNo);
        purchaseOrder.Get();
        
        if (purchaseOrder.getVirtualStock() == purchaseOrder.getOnHandStock()) {
            //The Stock are all on hand
            return false;
        } else if (purchaseOrder.getVirtualStock() < (purchaseOrder.getOnHandStock() + this.getOnHandStock())) {
            //The Stock amount exceed the actual amount
            return false;
        }

        //Update OnHandStock
        purchaseOrder.setOnHandStock(purchaseOrder.getOnHandStock() + this.getOnHandStock());
        purchaseOrder.Update();


        int currStock = purchaseOrder.getVirtualStock();
        if (currStock == (purchaseOrder.getOnHandStock() + this.getOnHandStock())) {
            //Correct Amount
        } else if (currStock > (purchaseOrder.getOnHandStock() + this.getOnHandStock())) {
            //The Amount is not enough
        }

        //Send Email
        MailSender mail = new MailSender("tancs8803@gmail.com", "Order Confirmation",
                new MailTemplate(this.getDoc_No(), MailTemplate.TemplateType.ORDER_CONFIRMATION));

        mail.Send();
        return true;
    }

    @Override
    //Human Error[wrong item quantity]
    //Send email to vendor if the amount is invalid
    //Update OnHandStock
    public boolean Update() {
        //Old GRN
        Transaction OldGoodReceivedNote = new GoodReceivedNotes(this.getDoc_No());
        OldGoodReceivedNote.Get();

        int diffOnHandStock = OldGoodReceivedNote.getOnHandStock() - this.getOnHandStock();
        this.setOnHandStock(this.getOnHandStock() + diffOnHandStock);

        //Old PO
        String PO = ""; //idk how to get PO
        Transaction OldPurchaseOrder = new PurchaseOrder(PO);
        OldPurchaseOrder.Get();

        OldPurchaseOrder.setOnHandStock(OldPurchaseOrder.getOnHandStock() + diffOnHandStock);
        OldPurchaseOrder.Update();
        //check if the amount is correct or not enough
        if (OldPurchaseOrder.getOnHandStock() == OldPurchaseOrder.getVirtualStock()) {
            
        } else if (OldPurchaseOrder.getVirtualStock() > OldPurchaseOrder.getOnHandStock()) {
            
        }



        //Sql Connector
        SqlConnector connector = new SqlConnector();

        connector.Connect();

        if (!connector.isConnected()) {
            return false;
        }

        String query = "UPDATE Transaction SET Item_ID = ?, Transaction_Date = ?, Quantity = ?, Transaction_Mode = ?, Transaction_Receipient = ?, Transaction_Created_By = ?, Transaction_Modified_By = ? WHERE Doc_No = ?;";

        boolean QueryExecuted = connector.PrepareExecuteDML(query,
                this.getItem().getItem_ID(),
                this.getTransaction_Date(), this.getOnHandStock(), this.getTransaction_Mode(),
                this.getTransaction_Receipient(),
                this.getTransaction_Created_By(), this.getTransaction_Modified_By(),
                this.getDoc_No());

        connector.Disconnect();
        

        return QueryExecuted;
    }
    
    @Override
    //Human Error[]
    public boolean Remove() {
        SqlConnector connector = new SqlConnector();

        connector.Connect();

        if (!connector.isConnected()) {
            return false;
        }

        String query = "DELETE FROM Transaction WHERE Doc_No = ?;";

        boolean QueryExecuted = connector.PrepareExecuteDML(query, this.getDoc_No());

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
                this.getDoc_No());

        if (purchaseOrder == null || purchaseOrder.isEmpty()) {
            return false;
        }

        Transaction transaction = purchaseOrder.get(0);
        this.setItem(transaction.getItem());
        this.setTransaction_Date(transaction.getTransaction_Date());
        //this.setQuantity(transaction.getQuantity());
        this.setTransaction_Mode(transaction.getTransaction_Mode());
        this.setTransaction_Receipient(transaction.getTransaction_Receipient());
        this.setTransaction_Created_By(transaction.getTransaction_Created_By());
        this.setTransaction_Modified_By(transaction.getTransaction_Modified_By());

        return true;
    }
    
    @Override 
    public String GenerateDocNo() {
        return "GRN" + SystemRunNo.Get("GRN");
    }
    
    //Constructor
    public GoodReceivedNotes() {
        this.setTransaction_Mode(TransactionMode.STOCK_IN);
    }

    public GoodReceivedNotes(String _Doc_No) {
        this.setDoc_No(_Doc_No);
        this.setTransaction_Mode(1);
    }

    
}
