package project.view;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

import project.global.ConsoleUI;
import project.modules.schedule.Vehicle;
import project.modules.user.User;

public class ViewVehicle {
    private static final Scanner sc = new Scanner(System.in);

    private ArrayList<Vehicle> vehicleList;
    private static User user;

    public ViewVehicle(User user) {
        ViewVehicle.user = user;
        this.vehicleList = Vehicle.GetAll();
    }

    public User getUser() {
        return user;
    }

    public ArrayList<Vehicle> getVehicleList() {
        return vehicleList;
    }

    

    public Vehicle
    selectVehicleFromList() {
        this.vehicleList = Vehicle.GetAll();
        int totalVehicle = 0;

        String[] columnNames = {"Vehicle Plate", "Vehicle Type", "Vehicle Driver"};

        if (vehicleList != null && !vehicleList.isEmpty()) {
            System.out.println("Vehicle List");
            distinctTableLine();
            System.out.printf("|");
            for (String columnName : columnNames) {
                if(columnName.equals("Vehicle Driver")){
                    System.out.printf(" %-35s ", "Driver Name");
                    System.out.printf("|");
                }else{
                System.out.printf(" %-15s ", columnName);
                System.out.printf("|");
                }
            }
            System.out.println("");
            distinctTableLine();
            for (Vehicle vehicle : vehicleList) {
                System.out.print(vehicle.toString());
                totalVehicle++;
                distinctTableLine();
            }

            System.out.println("Total Vehicle found in the database: " + totalVehicle);

            Vehicle selectedVehicle = null;
    
            do { 
                System.out.print("\n\nEnter the Vehicle Plate: ");
                String vehiclePlate = sc.nextLine();
                selectedVehicle = Vehicle.Get(vehiclePlate);
                if (selectedVehicle == null) {
                    System.out.println("Vehicle not found. Please try again.");
                }
            } while (selectedVehicle == null);
    
            return selectedVehicle;

        } else {
            System.out.println("No vehicle found.");
            ConsoleUI.pause();
        }
        return null;
    }

    public Vehicle selectVehicleFromList(LocalTime timeslot, LocalDate date) {

        this.vehicleList = Vehicle.GetAll(timeslot, date);
        int totalVehicle = 0;
    
        String[] columnNames = {"Vehicle Plate", "Vehicle Type", "Vehicle Driver"};
    
        if (vehicleList != null && !vehicleList.isEmpty()) {
            System.out.println("Vehicle List");
            distinctTableLine();
            System.out.printf("|");
            for (String columnName : columnNames) {
                if(columnName.equals("Vehicle Driver")){
                    System.out.printf(" %-35s ", "Driver Name");
                    System.out.printf("|");
                }else{
                System.out.printf(" %-15s ", columnName);
                System.out.printf("|");
                }
            }
            System.out.println("");
            distinctTableLine();
            for (Vehicle vehicle : vehicleList) {
                System.out.print(vehicle.toString());
                totalVehicle++;
                distinctTableLine();
            }
    
            System.out.println("Total available vehicle found in the database: " + totalVehicle);
            
            Vehicle selectedVehicle = null;
    
            do { 
                System.out.print("\n\nEnter the Vehicle Plate: ");
                String vehiclePlate = sc.nextLine();
                selectedVehicle = Vehicle.Get(vehiclePlate);
                if (selectedVehicle == null) {
                    System.out.println("Vehicle not found. Please try again.");
                }
            } while (selectedVehicle == null);
    
            return selectedVehicle;
    
        } else {
            System.out.println("No vehicle found.");
            ConsoleUI.pause();
            return null;
        }
    }
    

    public void
    displayVehicleList() {
        this.vehicleList = Vehicle.GetAll();
        int totalVehicle = 0;

        String[] columnNames = {"Vehicle Plate", "Vehicle Type", "Vehicle Driver"};

        if (vehicleList != null && !vehicleList.isEmpty()) {
            System.out.println("Vehicle List");
            distinctTableLine();
            System.out.printf("|");
            for (String columnName : columnNames) {
                if(columnName.equals("Vehicle Driver")){
                    System.out.printf(" %-35s ", "Driver Name");
                    System.out.printf("|");
                }else{
                System.out.printf(" %-15s ", columnName);
                System.out.printf("|");
                }
            }
            System.out.println("");
            distinctTableLine();
            for (Vehicle vehicle : vehicleList) {
                System.out.print(vehicle.toString());
                totalVehicle++;
                distinctTableLine();
            }

            System.out.println("Total Vehicle found in the database: " + totalVehicle);

            ConsoleUI.pause();

        } else {
            System.out.println("No vehicle found.");
            ConsoleUI.pause();
        }
    }

    private static void distinctTableLine(){
        System.out.println
        ("---------------------------------------------------------------------------");
    }
}
