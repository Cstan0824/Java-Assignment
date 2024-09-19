package project.modules.item;

import java.util.ArrayList;

import project.global.SqlConnector;

public class ItemCategory {
    private int Item_Category_ID;
    private String Item_Type;

    //getter and setter
    public int getItem_Category_ID() {
        return this.Item_Category_ID;
    }

    public void setItem_Category_ID(int _ItemCategoryID) {
        this.Item_Category_ID = _ItemCategoryID;
    }

    public String getItem_Type() {
        return this.Item_Type;
    }

    public void setItem_Type(String _ItemType) {
        this.Item_Type = _ItemType;
    }

    //constructor
    public ItemCategory() {

    }

    public ItemCategory(int _ItemCategoryID, String _ItemType) {
        this.Item_Category_ID = _ItemCategoryID;
        this.Item_Type = _ItemType;
    }
    
    //methods
    @Override
    public String toString() {
        return String.format("%-40s", this.Item_Type);
    }
    //select all Item Category from database
    public static ArrayList<ItemCategory> GetAll() {
        SqlConnector connector = new SqlConnector();
        connector.Connect();
        if (!connector.isConnected()) {
            return null;
        }
        String query = "SELECT * FROM item_category";

        ArrayList<ItemCategory> result = connector.ExecuteRead(query, ItemCategory.class);
        return result;
    }

    //select Item Category based on id
    public boolean Get() {
        SqlConnector connector = new SqlConnector();
        connector.Connect();
        if (!connector.isConnected()) {
            return false;
        }

        String query = "SELECT * FROM item_category WHERE Item_Category_ID = ?";

        ArrayList<ItemCategory> itemCategories = connector.PrepareExecuteRead(query, ItemCategory.class,
                this.Item_Category_ID);

        connector.Disconnect();

        ItemCategory itemCategory = itemCategories.get(0);

        this.Item_Category_ID = itemCategory.getItem_Category_ID();
        this.Item_Type = itemCategory.getItem_Type();

        return true;
    }
    //add new Item Category to database
    public boolean Add() {
        SqlConnector connector = new SqlConnector();
        connector.Connect();
        if (!connector.isConnected()) {
            return false;
        }
        //check Item Type is already exist
        String chkExistenceQuery = "SELECT * FROM item_category WHERE Item_Type = ?";
        ArrayList<ItemCategory> result = connector.PrepareExecuteRead(chkExistenceQuery, ItemCategory.class,
                this.Item_Type);
        if (result == null || result.isEmpty()) {
            return false;
        }

        //insert if type is not exists
        String query = "INSERT INTO item_category (Item_Type) VALUES (?)";
        boolean queryExecuted = connector.PrepareExecuteDML(query, this.Item_Type);
        return queryExecuted;
    }

}
