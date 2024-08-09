package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Scanner;

public abstract class User {
    //private data field can be only access by the class itself
    private String userId;
    private String userName;
    private String userPassword;
    private String userEmail;
    private String otpCode;
    private String userType;
    private LocalDateTime userRegDate;
    private LocalDateTime otpExpiry;

    public User(String userId, String userName, String userPassword, String userEmail, String userType ) {
        this.userId = userId;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
        this.userType = userType;
        this.userRegDate = LocalDateTime.now(); // Set registration date to current date and time
    }

    public User() {
        // Default constructor
    }

    

    //ID
    public  String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    //Name
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }



    //Password
    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    

    //Email 
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }


    //OTP
    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }


    public LocalDateTime getOtpExpiry() {
        return otpExpiry;
    }

    public void setOtpExpiry(LocalDateTime otpExpiry) {

        this.otpExpiry = otpExpiry;
    }



    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    


    public LocalDateTime getUserRegDate() {
        return userRegDate;
    }

    public void setUserRegDate(LocalDateTime userRegDate) {
        this.userRegDate = userRegDate;
    }

   
    public abstract void UserMenu();          // converted to abstract method
    public abstract void login();             // converted to abstract method
    // public abstract void forgotPassword(); // converted to abstract method
    // implement a forgot password method -->  inherit to admin and retailer class





    

    



    public void displayUserDetails() { // can be override
        System.out.println("User ID: " + this.userId);
        System.out.println("User Name: " + this.userName);
        System.out.println("User Password: " + this.userPassword);
        System.out.println("User Email: " + this.userEmail);
        System.out.println("User Type: " + this.userType);
    }


    public void createUser()  {  // CRUD use interface , separatw by 2 class , override the sql statement cuz theres two tables now 
        String sql = "INSERT INTO user (user_Id, user_Name, user_Password, user_Email, user_Type, user_Regis_Date) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.mySQLConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter User ID: ");
            this.userId = scanner.nextLine();
        
            System.out.print("Enter User Name: ");
            this.userName = scanner.nextLine();
        
            System.out.print("Enter User Password: ");
            this.userPassword = scanner.nextLine();
        
            System.out.print("Enter User Email: ");
            this.userEmail = scanner.nextLine();
        
            System.out.print("Enter User Type (Admin/Retailer): ");
            this.userType = scanner.nextLine();
        
            this.userRegDate = LocalDateTime.now();



            stmt.setString(1, this.getUserId());
            stmt.setString(2, this.getUserName());
            stmt.setString(3, this.getUserPassword());
            stmt.setString(4, this.getUserEmail());
            stmt.setString(5, this.getUserType());
            stmt.setObject(6, this.getUserRegDate());
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editUser() { // can be override
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter User ID to update: ");
        String userIdToUpdate = scanner.nextLine();

        System.out.println("Current User Details:");
        if (!searchUser(userIdToUpdate)) {
            System.out.println("User not found.");
            return;
        }
        else {
            displayUserDetails();
        }


        System.out.println("Which field would you like to update?");
        System.out.println("1. User Name");
        System.out.println("2. User Password");
        System.out.println("3. User Email");
        System.out.println("4. User Type");
        System.out.print("Enter choice (1-4): ");
        int choice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        String field = "";
        String newValue = "";
        switch (choice) {
            case 1:
                field = "user_Name";
                System.out.print("Enter new User Name: ");
                newValue = scanner.nextLine();
                break;
            case 2:
                field = "user_Password";
                System.out.print("Enter new User Password: ");
                newValue = scanner.nextLine();
                break;
            case 3:
                field = "user_Email";
                System.out.print("Enter new User Email: ");
                newValue = scanner.nextLine();
                break;
            case 4:
                field = "user_Type";
                System.out.print("Enter new User Type: ");
                newValue = scanner.nextLine();
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        String sql = "UPDATE user SET " + field + " = ? WHERE user_Id = ?";
        try (Connection conn = DatabaseConnection.mySQLConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newValue);
            stmt.setString(2, userIdToUpdate);
            stmt.executeUpdate();
            System.out.println("User updated successfully.");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean searchUser(String userId)  {
        String sql = "SELECT * FROM user WHERE user_Id = ?";
        try (Connection conn = DatabaseConnection.mySQLConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {

                    this.setUserId(rs.getString("user_Id"));
                    this.setUserName(rs.getString("user_Name"));
                    this.setUserPassword(rs.getString("user_Password"));
                    this.setUserEmail(rs.getString("user_Email"));
                    this.setUserType(rs.getString("user_Type"));

                    return true;

                } else {

                    return false;
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void deleteUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter User ID to delete: ");
        String userIdToDelete = scanner.nextLine();

        if (!searchUser(userIdToDelete)) {
            System.out.println("User not found.");
            return;
        }
        else {
            displayUserDetails();
        }

        System.out.print("Are you sure you want to delete this user? (Y/N): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("Y")) {
            String sql = "DELETE FROM user WHERE user_Id = ?";
            try (Connection conn = DatabaseConnection.mySQLConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, userIdToDelete);
                stmt.executeUpdate();
                System.out.println("User deleted successfully.");
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


  
}