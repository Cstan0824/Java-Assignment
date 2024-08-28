package project.start;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import project.global.MailSender;
import project.global.MailTemplate;
import project.global.PdfConverter;
import project.global.PdfTemplate;
import project.global.SqlConnector;
import project.modules.item.Item;
import project.modules.schedule.Vehicle;
import project.modules.transaction.AutoReplenishment;
import project.modules.transaction.DeliveryOrder;
import project.modules.transaction.PurchaseOrder;
import project.modules.transaction.SalesOrder;
import project.modules.transaction.Transaction;

public class App {
    
    public static void main(String[] args) {
        testCreateDO();
        }

    //test GoodReceivedNotes function
    //Done - by intuition :)

    //test TransactionRead Function
    //Done
    public static void testTransactionRead() {
        //get item
        SqlConnector connector = new SqlConnector();

        connector.Connect();

        String query = "SELECT * FROM TRANSACTION WHERE DOC_NO LIKE 'PO%';";
        ArrayList<PurchaseOrder> transactions = connector.PrepareExecuteRead(query, PurchaseOrder.class);

        transactions.forEach(transaction -> {
            DisplayTransaction(transaction);
        });

        connector.Disconnect();
    }

    //Done
    public static void DisplayTransaction(Transaction transaction) {
        //display Transaction details
        System.out.println("Doc No: " + transaction.getDoc_No());
        System.out.println("Source Doc No: " + transaction.getSource_Doc_No());
        System.out.println("Transaction Date: " + transaction.getTransaction_Date());
        System.out.println("Quantity: " + transaction.getQuantity());
        System.out.println("Transaction Mode: " + transaction.getTransaction_Mode());
        System.out.println("Transaction Recipient: " + transaction.getTransaction_Recipient());
        System.out.println("Transaction Created By: " + transaction.getTransaction_Created_By());
        System.out.println("Transaction Modified By: " + transaction.getTransaction_Modified_By());
    }

    //test PurchaseOrder function
    //Done
    public static void testAddPurchaseOrder() {
        //get item
        ArrayList<Item> items = Item.GetAll();

        //Add PurchaseOrder
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setItem(items.get(0));
        purchaseOrder.setDoc_No(purchaseOrder.GenerateDocNo());
        purchaseOrder.setSource_Doc_No(purchaseOrder.getDoc_No());
        purchaseOrder.setTransaction_Date(new Date(System.currentTimeMillis()));
        purchaseOrder.setQuantity(5);
        purchaseOrder.setTransaction_Mode(1);
        purchaseOrder.setTransaction_Recipient("R001");
        purchaseOrder.setTransaction_Created_By("A001");
        purchaseOrder.setTransaction_Modified_By("A001");
        purchaseOrder.Add();
        System.out.println("Add Purchase Order");
    }

    //Done
    public static void testUpdatePurchaseOrder() {
        //get item
        ArrayList<Item> items = Item.GetAll();

        //Update PurchaseOrder
        PurchaseOrder purchaseOrder = new PurchaseOrder("PO00004");
        purchaseOrder.setItem(items.get(0));
        
        purchaseOrder.setTransaction_Date(new Date(System.currentTimeMillis()));
        purchaseOrder.setQuantity(15);
        purchaseOrder.setTransaction_Mode(1);
        purchaseOrder.setTransaction_Recipient("R001");
        purchaseOrder.setTransaction_Created_By("A001");
        purchaseOrder.setTransaction_Modified_By("A001");
        purchaseOrder.Update();
        System.out.println("Update Purchase Order");
    }

    //Done
    public static void testRemovePurchaseOrder() {
        //Remove PurchaseOrder
        PurchaseOrder purchaseOrder = new PurchaseOrder("PO00004");
        System.out.println(purchaseOrder.Remove());
        System.out.println("Remove Purchase Order");
    }

    //Done
    public static void testGetPurchaseOrder() {
        //get PurchaseOrder
        PurchaseOrder purchaseOrder = new PurchaseOrder("PO00004");
        purchaseOrder.Get();
        System.out.println("Get Purchase Order");
        //display PurchaseOrder details
        DisplayPurchaseOrder(purchaseOrder);
    }

    //Done
    public static void testGetAllPurchaseOrder() {
        //get all PurchaseOrder
        PurchaseOrder.GetAll().forEach(purchaseOrder -> {
            DisplayPurchaseOrder(purchaseOrder);
        });
    }

