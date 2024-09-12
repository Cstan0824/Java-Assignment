package project.modules.schedule;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import project.global.CrudOperation;
import project.global.SqlConnector;


public class Vehicle implements CrudOperation {
    private String Vehicle_Plate;
    private String Vehicle_Type;
    private String Driver;

    //Constructors
    public Vehicle() {
    }

    public Vehicle(String _Vehicle_Plate, String _Vehicle_Type, String _Driver) {
        this.Vehicle_Plate = _Vehicle_Plate;
        this.Vehicle_Type = _Vehicle_Type;
        this.Driver = _Driver;
    }

    //Getters and Setters
    public String getVehicle_Plate() {
        return this.Vehicle_Plate;
    }

    public void setVehicle_Plate(String _Vehicle_Plate) {
        this.Vehicle_Plate = _Vehicle_Plate;
    }

    public String getVehicle_Type() {
        return this.Vehicle_Type;
    }
    
    public void setVehicle_Type(String _Vehicle_Type) {
        this.Vehicle_Type = _Vehicle_Type;
    }

    public String getDriver() {
        return this.Driver;
    }

    public void setDriver(String _Driver) {
        this.Driver = _Driver;
    }

    //Methods
    @Override
    public boolean Add() {
        SqlConnector connector = new SqlConnector();
        connector.Connect();
        if (!connector.isConnected()) {
            return false;
        }

        String query = "INSERT INTO Vehicle(Vehicle_Plate, Vehicle_Type, Driver) VALUES(?, ?, ?)";
        boolean queryExecuted = connector.PrepareExecuteDML(query, this.getVehicle_Plate(), this.getVehicle_Type(), this.getDriver());

        if (!queryExecuted) {
            connector.Disconnect();
            return false;
        }

        return queryExecuted;
    }
    
    @Override
    public boolean Update() {

        SqlConnector connector = new SqlConnector();
        connector.Connect();
        if (!connector.isConnected()) {
            return false;
        }
        
        String query = "UPDATE Vehicle SET Vehicle_Type = ?, Driver = ? WHERE Vehicle_Plate = ?";
        boolean queryExecuted = connector.PrepareExecuteDML(query, this.getVehicle_Type(), this.getDriver(), this.getVehicle_Plate());

        if (!queryExecuted) {
            connector.Disconnect();
            return false;
        }

        return queryExecuted;
    }

    @Override
    public boolean Remove() {

        SqlConnector connector = new SqlConnector();
        connector.Connect();
        if (!connector.isConnected()) {
            return false;
        }

        String query = "DELETE FROM Vehicle WHERE Vehicle_Plate = ?";
        boolean queryExecuted = connector.PrepareExecuteDML(query, this.getVehicle_Plate());

        if (!queryExecuted) {
            connector.Disconnect();
            return false;
        }
        return queryExecuted;
    }

    @Override
    public boolean Get() {
        Vehicle vehicle = Vehicle.Get(this.getVehicle_Plate());
        if (vehicle == null) {
            return false;
        }

        this.setVehicle_Type(vehicle.getVehicle_Type());
        this.setDriver(vehicle.getDriver());

        
        return true;
    }

    public static Vehicle Get(String _Vehicle_Plate) {

        SqlConnector connector = new SqlConnector();
        connector.Connect();
        if (!connector.isConnected()) {
            return null;
        }

        String query = "SELECT * FROM Vehicle WHERE Vehicle_Plate = ?";
        ArrayList<Vehicle> vehicles = connector.PrepareExecuteRead(query, Vehicle.class, _Vehicle_Plate);
        
        if (vehicles != null && !vehicles.isEmpty()) {
            Vehicle vehicle = vehicles.get(0);
            connector.Disconnect();
            return vehicle;
        }else {
            connector.Disconnect();
            return null;
        }
    }

    public static ArrayList<Vehicle> GetAll(){

        SqlConnector connector = new SqlConnector();
        
        try{
            connector.Connect();
            if (!connector.isConnected()) {
                return null;
            }

            String query = "SELECT * FROM Vehicle";
            ArrayList<Vehicle> vehicles = connector.PrepareExecuteRead(query, Vehicle.class);
            
            if (vehicles != null && !vehicles.isEmpty()) {
                return vehicles;
            }else {
                System.out.println("No vehicles found");
                return null;
            }

        }finally{
            connector.Disconnect();
        }



    }

    public static ArrayList<Vehicle> GetAll(LocalTime timeslot, LocalDate date) {

        SqlConnector connector = new SqlConnector();
        connector.Connect();
        if (!connector.isConnected()) {
            return null;
        }

        String query = "SELECT * FROM Vehicle WHERE Vehicle_Plate NOT IN (SELECT Vehicle_Plate FROM Schedule WHERE Time_Slot = ? AND Schedule_Date = ?)";
        ArrayList<Vehicle> vehicles = connector.PrepareExecuteRead(query, Vehicle.class, timeslot, date);

        if (vehicles != null && !vehicles.isEmpty()) {
            connector.Disconnect();
            return vehicles;
        }else {
            connector.Disconnect();
            return null;
        }


    }

    
    @Override
    public String toString(){

        String format = "| %-15s | %-15s | %-40s |%n";

        return String.format(format, this.getVehicle_Plate(), this.getVehicle_Type(), this.getDriver());

    }
}
