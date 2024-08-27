package project.modules.transaction;

import java.sql.Date;
import java.util.ArrayList;

import project.global.SystemRunNo;
import project.modules.item.Item;


public class DeliveryOrder extends Transaction {

    public DeliveryOrder() {
        this.setTransaction_Mode(TransactionMode.STOCK_OUT);
        this.setTransaction_Date(new Date(System.currentTimeMillis()));
    }

    public DeliveryOrder(String _DocNo) {
        this.setTransaction_Mode(TransactionMode.STOCK_OUT);
        this.setTransaction_Date(new Date(System.currentTimeMillis()));
        this.setDoc_No(_DocNo);
    }

    public DeliveryOrder(Item _item, String _Doc_No, Date _Transaction_Date, int _Quantity,
            String _Transaction_Recipient, String _Transaction_Created_By, String _Transaction_Modified_By) {
        super(_item, _Doc_No, _Transaction_Date, _Quantity, TransactionMode.STOCK_OUT,
                _Transaction_Recipient,
                _Transaction_Created_By, _Transaction_Modified_By);
    }

    //Setter and Getters inherited

    //Implementations of CRUD Operations
    @Override
    public boolean Add() {



        return false;
    }

    public boolean addMultipleItemDO() {



        return false;
    }

    @Override
    public boolean Update() {



        return false;
    }

    @Override
    public boolean Remove() {



        return false;
    }

    @Override
    public boolean Get() {



        return false;
    }

    public DeliveryOrder Get(String _DocNo) {



        return null;
    }

    
    public ArrayList<DeliveryOrder> GetAll() {


        return null;
    }


    //Generate Doc No
    @Override
    public String GenerateDocNo() {
        return "DO" + String.format("%05d", SystemRunNo.Get("DO"));
    }
}
