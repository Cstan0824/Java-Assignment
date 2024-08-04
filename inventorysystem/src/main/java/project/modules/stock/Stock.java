package project.modules.stock;

import java.sql.Date;
import java.util.ArrayList;

import project.global.SqlConnector;

public class Stock{
    //Data Fields
    private int Stock_Flow_ID;
    private String Stock_Receipent_ID;
    private String Stock_Flow_Category;
    private Date Stock_Flow_Date;
    private double Stock_Flow_Total_Price;
    private String Stock_Flow_Created_By = "-";
    private String Stock_Flow_Modifed_By = "-";

    //getter & setter methods

    //Stock_Receipent_ID
    public void setStock_Receipent_ID(String _StockReceipentID) {
        this.Stock_Receipent_ID = _StockReceipentID;
    }

    //Stock_Flow_Category
    public void setStock_Flow_Category(String _StockFlowCategory) {
        this.Stock_Flow_Category = _StockFlowCategory;
    }

    //Stock_Flow_Date
    public void setStock_Flow_Date(Date _StockFlowDate) {
        this.Stock_Flow_Date = _StockFlowDate;
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
    public String getStock_Flow_Category() {
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
    public String getStock_Flow_Modifed_By() {
        return this.Stock_Flow_Modifed_By;
    }

    

    //Methods
    protected boolean Add() {
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

    protected void Get(Integer _Id) {
        SqlConnector connector = new SqlConnector();
        connector.Connect();
        if (connector.isConnected()) {
            return;
        }

        String query = "SELECT * FROM stock_flow WHERE stock_flow_id = ?";

        ArrayList<Stock> stocks = connector.PrepareExecuteRead(query, Stock.class, _Id.toString());
        connector.Disconnect();

        if (stocks == null || stocks.isEmpty()) {
            return;
        }

        //get the first result
        Stock stock = stocks.get(0);

        this.Stock_Flow_ID = stock.getStock_Flow_ID();
        this.Stock_Receipent_ID = stock.getStock_Receipent_ID();
        this.Stock_Flow_Category = stock.getStock_Flow_Category();
        this.Stock_Flow_Date = stock.getStock_Flow_Date();
        this.Stock_Flow_Created_By = stock.getStock_Flow_Created_By();
        this.Stock_Flow_Modifed_By = stock.getStock_Flow_Modifed_By();
    }

    protected boolean Update(Integer _Id) {
        SqlConnector connector = new SqlConnector();
        connector.Connect();

        if (!connector.isConnected()) {
            return false;
        }

        String query = "UPDATE stock_flow SET stock_receipent_id = ?, stock_flow_category = ?, stock_flow_date = ?, stock_flow_modified_by = ? WHERE stock_flow_id = ?";
        boolean QueryExecuted = connector.PrepareExecuteDML(query,
                this.Stock_Receipent_ID, this.Stock_Flow_Category,
                this.Stock_Flow_Date, this.Stock_Flow_Modifed_By, _Id.toString());

        connector.Disconnect();
        return QueryExecuted;
    }

    protected static boolean Remove(Integer _Id) {
        SqlConnector connector = new SqlConnector();
        connector.Connect();
        if (!connector.isConnected()) {
            return false;
        }

        String query = "DELETE FROM stock_flow WHERE stock_flow_id = ? ;";
        boolean QueryExecuted = connector.PrepareExecuteDML(query,
                _Id.toString());

        connector.Disconnect();

        return QueryExecuted;
    }

    //Constructor
    public Stock() {
    }
}
