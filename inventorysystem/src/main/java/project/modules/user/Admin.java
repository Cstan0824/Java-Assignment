package com.example;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;


public class Admin extends User {

    public Admin(String userId, String userName, String userPassword, String userEmail, String usertype) {
        super(userId, userName, userPassword, userEmail, usertype);
    }

    public Admin() {
        super(null ,"Admin"); // Default constructor
    }

    public void createAdmin()  // can work
    {
        Scanner scanner = new Scanner(System.in);
        boolean idExists ;   

        do {
            System.out.println("Enter Admin ID: ");
            this.setUserId(scanner.nextLine()); 
            idExists = this.Get();    
    
            if (idExists) {
                System.out.println("Admin ID already exists. Please enter a different ID.");
            }
        } while (idExists); 

        System.out.println("Enter Admin Name: ");
        this.setUserName(scanner.nextLine());

        while(!Validation.validateUserName(this.getUserName())) {
            System.out.println("Format not allowed");
            System.out.println("Enter Admin Name: ");
            this.setUserName(scanner.nextLine());
        }

        System.out.println("Enter Admin Password: ");
        this.setUserPassword(scanner.nextLine());

        while(!Validation.validateUserPassword(this.getUserPassword())) {
            System.out.println("Format not allowed");
            System.out.println("Enter Admin Password: ");
            this.setUserPassword(scanner.nextLine());
        }

        System.out.println("Enter Admin Email: ");
        this.setUserEmail(scanner.nextLine());

        while(!Validation.validateUserEmail(this.getUserEmail())) {
            System.out.println("Format not allowed");
            System.out.println("Enter Admin Email: ");
            this.setUserEmail(scanner.nextLine());
        }

        scanner.close();


        this.Add();

       
    }
    
    public void searchAdmin() //can work , validated 
    {
        try (Scanner sc = new Scanner(System.in)) {

            do{

                System.out.println("Enter Admin ID: ");
                this.setUserId(sc.nextLine());

            }while(!Validation.validateUserId(this.getUserId()));

        }


        if (this.Get()) {  

            this.displayUserDetails(); 

        } else {

            System.out.println("Admin ID does not exist.");  
        }
    }

    public void deleteAdmin() // can work
    {
        try (Scanner sc = new Scanner(System.in)) {
            do{

                System.out.println("Enter Admin ID: ");
                this.setUserId(sc.nextLine());

            }while(!Validation.validateUserId(this.getUserId()));
        }

        this.Remove();
    }

    public void UpdateAdmin() // can work
    {
        Scanner scanner = new Scanner(System.in);

        do{
                System.out.println("Enter Admin ID: ");
                this.setUserId(scanner.nextLine());
                
        }while (!Validation.validateUserId(this.getUserId()));

        if (!Get()){

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
                break;
        }


        super.Update(field, value);

        scanner.close();

      

        

        
    }

    public void createRetailer() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter Retailer ID: ");
        String retailerId = sc.nextLine();

        System.out.println("Enter Retailer Name: ");
        String retailerName = sc.nextLine();

        System.out.println("Enter Retailer Password: ");
        String retailerPassword = sc.nextLine();

        System.out.println("Enter Retailer Email: ");
        String retailerEmail = sc.nextLine();

        System.out.println("Enter Retailer Address: ");
        String retailerAddress = sc.nextLine();

        sc.close();

        // Call the AddRetailer method to add the retailer to the database

        Retailer retailer = new Retailer(retailerId, retailerName, retailerPassword, retailerEmail, retailerAddress, this.getUserId());
        System.out.println("Retailer Created By (Admin ID): " + retailer.getRetailerCreatedBy());

        retailer.Add();

    }
    @Override
    public void Add()
    {
       String sql = "INSERT INTO Admin (Admin_Id, Admin_Name, Admin_Password, Admin_Email, Admin_Reg_Date) VALUES (?, ?, ?, ?, ?)";

        SqlConnector Connector = new SqlConnector(); 
        Connector.Connect();   
        
        if(!Connector.isConnected()) {
            System.out.println("Connection failed");
            return;
        }

        Connector.PrepareExecuteDML(sql, this.getUserId(), this.getUserName(), this.getUserPassword(), this.getUserEmail(), LocalDateTime.now());

    }

    public void AddRetailer(String retailerId, String retailerName, String retailerPassword, String retailerEmail, String retailerAddress) {
        Retailer retailer = new Retailer(retailerId, retailerName, retailerPassword, retailerEmail, retailerAddress, this.getUserId());
        retailer.Add();
    }

    @Override
    public void UserMenu(){
        Scanner scanner = new Scanner(System.in);

        User user =  new Retailer();
        Retailer retailer  =  (Retailer) user ;
  
          System.out.println("1. Create Admin");
          System.out.println("2. View Admin");
          System.out.println("3. Update Admin");
          System.out.println("4. Delete Admin");
          System.out.println("5. Add Retailer");
          System.out.println("6. Delete Retailer");
          System.out.println("7. Notification Retailer");
          System.out.println("8. Exit");
          System.out.print("Enter choice: ");
          int choice = scanner.nextInt();
          scanner.nextLine();

  
          switch (choice) {
              case 1:
                createAdmin();
                break;
              case 2:
                searchAdmin();
                break;
              case 3:
                UpdateAdmin();
                break;
              case 4: 
                deleteAdmin();
                  break;
              case 5: 
                createRetailer();      
                  break;
              case 6:
                retailer.deleteRetailer();
                break;
            case 7:
                Notification();
                break;
              default:
                System.out.println("Invalid choice.");
                break;
        }
    }

    public void Notification() {
        ArrayList<Request> pendingRequests = Request.viewRequest(); // Assuming viewRequest is static in Request

        // Step 2: If there are pending requests, proceed with approval/rejection
        if (pendingRequests != null && !pendingRequests.isEmpty()) {
            Scanner scanner = new Scanner(System.in);

            // Admin selects the request to act on
            System.out.println("Enter Request ID to further proceed: ");
            int requestId = scanner.nextInt();

            // Find the request by ID
            Request selectedRequest = null;
            for (Request req : pendingRequests) {
                if (req.getRequest_ID() == requestId) {
                    selectedRequest = req;
                    break;
                }
            }

            // If the request is found, proceed with approval or rejection
            if (selectedRequest != null) {
                System.out.println("Do you want to approve or reject the request (A/R): ");
                String choice = scanner.next();

                if (choice.equalsIgnoreCase("A")) {
                    selectedRequest.approveRequest(this.getUserId()); // Passing admin ID to approveRequest



                } 
                else if (choice.equalsIgnoreCase("R")) {
                    selectedRequest.rejectRequest();


                } 
                else {
                    System.out.println("Invalid choice.");
                }

                System.out.println("Do you want to handle more requests (Y/N): ");
                String moreRequests = scanner.next();

                if (moreRequests.equalsIgnoreCase("Y")) {
                    Notification(); // Recursively call the method to handle more requests
                }
                else if (moreRequests.equalsIgnoreCase("N")) {
                    // return back to menu
                }
                else {
                    System.out.println("Invalid choice.");
                    // return back to menu
                }







            } else {
                System.out.println("Request ID not found.");
                // return back to menu 
            }

            scanner.close();
        } else {
            System.out.println("No pending requests found.");
            // return back to menu
        }


        
    }











}
