package project.modules.user;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Scanner;

import project.global.MailSender;
import project.global.MailTemplate;
import project.global.SqlConnector;

public abstract class User {
    //private data field can be only access by the class itself
    private final SqlConnector Connector = new SqlConnector();
    private static Scanner scanner = new Scanner(System.in);
    private String userId;
    private String userName;
    private String userPassword;
    private String userEmail;
    private String otpCode;
    private LocalDateTime userRegDate;
    private LocalDateTime otpExpiry;
    private String userType;
    private static String loggedInUserId;
    private String userAddress;
    private final String url = Connector.getUrl();
    private final String user = Connector.getUser();
    private final String password = ""; 

    public User(String userId, String userName, String userPassword, String userEmail, String usertype) {
        this.userId = userId;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
        this.userRegDate = LocalDateTime.now(); 
        this.userType = usertype;
    }

    public User() {
        // Default constructor
    }

    public User(String userId , String usertype) {
        this.userId = userId;
        this.userType = usertype;
    }

    public  String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

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

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public static void setLoggedInUserId(String userId) {
        loggedInUserId = userId;
    }

    public static String getLoggedInUserId() {
        return loggedInUserId;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public abstract void Add(); 
    public abstract void UserMenu(); 
   
    public boolean Get() // can work
    {

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            if (conn == null || conn.isClosed()) {
                System.out.println("Failed to establish a connection.");
                return false;
            }

            String sql = "SELECT " + this.userType + "_Name, " + this.userType + "_Email, " + this.userType + "_Reg_Date, " + this.userType + "_Password " +
            "FROM " + this.userType + " WHERE " + this.getUserType() + "_Id = ?";


            
            
            try (PreparedStatement statement = conn.prepareStatement(sql)) {

                statement.setString(1, this.getUserId()); 

                ResultSet result = statement.executeQuery(); 
                if (result.next()) {
                
                    this.setUserName(result.getString(1));
                    this.setUserEmail(result.getString(2));
                    this.setUserRegDate(result.getTimestamp(3).toLocalDateTime());
                    this.setUserPassword(result.getString(4));


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

    public void Update(String field , String value) // can work
    {
        String sql = "UPDATE " + this.userType + " SET " + field + " = ? WHERE " + this.getUserType() + "_Id = ?";

        this.Connector.Connect();   
        
        if(!this.Connector.isConnected())
        {
            System.out.println("Connection failed");
            return;
        }

        Boolean checking = this.Connector.PrepareExecuteDML(sql, value, this.getUserId());

        this.Connector.Disconnect();


        if(checking)
        {
            System.out.println("Update successful");
        }
        else
        {
            System.out.println("Update failed");
        }
    }
    
    public void Remove() // can work
    {
        String sql = "DELETE FROM " + this.userType + " WHERE " + this.getUserType() + "_Id = ?";

        this.Connector.Connect();   
        
        if(!this.Connector.isConnected())
        {
            System.out.println("Connection failed");
            return;
        }

        Boolean checking = this.Connector.PrepareExecuteDML(sql, this.getUserId());

        this.Connector.Disconnect();

        if(checking)
        {
            System.out.println("Delete successful");
        }
        else
        {
            System.out.println("Delete failed");
        }
    }

    public boolean handleLogin() // can work
    {
        final int MAX_ATTEMPTS = 3;
        int attempts = 0;

        while (attempts < MAX_ATTEMPTS) {

            //input user id and password
            System.out.println("Enter " + this.getUserType() + " ID: ");
            this.setUserId(scanner.nextLine());

            while (!Validation.validateUserId(this.getUserId())) {
                System.out.println("Enter " + this.getUserType() + " ID: ");
                this.setUserId(scanner.nextLine());
            }

            System.out.println("Enter " + this.getUserType() + " Password: ");
            this.setUserPassword(scanner.nextLine());

            //connect to database and check existence of user
            try (Connection conn = DriverManager.getConnection(url, user, password)) {
                if (conn == null || conn.isClosed()) {
                    System.out.println("Failed to establish a connection.");
                    return false;
                }

                // for sucessful login 
                String sql = "SELECT * FROM " + this.userType + " WHERE " + this.getUserType() + "_ID = ? AND "
                        + this.getUserType() + "_Password = ?";
                // for fetching email and reg date
                String sql1 = "SELECT * FROM " + this.userType + " WHERE " + this.getUserType() + "_ID = ?";

                try (PreparedStatement statement = conn.prepareStatement(sql)) {
                    statement.setString(1, this.getUserId());
                    statement.setString(2, this.getUserPassword());

                    ResultSet resultSet = statement.executeQuery();

                    if (!resultSet.next()) {
                        System.out.println("Invalid ID or password.");
                        System.out.println("You have " + (MAX_ATTEMPTS - attempts - 1) + " attempts left.");
                        attempts++;

                        // Fetch Email and Reg Date for OTP if password fails
                        try (PreparedStatement statement1 = conn.prepareStatement(sql1)) {
                            statement1.setString(1, this.getUserId());
                            ResultSet resultSet1 = statement1.executeQuery();

                            if (resultSet1.next()) {
                                this.setUserEmail(resultSet1.getString(this.getUserType() + "_Email"));
                                this.setUserRegDate(resultSet1.getTimestamp(this.getUserType() + "_Reg_Date")
                                        .toLocalDateTime());
                            }
                        }

                        continue; // Go for next login attempt
                    }

                    // Case 2: Successful Login
                    this.setUserId(resultSet.getString(this.getUserType() + "_ID"));
                    this.setUserName(resultSet.getString(this.getUserType() + "_Name"));
                    this.setUserPassword(resultSet.getString(this.getUserType() + "_Password"));
                    this.setUserEmail(resultSet.getString(this.getUserType() + "_Email"));
                    this.setUserRegDate(resultSet.getTimestamp(this.getUserType() + "_Reg_Date").toLocalDateTime());

                    System.out.println("Login successful.");
                    setLoggedInUserId(this.userId); // Set the logged-in user's ID
                    //this.UserMenu();
                    return true; // Exit loop on successful logi
                }

            } catch (SQLException e) {
                System.out.println("SQL Error: " + e.getMessage());
            }

        }

        // Case 3: Maximum attempts reached
        if (attempts >= MAX_ATTEMPTS) {
            System.out.println("Maximum attempts reached. Please try again later.");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted.");
            }

            System.out.println("1. Try again");
            System.out.println("2. Forgot Password");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    handleLogin();
                    break;
                case 2:
                    this.generateOTP();
                    this.handleOTP();
                    this.verifyOTP();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }

        return false;
    }


    private void generateOTP() // can work
    {

        // Generate a random 6-digit OTP
        int otp = (int) (Math.random() * 900000) + 100000;
        this.otpCode = Integer.toString(otp);
        this.otpExpiry = LocalDateTime.now().plusSeconds(1000);

        //insert into database 
        this.Connector.Connect();

        if (!this.Connector.isConnected()) {
            System.out.println("Connection failed");
            return;
        }

        String query = "Update " + this.userType + " set otp_code = ?, otp_expiry = ? where " + this.userType
                + "_Id = ?";

        Boolean checking = this.Connector.PrepareExecuteDML(query, this.otpCode, this.otpExpiry, this.getUserId());

        this.Connector.Disconnect();

        if (checking) {
            System.out.println("OTP generated successfully.");
        } else {
            System.out.println("OTP generation failed.");
        }

    }

    private void verifyOTP() //  can work
    {  
        

        System.out.println("Enter the OTP sent to your email: ");
        String userOTP = scanner.nextLine();

    
        String sql = "SELECT otp_expiry, otp_code, " + this.getUserType() + "_Password FROM " + this.getUserType() + " WHERE " + this.getUserType() + "_Id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            if (conn == null || conn.isClosed()) {
                System.out.println("Failed to establish a connection.");
                return;
            }

            try (PreparedStatement statement = conn.prepareStatement(sql)) {

                statement.setString(1, this.getUserId());

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    this.otpExpiry = resultSet.getTimestamp(1).toLocalDateTime();
                    this.otpCode = resultSet.getString(2);
                    this.setUserPassword(resultSet.getString(3)); 
                }

            } catch (SQLException e) {
                System.out.println("SQL Error: " + e.getMessage());
            }

        }catch (SQLException e) {
                System.out.println("Connection Error: " + e.getMessage());
        }
        

    
        if (LocalDateTime.now().isAfter(this.otpExpiry)) {
            System.out.println("OTP expired. Please try again.");
            // Show password and then enter menu

        } else if (userOTP.equals(this.otpCode)) {
            System.out.println("OTP verified successfully!");
            System.out.println("Your password is: " + this.getUserPassword());
            try {
                Thread.sleep(3000);
                handleLogin();
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted.");
            }
        }
        else{
            System.out.println("Invalid OTP. Please try again.");
        }


    }

    private void handleOTP()
    {
        System.out.println(this.otpCode);
        MailSender mail = new MailSender(
            this.userEmail,
            "OTP Recovery",
            new MailTemplate(this.otpCode, MailTemplate.TemplateType.OTP));
        mail.Send();
    }


    public void displayUserDetails() {  // can work
        System.out.println(this.getUserType()+" ID: " + this.getUserId());
        System.out.println(this.getUserType()+" Name: " + this.getUserName());
        System.out.println(this.getUserType()+" Email: " + this.getUserEmail());
        System.out.println(this.getUserType()+" Password: " + this.getUserPassword());
        System.out.println(this.getUserType()+" Registration Date: " + this.getUserRegDate());        
    }
















}


    

  
