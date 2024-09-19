package project.modules.item;

import java.util.ArrayList;
import java.util.Objects;

import project.global.CrudOperation;
import project.global.SqlConnector;
import project.modules.vendor.Vendor;

public class Item implements CrudOperation{
    //Data Fields
    private int Item_ID;
    private ItemCategory item_Category;
    private Vendor vendor;

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

    public void setItem_ID(int _ItemID) {
        this.Item_ID = _ItemID;
    }

    //Item_Category
    public void setItemCategory(ItemCategory _itemCategory) {
        this.item_Category = _itemCategory;
    }

    public ItemCategory getItemCategory() {
        return this.item_Category;
    }

    //Vendor
    public void setVendor(Vendor _vendor) {
        
        this.vendor = _vendor;
    }

    public Vendor getVendor() {
        return this.vendor;
    }

    //Item_name
    public String getItem_Name() {
        return this.Item_name;
    }

    public void setItem_name(String _ItemName) {
        this.Item_name = _ItemName;
    }

    //Item_Desc
    public String getItem_Desc() {
        return this.Item_Desc;
    }

    public void setItem_Desc(String _ItemDesc) {
        this.Item_Desc = _ItemDesc;
    }

    //Item_Quantity
    public int getItem_Quantity() {
        return this.Item_Quantity;
    }

    public void setItem_Quantity(int _ItemQuantity) {
        this.Item_Quantity = _ItemQuantity;
    }

    //Item_Price
    public double getItem_Price() {
        return this.Item_Price;
    }

    public void setItem_Price(double _ItemPrice) {
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

    //Constructor
    public Item() {
    }

    public Item(Integer _Id) {
        this.Item_ID = _Id;
    }


    //Methods
    @Override
    //Add new item to database
    public boolean Add() {
        SqlConnector connector = new SqlConnector();

        connector.Connect();
        if (!connector.isConnected()) {
            return false;
        }

        String query = "INSERT INTO item (Item_Category_ID, Vendor_ID, Item_name, Item_Desc, Item_Quantity, Item_Price, Item_Created_By, Item_Modified_By) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        boolean QueryExecuted = connector.PrepareExecuteDML(query,
                this.item_Category.getItem_Category_ID(), this.vendor.getVendor_ID(),
                this.Item_name, this.Item_Desc, this.Item_Quantity, this.Item_Price,
                this.Item_Created_By, this.Item_Modified_By);

        connector.Disconnect();
        return QueryExecuted;
    }

    
    
    @Override
    //Get item from database based on item id
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
        item.getItemCategory().Get(); //get the item category from the item category id
        item.getVendor().Get(); //get the vendor from the vendor id

        this.Item_ID = item.getItem_ID();
        this.item_Category = item.getItemCategory();
        this.vendor = item.getVendor();
        this.Item_name = item.getItem_Name();
        this.Item_Desc = item.getItem_Desc();
        this.Item_Quantity = item.getItem_Quantity();
        this.Item_Price = item.getItem_Price();
        this.Item_Created_By = item.getItem_Created_By();
        this.Item_Modified_By = item.getItem_Modified_By();

        return true;

    }

    //Get all items from database
    public static ArrayList<Item> GetAll() {

        SqlConnector connector = new SqlConnector();
        connector.Connect();
        if (!connector.isConnected()) {
            return null;
        }

        String query = "SELECT * FROM item;";
        ArrayList<Item> items = connector.ExecuteRead(query, Item.class);
        connector.Disconnect();

        if (items == null || items.isEmpty()) {
            return null;
        }

        items.forEach(item -> {
            item.getVendor().Get();
            item.getItemCategory().Get();
        });


        return items;
    }
    //Get all items from database that are not in autoreplenishment table
    public static ArrayList<Item> GetAllForNewReplenishment() {

        SqlConnector connector = new SqlConnector();
        connector.Connect();
        if (!connector.isConnected()) {
            return null;
        }

        String query = "SELECT * FROM item WHERE Item_ID NOT IN (SELECT Item_ID FROM autoreplenishment);";
        ArrayList<Item> items = connector.ExecuteRead(query, Item.class);
        connector.Disconnect();

        if (items == null || items.isEmpty()) {
            return null;
        }

        items.forEach(item -> {
            item.getVendor().Get();
            item.getItemCategory().Get();
        });


        return items;
    }
    //Get all items from database based on a field and value
    public static ArrayList<Item> Get(String _field, String _value) {

        SqlConnector connector = new SqlConnector();
        connector.Connect();
        if (!connector.isConnected()) {
            return null;
        }

        String query = "SELECT * FROM item WHERE " + _field + " LIKE ? ;";
        ArrayList<Item> items = connector.PrepareExecuteRead(query, Item.class, _value);

        connector.Disconnect();

        if (items == null || items.isEmpty()) {
            return null;
        }

       
        items.forEach(item -> {
            item.getVendor().Get();
            item.getItemCategory().Get();
        });
        return items;
        
    }

    @Override
    //Update item in database based on Item ID
    public boolean Update() {
        SqlConnector connector = new SqlConnector();
        connector.Connect();
        if (!connector.isConnected()) {
            return false;
        }

        String query = "UPDATE ITEM SET Item_Category_ID = ?, Vendor_ID = ?, Item_name = ?, Item_Desc = ?, Item_Quantity = ?, Item_Price = ?, Item_Modified_By = ? WHERE Item_ID = ?";

        boolean QueryExecuted = connector.PrepareExecuteDML(query,
                this.item_Category.getItem_Category_ID(), this.vendor.getVendor_ID(),
                this.Item_name, this.Item_Desc, this.Item_Quantity,
                this.Item_Price, this.Item_Modified_By,
                this.Item_ID);

        connector.Disconnect();

        return QueryExecuted;
    }

    @Override
    //Remove item in database based on Item ID
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

    @Override
    //Check if item's value is equal to another item's value
    public boolean equals(Object _obj) {
        if (this == _obj)
            return true;
        if (_obj == null || getClass() != _obj.getClass())
            return false;
        Item item = (Item) _obj;
        return Objects.equals(this.Item_ID, item.getItem_ID()); // Compare based on logical attribute
    }

    @Override
    //for HashSet and HashMap - idk why need this but it resolve the problem
    public int hashCode() {
        return Objects.hash(this.Item_ID); // Hash code based on logical attribute
    }


    @Override
    public String toString() {
        this.item_Category.Get();
        //display data in columns
        return String.format("| %-20s | %-20s | %-40s | %-50s | %-20s |",
                this.item_Category.getItem_Type(), this.vendor.getVendor_Name(),
                this.Item_name, this.Item_Desc, this.Item_Price);
    }

    
}
