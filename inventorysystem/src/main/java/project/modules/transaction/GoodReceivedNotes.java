package project.modules.transaction;

import java.sql.Date;
import java.util.ArrayList;

import project.global.MailSender;
import project.global.MailTemplate;
import project.global.SqlConnector;
import project.global.SystemRunNo;
import project.global.UserInputHandler;
import project.modules.item.Item;

public class GoodReceivedNotes extends Transaction {
    
    public enum DocumentType 
    {
        GOOD_RECEIVED_NOTES,
        PURCHASE_ORDER
    }

    @Override
    //update PO row - OnHandStock
    //if the amount not same then send email to update the status with vendor
    //GRN NO
    //Done
    public boolean Add() {
        ArrayList<GoodReceivedNotes> goodReceivedNotes = GoodReceivedNotes.Get(this.getSource_Doc_No(),
                GoodReceivedNotes.DocumentType.PURCHASE_ORDER);
        
        Transaction purchaseOrder 
                = PurchaseOrder.Get(
                    this.getItem(),
                        ((goodReceivedNotes == null || goodReceivedNotes.isEmpty())
                            ?
                                this
                            :
                                goodReceivedNotes.get(0)
                        ).getSource_Doc_No());
        
        int VirtualStock = purchaseOrder.getQuantity();
        int OnHandStock = 0;

        if (goodReceivedNotes != null && !goodReceivedNotes.isEmpty()) {
            for (Transaction goodReceivedNote : goodReceivedNotes) {
                goodReceivedNote.getItem().Get();
                if (goodReceivedNote.getItem().getItem_ID() == purchaseOrder.getItem().getItem_ID()) {
                    OnHandStock += goodReceivedNote.getQuantity();

                }
            }
        }
        
        //System.out.println("Virtual Stock: " + VirtualStock);
        //System.out.println("On Hand Stock: " + OnHandStock);
        if (OnHandStock == VirtualStock) {
            return false;
        } else if (VirtualStock < (OnHandStock + this.getQuantity())) {
            return false;
        }

        //Connect to Sql - check the amount after the query inserted successfully
        SqlConnector connector = new SqlConnector();
        connector.Connect();

        if (!connector.isConnected()) {
            System.out.println("Cannot connect to database");
            return false;
        }

        String query = "INSERT INTO Transaction (Item_ID, Doc_No, Source_Doc_No, Transaction_Date, Quantity, Transaction_Mode, Transaction_Recipient, Transaction_Created_By, Transaction_Modified_By) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

        boolean QueryExecuted = connector.PrepareExecuteDML(query,
                this.getItem().getItem_ID(), this.getDoc_No(), this.getSource_Doc_No(),
                this.getTransaction_Date(), this.getQuantity(), this.getTransaction_Mode(),
                this.getTransaction_Recipient(),
                this.getTransaction_Created_By(), this.getTransaction_Modified_By());
        connector.Disconnect();

        if (QueryExecuted == false) {
            System.out.println("Cannot insert the data");
            return false;
        }

        MailSender mail;
        if(VirtualStock == OnHandStock + this.getQuantity()){
            //Send Email -Correct Amount
            mail = new MailSender("tancs8803@gmail.com", "Order Confirmation",
                new MailTemplate(this.getSource_Doc_No(), MailTemplate.TemplateType.ORDER_CONFIRMATION));
        } else {
            //The Stock amount is not enough
            mail = new MailSender("tancs8803@gmail.com", "Follow Up Order Status",
                    new MailTemplate(this.getSource_Doc_No(), MailTemplate.TemplateType.FOLLOW_ORDER_STATUS));
        }
        mail.Send();

        return true;
    }

