package project.modules.user;

public class Validation {
    //validation for all data field 

    public static  boolean validateUserId(String userId) { 
        if (userId.matches("^[ARV][0-9]{5}$")) {
            return true;
        } 
        else 
        {
            System.out.println("Invalid User ID Format ");
            return false;
        }
    }

 

    public static boolean validateUserPassword(String userPassword) {
        if(userPassword.matches("^(?=.*[@#$%^&+=])(?=\\S+$).{5,}$"))
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

    

    




