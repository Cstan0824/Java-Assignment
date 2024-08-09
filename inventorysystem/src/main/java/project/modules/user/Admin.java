package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Admin extends User {

    public Admin(String userId, String userName, String userPassword, String userEmail, String userType) {
        super(userId, userName, userPassword, userEmail, userType );
    }

    public Admin() {
        // Default constructor
    }

    @Override 
    public void UserMenu(){
        Scanner scanner = new Scanner(System.in);
  
          System.out.println("1. Create User");
          System.out.println("2. View User");
          System.out.println("3. Update User");
          System.out.println("4. Delete User");
          System.out.println("5. Exit");
          System.out.print("Enter choice: ");
          int choice = scanner.nextInt();
  
          switch (choice) {
              case 1:
                  createUser();
                  break;
              case 2:
                  System.out.print("Enter User ID to view: ");
                  String userIdToView = scanner.nextLine();
                  if (!searchUser(userIdToView)) {
                      System.out.println("User not found.");
                  }
                  else {
                      displayUserDetails();
                  }
                  break;
  
              case 3:
                  editUser();
                  break;
              case 4: 
                  deleteUser();
                  break;
              default:
                  System.out.println("Invalid choice.");
                  break;
        }    
    }

    @Override 
    public  void login()
    {
        Scanner scanner =  new Scanner (System.in);

        System.out.println("Enter User ID: ");
        String userId = scanner.nextLine();

        System.out.println("Enter User Password: ");
        String userPassword = scanner.nextLine();

        try(Connection conn = DatabaseConnection.mySQLConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user WHERE user_id = ? AND user_password = ?")) { // change the table name 

            stmt.setString(1, userId);
            stmt.setString(2, userPassword);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("Login successful.");

                this.setUserId(rs.getString("user_id"));
                this.setUserName(rs.getString("user_name"));
                this.setUserPassword(rs.getString("user_password"));
                this.setUserEmail(rs.getString("user_email"));
                this.setUserType(rs.getString("user_type"));
                this.setUserRegDate(rs.getObject("user_reg_date", LocalDateTime.class));

                UserMenu(); 
            }
            else {
                System.out.println("Login failed.");
                // implement forgot password 
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }

    // @Override
    // public void  forgotPassword() {
    //     Scanner scanner = new Scanner(System.in);

    //     System.out.print("OTP Sending ... ... ... ");

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