    @Override
    //Human Error[wrong item quantity]
    //Send email to vendor if the amount is invalid
    //Update OnHandStock
    //Pending
    public boolean Update() {
        //Old GRN
        Transaction OldGoodReceivedNote = new GoodReceivedNotes(this.getItem(), this.getDoc_No(),
                this.getSource_Doc_No());
        OldGoodReceivedNote.Get();

        //Purchase Order
        Transaction purchaseOrder = PurchaseOrder.Get(this.getItem(), this.getSource_Doc_No());
        if (purchaseOrder == null) {
            return false;
        }

        ArrayList<GoodReceivedNotes> goodReceivedNotes = GoodReceivedNotes.Get(this.getSource_Doc_No(),
                DocumentType.PURCHASE_ORDER);

        if (goodReceivedNotes == null || goodReceivedNotes.isEmpty()) {
            return false;
        }

        int VirtualStock = purchaseOrder.getQuantity();
        int OnHandStock = 0;

        for (Transaction goodReceivedNote : goodReceivedNotes) {
            OnHandStock += goodReceivedNote.getQuantity();
        }

        int differenceStockQuantity = this.getQuantity() - OldGoodReceivedNote.getQuantity();

        if (VirtualStock < (OnHandStock + differenceStockQuantity)) {
            //The Amount is exceed the actual amount 
            return false;
        }
        //

        //Sql Connector
        SqlConnector connector = new SqlConnector();

        connector.Connect();

        if (!connector.isConnected()) {
            return false;
        }
        boolean QueryExecuted;
        if (OldGoodReceivedNote.getItem().getItem_ID() == this.getItem().getItem_ID()) {
            //The Item is the same
            String query = "UPDATE Transaction SET  Transaction_Date = ?, Quantity = ?, Transaction_Mode = ?, Transaction_Recipient = ?, Transaction_Created_By = ?, Transaction_Modified_By = ? WHERE Doc_No = ?;";

            QueryExecuted = connector.PrepareExecuteDML(query,
                    this.getTransaction_Date(), this.getQuantity(), this.getTransaction_Mode(),
                    this.getTransaction_Recipient(),
                    this.getTransaction_Created_By(), this.getTransaction_Modified_By(),
                    this.getDoc_No());

        } else {
            String query = "UPDATE Transaction SET Item_ID = ?, Transaction_Date = ?, Quantity = ?, Transaction_Mode = ?, Transaction_Recipient = ?, Transaction_Created_By = ?, Transaction_Modified_By = ? WHERE Doc_No = ?;";

            QueryExecuted = connector.PrepareExecuteDML(query,
                    this.getItem().getItem_ID(),
                    this.getTransaction_Date(), this.getQuantity(), this.getTransaction_Mode(),
                    this.getTransaction_Recipient(),
                    this.getTransaction_Created_By(), this.getTransaction_Modified_By(),
                    this.getDoc_No());
        }

        connector.Disconnect();

        if (QueryExecuted == false) {
            return false;
        }

        MailSender mail;
        if (VirtualStock > (OnHandStock + differenceStockQuantity)) {
            //The Amount is still not enough, ask user whether need to follow the status with vendor
            if (!UserInputHandler.getConfirmation("Do you want to follow up the status of the stock?")
                    .equalsIgnoreCase("Y")) {
                return true;
            }
            mail = new MailSender("tancs8803@gmail.com", "Follow Up Order Status",
                    new MailTemplate(this.getSource_Doc_No(), MailTemplate.TemplateType.FOLLOW_ORDER_STATUS));
        } else {
            //The Amount is correct
            //Send Order Confirmation Email
            mail = new MailSender("tancs8803@gmail.com", "Order Confirmation",
                    new MailTemplate(this.getSource_Doc_No(), MailTemplate.TemplateType.ORDER_CONFIRMATION));

        }

        mail.Send();

        return QueryExecuted;
    }
    
    @Override
    //Human Error[]
    //Done
    public boolean Remove() {
        SqlConnector connector = new SqlConnector();

        connector.Connect();

        if (!connector.isConnected()) {
            return false;
        }
        //GRN No
        String query = "DELETE FROM Transaction WHERE Doc_No = ?;";

        boolean QueryExecuted = connector.PrepareExecuteDML(query, this.getDoc_No());

        connector.Disconnect();

        return QueryExecuted;
    }