    //Done
    public static void DisplayPurchaseOrder(Transaction purchaseOrder) {
        //display PurchaseOrder details
        System.out.println("Doc No: " + purchaseOrder.getDoc_No());
        System.out.println("Source Doc No: " + purchaseOrder.getSource_Doc_No());
        System.out.println("Transaction Date: " + purchaseOrder.getTransaction_Date());
        System.out.println("Quantity: " + purchaseOrder.getQuantity());
        System.out.println("Transaction Mode: " + purchaseOrder.getTransaction_Mode());
        System.out.println("Transaction Recipient: " + purchaseOrder.getTransaction_Recipient());
        System.out.println("Transaction Created By: " + purchaseOrder.getTransaction_Created_By());
        System.out.println("Transaction Modified By: " + purchaseOrder.getTransaction_Modified_By());
    }

    //test Pdf Converter function
    //Done
    public static void testPdfConverter() {
        String directory = "C:/Cstan/TARUMT Course/DIPLOMA IN INFORMATION TECHNOLOGY/YEAR2/Y2S1/Object Oriented Programming/Java-Assignment/inventorysystem/src/main/java/project/global";
        String fileName = "sameple.pdf";
        //String html = "<html><head></head><body><h1>Hello World</h1></body></html>";

        PdfConverter pdf;
        pdf = new PdfConverter(
                new File(directory, fileName),
                new PdfTemplate(new PurchaseOrder(), PdfTemplate.TemplateType.PURCHASE_ORDER));

        pdf.Save();
    }

    //test Pdf Converter + Mail function
    //Done
    public static void test_PdfConverter_Mail() {
        //Pdf Converter
        String directory = "C:/Cstan/TARUMT Course/DIPLOMA IN INFORMATION TECHNOLOGY/YEAR2/Y2S1/Object Oriented Programming/Java-Assignment/inventorysystem/src/main/java/project/global/Pdf";
        String fileName = "sameple.pdf";
        File file = new File(directory, fileName);
        //String html = "<html><head></head><body><h1>Hello World</h1></body></html>";

        PdfConverter pdf = new PdfConverter(file, new PdfTemplate(new PurchaseOrder(), PdfTemplate.TemplateType.PURCHASE_ORDER));

        pdf.Save();

        //Send mail
        MailSender mail = new MailSender("tarumtmoviesociety@gmail.com", "tancs8803@gmail.com", "Purchase Order",
                new MailTemplate("346759", MailTemplate.TemplateType.PURCHASE_ORDER));
        mail.AttachFile(file);

        mail.Send();
    }

    //test Mail function
    //Done
    public static void testMail() {
        //Send mail
        MailSender mail = new MailSender("tarumtmoviesociety@gmail.com", "tancs8803@gmail.com", "Purchase Order",
                new MailTemplate("346759", MailTemplate.TemplateType.OTP));
        mail.Send();
    }
    //Done
    public static void testMailWithAttachment() {
        //Send mail
        MailSender mail = new MailSender("tarumtmoviesociety@gmail.com", "tancs8803@gmail.com", "Purchase Order",
                new MailTemplate("PO00001", MailTemplate.TemplateType.PURCHASE_ORDER));
        mail.AttachFile(new File("C:/Users/PREDATOR/Downloads/Profile.pdf"));
        mail.Send();
    }

    //test item function
    //Done
    public static void getItem() {
        Item item = new Item(1);
        item.Get();
        //display item details 
        DisplayItem(item);
    }
    //Done
    public static void AddItem() {
        //Add Item
        Item item = new Item();
        //set value to item
        item.setItem_name("Item 1");
        item.setItem_Category_ID(2);
        item.setVendor_ID("V002");
        item.setItem_Desc("Item 4 Description");
        item.setItem_Quantity(10);
        item.setItem_Price(100.00);
        item.setItem_Created_By("A001");
        item.setItem_Modified_By("A001");
        item.Add();
        System.out.println("Add Item");
    }
    //Done
    public static void UpdateItem() {
        Item item = new Item(5);
        item.setItem_name("Item 3");
        item.setItem_Category_ID(1);
        item.setVendor_ID("V001");
        item.setItem_Desc("Item test Description");
        item.setItem_Quantity(10);
        item.setItem_Price(100.00);
        item.setItem_Created_By("A002");
        item.setItem_Modified_By("A002");
        item.Update();
        System.out.println("Update Item");
    }
    //Done
    public static void RemoveItem() {
        Item item = new Item(6);
        item.Remove();
        System.out.println("Remove Item");
    }
    //Done
    public static void GetAllItem() {
        Item.GetAll().forEach(item -> {
            DisplayItem(item);
        });
    }
    //Done
    public static void DisplayItem(Item item) {
        //display item details 
        System.out.println("Item ID: " + item.getItem_ID());
        System.out.println("Item Name: " + item.getItem_Name());
        System.out.println("Item Description: " + item.getItem_Desc());
        System.out.println("Item Quantity: " + item.getItem_Quantity());
        System.out.println("Item Price: " + item.getItem_Price());
        System.out.println("Item Category ID: " + item.getItem_Category_ID());
        System.out.println("Vendor ID: " + item.getVendor_ID());
        System.out.println("Item Created By: " + item.getItem_Created_By());
        System.out.println("Item Modified By: " + item.getItem_Modified_By());
    }

