package project.modules.schedule;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import project.global.CrudOperation;
import project.global.SqlConnector;
import project.modules.item.Item;
import project.modules.transaction.DeliveryOrder;


public class Schedule implements CrudOperation{
    private int Schedule_ID;
    private DeliveryOrder deliveryOrder;
    private Vehicle vehicle;
    private LocalDate Schedule_Date;
    private LocalTime Time_Slot;
    private int Status;

    public Schedule() {
    }

    public Schedule(int _Schedule_ID, DeliveryOrder _deliveryOrder, Vehicle _vehicle, LocalDate _Schedule_Date, LocalTime _Time_Slot, int _Status) {
        this.Schedule_ID = _Schedule_ID;
        this.deliveryOrder = _deliveryOrder;
        this.vehicle = _vehicle;
        this.Schedule_Date = _Schedule_Date;
        this.Time_Slot = _Time_Slot;
        this.Status = _Status;
    }

    public Schedule (DeliveryOrder DO){
        this.deliveryOrder = DO;
    }


    public int getSchedule_ID() {
        return this.Schedule_ID;
    }

    public void setSchedule_ID(int _Schedule_ID) {
        this.Schedule_ID = _Schedule_ID;
    }

    public DeliveryOrder getDeliveryOrder() {
        return this.deliveryOrder;
    }

    public void setDeliveryOrder(DeliveryOrder _DeliveryOrder) {
        this.deliveryOrder = _DeliveryOrder;
    }
    
    public Vehicle getVehicle() {
        return this.vehicle;
    }

    public void setVehicle(Vehicle _vehicle) {
        this.vehicle = _vehicle;
    }

    public LocalDate getSchedule_Date() {
        return this.Schedule_Date;
    }

    public void setSchedule_Date(LocalDate _Schedule_Date) {
        this.Schedule_Date = _Schedule_Date;
    }

    public LocalTime getTime_Slot() {
        return this.Time_Slot;
    }

    public void setTime_Slot(LocalTime _Time_Slot) {
        this.Time_Slot = _Time_Slot;
    }

    public int getStatus(){
        return this.Status;
    }

    public void setStatus(int _Status){
        this.Status = _Status;
    }


    @Override
    public boolean Add() {
        SqlConnector connector = new SqlConnector();
        connector.Connect();
        if (!connector.isConnected()) {
            return false;
        }

        String query = "INSERT INTO Schedule(Doc_No, Vehicle_Plate, Schedule_Date, Time_Slot, Status) VALUES(?, ?, ?, ?, ?)";
        boolean queryExecuted = connector.PrepareExecuteDML(query, this.getDeliveryOrder().getDoc_No(), this.getVehicle().getVehicle_Plate(), this.getSchedule_Date(), this.getTime_Slot(), this.getStatus());

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

        String query = "UPDATE Schedule SET Vehicle_Plate = ?, Schedule_Date = ?, Time_Slot = ?, Status = ? WHERE Schedule_ID = ?";
        boolean queryExecuted = connector.PrepareExecuteDML(query, this.getVehicle().getVehicle_Plate(), this.getSchedule_Date(), this.getTime_Slot(), this.getStatus(), this.getSchedule_ID());

        if (!queryExecuted) {
            connector.Disconnect();
            return false;
        }

        connector.Disconnect();

        return queryExecuted;
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

        if (schedule == null) {
            return false;
        }

        this.setDeliveryOrder(schedule.getDeliveryOrder());
        this.setVehicle(schedule.getVehicle());
        this.setSchedule_Date(schedule.getSchedule_Date());
        this.setTime_Slot(schedule.getTime_Slot());

        return true;
    }

    public static Schedule Get(int _Schedule_ID) {

        String query = "SELECT * FROM Schedule WHERE Schedule_ID = " + _Schedule_ID;
        ArrayList<Schedule> schedules = GetScheduleList(query);
        if (schedules.isEmpty()) {
            return null;
        }
        Schedule schedule = schedules.get(0);
        return schedule;

    }

    public static Schedule Get(String _DocNo) {

       
        String query = "SELECT * FROM Schedule WHERE Doc_No = '"+ _DocNo + "';";
        ArrayList<Schedule> schedules = GetScheduleList(query);
        Schedule schedule = schedules.get(0);
        return schedule;

    }

    public static Schedule Get(Vehicle _vehicle) {

        String query = "SELECT * FROM Schedule WHERE Vehicle_Plate = '"+ _vehicle.getVehicle_Plate() + "'";
        ArrayList<Schedule> schedules = GetScheduleList(query);
        Schedule schedule = schedules.get(0);
        return schedule;
    
    }

