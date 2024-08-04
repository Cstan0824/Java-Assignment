package project.modules.stock;

import java.util.ArrayList;

import project.global.SqlConnector;

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

    //Stock_Flow_ID
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
    //Resolve
    public boolean Add(ArrayList<StockFlow> _StockIns, Integer _Id) {
        //super.Add(); //Create Stock_Flow
        this.Stock_Flow_ID = _Id; //stock flow id is not declared in super.Add() need extra action to receive the value

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
                });

        //execute query
        boolean QueryExecuted = connector.ExecuteStackBatch();

        connector.Disconnect();

        return QueryExecuted;
    }

    //Resolve
    public boolean Update(ArrayList<StockFlow> _StockIns, Integer _Id) {
        SqlConnector connector = new SqlConnector();
        connector.Connect();

        if (!connector.isConnected()) {
            return false;
        }

        _StockIns.forEach(
                stockIn -> {
                    String query = "UPDATE Item_StockFlow SET Item_ID = ?, Item_Quantity = ? WHERE Stock_Flow_ID = ?;";
                    connector.PrepareDMLStackBatch(query,
                            stockIn.getItem_ID(), stockIn.getItem_Quantity(),
                            _Id.toString());
                });
        boolean QueryExecuted = connector.ExecuteStackBatch();

        connector.Disconnect();

        return QueryExecuted;
    }


    //Resolve
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

    //Resolve
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
    
    //Resolve
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
}