package project.modules.user;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

import project.global.ConsoleUI;
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
        ConsoleUI.clearScreen();
        this.setUserId(super.generateUserId("A"));

        this.setUserName(UserInputHandler.getString("Enter Admin Name", 3));

        this.setUserPassword(UserInputHandler.getString("Enter Admin Password", "^(?=.*[@#$%^&+=])(?=\\S+$).{5,}$"));

        
        System.out.print("Enter Admin Email: ");
        this.setUserEmail(scanner.nextLine());

        if(this.Add()){

            System.out.println("Admin with userID " + this.getUserId() +" has been created sucessfully ");


            String choice = UserInputHandler.getConfirmation("\nDo you want to create another Admin?");

            if (choice.equalsIgnoreCase("Y")) {
                createAdmin();

            }

            super.setCurrentUser(User.getLoggedInUserId());

        }else {
            super.setCurrentUser(User.getLoggedInUserId());
            System.out.println("Admin creation failed.");
        }
        
    }
    
   

    public void deleteAdmin() // can work , validated , X redirect
    {
        ConsoleUI.clearScreen();
        displayAllUsers();
        this.setUserId(UserInputHandler.getString("Enter Admin ID to delete", "^[ARV][0-9]{5}$"));
    
        if (!Get()) {
            System.out.println("Admin ID does not exist.");
            String choice = UserInputHandler.getConfirmation("Try again?");
            super.setCurrentUser(User.getLoggedInUserId());
            if (choice.equalsIgnoreCase("Y")) {
                deleteAdmin();
            } 
            return;  
        } 
        else if (this.getUserId().equals(User.getLoggedInUserId())) {
            super.setCurrentUser(User.getLoggedInUserId());
            System.out.println("Error: You cannot delete your own account while logged in.");
            return; 
        } 
    
        String choice = UserInputHandler.getConfirmation("\nAre you sure you want to delete this Admin");
    
        if (choice.equalsIgnoreCase("Y")) {
            if(this.Remove()){
                System.out.println("Admin account deleted successfully.");

                super.setCurrentUser(User.getLoggedInUserId());

                String choice2 = UserInputHandler.getConfirmation("\nDo you want to delete another Admin");

                if (choice2.equalsIgnoreCase("Y")) {
                    deleteAdmin();
                }

            } else {
                super.setCurrentUser(User.getLoggedInUserId());
                System.out.println("Admin account deletion failed.");
            }
       } else {
            super.setCurrentUser(User.getLoggedInUserId());
            System.out.println("Operation cancelled.");
            ConsoleUI.pause();
        }
    

    }

    public void UpdateAdmin() // can work
    {
        ConsoleUI.clearScreen();
        displayAllUsers();
        this.setUserId(UserInputHandler.getString("\nEnter Admin ID", "^[ARV][0-9]{5}$"));

        if (!Get()){

            System.out.println("Admin ID does not exist.");
            super.setCurrentUser(User.getLoggedInUserId());
            String choice = UserInputHandler.getConfirmation("Try again?");

            if (choice.equalsIgnoreCase("Y")) {
                UpdateAdmin();
            }

        }
        
        boolean continueEditing = true;

        while (continueEditing) {

            System.out.println("Which field would you like to update?");
            System.out.println("1. Admin Name");
            System.out.println("2. Admin Password");
            System.out.println("3. Admin Email");
            int choice = UserInputHandler.getInteger("\nEnter choice: ", 1, 3);
            
            String field;
            String value;
    
            switch (choice) {
                case 1:
                    field = "Admin_Name";
                    System.out.print("Enter new Admin Name: ");
                    value = scanner.nextLine();
                    break;
                case 2:
                    field = "Admin_Password";
                    value = UserInputHandler.getString("Enter new Admin Password", "^(?=.*[@#$%^&+=])(?=\\S+$).{5,}$");

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
            
            

            String anotherFieldChoice = UserInputHandler.getConfirmation("Would you like to update another field?");
            continueEditing = anotherFieldChoice.equalsIgnoreCase("Y");
        }
        super.setCurrentUser(User.getLoggedInUserId());
    }

   


    @Override
    public boolean Add()
    {
       String sql = "INSERT INTO Admin (Admin_Id, Admin_Name, Admin_Password, Admin_Email, Admin_Reg_Date) VALUES (?, ?, ?, ?, ?)";

        SqlConnector Connector = new SqlConnector(); 
        Connector.Connect();   
        
        if(!Connector.isConnected()) {
            System.out.println("Connection failed");
            return false;
        }

        return Connector.PrepareExecuteDML(sql, this.getUserId(), this.getUserName(), this.getUserPassword(), this.getUserEmail(), LocalDateTime.now());

    }

   

    @Override
    public  void UserMenu(){

        Retailer retailer = new Retailer();
        boolean exit = false;
        while(!exit){
            ConsoleUI.clearScreen();
            System.out.println("======= Admin & Retailer Management =======");
            System.out.println("1. Create Admin");
            System.out.println("2. View Admin");
            System.out.println("3. Update Admin");
            System.out.println("4. Delete Admin");
            System.out.println("5. View Retailer");
            System.out.println("6. Add Retailer");
            System.out.println("7. Delete Retailer");
            System.out.println("8. Notification Retailer");
            System.out.println("9. Exit");
            System.out.println("===========================================");
            int choice = UserInputHandler.getInteger("Enter choice: ", 1, 9);

            switch (choice) {
                case 1:
                    createAdmin();
                    break;
                case 2:
                    displayAllUsers();
                    break;
                case 3:
                    UpdateAdmin();
                    break;
                case 4: 
                    deleteAdmin();
                    break;
                case 5: 
                    retailer.displayAllUsers();
                    break;
                case 6: 
                    retailer.createRetailer();      
                    break;
                case 7:
                    retailer.deleteRetailer();
                    break;
                case 8:
                    Notification();
                    //implement send notification to retailer
                    break;
                case 9:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
    }

    public void Notification() {
        ArrayList<Request> pendingRequests = Request.viewRequest();

        if (pendingRequests != null && !pendingRequests.isEmpty()) {
            int requestId = UserInputHandler.getInteger("\nEnter Request ID to further proceed: ", 0, Integer.MAX_VALUE);

            Request selectedRequest = null;
            for (Request req : pendingRequests) {
                if (req.getRequest_ID() == requestId) {
                    selectedRequest = req;
                    break;
                }
            }

            // If the request is found, proceed with approval or rejection
            if (selectedRequest != null) {
                
                String choice = UserInputHandler.getConfirmation("Do you want to approve or reject the request");

                if (choice.equalsIgnoreCase("Y")) {
                    selectedRequest.approveRequest(this.getUserId()); // Passing admin ID to approveRequest
                } 
                else if (choice.equalsIgnoreCase("N")) {
                    selectedRequest.rejectRequest();
                } 
                else {
                    System.out.println("Invalid choice.");
                }

                String moreRequests = UserInputHandler.getConfirmation("Do you want to handle more requests");

                if (moreRequests.equalsIgnoreCase("Y")) {
                    Notification(); // Recursively call the method to handle more requests
                } 
            } else {
                System.out.println("Request ID not found.");
                ConsoleUI.pause();
            }

        } else {
            ConsoleUI.pause();
        }
        
    }


}
