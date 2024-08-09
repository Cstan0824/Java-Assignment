package project.modules.stock;

import java.sql.Date;
import java.util.ArrayList;

import project.global.CrudOperation;
import project.global.SqlConnector;
import project.modules.item.Item;

public class Stock implements CrudOperation
{
    //Data Fields
    private int Stock_Flow_ID;
    private String Stock_Receipent_ID;
    private int Stock_Flow_Category;
    private Date Stock_Flow_Date;
    private double Stock_Flow_Total_Price;
    private String Stock_Flow_Created_By = "-";
    private String Stock_Flow_Modified_By = "-";

    //getter & setter methods

    //Stock_Receipent_ID
    public void setStock_Receipent_ID(String _StockReceipentID) {
        this.Stock_Receipent_ID = _StockReceipentID;
    }

    //Stock_Flow_Category
    public void setStock_Flow_Category(int _StockFlowCategory) {
        this.Stock_Flow_Category = _StockFlowCategory;
    }

    //Stock_Flow_Date
    public void setStock_Flow_Date(Date _StockFlowDate) {
        this.Stock_Flow_Date = _StockFlowDate;
    }

    //Stock_Flow_Total_Price
    public void setStock_Flow_Total_Price(double _StockFlowTotalPrice) {
        this.Stock_Flow_Total_Price = _StockFlowTotalPrice;
    }



    //Stock_Flow_ID
    public int getStock_Flow_ID() {
        return this.Stock_Flow_ID;
    }

    //Stock_Receipent_ID
    public String getStock_Receipent_ID() {
        return this.Stock_Receipent_ID;
    }

    //Stock_Flow_Category
    public int getStock_Flow_Category() {
        return this.Stock_Flow_Category;
    }

    //Stock_Flow_Date
    public Date getStock_Flow_Date() {
        return this.Stock_Flow_Date;
    }
    
    //Stock_Flow_Total_Price - might change to protected where only Stock_In class can be used
    public double getStock_Flow_Total_Price() {
        return this.Stock_Flow_Total_Price;
    }

    //Stock_Flow_Created_By
    public String getStock_Flow_Created_By() {
        return this.Stock_Flow_Created_By;
    }

    //Stock_Flow_Modifed_By
    public String getStock_Flow_Modified_By() {
        return this.Stock_Flow_Modified_By;
    }

    

    //Methods
    @Override
    public boolean Add() {
        SqlConnector connector = new SqlConnector();
        connector.Connect();
        if (connector.isConnected()) {
            return false;
        }
        String query = "INSERT INTO stock_flow (stock_receipent_id, stock_flow_category, stock_flow_date, stock_flow_created_by) VALUES (?,?,?,?)";
        boolean QueryExecuted = connector.PrepareExecuteDML(query,
                this.Stock_Receipent_ID, this.Stock_Flow_Category,
                this.Stock_Flow_Date, this.Stock_Flow_Created_By);

        connector.Disconnect();
        return QueryExecuted;
    }

    @Override
    public boolean Get() {
        SqlConnector connector = new SqlConnector();
        connector.Connect();
        if (connector.isConnected()) {
            return false;
        }

        String query = "SELECT * FROM stock_flow WHERE stock_flow_id = ?";

        ArrayList<Stock> stocks = connector.PrepareExecuteRead(query, Stock.class, this.Stock_Flow_ID);
        connector.Disconnect();

        if (stocks == null || stocks.isEmpty()) {
            return false;
        }

        //get the first result
        Stock stock = stocks.get(0);

        //pass the value to this
        this.Stock_Receipent_ID = stock.getStock_Receipent_ID();
        this.Stock_Flow_Category = stock.getStock_Flow_Category();
        this.Stock_Flow_Date = stock.getStock_Flow_Date();
        this.Stock_Flow_Total_Price = stock.getStock_Flow_Total_Price();
        this.Stock_Flow_Created_By = stock.getStock_Flow_Created_By();
        this.Stock_Flow_Modified_By = stock.getStock_Flow_Modified_By();

        return true;

    }

    @Override
    public boolean Update() {
        SqlConnector connector = new SqlConnector();
        connector.Connect();

        if (!connector.isConnected()) {
            return false;
        }

        String query = "UPDATE stock_flow SET stock_receipent_id = ?, stock_flow_category = ?, stock_flow_date = ?, stock_flow_modified_by = ? WHERE stock_flow_id = ?";
        boolean QueryExecuted = connector.PrepareExecuteDML(query,
                this.Stock_Receipent_ID, this.Stock_Flow_Category,
                this.Stock_Flow_Date, this.Stock_Flow_Modified_By, this.Stock_Flow_ID);

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

        String query = "DELETE FROM stock_flow WHERE stock_flow_id = ? ;";
        boolean QueryExecuted = connector.PrepareExecuteDML(query,
                this.Stock_Flow_ID);

        connector.Disconnect();

        return QueryExecuted;
    }

    public static int ReceiveCurrentStockFlowID() {
        SqlConnector connector = new SqlConnector();
        connector.Connect();

        if (!connector.isConnected()) {
            return -1;
        }

        String query = "SELELCT stock_flow_id FROM stock_flow ORDER BY DESC LIMIT 1";
        ArrayList<Integer> Ids = connector.ExecuteRead(query); //need to test whether it actually need to pass the <Integer> to ExcuteRead()

        if (Ids == null || Ids.isEmpty()) 
        {
            return -1;
        }

        Integer stock_flow_id = Ids.get(0);

        connector.Disconnect();

        return stock_flow_id;
    }

    public void CalculateStockTotalPrice(ArrayList<StockFlow> _StockIns) 
    {
        //get all items
        ArrayList<Item> items = Item.GetAll();

        //unable to modify local variable in lambda expression -- use array as workaround
        double[] totalPriceWrapper = { 0 };
        
        //calculate total price
        _StockIns.forEach(
                stockIn -> {
                    int tempItemID = stockIn.getItem_ID();
                    items.forEach(item -> {
                        if (item.getItem_ID() == tempItemID) 
                        {
                            totalPriceWrapper[0] += item.getItem_Price() * stockIn.getItem_Quantity();
                        }
                    });
                });

        this.Stock_Flow_Total_Price = totalPriceWrapper[0];
    }

    //Constructor
    public Stock() 
    {}

    //a constructor to declare all value by passing arg
    public Stock(String _StockReceipentID, int _StockFlowCategory, Date _StockFlowDate, double _StockFlowTotalPrice,
            String _StockFlowCreatedBy, String _StockFlowModifiedBy) {
        this.Stock_Receipent_ID = _StockReceipentID;
        this.Stock_Flow_Category = _StockFlowCategory;
        this.Stock_Flow_Date = _StockFlowDate;
        this.Stock_Flow_Total_Price = _StockFlowTotalPrice;
        this.Stock_Flow_Created_By = _StockFlowCreatedBy;
        this.Stock_Flow_Modified_By = _StockFlowModifiedBy;
    }
    
    //pass by Stock object as argument
    protected Stock(Stock _stock) {
        this(_stock.getStock_Receipent_ID(), _stock.getStock_Flow_Category(),
                _stock.getStock_Flow_Date(), _stock.getStock_Flow_Total_Price(), _stock.getStock_Flow_Created_By(),
                _stock.getStock_Flow_Modified_By());
    }
    
}
