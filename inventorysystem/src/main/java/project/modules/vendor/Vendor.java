package project.modules.vendor;

import java.util.ArrayList;

import project.global.SqlConnector;
import project.global.SystemRunNo;
import project.global.UserInputHandler;

public class Vendor {
    //variables
    private String Vendor_ID;
    private int Item_Category_ID;
    private String Vendor_Name;
    private String Vendor_Email;
    private String Vendor_Address;
    private String Vendor_Created_By;
    private String Vendor_Modified_By;

    //constructor
    public Vendor(){

    }

    public Vendor(String Vendor_ID, int Item_Category_ID, String Vendor_Name, String Vendor_Email, String Vendor_Address, String Vendor_Created_By, String Vendor_Modified_By){
        this.Vendor_ID = Vendor_ID;
        this.Item_Category_ID = Item_Category_ID;
        this.Vendor_Name = Vendor_Name;
        this.Vendor_Address = Vendor_Address;
        this.Vendor_Created_By = Vendor_Created_By;
        this.Vendor_Modified_By = Vendor_Modified_By;
        this.Vendor_Email = Vendor_Email;
    }
    
    //get vendor id
    public String getVendor_ID() {
        return this.Vendor_ID;
    }

    //set vendor id
    public void setVendor_ID(String Vendor_ID){
        this.Vendor_ID = Vendor_ID;
    }

    //get item category id
    public int getItem_Category_ID(){
        return this.Item_Category_ID;
    }

    //set item category id
    public void setItem_Category_ID(int Item_Category_ID){
        this.Item_Category_ID = Item_Category_ID;
    }

    //get vendor name
    public String getVendor_Name(){
        return this.Vendor_Name;
    }

    //set vendor name
    public void setVendor_Name(String Vendor_Name){
        this.Vendor_Name = Vendor_Name;
    }

    //get vendor email
    public String getVendor_Email(){
        return this.Vendor_Email;
    }

    //set vendor email
    public void setVendorEmail(String Vendor_Email){
        this.Vendor_Email = Vendor_Email;
    }

    //get vendor address
    public String getVendor_Address(){
        return this.Vendor_Address;
    }

    //set vendor address
    public void setVendor_Address(String Vendor_Address){
        this.Vendor_Address = Vendor_Address;
    }

    //get vendor created by
    public String getVendor_Created_By(){
        return this.Vendor_Created_By;
    }

    //set vendor created by
    public void setVendor_Created_By(String Vendor_Created_By){
        this.Vendor_Created_By = Vendor_Created_By;
    }

    //get vendor modified by
    public String getVendor_Modified_By(){
        return this.Vendor_Modified_By;
    }

    //set vendor modified by
    public void setVendor_Modified_By(String Vendor_Modified_By){
        this.Vendor_Modified_By = Vendor_Modified_By;
    }


    //FUNCTIONS
    //=========================================================================================================
    //=========================================================================================================
    
    //GET VENDOR
    public boolean Get() {

        SqlConnector connector = new SqlConnector();
        connector.Connect();
        if (!connector.isConnected()) {
            return false;
        }

        String query = "SELECT * FROM vendor WHERE Vendor_ID = ?;";

        ArrayList<Vendor> vendors = connector.PrepareExecuteRead(query, Vendor.class, this.getVendor_ID());

        if (vendors == null || vendors.isEmpty()) {
            return false;
        }

        Vendor vendor = vendors.get(0); //get the first result

        //assign the value to current instance  
        this.setVendor_ID(vendor.getVendor_ID());
        this.setItem_Category_ID(vendor.getItem_Category_ID());
        this.setVendor_Name(vendor.getVendor_Name());
        this.setVendor_Address(vendor.getVendor_Address());
        this.setVendorEmail(vendor.getVendor_Email());
        this.setVendor_Created_By(vendor.getVendor_Created_By());
        this.setVendor_Modified_By(vendor.getVendor_Modified_By());

        connector.Disconnect();
        return true;
    }

    public static ArrayList<Vendor> GetAll() {
        SqlConnector connector = new SqlConnector();
        connector.Connect();
        if (!connector.isConnected()) {
            return null;
        }

        String query = "SELECT * FROM vendor;";

        ArrayList<Vendor> vendors = connector.ExecuteRead(query, Vendor.class);
        return vendors;
    }
    
