package project.modules.transaction;

import java.util.Date;

import project.global.CrudOperation;
import project.modules.item.Item;

public abstract class Transaction implements CrudOperation {
    private Item item;
    private String Doc_No;
    private String Source_Doc_No;
    private Date Transaction_Date;
    private int Quantity;
    private int Transaction_Mode;
    private String Transaction_Recipient;
    private String Transaction_Created_By;
    private String Transaction_Modified_By;

    public Item getItem() {
        return this.item;
    }

    public void setItem(Item _item) {
        this.item = _item;
    }

    public String getDoc_No() {
        return this.Doc_No;
    }

    public void setDoc_No(String _Doc_No) {
        this.Doc_No = _Doc_No;
    }

    public String getSource_Doc_No() {
        return this.Source_Doc_No;
    }

    public void setSource_Doc_No(String _Source_Doc_No) {
        this.Source_Doc_No = _Source_Doc_No;
    }

    public Date getTransaction_Date() {
        return this.Transaction_Date;
    }

    public void setTransaction_Date(Date _Transaction_Date) {
        this.Transaction_Date = _Transaction_Date;
    }

    public int getQuantity() {
        return this.Quantity;
    }

    public void setQuantity(int _Quantity) {
        this.Quantity = _Quantity;
    }

    public int getTransaction_Mode() {
        return this.Transaction_Mode;
    }

    public void setTransaction_Mode(int _Transaction_Mode) {
        this.Transaction_Mode = _Transaction_Mode;
    }

    public String getTransaction_Recipient() {
        return this.Transaction_Recipient;
    }

    public void setTransaction_Recipient(String _Transaction_Recipient) {
        this.Transaction_Recipient = _Transaction_Recipient;
    }

    public String getTransaction_Created_By() {
        return this.Transaction_Created_By;
    }

    public String getTransaction_Modified_By() {
        return this.Transaction_Modified_By;
    }

    public void setTransaction_Modified_By(String _Transaction_Modified_By) {
        this.Transaction_Modified_By = _Transaction_Modified_By;
    }

    public void setTransaction_Created_By(String _Transaction_Created_By) {
        this.Transaction_Created_By = _Transaction_Created_By;
    }

    
    public static class TransactionMode {
        public static final int STOCK_IN = 0;
        public static final int STOCK_OUT = 1;
    }

    @Override
    public abstract boolean Add();

    @Override
    public abstract boolean Update();

    @Override
    public abstract boolean Remove();

    @Override
    public abstract boolean Get();

    public abstract String GenerateDocNo();

    //Item
    public Transaction() {
    }
    
    //initialize all the value
    public Transaction(Item _item, String _Doc_No, Date _Transaction_Date, int _Quantity, int _Transaction_Mode,
            String _Transaction_Recipient, String _Transaction_Created_By, String _Transaction_Modified_By) {
        this.item = _item;
        this.Doc_No = _Doc_No;
        this.Transaction_Date = _Transaction_Date;
        this.Quantity = _Quantity;
        this.Transaction_Mode = _Transaction_Mode;
        this.Transaction_Recipient = _Transaction_Recipient;
        this.Transaction_Created_By = _Transaction_Created_By;
        this.Transaction_Modified_By = _Transaction_Modified_By;
    }
}
