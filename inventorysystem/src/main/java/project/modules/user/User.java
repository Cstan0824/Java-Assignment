package project.modules.user;

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
    private LocalDateTime userRegDate;
    private LocalDateTime otpExpiry;

    public User(String userId, String userName, String userPassword, String userEmail ) {
        this.userId = userId;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
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

    public LocalDateTime getUserRegDate() {
        return userRegDate;
    }

    public void setUserRegDate(LocalDateTime userRegDate) {
        this.userRegDate = userRegDate;
    }

   
    public abstract void UserMenu();          
    public abstract void login();     
    public abstract void Add();
    public abstract void Update();
    public abstract void Remove();
    public abstract boolean Get(String adminID);
   

    


    public void displayUserDetails() { 
        System.out.println("User ID: " + this.userId);
        System.out.println("User Name: " + this.userName);
        System.out.println("User Password: " + this.userPassword);
        System.out.println("User Email: " + this.userEmail);
    }


    

  
}