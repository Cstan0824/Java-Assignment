package project.modules.user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Scanner;

import project.global.MailSender;
import project.global.ConsoleUI;
import project.global.MailTemplate;
import project.global.SqlConnector;
import project.global.UserInputHandler;

public class Retailer extends User {

    private String retailerAddress;

    private final static Scanner scanner = new Scanner(System.in);
    private final SqlConnector Connector = new SqlConnector();
    private final String url = Connector.getUrl();
    private final String user = Connector.getUser();
    private final String password = ""; 

    public Retailer(String userId, String userName, String userPassword, String userEmail) {
        super(userId, userName, userPassword, userEmail, "Retailer");
    }

    public Retailer(String userId, String userName, String userPassword, String userEmail, String retailerAddress, String retailerCreatedBy) {
        super(userId, userName, userPassword, userEmail, "Retailer");
        this.retailerAddress = retailerAddress;

    }

    public Retailer() {
        super(null, "Retailer");
    }

    public String getRetailerAddress() {
        return retailerAddress;
    }

    public void setRetailerAddress(String userAddress) {
        this.retailerAddress = userAddress;
    }

    @Override
    public boolean Add() {
        String sql = "INSERT INTO Retailer (Retailer_Id, Retailer_Name, Retailer_Password, Retailer_Email, Retailer_Reg_Date, Retailer_Address, Retailer_Created_By) VALUES (?, ?, ?, ?, ?, ?, ?)";

        
        Connector.Connect();

        if (!Connector.isConnected()) {
            System.out.println("Connection failed");
            return false;
        }

        return Connector.PrepareExecuteDML(sql, this.getUserId(), this.getUserName(), this.getUserPassword(),
                this.getUserEmail(), LocalDateTime.now(), this.getRetailerAddress(), User.getLoggedInUserId());
    }

    @Override
    public boolean Get() // can work
    {

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            if (conn == null || conn.isClosed()) {
                System.out.println("Failed to establish a connection.");
                return false;
            }

            String sql = "SELECT Retailer_Name, Retailer_Email, Retailer_Reg_Date, Retailer_Password, Retailer_Address FROM RETAILER WHERE Retailer_Id = ?";
            
            try (PreparedStatement statement = conn.prepareStatement(sql)) {

                statement.setString(1, this.getUserId()); 

                ResultSet result = statement.executeQuery(); 
                if (result.next()) {
                
                    this.setUserName(result.getString(1));
                    this.setUserEmail(result.getString(2));
                    this.setUserRegDate(result.getDate(3).toLocalDate());
                    this.setUserPassword(result.getString(4));
                    this.setRetailerAddress(result.getString(5));
                    return true;
                
                } 
                else {
                    return false;
                }

            } catch (SQLException e) {
                System.out.println("SQL Error: " + e.getMessage());
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Connection Error: " + e.getMessage());
            return false;
        }
    }

    // for the admin use  , to let them create retailer and then send the retailer an email for the retailer ID
    public void createRetailer() {

        ConsoleUI.clearScreen();

        this.setUserId(super.generateUserId("R"));

        this.setUserName(UserInputHandler.getString("Enter Retailer Name", 3));

        this.setUserPassword(UserInputHandler.getString("Enter Retailer Password", "^(?=.*[@#$%^&+=])(?=\\S+$).{5,}$"));

        System.out.print("Enter Retailer Email: ");
        this.setUserEmail(scanner.nextLine());

        System.out.print("Enter Retailer Address: ");
        this.setRetailerAddress(scanner.nextLine());

        if(Add()){
            System.out.println("Retailer added successfully.");

            MailSender mail = new MailSender(
                    this.getUserEmail(),
                    "Retailer Registration",
                    new MailTemplate(this.getUserId(), MailTemplate.TemplateType.RETAILER_CREATED));
                    
                if(mail.Send()){
                    System.out.println("Email sent to retailer.");
                    ConsoleUI.pause();
                }
                else{
                    System.out.println("Failed to send email to retailer.");
                    ConsoleUI.pause();
                }       


            String choice = UserInputHandler.getConfirmation("Any more Retailers to add?");

            if (choice.equalsIgnoreCase("Y")) {
                createRetailer();
            } 
        } else {
            System.out.println("Retailer not added.");
            ConsoleUI.pause();
        }
    }

    // just show the retailer details 
    public void viewRetailer() {
        ConsoleUI.clearScreen();
        System.out.println("This is your Retailer Details: ");
        displayUserDetails();
        
    }

