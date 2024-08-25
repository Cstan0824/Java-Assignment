package project.modules.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Retailer extends User {
    
        private String address;
        private String createdBy;

        public Retailer(String userId, String userName, String userPassword, String userEmail) {
            super(userId, userName, userPassword, userEmail);
            this.address = address; 
            this.createdBy = createdBy;

        }
    
        public Retailer() {
            // Default constructor
        }

        public String getAddress() { 
            return address; 
        }
        public void setAddress(String address) { 
            this.address = address; 
        }
    
        public String getCreatedBy() { 
            return createdBy; 
        }

        public void setCreatedBy(String createdBy) { 
            this.createdBy = createdBy; 
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
                    System.out.println("Enter Retailer Id : ");
                    String retailerID = scanner.nextLine();
                    scanner.nextLine();

                    this.Get(retailerID);
                    break;

                case 2:
                        Update();
                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }



        }

        @Override
        public void login()
        {
        Scanner scanner =  new Scanner (System.in);

        System.out.println("Enter Retailer ID: ");
        String userId = scanner.nextLine();

        System.out.println("Enter Retailer Password: ");
        String userPassword = scanner.nextLine();

        try(Connection conn = DatabaseConnection.mySQLConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM retailer WHERE retailer_id = ? AND retailer_password = ?")) { 

            stmt.setString(1, userId);
            stmt.setString(2, userPassword);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("Login successful.");

                this.setUserId(rs.getString("retailer_id"));
                this.setUserName(rs.getString("retailer_name"));
                this.setUserPassword(rs.getString("retailer_password"));
                this.setUserEmail(rs.getString("retailer_email"));
                this.setUserRegDate(rs.getObject("retailer_regis_date", LocalDateTime.class));
                

                Menu.Submenu(); 
            }
            else {
                System.out.println("Login failed.");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }  
    }

    @Override
    public void Add(String createdBy)  {                                                                                                     //remember  to change the otp
        String sql = "INSERT INTO retailer (retailer_Id, retailer_Name, retailer_Password, retailer_Email, retailer_Regis_Date , retailer_Address , OTP_Code , Retailer_created_by) VALUES (?, ?, ?, ?, ?, ? ,0 ,? )";  
        try (Connection conn = DatabaseConnection.mySQLConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
        
                Scanner scanner = new Scanner(System.in);
        
                System.out.print("Enter Retailer  ID: ");
                String retailerId = scanner.nextLine();

                while(searchUser(retailerId))
                {
                    System.out.println("Retailer existed !!!");
                    System.out.println("Enter Retailer ID : ");
                    retailerId = scanner.nextLine();
                }
            
                System.out.print("Enter Retailer Name: ");
                String retailerName = scanner.nextLine();
            
                System.out.print("Enter Retailer Password: ");
                String retailerPassword = scanner.nextLine();
            
                System.out.print("Enter Retailer Email: ");
                String retailerEmail = scanner.nextLine();

                LocalDateTime retailerRegDate = LocalDateTime.now();

                System.out.println("Enter Retailer Address : ");
                String retailerAddress = scanner.nextLine();
                
                stmt.setString(1, retailerId);
                stmt.setString(2, retailerName);
                stmt.setString(3, retailerPassword);
                stmt.setString(4, retailerEmail);
                stmt.setObject(5, retailerRegDate);
                stmt.setString(6, retailerAddress);
                stmt.setString(7, createdBy);
                stmt.executeUpdate();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }

    
        @Override
        public void Update() { // can be override
            Scanner scanner = new Scanner(System.in);
    
            System.out.print("Enter Retailer ID to update: ");
            String RetailerIdToUpdate = scanner.nextLine();
            
            System.out.println("Current Retailer Details:");
            if (!searchUser(RetailerIdToUpdate)) {
                System.out.println("User not found.");
                return;
            }
            else {
                displayUserDetails();
            }
    
    
            System.out.println("Which field would you like to update?");
            System.out.println("1. Retailer Name");
            System.out.println("2. Retailer Password");
            System.out.println("3. Retailer Email");
            System.out.print("Enter choice (1-3): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  
    
            String field = "";
            String newValue = "";
            switch (choice) {
                case 1:
                    field = "Retailer_Name";
                    System.out.print("Enter new Retailer Name: ");
                    newValue = scanner.nextLine();
                    break;
                case 2:
                    field = "Retailer_Password";
                    System.out.print("Enter new Retailer Password: ");
                    newValue = scanner.nextLine();
                    break;
                case 3:
                    field = "Retailer_Email";
                    System.out.print("Enter new Retailer Email: ");
                    newValue = scanner.nextLine();
                    break;
                default:
                    System.out.println("Invalid choice.");
                    return;
            }
    
            String sql = "UPDATE Retailer SET " + field + " = ? WHERE Retailer_Id = ? ;";
            try (Connection conn = DatabaseConnection.mySQLConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
    
                stmt.setString(1, newValue);
                stmt.setString(2, RetailerIdToUpdate);
                stmt.executeUpdate();
                System.out.println("Retailer updated successfully.");
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
    }

    @Override
    public boolean Get(String retailerId)  {       

        String sql = "SELECT * FROM retailer WHERE retailer_Id = ?";

        try (Connection conn = DatabaseConnection.mySQLConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, retailerId);

            ResultSet rs = stmt.executeQuery();

                if (rs.next()) {



                    this.setUserId(rs.getString("retailer_Id"));
                    this.setUserName(rs.getString("retailer_Name"));
                    this.setUserPassword(rs.getString("retailer_Password"));
                    this.setUserEmail(rs.getString("retailer_Email"));
                    this.setUserRegDate(rs.getObject("retailer_Regis_Date", LocalDateTime.class));
                    this.setAddress(rs.getString("retailer_Address"));

                    displayUserDetails();

                    
                    System.out.println("User found ");

                    return true;

                } else {
                    System.out.println("user not found ");

                    return false;
                }
            
            }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void Remove() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Retailer ID to delete: ");
        String RetailerIdToDelete = scanner.nextLine();

        if (!Get(RetailerIdToDelete)) {
            System.out.println("Retailer not found.");
            return;
        }
        else {
            displayUserDetails();
        }

        System.out.print("Are you sure you want to delete this user? (Y/N): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("Y")) {
            String sql = "DELETE FROM Retailer WHERE Retailer_Id = ?";
            try (Connection conn = DatabaseConnection.mySQLConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, RetailerIdToDelete);
                stmt.executeUpdate();
                System.out.println("User deleted successfully.");
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }









    // @Override
    // public void  forgotPassword() {
    //     Scanner scanner = new Scanner(System.in);
        
    //     System.out.print("Enter OTP: ");
    //     String otp = scanner.nextLine();

    //     if (otp.equals(this.getOtpCode()) && LocalDateTime.now().isBefore(this.getOtpExpiry())) {
    //         System.out.print("Enter new password: ");
    //         String newPassword = scanner.nextLine();

    //         String sql = "UPDATE user SET user_Password = ? WHERE user_Id = ?"; // change the table name
    //         try (Connection conn = DatabaseConnection.mySQLConnection();
    //              PreparedStatement stmt = conn.prepareStatement(sql)) {

    //             stmt.setString(1, newPassword);
    //             stmt.setString(2, getUserId());
    //             stmt.executeUpdate();
    //             System.out.println("Password updated successfully.");
    //         }
    //         catch (SQLException e) {
    //             e.printStackTrace();
    //         }
    //     }
    //     else {
    //         System.out.println("Invalid OTP or OTP expired.");
    //     }
    // }













    
        //     @Override
        //     public void inputUserDetails() {
        //         super.inputUserDetails(); 
        //         // still can add your own input here
        
        //     }
    
}