    //ADD NEW VENDOR TO DB
    public boolean Add() {
        SqlConnector Connector = new SqlConnector();

        Connector.Connect();
        if (!Connector.isConnected()) {
            return false;
        }

        String insertVendorQuery = ("INSERT INTO vendor (Vendor_ID, Item_Category_ID, Vendor_Name, Vendor_Address, Vendor_Created_By, Vendor_Modified_By, Vendor_Email) VALUES (?,?,?,?,?,?,?)");
        String insertVendorItemCategoryQuery = ("INSERT INTO vendor_itemcategory (Item_Category_ID, Vendor_ID) VALUES (?,?)");

        boolean checkQuery = Connector.PrepareExecuteDML(insertVendorQuery, this.getVendor_ID(),
                this.getItem_Category_ID(), this.getVendor_Name(), this.getVendor_Address(),
                this.getVendor_Created_By(), this.getVendor_Modified_By(), this.getVendor_Email());
        boolean checkQuery2 = Connector.PrepareExecuteDML(insertVendorItemCategoryQuery, this.getItem_Category_ID(),
                this.getVendor_ID());

        Connector.Disconnect();
        return checkQuery && checkQuery2;
    }    

    //DELETE VENDOR FROM DB
    public boolean Remove(){
        SqlConnector Connector = new SqlConnector();
        Connector.Connect();
        if (!Connector.isConnected()) {
            return false;
        }


        String DeleteQuery = ("DELETE FROM vendor_itemcategory WHERE Vendor_ID = ?;");
        boolean DeleteQueryExecuted = Connector.PrepareExecuteDML(DeleteQuery,
        this.getVendor_ID());

        String DeleteVendorQuery = ("DELETE FROM vendor WHERE Vendor_ID = ?;");
        boolean QueryExecuted = Connector.PrepareExecuteDML(DeleteVendorQuery,
        this.getVendor_ID());

        Connector.Disconnect();

        return QueryExecuted && DeleteQueryExecuted;
    }
    

    //MODIFY VENDOR
    public void modifyVendor()
    {
        String column;
        String values;
        //display
        System.out.println(
                "\n\n\n=================================================================================\nMODIFY VENDOR\n");

        //search for vendor
        this.setVendor_ID(UserInputHandler.getString(String.format("%-40s", "Please enter vendor ID: "), 6,
                "^V[0-9]{5}"));

        do {
            //select what to modify
            System.out.println("1. Vendor Name\n2. Vendor Address\n3. Vendor Email");

            switch (UserInputHandler.getInteger("Enter choice ", 1, 3)) {
                case 1:
                    System.out.println("===Update Vendor Name===");
                    this.setVendor_Name(
                            UserInputHandler.getString(String.format("%-40s", "Please enter vendor name: "), 4, 50));

                    column = ("Vendor_Name");
                    values = ("= '" + getVendor_Name() + "'");

                    break;

                case 2:
                    System.out.println("===Update Vendor Address===");
                    this.setVendor_Address(
                            UserInputHandler.getString(String.format("%-40s", "Please enter vendor address: "), 4,
                                    100));

                    column = "Vendor_Address";
                    values = "= '" + getVendor_Address() + "'";
                    break;

                default:
                    System.out.println("===Update Vendor Email===");
                    this.setVendorEmail(
                            UserInputHandler.getString(String.format("%-40s", "Please enter vendor email: "), ""));

                    column = "Vendor_Email";
                    values = "= '" + getVendor_Email() + "'";
                    break;
            }
            //add to database

            Update(column, values);
        } while (UserInputHandler.getConfirmation(String.format("%-40s", "\nContinue to edit vendor?"))
                .equalsIgnoreCase("Y"));
    }

    //MODIFY VENDOR FROM DB
    public boolean Update(String field, String data) {
        SqlConnector Connector = new SqlConnector();
        //

        Connector.Connect();
        if (!Connector.isConnected()) {
            return false;
        }

        String UpdateVendorQuery = ("UPDATE vendor SET " + field + data + " WHERE Vendor_ID = ?;");
        boolean QueryExecuted = Connector.PrepareExecuteDML(UpdateVendorQuery,
                this.getVendor_ID());

        Connector.Disconnect();
        return QueryExecuted;
    }

    public String GenerateVendorID() {
        return "V" + String.format("%05d", SystemRunNo.Get("V"));
    }

    @Override
    public String toString() {
        return String.format("| %-10s | %-20s | %-20s | %-20s |", this.getVendor_ID(), this.getVendor_Name(),
                this.getVendor_Address(), this.getVendor_Email());
    }
}

