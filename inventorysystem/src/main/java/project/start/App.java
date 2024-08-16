package project.start;

import project.global.MailSender;
import project.global.MailTemplate;
import project.modules.item.Item;
import project.modules.transaction.AutoReplenishment;

public class App {
    public static void main(String[] args) {
       

        

    }
    //test Mail function
    //Pending
    public static void testMail() {
        //Send mail
        MailSender mail = new MailSender("Admin Or Retailer Email", "Vendor Email", "Purchase Order",
                new MailTemplate("PO00001", MailTemplate.TemplateType.PURCHASE_ORDER));
        mail.AttachFile("C:\\Users\\User\\Desktop\\test.txt");
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
}
