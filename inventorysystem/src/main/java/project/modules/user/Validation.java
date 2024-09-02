package project.modules.user;

public class Validation {
    //validation for all data field 

    public static  boolean validateUserId(String userId) { 
        if(userId.matches("^[ARV][0-9]{2}[1-9]{1}$")) {
            return true;
        } 
        else 
        {
            System.out.println("Invalid User ID Format ");
            return false;
        }
    }

    //String
    //Integer range
    //Date Checking



    public static boolean validateUserName(String userName) {
        if(userName.matches("^[a-zA-Z0-9]{6,20}$")) 
        {
            return true;
        }
        else
        {
            System.out.println("Invalid User Name. Please enter a valid User Name: ");
            return false;

        }
    }

    public static boolean validateUserPassword(String userPassword) {
        if(userPassword.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$")) 
        {
            return true;
        }
        else
        {
            System.out.println("Invalid User Password. Please enter a valid User Password: ");
            return false;

        }
    }

}

    

    