    // for admin use also , to let them delete retailer
    public void deleteRetailer() { 
        ConsoleUI.clearScreen();
        displayAllUsers();
        this.setUserId(UserInputHandler.getString("Enter Retailer ID to delete", "^[ARV][0-9]{5}$"));

        if (!Get()) {
            System.out.println("Retailer ID does not exist.");
            ConsoleUI.pause();
            
        } else {
            System.out.println("\n\nRetailer Name: " + this.getUserName());
            System.out.println("Retailer Email: " + this.getUserEmail());
            System.out.println("Retailer Registration Date: " + this.getUserRegDate());
            System.out.println("Retailer Address: " + this.getRetailerAddress());

        }

        String choice = UserInputHandler.getConfirmation("\nAre you sure you want to delete this Retailer?");

        if (choice.equalsIgnoreCase("Y")) {

            if (this.Remove()){
                System.out.println("\nRetailer deleted successfully.");
                MailSender mail = new MailSender(
                    this.getUserEmail(),
                    "Account deletion notification",
                    new MailTemplate(this.getUserId(), MailTemplate.TemplateType.RETAILER_DELETED));
                    
                if(mail.Send()){
                    System.out.println("Email sent to retailer.");
                    ConsoleUI.pause();
                }
                else{
                    System.out.println("Failed to send email to retailer.");
                    ConsoleUI.pause();
                }       

                String choice1 = UserInputHandler.getConfirmation("\nDo you want to delete more Retailer?");
                if (choice1.equalsIgnoreCase("Y")) {
                    deleteRetailer();


                }
            }else{
                System.out.println("Retailer not deleted.");
            }
        } else {
            System.out.println("Retailer not deleted.");
            ConsoleUI.pause();
        }
    }

    // for admin and retailer use , to let them update their details
    public void UpdateRetailer() {
        ConsoleUI.clearScreen();
        super.displayUserDetails();

        boolean continueEditing = true;

        while (continueEditing) {
            System.out.println("Which field would you like to update?");
            System.out.println("1. Retailer Name");
            System.out.println("2. Retailer Password");
            System.out.println("3. Retailer Email");
            System.out.println("4. Retailer Address");
            int choice = UserInputHandler.getInteger("\nEnter choice (1-4): ", 1, 4);

            String field;
            String value;

            switch (choice) {
                case 1:
                    field = "Retailer_Name";
                    System.out.print("Enter new Retailer Name: ");
                    value = scanner.nextLine();          
                    break;
                case 2:
                    field = "Retailer_Password";
                    value = UserInputHandler.getString("Enter new Retailer Password", "^(?=.*[@#$%^&+=])(?=\\S+$).{5,}$");
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
                    continue; // Go back to the field selection menu
            }

            this.Update(field, value);
            Get();
            

            String anotherFieldChoice = UserInputHandler.getConfirmation("Would you like to update another field?");
            continueEditing = anotherFieldChoice.equalsIgnoreCase("Y");

            
        }

         // After all updates, redirect to menu
    }

    @Override
    public void UserMenu() {
        boolean exit = false;
        while(!exit){
            ConsoleUI.clearScreen();
            System.out.println("====== Profile Management ======");
            System.out.println("1. View Profile");
            System.out.println("2. Update Profile");
            System.out.println("3. Exit");
            System.out.println("================================");
            int choice = UserInputHandler.getInteger("Enter choice: ", 1, 3);
            switch (choice) {
                case 1:
                    viewRetailer();
                    break;
                case 2:
                    UpdateRetailer();
                    break;
                case 3:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
    }

    // just register the retailer and send the request to the admin
    public void Register() // can work
    {
        ConsoleUI.clearScreen();
        Request request = new Request();

        request.setRetailer_ID(super.generateUserId("R"));

        System.out.print("Enter Retailer Name: ");
        request.setRetailer_Name(scanner.nextLine());

        request.setRetailer_Password(UserInputHandler.getString("Enter Retailer Password", "^(?=.*[@#$%^&+=])(?=\\S+$).{5,}$"));
        
        System.out.print("Enter Retailer Email: ");
        request.setRetailer_Email(scanner.nextLine());

        System.out.print("Enter Retailer Address: ");
        request.setRetailer_Address(scanner.nextLine());

        if(request.saveRequest()){
            System.out.println("Request submitted successfully. Please wait for approval.");
            ConsoleUI.pause();
        } else {
            System.out.println("Request not submitted.");
            ConsoleUI.pause();
        }

    }
    

}
