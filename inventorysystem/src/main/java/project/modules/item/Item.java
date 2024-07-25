package project.modules.item;

import java.util.ArrayList;

//import user defined package
import project.global.CrudRepository;

public class Item implements CrudRepository<Integer> {
    //Data Fields
    private int Item_ID = 0;
    private int Item_Category_ID;
    private int Vendor_ID;

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

    //Vendor_ID
    public void setVendor_ID(int _VendorID) {
        if (_VendorID < 0) {
            throw new IllegalArgumentException("Vendor_ID cannot be negative");
        }
        this.Vendor_ID = _VendorID;
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

    //Methods
    @Override
    public boolean Create() {
        //Connect to database
        //Insert into database  
        // return true if no error occurs
        return true;
    }

    /***
     * @params: Pass in the Item ID
     * @return: Item object
     * @summary: Read the item from the database    
    } */
   @Override
    public final void Read(Integer _Id) {
        //Connect to database
        //Select from database
        // return Item if no error occurs else return null
    }

    //No resize is needed just use Array instead of ArrayList<>
    public static ArrayList<Item> ReadAll() {
        //Connect to database
        //Select from database
        // return Array if no error occurs else return null
        return new ArrayList<Item>();
    }

    public static ArrayList<Item> Read(String _field, String _value) {
        //Connect to database
        //Select from database
        // return Array if no error occurs else return null
        return new ArrayList<Item>();
    }

    @Override
    public boolean Update(Integer _Id) {
        
        //Connect to database
        //Update database
        // return true if the item modified successfully else false
        return true;
    }

    @Override
    public boolean Remove(Integer _Id) {
        //Connect to database
        //Delete from database
        // return true if the item removed successfully else false
        return true;
    }

    //Constructor
    public Item() {
    }

    public Item(Integer _Id) {
        this.Read(_Id);
    }

    //for inserting new item
    public Item(int _ItemCategoryID, int _VendorID, String _ItemName, String _ItemDesc, int _ItemQuantity,
            double _ItemPrice, String _UserID) {
        //get from database if the value is null
        this.Item_ID = 0; // get the current icrement value from database

        //Insert value
        this.Item_Category_ID = _ItemCategoryID;
        this.Vendor_ID = _VendorID;
        this.Item_name = _ItemName;
        this.Item_Desc = _ItemDesc;
        this.Item_Quantity = _ItemQuantity;
        this.Item_Price = _ItemPrice;
        this.Item_Created_By = _UserID; // User ID
        this.Item_Modified_By = _UserID; // User ID

    }
}
