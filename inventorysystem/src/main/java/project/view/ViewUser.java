package project.view;

import java.util.Scanner;

import project.modules.user.Admin;
import project.modules.user.Retailer;
import project.modules.user.User;

public class ViewUser {

    private static User user;
    //getter and setter
    public User getUser() {
        return user;
    }

    public void setUser(User _user) {
        user = _user;
    }

    //method
    public void menu() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Are you ...");
            System.out.println("1. Admin");
            System.out.println("2. Retailer");
            System.out.println("Enter choice (1-2): ");
            int choice = scanner.nextInt();
            
            switch (choice) {
                case 1:
                    Admin admin = new Admin();
                    admin.handleLogin();
                    break;
                case 2:
                    Retailer retailer = new Retailer();
                    System.out.println("1. Login");
                    System.out.println("2. Register");
                    System.out.println("Enter choice (1-2): ");
                    
                    int choice2 = scanner.nextInt();
                    
                    switch (choice2) {
                        case 1:
                            retailer.handleLogin();
                            break;
                        case 2:
                            retailer.Register();
                            break;
                        default:
                            System.out.println("Invalid choice.");
                            break;
                    }
                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
    }
}
