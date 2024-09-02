package project.modules.transaction;

import java.io.File;
import java.util.ArrayList;

import project.global.CrudOperation;
import project.global.MailSender;
import project.global.MailTemplate;
import project.global.PdfConverter;
import project.global.PdfTemplate;
import project.global.SqlConnector;
import project.modules.item.Item;


public class AutoReplenishment implements CrudOperation {
    private int AutoReplenishment_ID;
    private Item item;
    private int Item_Threshold;

    //getter and setter methods
    public int getAutoReplenishment_ID() {
        return this.AutoReplenishment_ID;
    }

    public void setAutoReplenishment_ID(int _AutoReplenishment_ID) {
        this.AutoReplenishment_ID = _AutoReplenishment_ID;
    }

    public Item getItem() {
        return this.item;
    }

    public void setItem(Item _item) {
        this.item = _item;
    }

    public int getItem_Threshold() {
        return this.Item_Threshold;
    }

    public void setItem_Threshold(int _Item_Threshold) {
        this.Item_Threshold = _Item_Threshold;
    }

    //Constructor
    public AutoReplenishment() {

    }
    
    public AutoReplenishment(int _ItemID) {
        this.item = new Item(_ItemID);
    }
    
    public AutoReplenishment(AutoReplenishment _autoReplenishment) {
        this.AutoReplenishment_ID = _autoReplenishment.getAutoReplenishment_ID();
        this.item = _autoReplenishment.getItem();
        this.Item_Threshold = _autoReplenishment.getItem_Threshold();
    }

    //Methods
    @Override
    public boolean Get() {

        if (item.getItem_ID() < 0) {
            return false;
        }

        SqlConnector connector = new SqlConnector();
        connector.Connect();

        if (!connector.isConnected()) {
            return false;
        }

        String query = "SELECT * FROM AutoReplenishment WHERE Item_ID = ?;";

        ArrayList<AutoReplenishment> results = connector.PrepareExecuteRead(query, AutoReplenishment.class,
                item.getItem_ID());
        if (results == null || results.isEmpty()) {
            return false;
        }

        AutoReplenishment result = results.get(0);
        this.AutoReplenishment_ID = result.getAutoReplenishment_ID();
        this.item = result.getItem();
        this.item.Get();
        this.Item_Threshold = result.getItem_Threshold();

        connector.Disconnect();
        return false;
    }

    public static ArrayList<AutoReplenishment> GetAll() {
        SqlConnector connector = new SqlConnector();
        connector.Connect();

        if (!connector.isConnected()) {
            return null;
        }

        String query = "SELECT * FROM AutoReplenishment;";

        ArrayList<AutoReplenishment> results = connector.<AutoReplenishment>ExecuteRead(query, AutoReplenishment.class);
        results.forEach(result->{
            result.getItem().Get();
        });

        connector.Disconnect();

        return results;
    }

    @Override
    public boolean Add() {
        if (item.getItem_ID() < 0) {
            return false;
        }

        SqlConnector connector = new SqlConnector();
        connector.Connect();

        if (!connector.isConnected()) {
            return false;
        }

        String query = "INSERT INTO AutoReplenishment (Item_ID, Item_Threshold) VALUES (?, ?);";

        boolean QueryExecuted = connector.PrepareExecuteDML(query, this.item.getItem_ID(), this.Item_Threshold);

        connector.Disconnect();
        return QueryExecuted;
    }

    @Override
    public boolean Remove() {
        if (item.getItem_ID() < 0) {
            return false;
        }

        SqlConnector connector = new SqlConnector();

        connector.Connect();

        if (!connector.isConnected()) {
            return false;
        }

        String query = "DELETE FROM AutoReplenishment WHERE Item_ID = ?;";

        boolean QueryExecuted = connector.PrepareExecuteDML(query, this.item.getItem_ID());

        connector.Disconnect();

        return QueryExecuted;
    }

    @Override
    public boolean Update() {
        if (item.getItem_ID() < 0) {
            return false;
        }

        SqlConnector connector = new SqlConnector();

        connector.Connect();

        if (!connector.isConnected()) {
            return false;
        }

        String query = "UPDATE AutoReplenishment SET Item_Threshold = ? WHERE Item_ID = ?;";

        boolean QueryExecuted = connector.PrepareExecuteDML(query, this.Item_Threshold, this.item.getItem_ID());

        connector.Disconnect();

        return QueryExecuted;
    }

    public static void ExecuteAutomation() {
        //Create an extra thread to run the automation at the background while the system is running
        ArrayList<AutoReplenishment> autoReplenishments = GetAll();

        autoReplenishments.forEach((AutoReplenishment replenishment) -> {
            if (replenishment.getItem().getItem_Quantity() <= replenishment.getItem_Threshold()) {
                int RestockQuantity = replenishment.getItem_Threshold() * 2;
                //Send Purchase Order
                PurchaseOrder purchaseOrder = new PurchaseOrder();
                String PO_NO = purchaseOrder.GenerateDocNo();

                SendPurchaseOrder(replenishment, PO_NO, RestockQuantity);

                //Update Item Quantity
                replenishment.getItem()
                        .setItem_Quantity(
                                replenishment.getItem().getItem_Quantity() + RestockQuantity);
                replenishment.getItem().Update();
            }
        });
    }
    
    private static void SendPurchaseOrder(AutoReplenishment _replenishment, String _PoNo, int _RestockQuantity) {
        //Purchase Order
        Transaction purchaseOrder = new PurchaseOrder();
        purchaseOrder.setDoc_No(_PoNo);
        purchaseOrder.setQuantity(_RestockQuantity);
        purchaseOrder.setItem(_replenishment.getItem());
        purchaseOrder.setTransaction_Recipient("Vendor Name");
        purchaseOrder.setTransaction_Created_By("System");
        purchaseOrder.setTransaction_Modified_By("System");
        purchaseOrder.Add();

        //Generate PDF
        File file = new File(
                "C:/Cstan/TARUMT Course/DIPLOMA IN INFORMATION TECHNOLOGY/YEAR2/Y2S1/Object Oriented Programming/Java-Assignment/inventorysystem/src/main/java/project/global/Pdf",
                _PoNo + ".pdf");

        PdfConverter pdf = new PdfConverter(file,
                new PdfTemplate(purchaseOrder, PdfTemplate.TemplateType.PURCHASE_ORDER));
        pdf.Save();

        //Email
        //Send Order to Vendor
        MailSender purchaseOrderMail = new MailSender("tancs8803@gmail.com", "Purchase Order",
                new MailTemplate(purchaseOrder.getDoc_No(), MailTemplate.TemplateType.PURCHASE_ORDER));
        purchaseOrderMail.AttachFile(file);
        purchaseOrderMail.Send();
    }

    
    
}
