package project.modules.item;

import java.util.ArrayList;

import project.global.CrudOperation;
import project.global.SqlConnector;

public class Item implements CrudOperation{
    //Data Fields
    private int Item_ID;
    private int Item_Category_ID;
    private String Vendor_ID;

    private String Item_name;
    private String Item_Desc;
    private int Item_Quantity;
    private double Item_Price;

    private String Item_Created_By = "-"; // User ID
    private String Item_Modified_By = "-"; // User ID

    //getter & setter methods

    //Item_ID
    public int getItem_ID() {
        return this.Item_ID;
    }

    //Item_Category_ID
    public void setItem_Category_ID(int _ItemCategoryID) {
        if (_ItemCategoryID < 0) {
            throw new IllegalArgumentException("Item_Category_ID cannot be negative");
        }
        this.Item_Category_ID = _ItemCategoryID;
    }

    public int getItem_Category_ID() {
        return this.Item_Category_ID;
    }

    //Vendor_ID
    public void setVendor_ID(String _VendorID) {
        if (_VendorID == null) {
            throw new IllegalArgumentException("Vendor_ID cannot be negative");
        }
        this.Vendor_ID = _VendorID;
    }

    public String getVendor_ID() {
        return this.Vendor_ID;
    }

    //Item_name
    public String getItem_Name() {
        return this.Item_name;
    }

    public void setItem_name(String _ItemName) {
        if (_ItemName == null) {
            throw new IllegalArgumentException("Item_name cannot be null");
        }
        this.Item_name = _ItemName;
    }

    //Item_Desc
    public String getItem_Desc() {
        return this.Item_Desc;
    }

    public void setItem_Desc(String _ItemDesc) {
        if (_ItemDesc == null) {
            throw new IllegalArgumentException("Item_Desc cannot be null");
        }
        this.Item_Desc = _ItemDesc;
    }

    //Item_Quantity
    public int getItem_Quantity() {
        return this.Item_Quantity;
    }

    public void setItem_Quantity(int _ItemQuantity) {
        if (_ItemQuantity < 0) {
            throw new IllegalArgumentException("Item_Quantity cannot be negative");
        }
        this.Item_Quantity = _ItemQuantity;
    }

    //Item_Price
    public double getItem_Price() {
        return this.Item_Price;
    }

    public void setItem_Price(double _ItemPrice) {
        if (_ItemPrice < 0) {
            throw new IllegalArgumentException("Item_Price cannot be negative");
        }
        this.Item_Price = _ItemPrice;
    }


    //Item_Created_By
    public String getItem_Created_By() {
        return this.Item_Created_By;
    }

    public void setItem_Created_By(String _ItemCreatedBy) {
        this.Item_Created_By = _ItemCreatedBy;
    }

    //Item_Modified_By
    public String getItem_Modified_By() {
        return this.Item_Modified_By;
    }

    public void setItem_Modified_By(String _ItemModifiedBy) {
        this.Item_Modified_By = _ItemModifiedBy;
    }


    //Methods
    @Override
    public boolean Add() {
        SqlConnector connector = new SqlConnector();

        connector.Connect();
        if (!connector.isConnected()) {
            return false;
        }

        String query = "INSERT INTO item (Item_Category_ID, Vendor_ID, Item_name, Item_Desc, Item_Quantity, Item_Price, Item_Created_By, Item_Modified_By) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        boolean QueryExecuted = connector.PrepareExecuteDML(query,
                this.Item_Category_ID, this.Vendor_ID,
                this.Item_name, this.Item_Desc, this.Item_Quantity, this.Item_Price,
                this.Item_Created_By, this.Item_Modified_By);

        connector.Disconnect();
        return QueryExecuted;
    }

    
    /***
     * @params: Pass in the Item ID
     * @return: Item object
     * @summary: Read the item from the database    
    } */
   @Override
   public final boolean Get() {
       SqlConnector connector = new SqlConnector();
       connector.Connect();
       if (!connector.isConnected()) {
           return false;
       }

       String query = "SELECT * FROM item WHERE Item_ID = ?;";
       ArrayList<Item> items = connector.PrepareExecuteRead(query, Item.class, this.Item_ID);

       connector.Disconnect();

       if (items == null || items.isEmpty()) {
           return false;
       }

       Item item = items.get(0); //get the first result

       this.Item_ID = item.getItem_ID();
       this.Item_Category_ID = item.getItem_Category_ID();
       this.Vendor_ID = item.getVendor_ID();
       this.Item_name = item.getItem_Name();
       this.Item_Desc = item.getItem_Desc();
       this.Item_Quantity = item.getItem_Quantity();
       this.Item_Price = item.getItem_Price();
       this.Item_Created_By = item.getItem_Created_By();
       this.Item_Modified_By = item.getItem_Modified_By();

       return true;

   }

    public static ArrayList<Item> GetAll() {

        SqlConnector connector = new SqlConnector();
        connector.Connect();
        if (!connector.isConnected()) {
            return null;
        }

        String query = "SELECT * FROM item;";
        ArrayList<Item> items = connector.<Item>ExecuteRead(query, Item.class);
        connector.Disconnect();

        return items;
    }

    public static ArrayList<Item> Get(String _field, String _value) {

        SqlConnector connector = new SqlConnector();
        connector.Connect();
        if (!connector.isConnected()) {
            return null;
        }

        String query = "SELECT * FROM item WHERE " + _field + " = ?;";
        ArrayList<Item> items = connector.PrepareExecuteRead(query, Item.class, _value);

        connector.Disconnect();

        return items;
    }

    @Override
    public boolean Update() {
        SqlConnector connector = new SqlConnector();
        connector.Connect();
        if (!connector.isConnected()) {
            return false;
        }

        String query = "UPDATE ITEM SET Item_Category_ID = ?, Vendor_ID = ?, Item_name = ?, Item_Desc = ?, Item_Quantity = ?, Item_Price = ?, Item_Modified_By = ? WHERE Item_ID = ?";

        boolean QueryExecuted = connector.PrepareExecuteDML(query,
                this.Item_Category_ID, this.Vendor_ID,
                this.Item_name, this.Item_Desc, this.Item_Quantity,
                this.Item_Price, this.Item_Modified_By,
                this.Item_ID);

        connector.Disconnect();

        return QueryExecuted;
    }

    @Override
    public boolean Remove() {

        SqlConnector connector = new SqlConnector();
        connector.Connect();
        if (!connector.isConnected()) {
            return false;
        }

        String query = "DELETE FROM ITEM WHERE Item_ID = ?;";
        boolean QueryExecuted = connector.PrepareExecuteDML(query,
                this.Item_ID);

        connector.Disconnect();

        return QueryExecuted;
    }

    public boolean AddStock(int _ItemQuantity) {
        if (_ItemQuantity < 0) {
            return false;
        }
        this.Item_Quantity += _ItemQuantity;
        this.Update();
        return true;
    }

    //Constructor
    public Item() {
    }

    public Item(Integer _Id) {
        this.Item_ID = _Id;
    }
}
