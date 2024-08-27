package project.modules.schedule;

import java.time.LocalTime;
import java.util.Date;

import project.modules.transaction.DeliveryOrder;

public class Schedule {
    private int Schedule_ID;
    private DeliveryOrder DeliveryOrder;
    private Vehicle vehicle;
    private Date Schedule_Date;
    private LocalTime Schedule_Time;

    public Schedule() {
    }

    public Schedule(int _Schedule_ID, Vehicle _vehicle, Date _Schedule_Date, LocalTime _Schedule_Time) {
        this.Schedule_ID = _Schedule_ID;
        this.vehicle = _vehicle;
        this.Schedule_Date = _Schedule_Date;
        this.Schedule_Time = _Schedule_Time;
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

    public LocalTime getSchedule_Time() {
        return this.Schedule_Time;
    }

    public void setSchedule_Time(LocalTime _Schedule_Time) {
        this.Schedule_Time = _Schedule_Time;
    }

}
