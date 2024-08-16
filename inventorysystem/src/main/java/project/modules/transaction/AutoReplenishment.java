package project.modules.transaction;

import java.util.ArrayList;

import project.global.CrudOperation;
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

        ArrayList<AutoReplenishment> autoReplenishments = GetAll();

        autoReplenishments.forEach(replenishment->{
            if(replenishment.getItem().getItem_Quantity() <= replenishment.getItem_Threshold()){
                replenishment.getItem()
                        .setItem_Quantity(
                                replenishment.getItem().getItem_Quantity() + (replenishment.getItem_Threshold() * 2)
                            );
                replenishment.getItem().Update();
            }
        });
        //need to execute the transaction class also
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
}
