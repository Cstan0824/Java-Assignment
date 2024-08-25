package project.modules.user;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

import project.global.SqlConnector;


public class Admin extends User  {

    @Override 
    public void UserMenu(){
        
    }
    public Admin(String userId, String userName, String userPassword, String userEmail) {
        super(userId, userName, userPassword, userEmail );
    }

    public Admin() {
        // Default constructor
    }

    @Override 
    public void login()
    {
        //User Input
        Scanner scanner =  new Scanner (System.in);

        System.out.println("Enter Admin ID: ");
        String adminId = scanner.nextLine();

        System.out.println("Enter Admin Password: ");
        String adminPassword = scanner.nextLine();

        scanner.close();    

        //Sql Connection
        SqlConnector Connector = new SqlConnector();
        Connector.Connect();

        if(!Connector.isConnected()){
            return;
        }

        String query = "SELECT * FROM admin WHERE Admin_ID = ? AND Admin_Password = ?";
        ArrayList<Admin> admins = Connector.PrepareExecuteRead(query, Admin.class , adminId, adminPassword); 

        if (admins.isEmpty() ) {
            System.out.println("Login failed.");
            // forgot password and sleep for 3 seconds
        }
        else{
            Admin admin = admins.get(0); //get : arraylist function ,  call out the first value 

              //initialise the value
              this.setUserId(admin.getUserId());
              this.setUserName(admin.getUserName());
              this.setUserPassword(admin.getUserPassword());
              this.setUserEmail(admin.getUserEmail());

              

            if(admin.getUserId().equals(adminId) && admin.getUserPassword().equals(adminPassword) ) // get the value from the database
            {
                System.out.println("Login successful.");

            // menu
            }


        }
        Connector.Disconnect();

    }



    @Override
    public void Add(){
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Admin ID: ");
        String adminId = scanner.nextLine();

        while (Get(adminId)) {
            System.out.println("Admin ID already exists.");
            System.out.println("Please enter a different Admin ID.");
            System.out.print("Enter Admin ID: ");
            adminId = scanner.nextLine();
        }
            
        System.out.print("Enter Admin Name: ");
        String adminName = scanner.nextLine();
            
        System.out.print("Enter Admin Password: ");
        String adminPassword = scanner.nextLine();
            
        System.out.print("Enter Admin Email: ");
        String adminEmail = scanner.nextLine();

        LocalDateTime adminRegDate = LocalDateTime.now();
        scanner.close();    

        SqlConnector Connector = new SqlConnector();
        Connector.Connect();

        if (!Connector.isConnected()) {
            return;
        }
 
        // OTP Code implementation  
        String query = "INSERT INTO admin (Admin_ID, Admin_Name, Admin_Password, Admin_Email, OTP_Code, Admin_Reg_Date) VALUES (?, ?, ?, ?, ?, ?)";
        Connector.PrepareExecuteDML(query , adminId, adminName, adminPassword, adminEmail, 1 ,adminRegDate);

       

        if(Get(adminId)){
            System.out.println("Admin added successfully.");
        }
        else{
            System.out.println("Admin not added.");
        }


        Connector.Disconnect();   
    }

    @Override
    public boolean Get(String adminID){
        SqlConnector Connector = new SqlConnector();    
        Connector.Connect();

        if (!Connector.isConnected()) {
            return false;
        }   

        String query = "SELECT * FROM admin WHERE Admin_ID = ?";    

        ArrayList<User> admins = Connector.PrepareExecuteRead(query, User.class, adminID);

        if (admins == null || admins.isEmpty()) {
            return false;
        }


        

        User admin = admins.get(0);

       

        this.setUserId(admin.getUserId());      
        this.setUserName(admin.getUserName());
        this.setUserPassword(admin.getUserPassword());
        this.setUserEmail(admin.getUserEmail());  

        Connector.Disconnect();

        return true;
    }

    @Override
    public void Update()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Admin ID to update: ");
        String adminId = scanner.nextLine();

        if (!Get(adminId)) {
            System.out.println("Admin ID does not exist.");
            return;
        }
        else{
            super.displayUserDetails();
        }
        
        System.out.println("Which field would you like to update?");
        System.out.println("1. Admin Name");
        System.out.println("2. Admin Password");
        System.out.println("3. Admin Email");
        System.out.print("Enter choice (1-3): ");
        int choice = scanner.nextInt();
        scanner.nextLine();  

        String field ="";
        String value ="";

        switch(choice){
            case 1:
                field = "Admin_Name";   
                System.out.print("Enter new Admin Name: ");
                value = scanner.nextLine();

                break;
            case 2:
                field = "Admin_Password";
                System.out.print("Enter new Admin Password: ");
                value = scanner.nextLine();
                break;
            case 3:
                field = "Admin_Email";
                System.out.print("Enter new Admin Email: ");
                value = scanner.nextLine();
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        scanner.close();

        String query = "UPDATE admin SET " + field + " = ? WHERE Admin_ID = ?";

        SqlConnector Connector = new SqlConnector();
        Connector.Connect();    

        if (!Connector.isConnected()) {
            return;
        }

        Boolean checking = Connector.PrepareExecuteDML(query, value, adminId);

        if (checking) {
            System.out.println("Admin updated successfully.");
        }
        else{
            System.out.println("Admin not updated.");
        }


    }

    @Override
    public void Remove()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Admin ID to remove: ");
        String adminId = scanner.nextLine();

        if (!Get(adminId)) {
            System.out.println("Admin ID does not exist.");
            return;
        }
        else{
            super.displayUserDetails();
        }

        System.out.print("Are you sure you want to remove this admin? (Y/N): ");
        String choice = scanner.nextLine();

        if (choice.equalsIgnoreCase("Y")) {
            String query = "DELETE FROM admin WHERE Admin_ID = ?";

            SqlConnector Connector = new SqlConnector();
            Connector.Connect();

            if (!Connector.isConnected()) {
                return;
            }

            Boolean checking = Connector.PrepareExecuteDML(query, adminId);

            if (checking) {
                System.out.println("Admin removed successfully.");
            }
            else{
                System.out.println("Admin not removed.");
            }
        }
        else{
            System.out.println("Admin not removed.");
        }
        scanner.close();
    }

    







}