    @Override
    //Done
    public boolean Get() {
        SqlConnector connector = new SqlConnector();

        connector.Connect();

        if (!connector.isConnected()) {
            return false;
        }
        //PO NO
        String query = "SELECT * FROM Transaction WHERE Source_Doc_No = ? AND Doc_No = ? AND Item_Id = ?;";

        ArrayList<GoodReceivedNotes> purchaseOrder = connector.PrepareExecuteRead(query, GoodReceivedNotes.class,
                this.getSource_Doc_No(), this.getDoc_No(), this.getItem().getItem_ID());

        if (purchaseOrder == null || purchaseOrder.isEmpty()) {
            return false;
        }

        Transaction transaction = purchaseOrder.get(0);
        this.setItem(transaction.getItem());
        this.setTransaction_Date(transaction.getTransaction_Date());
        this.setQuantity(transaction.getQuantity());
        this.setTransaction_Mode(transaction.getTransaction_Mode());
        this.setTransaction_Recipient(transaction.getTransaction_Recipient());
        this.setTransaction_Created_By(transaction.getTransaction_Created_By());
        this.setTransaction_Modified_By(transaction.getTransaction_Modified_By());

        return true;
    }

    public static ArrayList<GoodReceivedNotes> Get(String _DocNo, DocumentType _DocumentType) {
        String query = null;
        switch (_DocumentType) {
            case GOOD_RECEIVED_NOTES:
                query = "SELECT * FROM Transaction WHERE Doc_No = ? ORDER BY Source_Doc_No, Doc_No;";
                break;
            case PURCHASE_ORDER:
                query = "SELECT * FROM Transaction WHERE Source_Doc_No = ? ORDER BY Source_Doc_No, Doc_No;";
                break;
            default:
                break;
        }

        SqlConnector connector = new SqlConnector();
        connector.Connect();

        if (!connector.isConnected()) {
            return null;
        }
        ArrayList<GoodReceivedNotes> GoodReceivedNotes = connector.PrepareExecuteRead(query, GoodReceivedNotes.class,
                _DocNo);

        connector.Disconnect();

        if (GoodReceivedNotes == null || GoodReceivedNotes.isEmpty()) {
            return null;
        }

        return GoodReceivedNotes;
    }
    
    public static ArrayList<GoodReceivedNotes> Get(Item _item, String _SourceDocNo) {
        String query = "SELECT * FROM Transaction WHERE Item_ID = ? AND Source_Doc_No = ? ORDER BY Source_Doc_No, Doc_No;";

        SqlConnector connector = new SqlConnector();
        connector.Connect();

        if (!connector.isConnected()) {
            return null;
        }
        ArrayList<GoodReceivedNotes> GoodReceivedNotes = connector.PrepareExecuteRead(query, GoodReceivedNotes.class,
                _item.getItem_ID(), _SourceDocNo);

        connector.Disconnect();

        if (GoodReceivedNotes == null || GoodReceivedNotes.isEmpty()) {
            return null;
        }

        return GoodReceivedNotes;
    }
        
    //Done
    @Override
    public String GenerateDocNo() {
        return "GRN" + String.format("%05d", SystemRunNo.Get("GRN"));
    }
    
    

    //Constructor
    public GoodReceivedNotes() {
        this.setTransaction_Mode(TransactionMode.STOCK_IN);
        this.setTransaction_Date(new Date(System.currentTimeMillis()));
    }

    public GoodReceivedNotes(Item _item, String _DocNo, String _SourceDocNo) {
        this.setTransaction_Mode(TransactionMode.STOCK_IN);
        this.setItem(_item);
        this.setDoc_No(_DocNo);
        this.setSource_Doc_No(_SourceDocNo);
        this.setTransaction_Date(new Date(System.currentTimeMillis()));

    }

    public GoodReceivedNotes(Item _item, String _Doc_No, Date _Transaction_Date, int _Quantity,
            String _Transaction_Recipient, String _Transaction_Created_By, String _Transaction_Modified_By) {
        super(_item, _Doc_No, _Transaction_Date, _Quantity, TransactionMode.STOCK_IN,
                _Transaction_Recipient,
                _Transaction_Created_By, _Transaction_Modified_By);
    }

}
