package project.modules.user;

import java.time.LocalDateTime;
import java.util.Scanner;

import project.global.SqlConnector;

public class Retailer extends User {

    private String retailerAddress;

    private final static Scanner scanner = new Scanner(System.in);

    public Retailer(String userId, String userName, String userPassword, String userEmail) {
        super(userId, userName, userPassword, userEmail, "Retailer");
    }

    public Retailer(String userId, String userName, String userPassword, String userEmail, String retailerAddress,
            String retailerCreatedBy) {
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
    public void Add() {
        String sql = "INSERT INTO Retailer (Retailer_Id, Retailer_Name, Retailer_Password, Retailer_Email, Retailer_Reg_Date, Retailer_Address, Retailer_Created_By) VALUES (?, ?, ?, ?, ?, ?, ?)";

        SqlConnector Connector = new SqlConnector();
        Connector.Connect();

        if (!Connector.isConnected()) {
            System.out.println("Connection failed");
            return;
        }

        Connector.PrepareExecuteDML(sql, this.getUserId(), this.getUserName(), this.getUserPassword(),
                this.getUserEmail(), LocalDateTime.now(), this.getRetailerAddress(), User.getLoggedInUserId());
        System.out.println("Retailer added successfully.");
    }

    public void createRetailer() {
        Admin admin = new Admin();

        this.setUserId(super.generateUserId("R"));

        System.out.println("Enter Retailer Name: ");
        this.setUserName(scanner.nextLine());

        while (!Validation.validateUserName(this.getUserName())) {
            System.out.println("Enter Retailer Name: ");
            this.setUserName(scanner.nextLine());
        }

        System.out.println("Enter Retailer Password: ");
        this.setUserPassword(scanner.nextLine());

        while (!Validation.validateUserPassword(this.getUserPassword())) {
            System.out.println("Enter Retailer Password: ");
            this.setUserPassword(scanner.nextLine());
        }

        System.out.println("Enter Retailer Email: ");
        this.setUserEmail(scanner.nextLine());

        System.out.println("Enter Retailer Address: ");
        this.setRetailerAddress(scanner.nextLine());

        Add();

        System.out.println("Any more Retailers to add? (Y/N): ");
        String choice = scanner.next();

        if (choice.equalsIgnoreCase("Y")) {
            createRetailer();
        } else {
            System.out.println("Returning to main menu...");
            try {
                Thread.sleep(1000);
                admin.UserMenu();
            } catch (InterruptedException e) {
                System.out.println("Returning to main menu...");

            }
        }

    }

    public void viewRetailer() {
        System.out.println("This is your Retailer Details: ");
        displayUserDetails();
        redirectToMenu(new Scanner(System.in));
        
    }

    public void deleteRetailer() { //can work

        Admin admin = new Admin();

        System.out.println("Enter Retailer ID: ");
        this.setUserId(scanner.nextLine());

        if (!Get()) {
            System.out.println("Retailer ID does not exist.");
            redirectToMenu(scanner);
        } else {
            displayUserDetails();
        }

        System.out.println("Are you sure you want to delete this Retailer? (Y/N): ");
        String choice = scanner.next();

        if (choice.equalsIgnoreCase("Y")) {

            this.Remove();

            System.out.println("Retailer deleted successfully.");

            try {
                Thread.sleep(1000);
                admin.UserMenu();
            } catch (InterruptedException e) {
                System.out.println("Returning to main menu...");
            }
        

        } else {
            System.out.println("Retailer not deleted.");
            System.out.println("Returning to main menu...");

            try {
                Thread.sleep(1000);
                admin.UserMenu();
            } catch (InterruptedException e) {
                System.out.println("Returning to main menu...");
            }

        }
    }

    
    public void UpdateRetailer() {

            super.displayUserDetails();


            boolean continueEditing = true;

            while (continueEditing) {
                System.out.println("Which field would you like to update?");
                System.out.println("1. Retailer Name");
                System.out.println("2. Retailer Password");
                System.out.println("3. Retailer Email");
                System.out.println("4. Retailer Address");
                System.out.print("Enter choice (1-4): ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                String field;
                String value;

                switch (choice) {
                    case 1:
                        field = "Retailer_Name";
                        System.out.print("Enter new Retailer Name: ");
                        value = scanner.nextLine();

                        while (!Validation.validateUserName(value)) {
                            System.out.print("Enter new Retailer Name: ");
                            value = scanner.nextLine();
                        }
                        break;
                    case 2:
                        field = "Retailer_Password";
                        System.out.print("Enter new Retailer Password: ");
                        value = scanner.nextLine();

                        while (!Validation.validateUserPassword(value)) {
                            System.out.print("Enter new Retailer Password: ");
                            value = scanner.nextLine();
                        }
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

                System.out.println("Would you like to update another field? (Y/N): ");
                String anotherFieldChoice = scanner.nextLine();
                continueEditing = anotherFieldChoice.equalsIgnoreCase("Y");
            }

            redirectToMenu(scanner); // After all updates, redirect to menu
    }

    @Override
    public void UserMenu() {

        System.out.println("Retailer User Management System");
        System.out.println("1. View Retailer");
        System.out.println("2. Update Retailer");
        System.out.println("3. Exit");
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                viewRetailer();
                break;
            case 2:
                UpdateRetailer();
                break;
            default:
                System.out.println("Invalid choice.");
                UserMenu();
                break;
        }
    }

    public void Register() // can work
    {

        Request request = new Request();

        do {
            System.out.println("Enter Retailer ID: ");
            request.setRetailer_ID(scanner.nextLine());
            this.setUserId(request.getRetailer_ID());

            if (Get()) {
                System.out.println("Retailer ID already exists. Please enter a different ID.");
            } else {
                break;
            }
        } while (true);

        while (Validation.validateUserId(request.getRetailer_ID()) == false) {
            System.out.println("Enter Retailer ID: ");
            request.setRetailer_ID(scanner.nextLine());
        }

        do {
            System.out.println("Enter Retailer Name: ");
            request.setRetailer_Name(scanner.nextLine());
        } while (!Validation.validateUserName(request.getRetailer_Name()));

        do {
            System.out.println("Enter Retailer Password: ");
            request.setRetailer_Password(scanner.nextLine());
        } while (!Validation.validateUserPassword(request.getRetailer_Password()));

        System.out.println("Enter Retailer Email: ");
        request.setRetailer_Email(scanner.nextLine());

        System.out.println("Enter Retailer Address: ");
        request.setRetailer_Address(scanner.nextLine());

        request.saveRequest();

        System.out.println("Request submitted successfully. Please wait for approval.");

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
