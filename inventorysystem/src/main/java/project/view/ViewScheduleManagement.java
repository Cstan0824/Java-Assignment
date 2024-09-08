package project.view;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

import project.global.ConsoleUI;
import project.global.MailSender;
import project.global.MailTemplate;
import project.global.PdfConverter;
import project.global.PdfTemplate;
import project.global.UserInputHandler;
import project.modules.schedule.Schedule;
import project.modules.schedule.Vehicle;
import project.modules.transaction.DeliveryOrder;
import project.modules.user.Retailer;
import project.modules.user.User;

public class ViewScheduleManagement {
    private static final Scanner sc = new Scanner(System.in);
    private ViewDeliveryOrder viewDeliveryOrder;
    private ViewSchedule viewSchedule;
    private ViewVehicle viewVehicle;
    private static User user;


    public ViewScheduleManagement(User user) {
        ViewScheduleManagement.user = user;
        this.viewDeliveryOrder = new ViewDeliveryOrder(user);
        this.viewVehicle = new ViewVehicle(user);
        this.viewSchedule = new ViewSchedule(user);
    }

    //getter and setter

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        ViewScheduleManagement.user = user;
    }

    public ViewDeliveryOrder getViewDeliveryOrder() {
        return viewDeliveryOrder;
    }

    public void setViewDeliveryOrder(ViewDeliveryOrder viewDeliveryOrder) {
        this.viewDeliveryOrder = viewDeliveryOrder;
    }

    public ViewSchedule getViewSchedule() {
        return viewSchedule;
    }

    public void setViewSchedule(ViewSchedule viewSchedule) {
        this.viewSchedule = viewSchedule;
    }

    public ViewVehicle getViewVehicle() {
        return viewVehicle;
    }

    public void setViewVehicle(ViewVehicle viewVehicle) {
        this.viewVehicle = viewVehicle;
    }

    public void adminMenu() {
        boolean exit = false;
        while(!exit) {
            ConsoleUI.clearScreen();
            System.out.println("Schedule Management");
            System.out.println("1. View Delivery Order Records");
            System.out.println("2. Cancel Delivery Order");
            System.out.println("3. View Schedule Records");
            System.out.println("4. Create Schedule");
            System.out.println("5. Modify Schedule");
            System.out.println("6. Cancel Schedule");
            System.out.println("7. Vehicle Management");
            System.out.println("8. Exit");
            int choice = UserInputHandler.getInteger("Choose your actions: ", 1, 8);
            switch (choice) {
                case 1:
                    viewDORecords();
                    break;
                case 2:
                    DOCancellation();
                    break;
                case 3:
                    viewScheduleRecords();
                    break;
                case 4:
                    createSchedule();
                    break;
                case 5:
                    scheduleModification();
                    break;
                case 6:
                    scheduleCancellation();
                    break;
                case 7:
                    vehicleManagementMenu();
                    break;
                case 8:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    public void retailerMenu(){

        boolean exit = false;
        while(!exit) {
            ConsoleUI.clearScreen();
            System.out.println("Schedule Management");
            System.out.println("1. Check Order");
            System.out.println("2. Check Order Schedule");
            System.out.println("3. Exit");
            int choice = UserInputHandler.getInteger("Choose your actions: ", 1, 3);
            switch (choice) {
                case 1:
                    viewDORecords();
                    break;
                case 2:
                    viewScheduleRecords();
                    break;
                case 3:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }




    }

    //DO management
    private void viewDORecords() {
        ConsoleUI.clearScreen();
        if (user.getUserType().equals("Admin") || user.getUserType().equals("Retailer")) {
            boolean error = false;
            do {
                ArrayList<DeliveryOrder> deliveryOrders = viewDeliveryOrder.selectDeliveryOrderFromList();
                if (deliveryOrders == null || deliveryOrders.isEmpty()){
                    System.out.println("No Delivery Order found.");
                    error = true;
                }
            }while (error);

        }

    }

    //cancel DO so that can edit Sales Order
    private void DOCancellation() {
        ConsoleUI.clearScreen();
        if (user.getUserType().equals("Admin")) {
            ArrayList<DeliveryOrder> deliveryOrders = viewDeliveryOrder.selectPendingDeliveryOrder();
            if (deliveryOrders == null || deliveryOrders.isEmpty()) {
                System.out.println("No Delivery Order found.");
            }else {

                String choice = UserInputHandler.getConfirmation("Are you sure you want to cancel the selected delivery order? ");

                if (!choice.equalsIgnoreCase("Y")) {
                    return;
                }

                for (DeliveryOrder deliveryOrder : deliveryOrders) {
                    if (deliveryOrder.Remove()) {
                        System.out.println("\nDelivery Order " + deliveryOrder.getDoc_No() + " cancelled successfully.");
                    } else {
                        System.out.println("Failed to cancel delivery order " + deliveryOrder.getDoc_No());
                    }
                }
            }
        }
    }
    
    //schedule management
    private void viewScheduleRecords() {
        ConsoleUI.clearScreen();
        if (user.getUserType().equals("Admin") || user.getUserType().equals("Retailer")) {
            
            viewSchedule.displayScheduleList();
               
        }
    }

    private void createSchedule() {
        ConsoleUI.clearScreen();

        if (user.getUserType().equals("Admin")) {
            File file;
            MailSender mail;
            PdfConverter pdf;
            ArrayList<DeliveryOrder> deliveryOrders = viewDeliveryOrder.selectPendingDeliveryOrder();

            if (deliveryOrders == null || deliveryOrders.isEmpty()){
                return;
            }

            DeliveryOrder deliveryOrder = deliveryOrders.get(0);

            //create Schedule
            Schedule schedule = new Schedule(deliveryOrder);
            schedule.setSchedule_Date(chooseDate(LocalDate.now()));
            schedule.setTime_Slot(chooseTime());
            System.out.println("\nPlease select a vehicle for the delivery schedule: ");
            schedule.setVehicle(viewVehicle.selectVehicleFromList(schedule.getTime_Slot(),schedule.getSchedule_Date()));
            schedule.setStatus(0);

            if (schedule.Add()){
                System.out.println("\n\nSchedule for delivery order "+ schedule.getDeliveryOrder().getDoc_No()+ " created successfully.");

                //display created schedule
                System.out.println("\n\nCreated Schedule Details");

                String[] columnNames = {"ScheduleID", "DocNo", "Vehicle Plate","Driver", "Time Slot", "Date", "Status"};
                distinctTableLine();
                System.out.printf("|");
                for (String columnName : columnNames) {
                    System.out.printf(" %-15s ", columnName);
                    System.out.printf("|");
                }
                System.out.println("");
                distinctTableLine();
                System.out.print(schedule.toString());
                distinctTableLine();

                ConsoleUI.pause();

                //implement pdf and email here
                URL resource = getClass().getClassLoader()
                .getResource("project/Report");
                file = new File(
                resource.getPath().replace("%20", " "), schedule.getDeliveryOrder().getDoc_No() + "-Schedule.pdf");

                pdf = new PdfConverter(file,
                        new PdfTemplate(schedule));
                pdf.Save();

                Retailer retailer = new Retailer();
                retailer.setUserId(schedule.getDeliveryOrder().getTransaction_Recipient());
                retailer.Get();

                mail = new MailSender(
                        retailer.getUserEmail(),
                        "Delivery Schedule",
                        new MailTemplate(schedule.getDeliveryOrder().getDoc_No(), MailTemplate.TemplateType.SCHEDULE_CREATION));
                mail.AttachFile(file);
                mail.Send();

            }else{
                System.out.println("Failed to create schedule for delivery order "+ schedule.getDeliveryOrder().getDoc_No());
            }

        }
    }

    private void scheduleModification() {
        ConsoleUI.clearScreen();

        if (user.getUserType().equals("Admin")) {
            File file;
            MailSender mail;
            PdfConverter pdf;
            Schedule schedule = viewSchedule.selectPendingSchedule();

            if (schedule == null){
                return;
            }

            //modify schedule
            schedule.setSchedule_Date(chooseDate(schedule.getSchedule_Date()));

            schedule.setTime_Slot(chooseTime());

            schedule.setVehicle(viewVehicle.selectVehicleFromList(schedule.getTime_Slot(),schedule.getSchedule_Date()));

            if (schedule.Update()){
                System.out.println("Schedule for delivery order "+ schedule.getDeliveryOrder().getDoc_No()+ " updated successfully.");

                //display updated schedule
                System.out.println("\n\nUpdated Schedule Details");

                String[] columnNames = {"ScheduleID", "DocNo", "Vehicle Plate","Driver", "Time Slot", "Date", "Status"};
                distinctTableLine();
                System.out.printf("|");
                for (String columnName : columnNames) {
                    System.out.printf(" %-15s ", columnName);
                    System.out.printf("|");
                }
                System.out.println("");
                distinctTableLine();
                System.out.print(schedule.toString());
                distinctTableLine();

                ConsoleUI.pause();

                //implement pdf and email here
                URL resource = getClass().getClassLoader()
                .getResource("project/Report");
                file = new File(
                resource.getPath().replace("%20", " "),"New " + schedule.getDeliveryOrder().getDoc_No() + "-Schedule.pdf");

                pdf = new PdfConverter(file,
                        new PdfTemplate(schedule));
                pdf.Save();

                Retailer retailer = new Retailer();
                retailer.setUserId(schedule.getDeliveryOrder().getTransaction_Recipient());
                retailer.Get();

                mail = new MailSender(
                        retailer.getUserEmail(),
                        "New Delivery Schedule",
                        new MailTemplate(schedule.getDeliveryOrder().getDoc_No(), MailTemplate.TemplateType.SCHEDULE_MODIFICATION));
                mail.AttachFile(file);
                mail.Send();

            }else{
                System.out.println("Failed to update schedule for delivery order "+ schedule.getDeliveryOrder().getDoc_No());
            }

        }


    }
    
    private void scheduleCancellation() {
        ConsoleUI.clearScreen();

        if (user.getUserType().equals("Admin")) {
            Schedule schedule = viewSchedule.selectPendingSchedule();
            if (schedule == null) {
                return;
            }
            String choice = UserInputHandler.getConfirmation("Are you sure you want to cancel the selected schedule?");
            if (!choice.equalsIgnoreCase("Y")) {
                return;
            }
            if (schedule.Remove()) {
                System.out.println("Schedule for delivery order " + schedule.getDeliveryOrder().getDoc_No() + " cancelled successfully.");

                //add mail notification here
                MailSender mail;
                Retailer retailer = new Retailer();
                DeliveryOrder deliveryOrder = DeliveryOrder.Get(schedule.getDeliveryOrder().getDoc_No());
                retailer.setUserId(deliveryOrder.getTransaction_Recipient());
                retailer.Get();
                mail = new MailSender(
                retailer.getUserEmail(),
                "Delivery Schedule Cancelled",
                new MailTemplate(deliveryOrder.getDoc_No(), MailTemplate.TemplateType.SCHEDULE_CANCELLATION));
                mail.Send();

            } else {
                System.out.println("Failed to cancel schedule for delivery order " + schedule.getDeliveryOrder().getDoc_No());
            }
        }
    }
    
    
    //vehicle management

    private void vehicleManagementMenu(){
        boolean exit = false;
        while(!exit) {
            ConsoleUI.clearScreen();
            System.out.println("Vehicle Management");
            System.out.println("1. View Vehicle Records");
            System.out.println("2. Add Vehicle");
            System.out.println("3. Modify Vehicle");
            System.out.println("4. Remove Vehicle");
            System.out.println("5. Exit");
            int choice = UserInputHandler.getInteger("Choose your actions: ", 1, 5);
            switch (choice) {
                case 1:
                    viewVehicleRecords();
                    break;
                case 2:
                    addVehicle();
                    break;
                case 3:
                    modifyVehicle();
                    break;
                case 4:
                    removeVehicle();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }

    }

    private void viewVehicleRecords() {
        ConsoleUI.clearScreen();

        if (user.getUserType().equals("Admin")) {
            
            viewVehicle.displayVehicleList();

        }
    }

    private void addVehicle() {
        ConsoleUI.clearScreen();

        if (user.getUserType().equals("Admin")) {
            Vehicle vehicle = new Vehicle();
            
            System.out.print("Enter vehicle plate: ");
            String vehiclePlate = sc.nextLine();
            vehicle.setVehicle_Plate(vehiclePlate);

            System.out.print("Enter vehicle type: ");
            String vehicleType = sc.nextLine();
            vehicle.setVehicle_Type(vehicleType);

            System.out.print("Enter vehicle driver name: ");
            String driverName = sc.nextLine();
            vehicle.setDriver(driverName);


            if (vehicle.Add()) {
                System.out.println("Vehicle " + vehicle.getVehicle_Plate()+" added successfully.");

                //display added vehicle
                String[] columnNames = {"Vehicle Plate", "Vehicle Type", "Vehicle Driver"};

                System.out.println("New Vehicle Details");

                vehicleTableLine();
                System.out.printf("|");
                for (String columnName : columnNames) {
                    System.out.printf(" %-15s ", columnName);
                    System.out.printf("|");
                }
                System.out.println("");
                vehicleTableLine();
                System.out.print(vehicle.toString());
                vehicleTableLine();
                
                ConsoleUI.pause();
                
            } else {
                System.out.println("Failed to add vehicle.");
            }
        }
    }

    private void modifyVehicle() {
        ConsoleUI.clearScreen();


        if (user.getUserType().equals("Admin")) {
            Vehicle vehicle = viewVehicle.selectVehicleFromList();
            if (vehicle == null) {
                System.out.println("No Vehicle found.");
                return;
            }

            System.out.print("Enter new driver name: ");
            String driverName = sc.nextLine();
            vehicle.setDriver(driverName);

            if (vehicle.Update()) {
                System.out.println("\n\nVehicle " + vehicle.getVehicle_Plate() + " updated successfully.");

                //display updated vehicle
                String[] columnNames = {"Vehicle Plate", "Vehicle Type", "Vehicle Driver"};

                System.out.println("\n\nUpdated Vehicle Details");

                vehicleTableLine();
                System.out.printf("|");
                for (String columnName : columnNames) {
                    System.out.printf(" %-15s ", columnName);
                    System.out.printf("|");
                }
                System.out.println("");
                vehicleTableLine();
                System.out.print(vehicle.toString());
                vehicleTableLine();

                ConsoleUI.pause();
            } else {
                System.out.println("Failed to update vehicle " + vehicle.getVehicle_Plate());
            }
        }



    }
    
    private void removeVehicle() {
        ConsoleUI.clearScreen();

        if (user.getUserType().equals("Admin")) {
            Vehicle vehicle = viewVehicle.selectVehicleFromList();
            if (vehicle == null) {
                System.out.println("No Vehicle found.");
                return;
            }
            
            String choice = UserInputHandler.getConfirmation("Are you sure you want to remove the selected vehicle? ");

            if (!choice.equalsIgnoreCase("Y")) {
                return;
            }

            if (vehicle.Remove()) {
                System.out.println("Vehicle " + vehicle.getVehicle_Plate() + " removed successfully.");

                //remove the upcoming schedules associated with the vehicle
                ArrayList<Schedule> schedules = Schedule.GetAllForCancel(vehicle);
                if (schedules != null && !schedules.isEmpty()) {
                    for (Schedule schedule : schedules) {
                        schedule.Remove();
                    }
                }

                ConsoleUI.pause();
            } else {
                System.out.println("Failed to remove vehicle " + vehicle.getVehicle_Plate());
            }
        }
    }
    
    //display date and choose date
    private LocalDate chooseDate(LocalDate startDate){

        LocalDate[] dates = new LocalDate[30];
        int columnWidth = 20; // Adjust column width to ensure proper alignment

        // Populate the dates array
        for (int i = 0; i < 30; i++) {
            dates[i] = startDate.plusDays(i);
        }

        // Display the header
        System.out.println("\n\nPlease choose a date by entering the corresponding index number:");
        System.out.println(repeatChar('-', (columnWidth + 3) * 5)); // Add separator line

        // Display the index numbers and dates in a table format
        for (int i = 0; i < 30; i++) {
            // Display index numbers with leading zeros (e.g., 001, 002)
            System.out.printf("| %" + columnWidth + "s ", String.format("%03d", i + 1));
            if ((i + 1) % 5 == 0) {
                System.out.println("|"); // End of row, print closing "|"
                for (int j = i - 4; j <= i; j++) {
                    System.out.printf("| %" + columnWidth + "s ", dates[j]);
                }
                System.out.println("|");
                System.out.println(repeatChar('-', (columnWidth + 3) * 5)); // Use custom method for separator
            }
        }
        int index;
        boolean error;
        // User input to select the date
        do{
            index = UserInputHandler.getInteger("Enter the index number of the date you choose: ", 1, 30);

            if (index >= 1 && index <= 30) {
                LocalDate selectedDate = dates[index - 1];
                System.out.println("You have selected: " + selectedDate);
                ConsoleUI.pause();
                return selectedDate;
            } else {
                System.out.println("Invalid index. Please select a number between 1 and 30.");
                error = true;
            }
        }while (error);


        return null;
    }

    private static String repeatChar(char c, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(c);
        }
        return sb.toString();
    }

    //display time and choose time
    private LocalTime chooseTime(){

        LocalTime startTime = LocalTime.of(10, 0); // 10:00 AM
        int columnWidth = 20; // Adjust column width to ensure proper alignment
        
        // Generate the time slots
        LocalTime[] timeSlots = new LocalTime[5];
        for (int i = 0; i < 5; i++) {
            timeSlots[i] = startTime.plusHours(i * 2);
        }
        
        // Display the header
        System.out.println("\n\nPlease choose a time slot by entering the corresponding index number:\n");
        System.out.println(repeatChar('-', (columnWidth + 3) * 5)); // Add separator line
        
        // Display the time slots in a table format
        for (int i = 0; i < 5; i++) {
            // Display index numbers with leading zeros (e.g., 001, 002)
            System.out.printf("| %" + columnWidth + "s ", String.format("%02d", i + 1));
        }
        System.out.println("|"); // End of row, print closing "|"
        
        for (int i = 0; i < 5; i++) {
            System.out.printf("| %" + columnWidth + "s ", timeSlots[i]);
        }
        System.out.println("|");
        System.out.println(repeatChar('-', (columnWidth + 3) * 5)); // Use custom method for separator
        
        int index;
        boolean error;
        do{
            // User input to select the time slot
            index = UserInputHandler.getInteger("Enter the index number of the time slot you choose: ", 1, 5);
            
            if (index >= 1 && index <= 5) {
                LocalTime selectedTimeSlot = timeSlots[index - 1];
                System.out.println("You have selected: " + selectedTimeSlot);
                ConsoleUI.pause();
                return selectedTimeSlot;
            } else {
                System.out.println("Invalid index. Please select a number between 1 and 5.");
                error = true;
            }
        }while (error);
            
        return null;
    }


    //design
    private static void distinctTableLine(){
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------");
    }

    private static void vehicleTableLine(){
        System.out.println
        ("-------------------------------------------------------");
    }   

}