    //test AutoReplenishment function
    //Done
    public static void GetAutoReplenishment() {
        AutoReplenishment replenishment = new AutoReplenishment(2);
        replenishment.Get();
        replenishment.getItem().Get();
        //display item details
        DisplayAutoReplenishment(replenishment);
    }
    //Done
    public static void GetAllAutoReplenishment() {
        AutoReplenishment.GetAll().forEach(autoReplenishment -> {
            autoReplenishment.getItem().Get();
            DisplayAutoReplenishment(autoReplenishment);
        });
    }
    //Done
    public static void AddAutoReplenishment() {
        AutoReplenishment autoReplenishment = new AutoReplenishment(1);
        autoReplenishment.setItem_Threshold(5);
        autoReplenishment.Add();
        System.out.println("Add AutoReplenishment");
    }
    //Done
    public static void UpdateAutoReplenishment() {
        AutoReplenishment autoReplenishment = new AutoReplenishment(1);
        autoReplenishment.setItem_Threshold(10);
        autoReplenishment.Update();
        System.out.println("Update AutoReplenishment");
    }
    //Done
    public static void RemoveAutoReplenishment() {
        AutoReplenishment autoReplenishment = new AutoReplenishment(1);
        autoReplenishment.Remove();
        System.out.println("Remove AutoReplenishment");
    }
    //Done
    public static void ExecuteAutoReplenishment() {
        AutoReplenishment.ExecuteAutomation();
    }
    //Done
    public static void DisplayAutoReplenishment(AutoReplenishment autoReplenishment) {
        //display
        System.out.println("Item ID: " + autoReplenishment.getItem().getItem_ID());
        System.out.println("Item Name: " + autoReplenishment.getItem().getItem_Name());
        System.out.println("Item Quantity: " + autoReplenishment.getItem().getItem_Quantity());
        System.out.println("Item Price: " + autoReplenishment.getItem().getItem_Price());
        System.out.println("Item Treshold: " + autoReplenishment.getItem_Threshold());
    }


    //done
    //test SalesOrder function
    public static void testSalesOrder() {
        //get item
        ArrayList<Item> items = Item.GetAll();

        //Add SalesOrder
        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setItem(items.get(0));
        salesOrder.setDoc_No(salesOrder.GenerateDocNo());
        salesOrder.setSource_Doc_No(salesOrder.getDoc_No());
        salesOrder.setTransaction_Date(new Date(System.currentTimeMillis()));
        salesOrder.setQuantity(5);
        salesOrder.setTransaction_Mode(1);
        salesOrder.setTransaction_Recipient("R001");
        salesOrder.setTransaction_Created_By("A001");
        salesOrder.setTransaction_Modified_By("A001");
        if(salesOrder.Add()){
            System.out.println("Sales Order Added.");
        }else{
            System.out.println("error");
        }

        //Get All SalesOrder
        System.out.println("");
        ArrayList<SalesOrder> salesOrders = SalesOrder.GetAll();
        salesOrders.forEach(salesOrder1 -> DisplayTransaction(salesOrder1));

        //Get SalesOrder
        SalesOrder salesOrder1 = new SalesOrder("SO00004");
        salesOrder1.Get();
        DisplayTransaction(salesOrder1);

        //Update SalesOrder
        salesOrder.setItem(items.get(1));
        salesOrder.setQuantity(10);
        if(salesOrder.Update()){
            System.out.println("Sales Order Updated.");
        }else{
            System.out.println("error");
        }

        //Remove SalesOrder
        if(salesOrder.Remove()){
            System.out.println("Sales Order Removed.");
        }else{
            System.out.println("error");
        }

    }

    //done
    //test Vehicle function
    public static void TestVehicle(){

        //add
        Vehicle vehicle = new Vehicle("WYS3378", "Sedan", "Gan Chin Chung");
        if(vehicle.Add()){
            System.out.println("Vehicle Added.");
        } else {
            System.out.println("Vehicle not Added.");
        }

        //Get All
        ArrayList<Vehicle> vehicles = Vehicle.GetAll();
        if (vehicles != null && !vehicles.isEmpty()) {
            vehicles.forEach(vehicle1 ->{
                System.out.println("Vehicle Plate: " + vehicle1.getVehicle_Plate());
                System.out.println("Vehicle Type: " + vehicle1.getVehicle_Type());
                System.out.println("Driver: " + vehicle1.getDriver());
            });
        }

        //Get
        Vehicle vehicle1 = Vehicle.Get("WYS3378");
        if(vehicle1 != null){
            System.out.println("Vehicle Plate: " + vehicle1.getVehicle_Plate());
            System.out.println("Vehicle Type: " + vehicle1.getVehicle_Type());
            System.out.println("Driver: " + vehicle1.getDriver());
        }

        //Update
        vehicle.setVehicle_Type("SUV");
        vehicle.setDriver("Tan Choon Shen");
        if(vehicle.Update()){
            System.out.println("Vehicle Updated.");
        } else {
            System.out.println("Vehicle not Updated.");
        }

        //Remove
        if(vehicle1 != null){
            if(vehicle1.Remove()){
                System.out.println("Vehicle Removed.");
            } else {
                System.out.println("Vehicle not Removed.");
            }
        }

    }

