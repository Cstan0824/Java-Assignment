package project.modules.transaction;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;

import project.global.MailSender;
import project.global.MailTemplate;
import project.global.PdfConverter;
import project.global.PdfTemplate;
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

        String query = "INSERT INTO Transaction(Item_ID, Doc_No, Transaction_Date, VirtualStock,OnHandStock, Transaction_Mode, Transaction_Receipient, Transaction_Created_By, Transaction_Modified_By) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        boolean QueryExecuted = connector.PrepareExecuteDML(query,
                this.getItem().getItem_ID(), this.getDoc_No(),
                this.getTransaction_Date(), this.getVirtualStock(), this.getOnHandStock(), this.getTransaction_Mode(),
                this.getTransaction_Receipient(),
                this.getTransaction_Created_By(), this.getTransaction_Modified_By());

        connector.Disconnect();

        if (QueryExecuted == false) {
            return false;
        }

        //Generate Purchase Order Document
        //make it to relative path
        File file = new File(
                "C:/Cstan/TARUMT Course/DIPLOMA IN INFORMATION TECHNOLOGY/YEAR2/Y2S1/Object Oriented Programming/Java-Assignment/inventorysystem/src/main/java/project/global/Pdf",
                this.getDoc_No() + ".pdf");

        PdfConverter pdf = new PdfConverter(file,
                new PdfTemplate(this, PdfTemplate.TemplateType.PURCHASE_ORDER));
        pdf.Save();

        //Send Mail - receipent email get from vendor
        MailSender mail = new MailSender("tarumtmoviesociety@gmail.com", "tancs8803@gmail.com", "Purchase Order",
                new MailTemplate(this.getDoc_No(), MailTemplate.TemplateType.PURCHASE_ORDER));
        mail.AttachFile(file);
        mail.Send();

        return true;
    }

    //Only change vendor or item will use this method
    //Item_ID, Transaction_date, Quantity, Transaction_Receipent
    //need to combine with GRN - check if the user has already receive the stock; if yes user cant Update anymore
    @Override
    public boolean Update() {
        Transaction OldPurchaseOrder = PurchaseOrder.Get(this.getDoc_No());

        //Scenario 1: Change vendor
        //Scenario 2: Only change item but still same vendor
        if (!this.getTransaction_Receipient().equals(OldPurchaseOrder.getTransaction_Receipient())) {
            //Send the order cancellation to old vendor
            MailSender OrderCancellationMail = new MailSender("tancs8803@gmail.com", "Order Cancelled",
                    new MailTemplate(this.getDoc_No(), MailTemplate.TemplateType.ORDER_CANCELLATION));
            OrderCancellationMail.Send();

            //send the purchase order to new vendor with new item and new quantity
            MailSender PurchaseOrderMail = new MailSender("tancs8803@gmail.com", "Purchase Order",
                    new MailTemplate(this.getDoc_No(), MailTemplate.TemplateType.PURCHASE_ORDER));
            PurchaseOrderMail.Send();

        } else if (this.getItem().getItem_ID() != OldPurchaseOrder.getItem().getItem_ID()) {
            //Send Reordering to vendor
            //generate pdf
            File file = new File("");
            PdfConverter pdf = new PdfConverter(file, new PdfTemplate(this, PdfTemplate.TemplateType.PURCHASE_ORDER));
            pdf.Save();

            //send reordering mail
            MailSender ReorderingMail = new MailSender("tancs8803@gmail.com", "Reordering",
                    new MailTemplate(this.getDoc_No(), MailTemplate.TemplateType.REORDERING));
            ReorderingMail.AttachFile(file);
            ReorderingMail.Send();
        }

        SqlConnector connector = new SqlConnector();
        connector.Connect();

        if (!connector.isConnected()) {
            return false;
        }
        String query = "UPDATE Transaction SET Item_ID = ?, Transaction_Date = ?, VirtualStock = ?, OnHandStock = ?, Transaction_Receipient = ?, Transaction_Modified_By = ? WHERE Doc_No = ?";

        boolean QueryExecuted = connector.PrepareExecuteDML(query,
                this.getItem().getItem_ID(), this.getTransaction_Date(),
                this.getVirtualStock(), this.getOnHandStock(),
                this.getTransaction_Receipient(),
                this.getTransaction_Modified_By(),
                this.getDoc_No());

        connector.Disconnect();

        return QueryExecuted;
    }

    @Override
    //Done
    public boolean Remove() {
        SqlConnector connector = new SqlConnector();
        connector.Connect();

        if (!connector.isConnected()) {
            return false;
        }
        String query = "DELETE FROM Transaction WHERE Doc_No = ?";

        boolean QueryExecuted = connector.PrepareExecuteDML(query, this.getDoc_No());

        connector.Disconnect();

        if (QueryExecuted == false) {
            return false;
        }

        //Generate Order Cancellation Mail - send to vendor
        MailSender mail = new MailSender( "tancs8803@gmail.com", "Order Cancelled",
                new MailTemplate(this.getDoc_No(), MailTemplate.TemplateType.ORDER_CANCELLATION));
        mail.Send();

        //Delete the pdf file
        File file = new File(
                "C:/Cstan/TARUMT Course/DIPLOMA IN INFORMATION TECHNOLOGY/YEAR2/Y2S1/Object Oriented Programming/Java-Assignment/inventorysystem/src/main/java/project/global/Pdf",
                this.getDoc_No() + ".pdf");

        return file.delete();
    }

    @Override
    //Need to combine with GRN
    //Check the stock status and ask user whether they want to proceed with the stock[send mail to the vendor for follow up status] or change vendor
    public boolean Get() {
        Transaction purchaseOrder = PurchaseOrder.Get(this.getDoc_No());

        if (purchaseOrder == null) {
            return false;
        }

        //initialise the value
        this.setItem(purchaseOrder.getItem());
        this.setTransaction_Date(purchaseOrder.getTransaction_Date());
        this.setVirtualStock(purchaseOrder.getVirtualStock());
        this.setOnHandStock(purchaseOrder.getOnHandStock());
        this.setTransaction_Mode(purchaseOrder.getTransaction_Mode());
        this.setTransaction_Receipient(purchaseOrder.getTransaction_Receipient());
        this.setTransaction_Created_By(purchaseOrder.getTransaction_Created_By());
        this.setTransaction_Modified_By(purchaseOrder.getTransaction_Modified_By());




        return true;
    }

    @Override 
    public String GenerateDocNo() {
        return "PO" + String.format("%05d", SystemRunNo.Get("PO"));
    }
    
    public static Transaction Get(String _DocNo) {
        SqlConnector connector = new SqlConnector();
        connector.Connect();

        if (!connector.isConnected()) {
            return null;
        }
        String query = "SELECT * FROM Transaction WHERE Doc_No = ?";

        ArrayList<Transaction> purchaseOrders = connector.PrepareExecuteRead(query, Transaction.class,
                _DocNo);

        connector.Disconnect();

        if(purchaseOrders == null || purchaseOrders.isEmpty()) {
            return null;
        }

        //Save the value to instances
        return purchaseOrders.get(0);
        
    }
    //Constructor
    public PurchaseOrder() {
    }

    public PurchaseOrder(String _DocNo) {
        this.setDoc_No(_DocNo);
    }

    public PurchaseOrder(Item _item, String _Doc_No, Date _Transaction_Date, int _VirtualStock,int _OnHandStock, int _Transaction_Mode,
            String _Transaction_Receipient, String _Transaction_Created_By, String _Transaction_Modified_By) {
        super(_item, _Doc_No, _Transaction_Date, _VirtualStock, _OnHandStock, _Transaction_Mode,
                _Transaction_Receipient,
                _Transaction_Created_By, _Transaction_Modified_By);
    }

    
}
