package project.modules.vendor;

import java.util.ArrayList;
import java.util.Objects;

import project.global.SqlConnector;
import project.global.SystemRunNo;

public class Vendor {
    //variables
    private String Vendor_ID;
    private String Vendor_Name;
    private String Vendor_Email;
    private String Vendor_Address;
    private String Vendor_Created_By;
    private String Vendor_Modified_By;

    //constructor
    public Vendor() {

    }

    public Vendor(String Vendor_ID, String Vendor_Name, String Vendor_Email,
            String Vendor_Address, String Vendor_Created_By, String Vendor_Modified_By) {
        this.Vendor_ID = Vendor_ID;
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
    public void setVendor_ID(String Vendor_ID) {
        this.Vendor_ID = Vendor_ID;
    }

    //get vendor name
    public String getVendor_Name() {
        return this.Vendor_Name;
    }

    //set vendor name
    public void setVendor_Name(String Vendor_Name) {
        this.Vendor_Name = Vendor_Name;
    }

    //get vendor email
    public String getVendor_Email() {
        return this.Vendor_Email;
    }

    //set vendor email
    public void setVendorEmail(String Vendor_Email) {
        this.Vendor_Email = Vendor_Email;
    }

    //get vendor address
    public String getVendor_Address() {
        return this.Vendor_Address;
    }

    //set vendor address
    public void setVendor_Address(String Vendor_Address) {
        this.Vendor_Address = Vendor_Address;
    }

    //get vendor created by
    public String getVendor_Created_By() {
        return this.Vendor_Created_By;
    }

    //set vendor created by
    public void setVendor_Created_By(String Vendor_Created_By) {
        this.Vendor_Created_By = Vendor_Created_By;
    }

    //get vendor modified by
    public String getVendor_Modified_By() {
        return this.Vendor_Modified_By;
    }

    //set vendor modified by
    public void setVendor_Modified_By(String Vendor_Modified_By) {
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
        this.setVendor_Name(vendor.getVendor_Name());
        this.setVendor_Address(vendor.getVendor_Address());
        this.setVendorEmail(vendor.getVendor_Email());
        this.setVendor_Created_By(vendor.getVendor_Created_By());
        this.setVendor_Modified_By(vendor.getVendor_Modified_By());

        connector.Disconnect();
        return true;
    }

    public static ArrayList<Vendor> Get(String _field, String _value) {
        SqlConnector connector = new SqlConnector();
        connector.Connect();
        if (!connector.isConnected()) {
            return null;
        }

        String query = "SELECT * FROM vendor WHERE " + _field + " = ?;";

        ArrayList<Vendor> vendors = connector.PrepareExecuteRead(query, Vendor.class, _value);
        return vendors;
    }

    //GET ALL VENDORS
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

        String insertVendorQuery = ("INSERT INTO vendor (Vendor_ID, Vendor_Name, Vendor_Address,Vendor_Email, Vendor_Created_By, Vendor_Modified_By) VALUES (?,?,?,?,?,?)");

        boolean checkQuery = Connector.PrepareExecuteDML(insertVendorQuery, this.getVendor_ID(),
                this.getVendor_Name(), this.getVendor_Address(),
                this.getVendor_Email(),
                this.getVendor_Created_By(), this.getVendor_Modified_By());

        Connector.Disconnect();

        return checkQuery;
    }

    //DELETE VENDOR FROM DB
    public boolean Remove() {
        SqlConnector Connector = new SqlConnector();
        Connector.Connect();
        if (!Connector.isConnected()) {
            return false;
        }

        String query = ("DELETE FROM vendor WHERE Vendor_ID = ?;");
        boolean QueryExecuted = Connector.PrepareExecuteDML(query,
                this.getVendor_ID());
        Connector.Disconnect();

        return QueryExecuted;
    }

    //MODIFY VENDOR

    //MODIFY VENDOR FROM DB
    public boolean Update(String _field, String _data) {
        SqlConnector Connector = new SqlConnector();
        //

        Connector.Connect();
        if (!Connector.isConnected()) {
            return false;
        }

        String UpdateVendorQuery = ("UPDATE vendor SET " + _field + " = '" + _data + "' WHERE Vendor_ID = ?;");
        boolean QueryExecuted = Connector.PrepareExecuteDML(UpdateVendorQuery,
                this.getVendor_ID());

        Connector.Disconnect();
        return QueryExecuted;
    }

    public String GenerateVendorID() {
        return "V" + String.format("%05d", SystemRunNo.Get("V"));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Vendor item = (Vendor) obj;
        return Objects.equals(this.Vendor_ID, item.getVendor_ID()); // Compare based on logical attribute
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.Vendor_ID); // Hash code based on logical attribute
    }

    @Override
    public String toString() {
        return String.format("| %-10s | %-20s | %-30s | %-35s |", this.getVendor_ID(), this.getVendor_Name(),
                this.getVendor_Address(), this.getVendor_Email());
    }
}