    public static ArrayList<Schedule> GetScheduleList(String query) {
        ArrayList<Schedule> schedules = new ArrayList<>();
        final String url = "jdbc:mysql://localhost:3306/furniture_db";
        final String user = "root";
        final String password = "";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Step 1: Establish the connection
            connection = DriverManager.getConnection(url, user, password);

            // Step 2: Execute the SQL query
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            // Step 3: Process the ResultSet and map the data to Schedule objects
            while (resultSet.next()) {
                int scheduleId = resultSet.getInt("Schedule_ID");
                String docNo = resultSet.getString("Doc_No");
                String vehiclePlate = resultSet.getString("Vehicle_Plate");
                LocalTime timeSlot = resultSet.getTime("Time_Slot").toLocalTime();
                LocalDate scheduleDate = resultSet.getDate("Schedule_Date").toLocalDate();
                int status = resultSet.getInt("Status");

                // Retrieve the DeliveryOrder and Vehicle objects
                DeliveryOrder deliveryOrder = DeliveryOrder.Get(docNo);
                Vehicle vehicle = Vehicle.Get(vehiclePlate);

                // Create a new Schedule object
                Schedule schedule = new Schedule(scheduleId, deliveryOrder, vehicle, scheduleDate, timeSlot, status);

                // Add the Schedule object to the list
                schedules.add(schedule);
            }

        } catch (SQLException e) { 
        } finally {
            // Step 4: Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
            }
        }

        return schedules;
    }

    public static ArrayList<Schedule> GetAll() {

        String query = "SELECT * FROM Schedule";
        return GetScheduleList(query);
    }


    public static ArrayList<Schedule> GetAll(String recipient) {

        String query = "SELECT * FROM Schedule WHERE Doc_No LIKE 'DO%' AND Doc_No IN (SELECT Doc_No FROM transaction WHERE Doc_No LIKE 'DO%' AND Transaction_Recipient ='" + recipient + "')";

        return GetScheduleList(query);
       
    }

    public static ArrayList<Schedule> GetAllPending() {

        //String query = "SELECT * FROM schedule WHERE (Schedule_Date > CURDATE()) OR (Schedule_Date = CURDATE() AND Time_Slot > CURTIME())";

        String query = "SELECT * FROM schedule WHERE Status = 0";

        return GetScheduleList(query);
        
    }

    public static ArrayList<Schedule> GetAllForCancel(Vehicle vehicle) {

        String query = "SELECT * FROM Schedule WHERE Vehicle_Plate ='"+ vehicle + "'  AND (Schedule_Date > CURDATE()) OR (Schedule_Date = CURDATE() AND Time_Slot > CURTIME())";
       
        return GetScheduleList(query);
       
    }

    public static void StockOutProcess(){

        ArrayList<Schedule> schedules = Schedule.GetAllPending();

        if (schedules == null || schedules.isEmpty()) {
            return;
        }

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        for (Schedule schedule : schedules) {
            boolean process = false;

            if (schedule.getSchedule_Date().isBefore(today)) {
                // The date is before today, execute the code
                process = true;

            } 
            // Check if the schedule date is today
            else if (schedule.getSchedule_Date().isEqual(today)) {
                // If the time slot is before the current time, execute the code
                if (schedule.getTime_Slot().isBefore(now)) {
                    // The date is before today, execute the code
                   process = true;
                }
            }

            if (process){
                ArrayList<DeliveryOrder> deliveryOrders = DeliveryOrder.GetAll(schedule.getDeliveryOrder().getDoc_No());
    
                for (DeliveryOrder deliveryOrder : deliveryOrders) {
                    Item item = new Item(deliveryOrder.getItem().getItem_ID());
                    item.Get();
                    item.setItem_Quantity(item.getItem_Quantity() - deliveryOrder.getQuantity());
                    item.Update();
                }
                schedule.setStatus(1);
                schedule.Update();

            }
        }
    }

    @Override
    public String toString() {

        String format = "| %-15s | %-15s | %-15s | %-15s | %-15s | %-15s | %-15s |%n";

        this.getVehicle().Get();
        this.getDeliveryOrder().Get();

        String status = (this.getStatus() == 0 ? "Pending" : "Delivered");

        return String.format(format,
                this.getSchedule_ID(),
                this.deliveryOrder.getDoc_No(),
                this.vehicle.getVehicle_Plate(),
                this.vehicle.getDriver(),
                this.getTime_Slot(),
                this.getSchedule_Date(),
                status
                );
    }

   



}

