package project.modules.user;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

import project.global.SqlConnector;
import project.global.UserInputHandler;



public class Admin extends User {
    private static Scanner scanner = new Scanner(System.in);

    public Admin(String userId, String userName, String userPassword, String userEmail, String usertype) {
        super(userId, userName, userPassword, userEmail, usertype);
        
    }

    public Admin() {
        super(null ,"Admin"); // Default constructor
    }

    


    public void createAdmin()  // can work , validated  ,  redirected
    {
        this.setUserId(super.generateUserId("A"));

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

        this.Add();

        System.out.println("Admin created sucessfully ");

        System.out.println("Do you want to create another Admin? (Y/N): ");
        String choice = scanner.next();

        if (choice.equalsIgnoreCase("Y")) {
            createAdmin();

        } else {
            redirectToMenu(scanner);
        }


       
    }
    
   

    public void deleteAdmin() // can work , validated , X redirect
    {
        do {

            System.out.println("Enter Admin ID to delete: ");
            //adminID = scanner.nextLine();
            this.setUserId(scanner.nextLine());
            //User selectedAdmin = new Admin(adminID);
            //selectedAdmin.Get();
            //selectedAdmin.Remove();

        } while (!Validation.validateUserId(this.getUserId()));
    
        if (!Get()) {
            System.out.println("Admin ID does not exist.");

            System.out.println("Try again? (Y/N): ");
            String choice = scanner.nextLine();

            if (choice.equalsIgnoreCase("Y")) {
                deleteAdmin();
            } else {
                redirectToMenu(scanner);
            }

            return;  
        } 
        else if (this.getUserId().equals(User.getLoggedInUserId())) {
            System.out.println("Error: You cannot delete your own account while logged in.");
            redirectToMenu(scanner);
            return; 
        } 
    
        displayUserDetails();
    
        System.out.println("Are you sure you want to delete this Admin (Y/N): ");
        String choice = scanner.nextLine();
    
        if (choice.equalsIgnoreCase("Y")) {
            this.Remove();
            System.out.println("Admin account deleted successfully.");

            System.out.println("Do you want to delete another Admin? (Y/N): ");
            String choice2 = scanner.nextLine();

            if (choice2.equalsIgnoreCase("Y")) {
                deleteAdmin();
            } else {
                redirectToMenu(scanner);
            }
       } else {
            System.out.println("Operation cancelled.");
            redirectToMenu(scanner);

        }
    

    }

    public void UpdateAdmin() // can work
    {

        do{
                System.out.println("Enter Admin ID: ");
                this.setUserId(scanner.nextLine());
                
        }while (!Validation.validateUserId(this.getUserId()));

        if (!Get()){

            System.out.println("Admin ID does not exist.");

            System.out.println("Try again? (Y/N): ");
            String choice = scanner.nextLine();

            if (choice.equalsIgnoreCase("Y")) {
                UpdateAdmin();
            } else {
                redirectToMenu(scanner);
            }

        }
        

        boolean continueEditing = true;
        
        displayUserDetails();


        while (continueEditing) {


            System.out.println("Which field would you like to update?");
            System.out.println("1. Admin Name");
            System.out.println("2. Admin Password");
            System.out.println("3. Admin Email");
            int choice = UserInputHandler.getInteger("Enter choice: ", 1, 3);
            
    
            String field;
            String value;
    
            switch (choice) {
                case 1:
                    field = "Admin_Name";
                    System.out.print("Enter new Admin Name: ");
                    value = scanner.nextLine();
    
                    while (!Validation.validateUserName(value)) {
                        System.out.println("Enter new Admin Name: ");
                        value = scanner.nextLine();
                    }
                    break;
                case 2:
                    field = "Admin_Password";
                    System.out.print("Enter new Admin Password: ");
                    value = scanner.nextLine();
    
                    while (!Validation.validateUserPassword(value)) {
                        System.out.println("Enter new Admin Password: ");
                        value = scanner.nextLine();
                    }
                    break;
                case 3:
                    field = "Admin_Email";
                    System.out.print("Enter new Admin Email: ");
                    value = scanner.nextLine();
    
                    break;
                default:
                    System.out.println("Invalid choice.");
                    continue;
            }
    
            super.Update(field, value);
    
            System.out.println("Would you like to update another field? (Y/N): ");
            String anotherFieldChoice = scanner.nextLine();

            continueEditing = anotherFieldChoice.equalsIgnoreCase("Y");
        }

        redirectToMenu(scanner);
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

   

    @Override
    public  void UserMenu(){

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
          int choice = UserInputHandler.getInteger("Enter choice: ", 1, 8);

  
          switch (choice) {
              case 1:
                createAdmin();
                break;
              case 2:
                displayAllUsers();
                redirectToMenu(scanner);
                break;
              case 3:
                UpdateAdmin();
                break;
              case 4: 
                deleteAdmin();
                  break;
              case 5: 
                retailer.createRetailer();      
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
        ArrayList<Request> pendingRequests = Request.viewRequest();

        if (pendingRequests != null && !pendingRequests.isEmpty()) {
            System.out.println("Enter Request ID to further proceed: ");
            int requestId = UserInputHandler.getInteger("Enter Request ID", 1, Integer.MAX_VALUE);

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
                    redirectToMenu(scanner);
                }
                else {
                    System.out.println("Invalid choice.");
                    redirectToMenu(scanner);
                }

            } else {
                System.out.println("Request ID not found.");
                redirectToMenu(scanner);
            }

        } else {
            System.out.println("No pending requests found.");
            redirectToMenu(scanner);
        }


        
    }


    private void redirectToMenu(Scanner scanner) {
        System.out.println("Do you want to return to the main menu? (Y/N): ");
        String choice = scanner.next();
        if (choice.equalsIgnoreCase("Y")) {
            UserMenu();
        } else {
            System.out.println("Exiting...");
            System.exit(0);
        }
    }











}
