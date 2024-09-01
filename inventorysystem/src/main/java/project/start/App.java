package project.start;

import java.util.Scanner;

import project.modules.user.Admin;
import project.modules.user.User;
import project.view.ViewSalesManagement;

public class App {
    private static final Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
         //initialise a user details for system temporary use
        User user = new Admin();
        user.setUserId("A001");
        user.setUserType("Admin");
        
        ViewSalesManagement view = new ViewSalesManagement(user);
        view.adminMenu();

    }
}
