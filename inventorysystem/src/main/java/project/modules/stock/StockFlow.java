package project.modules.stock;

import java.util.ArrayList;

import project.global.SqlConnector;
import project.modules.item.Item;

//REDO/REDESIGN this shitty code
public class StockFlow
{
    //Data Field
    private int Item_ID;
    private int Stock_Flow_ID;
    private int Item_Quantity;

    //getter & setter methods
    //Item_ID
    public int getItem_ID() {
        return this.Item_ID;
    }

    //Item_ID
    public void setItem_ID(int _ItemID) {
        this.Item_ID = _ItemID;
    }

    //Stock_Flow_IDw
    public int getStock_Flow_ID() {
        return this.Stock_Flow_ID;
    }

    //Stock_Flow_ID
    public void setStock_Flow_ID(int _StockFlowID) {
        this.Stock_Flow_ID = _StockFlowID;
    }

    //Item_Quantity
    public int getItem_Quantity() {
        return this.Item_Quantity;
    }

    //Item_Quantity
    public void setItem_Quantity(int _ItemQuantity) {
        this.Item_Quantity = _ItemQuantity;
    }

    //Methods
    /*
    1. pull out item quantity based on the stock in amount - done
    2. need to validate the quantity of the item
     * 
    */
    public static boolean Add(Stock _stock, ArrayList<StockFlow> _StockIns, Integer _Id) {
        _stock.Add(); //add stock flow first

        ArrayList<Item> items = Item.GetAll();

        SqlConnector connector = new SqlConnector();
        connector.Connect();
        if (!connector.isConnected()) {
            return false;
        }

        _StockIns.forEach(
                stockIn -> {
                    String query = "INSERT INTO Item_StockFlow (Item_ID, Stock_Flow_ID, Item_Quantity) VALUES (?, ?, ?);";
                    connector.PrepareDMLStackBatch(query,
                            stockIn.getItem_ID(), _Id.toString(), stockIn.getItem_Quantity());

                    //update the item quantity
                    items.forEach(
                            item -> {
                                if (item.getItem_ID() == stockIn.getItem_ID()) {
                                    item.setItem_Quantity(item.getItem_Quantity() - stockIn.getItem_Quantity());
                                    item.Update();
                                }
                            });
                });

        //execute query
        boolean QueryExecuted = connector.ExecuteStackBatch();

        connector.Disconnect();

        return QueryExecuted;
    }

    /*
    1. compare the previous amount and update the difference - check if the calculation is correct
    2. validate the quantity of the item
    */
    public static boolean Update(ArrayList<StockFlow> _StockIns, Integer _Id) {
        ArrayList<Item> items = Item.GetAll(); //get all items

        ArrayList<StockFlow> oldStockIns = StockFlow.Gets(_Id); //get stock flow

        SqlConnector connector = new SqlConnector();
        connector.Connect();

        if (!connector.isConnected()) {
            return false;
        }
        //AtomicBoolean shouldBreak = new AtomicBoolean(false); -- used to break the loop
        _StockIns.forEach(
                stockIn -> {
                    String query = "UPDATE Item_StockFlow SET Item_ID = ?, Item_Quantity = ? WHERE Stock_Flow_ID = ?;";
                    connector.PrepareDMLStackBatch(query,
                            stockIn.getItem_ID(), stockIn.getItem_Quantity(),
                            _Id.toString());

                    //update the item quantity
                    items.forEach(
                            item -> {
                                if (item.getItem_ID() == stockIn.getItem_ID()) {
                                    //diff = new item quantity - old item quantity
                                    //new total item quantity = old total item quantity + diff
                                    

                                    item.Update();
                                }
                            });
                });
        boolean QueryExecuted = connector.ExecuteStackBatch();

        connector.Disconnect();

        return QueryExecuted;
    }

    public static boolean Remove(Integer _Id) {

        SqlConnector connector = new SqlConnector();
        connector.Connect();

        if (!connector.isConnected()) {
            return false;
        }

        String query = "DELETE FROM Item_StockFlow WHERE Stock_Flow_ID = ? ;";
        boolean QueryExecuted = connector.PrepareExecuteDML(query, _Id);

        connector.Disconnect();

        Stock.Remove(_Id); //Remove Stock_Flow

        return QueryExecuted;
    }

    public static ArrayList<StockFlow> Gets(Integer _Id) 
    {
        //need to read from stock_Flow table first
        SqlConnector connector = new SqlConnector();
        connector.Connect();
        if (!connector.isConnected()) {
            return null;
        }

        String query = "SELECT * FROM Item_StockFlow WHERE Stock_Flow_ID = ?;";
        ArrayList<StockFlow> stockIns = connector.PrepareExecuteRead(query, StockFlow.class, _Id.toString());

        connector.Disconnect();

        return stockIns;
    }
    
    public static ArrayList<StockFlow> GetAll() {
        SqlConnector connector = new SqlConnector();
        connector.Connect();
        if (!connector.isConnected()) {
            return null;
        }

        String query = "SELECT * FROM Item_StockFlow;";
        ArrayList<StockFlow> stockIns = connector.PrepareExecuteRead(query, StockFlow.class);

        connector.Disconnect();

        return stockIns;
    }

    

    //Constructor
    public StockFlow(){}
}