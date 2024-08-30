package project.modules.schedule;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

import project.global.CrudOperation;
import project.global.SqlConnector;
import project.modules.transaction.DeliveryOrder;


public class Schedule implements CrudOperation{
    private int Schedule_ID;
    private DeliveryOrder DeliveryOrder;
    private Vehicle vehicle;
    private Date Schedule_Date;
    private LocalTime Time_Slot;

    public Schedule() {
    }

    public Schedule(int _Schedule_ID, Vehicle _vehicle, Date _Schedule_Date, LocalTime _Time_Slot) {
        this.Schedule_ID = _Schedule_ID;
        this.vehicle = _vehicle;
        this.Schedule_Date = _Schedule_Date;
        this.Time_Slot = _Time_Slot;
    }

    public int getSchedule_ID() {
        return this.Schedule_ID;
    }

    public void setSchedule_ID(int _Schedule_ID) {
        this.Schedule_ID = _Schedule_ID;
    }

    public DeliveryOrder getDeliveryOrder() {
        return this.DeliveryOrder;
    }

    public void setDeliveryOrder(DeliveryOrder _DeliveryOrder) {
        this.DeliveryOrder = _DeliveryOrder;
    }
    
    public Vehicle getVehicle() {
        return this.vehicle;
    }

    public void setVehicle(Vehicle _vehicle) {
        this.vehicle = _vehicle;
    }

    public Date getSchedule_Date() {
        return this.Schedule_Date;
    }

    public void setSchedule_Date(Date _Schedule_Date) {
        this.Schedule_Date = _Schedule_Date;
    }

    public LocalTime getTime_Slot() {
        return this.Time_Slot;
    }

    public void setTime_Slot(LocalTime _Time_Slot) {
        this.Time_Slot = _Time_Slot;
    }


    @Override
    public boolean Add() {
        SqlConnector connector = new SqlConnector();
        connector.Connect();
        if (!connector.isConnected()) {
            return false;
        }

        String query = "INSERT INTO Schedule(Doc_No, Vehicle_Plate, Schedule_Date, Time_Slot) VALUES(?, ?, ?, ?)";
        boolean queryExecuted = connector.PrepareExecuteDML(query, this.getDeliveryOrder().getDoc_No(), this.getVehicle().getVehicle_Plate(), this.getSchedule_Date(), this.getTime_Slot());

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

        String query = "UPDATE Schedule SET Vehicle_Plat = ?, Schedule_Date = ?, Time_Slot = ? WHERE Schedule_ID = ?";

        boolean queryExecuted = connector.PrepareExecuteDML(query, this.getVehicle().getVehicle_Plate(), this.getSchedule_Date(), this.getTime_Slot(), this.getSchedule_ID());

        if (!queryExecuted) {
            connector.Disconnect();
            return false;
        }

        connector.Disconnect();

        return false;
    }

    @Override
    public boolean Remove() {

        SqlConnector connector = new SqlConnector();
        connector.Connect();
        if (!connector.isConnected()) {
            return false;
        }

        String query = "DELETE FROM Schedule WHERE Schedule_ID = ?";
        boolean queryExecuted = connector.PrepareExecuteDML(query, this.getSchedule_ID());

        if (!queryExecuted) {
            connector.Disconnect();
            return false;
        }

        connector.Disconnect();

        return queryExecuted;
    }

    @Override
    public boolean Get() {
        Schedule schedule = Schedule.Get(this.Schedule_ID);

        if (schedule != null) {
            this.setDeliveryOrder(schedule.getDeliveryOrder());
            this.setVehicle(schedule.getVehicle());
            this.setSchedule_Date(schedule.getSchedule_Date());
            this.setTime_Slot(schedule.getTime_Slot());
            return true;
        }else{

            return false;

        }
    }

    public static Schedule Get(int _Schedule_ID) {

        SqlConnector connector = new SqlConnector();
        connector.Connect();
        if (!connector.isConnected()) {
            return null;
        }

        String query = "SELECT * FROM Schedule WHERE Schedule_ID = ?";
        ArrayList<Schedule> schedules = connector.PrepareExecuteRead(query, Schedule.class, _Schedule_ID);

        if (schedules != null && !schedules.isEmpty()) {
            Schedule schedule = schedules.get(0);
            connector.Disconnect();
            return schedule;
        }else{
            connector.Disconnect();
            return null;
        }

    }

    public static Schedule Get(String _DocNo) {

        SqlConnector connector = new SqlConnector();
        connector.Connect();
        if (!connector.isConnected()) {
            return null;
        }

        String query = "SELECT * FROM Schedule WHERE Doc_No = ?";
        ArrayList<Schedule> schedules = connector.PrepareExecuteRead(query, Schedule.class, _DocNo);

        if (schedules != null && !schedules.isEmpty()) {
            Schedule schedule = schedules.get(0);
            connector.Disconnect();
            return schedule;
        }else{
            connector.Disconnect();
            return null;
        }

    }

    


}
