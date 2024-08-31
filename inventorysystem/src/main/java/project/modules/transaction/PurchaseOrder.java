package project.modules.transaction;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

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

        String query = "INSERT INTO Transaction(Item_ID, Doc_No, Transaction_Date, Quantity, Transaction_Mode, Transaction_Recipient, Transaction_Created_By, Transaction_Modified_By) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        boolean QueryExecuted = connector.PrepareExecuteDML(query,
                this.getItem().getItem_ID(), this.getDoc_No(),
                this.getTransaction_Date(), this.getQuantity(), this.getTransaction_Mode(),
                this.getTransaction_Recipient(),
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
        Transaction oldPurchaseOrder = PurchaseOrder.Get(this.getItem(), this.getDoc_No());

        if (oldPurchaseOrder == null) {
            return false;
        }

        //Scenario 1: Change vendor
        //Scenario 2: Only change item but still same vendor
        if (!this.getTransaction_Recipient().equals(oldPurchaseOrder.getTransaction_Recipient())) {
            //Send the order cancellation to old vendor
            MailSender OrderCancellationMail = new MailSender("tancs8803@gmail.com", "Order Cancelled",
                    new MailTemplate(this.getDoc_No(), MailTemplate.TemplateType.ORDER_CANCELLATION));
            OrderCancellationMail.Send();

            //send the purchase order to new vendor with new item and new quantity
            MailSender PurchaseOrderMail = new MailSender("tancs8803@gmail.com", "Purchase Order",
                    new MailTemplate(this.getDoc_No(), MailTemplate.TemplateType.PURCHASE_ORDER));
            PurchaseOrderMail.Send();

        } else if (this.getItem().getItem_ID() != oldPurchaseOrder.getItem().getItem_ID()) {
            //Send Reordering to vendor
            //generate pdf
            File file = new File(
                "C:/Cstan/TARUMT Course/DIPLOMA IN INFORMATION TECHNOLOGY/YEAR2/Y2S1/Object Oriented Programming/Java-Assignment/inventorysystem/src/main/java/project/global/Pdf",
                this.getDoc_No() + ".pdf");
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
        String query = "UPDATE Transaction SET Item_ID = ?, Transaction_Date = ?, Quantity = ?, Transaction_Recipient = ?, Transaction_Modified_By = ? WHERE Doc_No = ?";

        boolean QueryExecuted = connector.PrepareExecuteDML(query,
                this.getItem().getItem_ID(), this.getTransaction_Date(),
                this.getQuantity(),
                this.getTransaction_Recipient(),
                this.getTransaction_Modified_By(),
                this.getDoc_No());

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
        String query = "DELETE FROM Transaction WHERE Doc_No = ?";

        boolean QueryExecuted = connector.PrepareExecuteDML(query, this.getDoc_No());

        connector.Disconnect();

        if (QueryExecuted == false) {
            return false;
        }
        

        //Delete the pdf file
        File file = new File(
                "C:/Cstan/TARUMT Course/DIPLOMA IN INFORMATION TECHNOLOGY/YEAR2/Y2S1/Object Oriented Programming/Java-Assignment/inventorysystem/src/main/java/project/global/Pdf",
                this.getDoc_No() + ".pdf");
        file.delete();

        return true;
    }

    @Override
    //Check the stock status and ask user whether they want to proceed with the stock[send mail to the vendor for follow up status]
    //Done
    public boolean Get() {
        Transaction purchaseOrder = PurchaseOrder.Get(this.getItem(), this.getDoc_No());

        if (purchaseOrder == null) {
            return false;
        }

        //initialize the value
        this.setItem(purchaseOrder.getItem());
        this.setDoc_No(purchaseOrder.getDoc_No());
        this.setSource_Doc_No(purchaseOrder.getSource_Doc_No());
        this.setTransaction_Date(purchaseOrder.getTransaction_Date());
        this.setQuantity(purchaseOrder.getQuantity());
        this.setTransaction_Mode(purchaseOrder.getTransaction_Mode());
        this.setTransaction_Recipient(purchaseOrder.getTransaction_Recipient());
        this.setTransaction_Created_By(purchaseOrder.getTransaction_Created_By());
        this.setTransaction_Modified_By(purchaseOrder.getTransaction_Modified_By());

        int VirtualStock = this.getQuantity();
        int OnHandStock = 0;

        //Check the stock status
        ArrayList<GoodReceivedNotes> goodReceiveNotes = GoodReceivedNotes.Get(this.getDoc_No(),
                GoodReceivedNotes.DocumentType.PURCHASE_ORDER);

        if (goodReceiveNotes == null || goodReceiveNotes.isEmpty()) {
            OnHandStock = 0;
        } else {
            for (Transaction goodReceiveNote : goodReceiveNotes) {
                OnHandStock += goodReceiveNote.getQuantity();
            }
        }

        Integer diff = VirtualStock - OnHandStock;
        if (diff == 0) {
            return true;
        }


        

        return true;
    }

    @Override
    public String GenerateDocNo() {
        return "PO" + String.format("%05d", SystemRunNo.Get("PO"));
    }
    

    public String toString(AtomicInteger StockStatus_) {
        super.getItem().Get();

        // Determine the stock status by checking GoodReceivedNotes
        ArrayList<GoodReceivedNotes> goodReceiveNotes = GoodReceivedNotes.Get(this.getDoc_No(),
                GoodReceivedNotes.DocumentType.PURCHASE_ORDER);
        int VirtualStock = this.getQuantity();
        int OnHandStock = 0;

        if (goodReceiveNotes != null && !goodReceiveNotes.isEmpty()) {
            for (Transaction goodReceiveNote : goodReceiveNotes) {
                OnHandStock += goodReceiveNote.getQuantity();
            }
        }

        // Update the StockStatus_ based on the comparison of VirtualStock and OnHandStock
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
        String format = "| %-15s | %-15s | %-10s | %-10s | %-10s |\n";
        String value = String.format(format,
                this.getItem().getItem_Name(),
                this.getDoc_No(),
                this.getTransaction_Date(),
                this.getQuantity(),
                status);

        // Return the formatted string with all the necessary details
        return value;
    }


    public static Transaction Get(Item _item, String _DocNo) {
        SqlConnector connector = new SqlConnector();
        connector.Connect();
        if (!connector.isConnected()) {
            return null;
        }

        String query = "SELECT * FROM Transaction WHERE Doc_No = ? AND Item_ID = ?";
        ArrayList<PurchaseOrder> purchaseOrders = connector.PrepareExecuteRead(query, PurchaseOrder.class, _DocNo,
                _item.getItem_ID());
        connector.Disconnect();

        if (purchaseOrders == null || purchaseOrders.isEmpty()) {
            return null;
        }

        return purchaseOrders.get(0);
    }

    public static ArrayList<PurchaseOrder> Get(String _DocNo) {
        SqlConnector connector = new SqlConnector();
        connector.Connect();
        if (!connector.isConnected()) {
            return null;
        }

        String query = "SELECT * FROM Transaction WHERE Doc_No = ?";
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

        String query = "SELECT * FROM Transaction WHERE DOC_NO LIKE 'PO%'";
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

        String status = (OnHandStock == VirtualStock) ? "Received" : "Pending";

        // Define a format string with placeholders for the important column values
        String format = "| %-15s | %-15s | %-10s | %-10s | %-10s |\n";
        String value = String.format(format,
                this.getItem().getItem_Name(),
                this.getDoc_No(),
                this.getTransaction_Date(),
                this.getQuantity(),
                status);
        System.out.println(value);
        System.out.println("Hellow");
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
