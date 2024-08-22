package project.modules.transaction;

import project.global.SystemRunNo;

public class PurchaseOrder extends Transaction {

    @Override 
    public boolean Add() {
        
        return true;
    }

    @Override
    public boolean Update() {
        return true;
    }

    @Override
    public boolean Remove() {
        return true;
    }

    @Override
    public boolean Get() {
        return true;
    }

    @Override 
    public String GenerateDocNo() {
        return "PO" + SystemRunNo.Get("PO");   
    }
}
