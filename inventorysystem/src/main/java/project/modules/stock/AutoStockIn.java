package project.modules.stock;

import java.util.ArrayList;

import project.global.SqlConnector;
import project.modules.item.Item;

//utility class that can use to automatically stock in items
public class AutoStockIn {
    //Data Field
    private int Auto_Stock_In_ID;
    private int Item_ID;
    private int Threshold;

    //getter & setter methods
    //Auto_Stock_In_ID
    public int getAuto_Stock_In_ID() {
        return this.Auto_Stock_In_ID;
    }

    //Item_ID
    public int getItem_ID() {
        return this.Item_ID;
    }

    public void setItem_ID(int _ItemID) {
        this.Item_ID = _ItemID;
    }

    //Threshold
    public int getThreshold() {
        return this.Threshold;
    }

    public void setThreshold(int _Threshold) {
        this.Threshold = _Threshold;
    }

    //Methods
    //return true if the item is currently automatically stock in
    public static boolean CheckAutomationStatus(int _ItemID) 
    {
        if (_ItemID < 0) {
            return false;
        }

        SqlConnector connector = new SqlConnector();

        connector.Connect();

        if (!connector.isConnected()) {
            return false;
        }

        String query = "SELECT Item_ID FROM AutoStockIn WHERE Item_ID = ?;";

        ArrayList<Integer> Ids = connector.PrepareExecuteRead(query, _ItemID);
        if (Ids == null || Ids.isEmpty()) {
            return false;
        }

        connector.Disconnect();
        return true;
    }

    //return true if the automation is successfully enabled
    public static boolean EnableAutomation(int _ItemID, int _threshold)
    {
        if (_ItemID < 0) {
            return false;
        }

        if (!CheckAutomationStatus(_ItemID)) {
            return false;
        }

        SqlConnector connector = new SqlConnector();

        connector.Connect();

        if (!connector.isConnected()) {
            return false;
        }

        String query = "INSERT INTO AutoStockIn(Item_ID,Threshold) VALUES(?,?);";

        boolean QueryExecuted = connector.PrepareExecuteDML(query, _ItemID, _threshold);

        connector.Disconnect();

        //enable automation for the item
        return QueryExecuted;
    }
    
    //return true if the automation is successfully disabled
    public static boolean DisableAutomation(int _ItemID) 
    {
        if (_ItemID < 0) {
            return false;
        }

        if (!CheckAutomationStatus(_ItemID)) {
            return false;
        }

        SqlConnector connector = new SqlConnector();

        connector.Connect();

        if (!connector.isConnected()) {
            return false;
        }

        String query = "DELETE FROM AutoStockIn WHERE Item_ID = ?;";

        boolean QueryExecuted = connector.PrepareExecuteDML(query, _ItemID);

        connector.Disconnect();

        //disable automation for the item
        return QueryExecuted;
    }

    public static boolean ExecuteAutomation() 
    {
        ArrayList<Item> items = Item.GetAll();

        ArrayList<AutoStockIn> autoStockIns = GetAutomatedStockIn();

        if (items == null || autoStockIns == null) {
            return false;
        }

        items.forEach(item -> {
            autoStockIns.forEach(autoStockIn -> {
                if (item.getItem_ID() == autoStockIn.getItem_ID()) {
                    if (item.getItem_Quantity() <= autoStockIn.getThreshold()) {
                        item.setItem_Quantity(item.getItem_Quantity() + (autoStockIn.getThreshold() / 2)); //stock in based on the threshold value / 2
                        item.Update();
                    }
                }
            });
        });

        return true;
    }
    
    private static ArrayList<AutoStockIn> GetAutomatedStockIn() {
        SqlConnector connector = new SqlConnector();

        connector.Connect();

        if (!connector.isConnected()) {
            return null;
        }

        String query = "SELECT * FROM AutoStockIn;";
        ArrayList<AutoStockIn> autoStockIns = connector.PrepareExecuteRead(query, AutoStockIn.class);
        
        connector.Disconnect();

        return autoStockIns;
    }

}
