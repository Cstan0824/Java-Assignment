package com.example;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Request {
    private  int Request_ID;
    private String Retailer_ID;
    private String Retailer_name;
    private String Retailer_Email;
    private String Retailer_Password;
    private String Retailer_Address;
    private String Status = "Pending";
    private String Retailer_Entry_Id;

    public Request(String Retailer_ID, String Retailer_name, String Retailer_Password, String Retailer_Email, String Retailer_Address) {
        this.Retailer_ID = Retailer_ID;
        this.Retailer_name = Retailer_name;
        this.Retailer_Email = Retailer_Email;
        this.Retailer_Password = Retailer_Password;
        this.Retailer_Address = Retailer_Address;
    }

    public Request() {
        this.Status = "Pending";

    }   

    public int getRequest_ID() {
        return Request_ID;
    }

    public void setRequest_ID(int Request_ID) {
        this.Request_ID = Request_ID;
    }
    
    public String getRetailer_ID() {
        return Retailer_ID;
    }
    
    
    public void setRetailer_ID(String Retailer_ID) {
        this.Retailer_ID = Retailer_ID;
    }
    
    public String getRetailer_Name() {
        return Retailer_name;
    }
    
    public void setRetailer_Name(String Retailer_name) {
        this.Retailer_name = Retailer_name;
    }
    
    public String getRetailer_Email() {
        return Retailer_Email;
    }
    
    public void setRetailer_Email(String Retailer_Email) {
        this.Retailer_Email = Retailer_Email;
    }
    
    public String getRetailer_Password() {
        return Retailer_Password;
    }
    
    public void setRetailer_Password(String Retailer_Password) {
        this.Retailer_Password = Retailer_Password;
    }
    
    public String getRetailer_Address() {
        return Retailer_Address;
    }
    
    public void setRetailer_Address(String Retailer_Address) {
        this.Retailer_Address = Retailer_Address;
    }
    
   
    public String getRetailer_Entry_Id() {
        return Retailer_Entry_Id;
    }
    
    public void setRetailer_Entry_Id(String Retailer_Entry_Id) {
        this.Retailer_Entry_Id = Retailer_Entry_Id;
    }

    public void saveRequest() // retailer use , so far ok 
    {
        String sql = "INSERT INTO Request (Request_Id, Retailer_Id, Retailer_Name, Retailer_Email, Retailer_Password, Retailer_Address, Status, Retailer_Entry_Id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        SqlConnector Connector = new SqlConnector();
        Connector.Connect();

        if(!Connector.isConnected()) {
            System.out.println("Connection failed");
            return;
        }

        boolean checking = Connector.PrepareExecuteDML(sql, this.getRequest_ID(), this.getRetailer_ID(), this.getRetailer_Name(), this.getRetailer_Email(), this.getRetailer_Password(), this.getRetailer_Address(), this.Status, this.getRetailer_Entry_Id());

        if(checking)
        {
            System.out.println("Request added successfully.");
        }
        else
        {
            System.out.println("Request failed.");
        }


    }

    public void approveRequest(String adminId) // admin use 
    {
        String updateRequestSQL = "UPDATE Request SET Status = 'Approved' WHERE Request_Id = ?";
        String insertRetailerSQL = "INSERT INTO Retailer (Retailer_Id, Retailer_Name, Retailer_Password, Retailer_Email, Retailer_Address, Retailer_Created_By, Retailer_Reg_Date) VALUES (?, ?, ?, ?, ?, ?, ?)";

        SqlConnector connector = new SqlConnector();
        connector.Connect();

        if (!connector.isConnected()) {
            System.out.println("Connection failed");
            return;
        }

        boolean requestUpdated = connector.PrepareExecuteDML(updateRequestSQL, this.getRequest_ID());

        if (requestUpdated) {
            

                boolean retailerInserted = connector.PrepareExecuteDML(insertRetailerSQL,this.Retailer_ID, this.Retailer_name, this.Retailer_Password, this.Retailer_Email, this.Retailer_Address, adminId, LocalDateTime.now());




            if (retailerInserted) {
                System.out.println("Request approved and retailer added successfully.");
            } 
            else {
                System.out.println("Failed to add retailer after approving request.");
            }
        } 
        else {
            System.out.println("Request approval failed.");
        }
    }

    public void rejectRequest() // admin use , can work
    {
        String sql = "UPDATE Request SET Status = 'Rejected' WHERE Request_Id = ?";

        SqlConnector Connector = new SqlConnector();
        Connector.Connect();

        if(!Connector.isConnected()) {
            System.out.println("Connection failed");
            return;
        }

        boolean checking = Connector.PrepareExecuteDML(sql, this.getRequest_ID());

        if(checking)
        {
            System.out.println("Request rejected successfully.");
        }
        else
        {
            System.out.println("Request failed.");
        }
    }

    public static ArrayList<Request> viewRequest() {
        String sql = "SELECT * FROM Request WHERE Status = 'Pending'";

        SqlConnector connector = new SqlConnector();
        connector.Connect();

        if (!connector.isConnected()) {
            System.out.println("Connection failed");
            return null;
        }

        ArrayList<Request> requests = connector.PrepareExecuteRead(sql, Request.class);

        if (requests == null || requests.isEmpty()) {
            System.out.println("No pending requests found.");
            // return back to menu
            return null;
        }

        System.out.println("================================================================================================================");
        System.out.println(" Request ID | Retailer ID | Retailer Name        | Retailer Email            | Retailer Address   | Status   ");
        System.out.println("================================================================================================================");
    
        // Data rows
        for (Request request : requests) {
            System.out.printf(" %-10s | %-11s | %-20s | %-24s | %-18s | %-8s %n",
                    request.getRequest_ID(),
                    request.getRetailer_ID(),
                    request.getRetailer_Name(),
                    request.getRetailer_Email(),
                    request.getRetailer_Address(),
                    request.Status);
        }
    
        System.out.println("================================================================================================================");
    
        return requests;
    }


    


    
    
     
   
}