    //done
    public static void testMultipleItem() {
        SalesOrder SODocNo = new SalesOrder();
        String SODN = SODocNo.GenerateDocNo();
    
        boolean continueAddItem;
        try (Scanner sc = new Scanner(System.in)) {
            do {
                SalesOrder salesOrder = new SalesOrder();
                Item item = new Item();
    
                System.out.println("Enter Item Id: ");
                int itemId = sc.nextInt();
    
                SqlConnector connector = new SqlConnector();
                connector.Connect();
                String query = "SELECT * FROM item WHERE Item_ID = ?";
    
                ArrayList<Item> items = connector.PrepareExecuteRead(query, Item.class, itemId);
                if (items != null && !items.isEmpty()) {
                    Item checkItem = items.get(0);
                    item.setItem_ID(checkItem.getItem_ID());
                    item.setItem_name(checkItem.getItem_Name());
                    item.setItem_Category_ID(checkItem.getItem_Category_ID());
                    item.setVendor_ID(checkItem.getVendor_ID());
                    item.setItem_Desc(checkItem.getItem_Desc());
                    item.setItem_Quantity(checkItem.getItem_Quantity());
                    item.setItem_Price(checkItem.getItem_Price());
                    item.setItem_Created_By(checkItem.getItem_Created_By());
                    item.setItem_Modified_By(checkItem.getItem_Modified_By());
                    salesOrder.setItem(item);
                    salesOrder.setDoc_No(SODN);
                    salesOrder.setSource_Doc_No(SODN);
                    salesOrder.setTransaction_Date(new Date(System.currentTimeMillis()));
    
                    System.out.println("Enter Quantity: ");
                    salesOrder.setQuantity(sc.nextInt());
                    salesOrder.setTransaction_Mode(1);
                    salesOrder.setTransaction_Recipient("R001");
                    salesOrder.setTransaction_Created_By("A001");
                    salesOrder.setTransaction_Modified_By("A001");
    
                    if (salesOrder.Add()) {
                        System.out.println("Sales Order Added.");
                    } else {
                        System.out.println("Error adding Sales Order.");
                    }
                } else {
                    System.out.println("Item not found.");
                }
    
                System.out.println("Do you want to add more items? (Y/N): ");
                String choice = sc.next();
                continueAddItem = choice.equalsIgnoreCase("Y");
    
            } while (continueAddItem);
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
    
    //done
    public static void testCreateDO() {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("Enter Sales Order Doc No: ");
            String salesOrderDocNo = sc.nextLine();
    
            SqlConnector connector = new SqlConnector();
            connector.Connect();
    
            if (!connector.isConnected()) {
                System.out.println("Database connection failed.");
                return;
            }
    
            String query = "SELECT * FROM Transaction WHERE Doc_No = ?";
            ArrayList<SalesOrder> salesOrders = connector.PrepareExecuteRead(query, SalesOrder.class, salesOrderDocNo);
    
            if (salesOrders != null && !salesOrders.isEmpty()) {
                DeliveryOrder DODocNo = new DeliveryOrder();
                String DODN = DODocNo.GenerateDocNo();
    
                for (SalesOrder salesOrder : salesOrders) {
                    DeliveryOrder deliveryOrder = new DeliveryOrder();
                    deliveryOrder.setItem(salesOrder.getItem());
                    deliveryOrder.setDoc_No(DODN);
                    deliveryOrder.setSource_Doc_No(salesOrder.getDoc_No());
                    deliveryOrder.setTransaction_Date(new Date(System.currentTimeMillis()));
                    deliveryOrder.setQuantity(salesOrder.getQuantity());
                    deliveryOrder.setTransaction_Mode(1);  // Customize as needed
                    deliveryOrder.setTransaction_Recipient("R001");  // Customize as needed
                    deliveryOrder.setTransaction_Created_By("A001");  // Customize as needed
                    deliveryOrder.setTransaction_Modified_By("A001");  // Customize as needed
    
                    if (deliveryOrder.Add()) {
                        System.out.println("Delivery Order Added.");
                    } else {
                        System.out.println("Error adding Delivery Order.");
                    }
                }
            } else {
                System.out.println("No sales orders found for the given Doc No.");
            }
    
            connector.Disconnect();  // Make sure to close the connection
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
    
    
}
