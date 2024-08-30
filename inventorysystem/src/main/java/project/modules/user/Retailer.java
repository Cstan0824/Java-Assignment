package project.modules.user;

import java.time.LocalDateTime;
import java.util.Scanner;

import project.global.SqlConnector;

public class Retailer extends User {

    private String retailerCreatedBy;
    private String retailerAddress;

    

    public Retailer(String userId, String userName, String userPassword, String userEmail) {
        super(userId, userName, userPassword, userEmail, "Retailer");
    }

    public Retailer(String userId, String userName, String userPassword, String userEmail, String retailerAddress, String retailerCreatedBy) {
        super(userId, userName, userPassword, userEmail, "Retailer");
        this.retailerCreatedBy = retailerCreatedBy;
        this.retailerAddress = retailerAddress;
        
    }

    public Retailer() {
        super(null, "Retailer");
    }

  

    public String getRetailerCreatedBy() {
        return retailerCreatedBy;
    }   

    public void setRetailerCreatedBy(String retailerCreatedBy) {
        this.retailerCreatedBy = retailerCreatedBy;
    }

    public String getRetailerAddress() {
        return retailerAddress;
    }

    public void setRetailerAddress(String userAddress) {
        this.retailerAddress = userAddress;
    }



    @Override
    public void Add() {
        String sql = "INSERT INTO Retailer (Retailer_Id, Retailer_Name, Retailer_Password, Retailer_Email, Retailer_Reg_Date, Retailer_Address, Retailer_Created_By) VALUES (?, ?, ?, ?, ?, ?, ?)";

        SqlConnector Connector = new SqlConnector(); 
        Connector.Connect();   
        
        if(!Connector.isConnected()) {
            System.out.println("Connection failed");
            return;
        }

        Connector.PrepareExecuteDML(sql, this.getUserId(), this.getUserName(), this.getUserPassword(), this.getUserEmail(), LocalDateTime.now(), this.getRetailerAddress(),this.getRetailerCreatedBy() );
        System.out.println("Retailer added successfully.");
    }

    public void searchRetailer() {

        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("Enter Retailer ID: ");
            this.setUserId(sc.nextLine()); // Set the userId in the current object
        }

        if (this.Get()) {  

            this.displayUserDetails(); 

        } else {

            System.out.println("Admin ID does not exist.");  
        }
    }

    public void deleteRetailer() {  //can work
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("Enter Retailer ID: ");
            this.setUserId(sc.nextLine()); 
        }

        
        this.Remove();
    }

    public void UpdateRetailer() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter Retailer ID: ");
            this.setUserId(scanner.nextLine()); 
        

            System.out.println("Which field would you like to update?");
            System.out.println("1. Retailer Name");
            System.out.println("2. Retailer Password");
            System.out.println("3. Retailer Email");
            System.out.println("4. Retailer Address");
            System.out.print("Enter choice (1-3): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  

            String field ="";
            String value ="";

            switch(choice){
                case 1:
                    field = "Retailer_Name";   
                    System.out.print("Enter new Retailer Name: ");
                    value = scanner.nextLine();

                    break;
                case 2:
                    field = "Retailer_Password";
                    System.out.print("Enter new Retailer Password: ");
                    value = scanner.nextLine();
                    break;
                case 3:
                    field = "Retailer_Email";
                    System.out.print("Enter new Retailer Email: ");
                    value = scanner.nextLine();
                    break;
                case 4:
                    field = "Retailer_Address";
                    System.out.print("Enter new Retailer Address: ");
                    value = scanner.nextLine();
                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }

    
            this.Update(field, value);

            scanner.close();
        }
      

        

        
    




        
    }

    @Override
    public void UserMenu(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Retailer User Management System");
        System.out.println("1. View Retailer");
        System.out.println("2. Update Retailer");
        System.out.println("3. Exit");
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                searchRetailer();
                break;
            case 2:
                UpdateRetailer();
                break;
            default:
                System.out.println("Invalid choice.");
                break;
        }

        scanner.close();



    }}
    