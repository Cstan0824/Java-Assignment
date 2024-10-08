package project.view;

import java.util.ArrayList;

import project.global.ConsoleUI;
import project.global.UserInputHandler;
import project.modules.schedule.Schedule;
import project.modules.user.Admin;
import project.modules.user.Retailer;
import project.modules.user.User;

public class ViewSchedule {
    
    private ArrayList<Schedule> scheduleList;
    private ArrayList<Schedule> pendingScheduleList;
    private static User user;

    

    public ViewSchedule(User user) {
        ViewSchedule.user = user;
        if (user instanceof Admin) {
            this.scheduleList = Schedule.GetAll();
            this.pendingScheduleList = Schedule.GetAllPending();
        } else if (user instanceof Retailer) {
            this.scheduleList = Schedule.GetAll(user.getUserId());
            this.pendingScheduleList = null;
        } else {
            this.scheduleList = null;
            this.pendingScheduleList = null;
        }
        
    }

    public User getUser() {
        return user;
    }

    public ArrayList<Schedule> getScheduleList() {
        return scheduleList;
    }

    public void displayScheduleList() {
       

        if (user instanceof Admin) {
            this.scheduleList = Schedule.GetAll();
        } else if (user instanceof Retailer) {
            this.scheduleList = Schedule.GetAll(user.getUserId());
        }

        int totalSchedule = 0;

        String[] columnNames = {"ScheduleID", "DocNo", "Vehicle Plate","Driver", "Time Slot", "Date", "Schedule Status"};

        if (scheduleList != null && !scheduleList.isEmpty()) {
            System.out.println("Schedule List");
            distinctTableLine();
            System.out.printf("|");
            for (String columnName : columnNames) {
                if(columnName.equals("Driver")){
                    System.out.printf(" %-40s ", columnName);
                    System.out.printf("|");
                } else{
                    System.out.printf(" %-15s ", columnName);
                    System.out.printf("|");
                }
            }
            System.out.println("");
            distinctTableLine();
            for (Schedule schedule : scheduleList) {
                System.out.print(schedule.toString());
                distinctTableLine();
                totalSchedule++;
            }

            System.out.println("Total Schedule found in the database: " + totalSchedule);
            ConsoleUI.pause();
            
        } else {
            System.out.println("No schedule found.");
            ConsoleUI.pause();
        }
    }

    //for modify or cancel schedule
    public Schedule selectPendingSchedule() {

        if (user instanceof Admin) {
            this.pendingScheduleList = Schedule.GetAllPending();
        } 

        int totalPendingSchedule = 0;

        String[] columnNames = {"ScheduleID", "DocNo", "Vehicle Plate", "Driver", "Time Slot", "Date", "Schedule Status"};

        if (pendingScheduleList != null && !pendingScheduleList.isEmpty()) {
            System.out.println("Pending Schedule List");
            distinctTableLine();
            System.out.printf("|");
            for (String columnName : columnNames) {
                if(columnName.equals("Driver")){
                    System.out.printf(" %-40s ", columnName);
                    System.out.printf("|");
                } else{
                    System.out.printf(" %-15s ", columnName);
                    System.out.printf("|");
                }
            }
            System.out.println("");
            distinctTableLine();
            for (Schedule schedule : pendingScheduleList) {
                
                System.out.print(schedule.toString());
                distinctTableLine();
                totalPendingSchedule++;
                
            }

            System.out.println("Total Pending Schedules available in the database: " + totalPendingSchedule);

            Schedule selectedSchedule = new Schedule();

            do {
                int scheduleID = UserInputHandler.getInteger("Enter Schedule ID: ", 1, Integer.MAX_VALUE);
                selectedSchedule = Schedule.Get(scheduleID);
                if (selectedSchedule == null) {
                    System.out.println("Schedule not found. Please try again.");
                }
            } while (selectedSchedule == null);

            return selectedSchedule;

        } else {
            System.out.println("No pending schedule found.");
            ConsoleUI.pause();
            return null;
        }
    }

    
    
    private static void distinctTableLine(){
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------");
    }
}
