package project.view;

import java.util.Scanner;

import project.global.UserInputHandler;
import project.modules.user.Admin;
import project.modules.user.Retailer;
import project.modules.user.User;

public class ViewUser {

    private static User user;

    private final static Scanner scanner = new Scanner(System.in);

    //method - menu
    public void menu() {

        System.out.println("Are you ...");
        System.out.println("1. Admin");
        System.out.println("2. Retailer");
        System.out.println("Enter choice (1-2): ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                user = new Admin();
                if (user.handleLogin()) {
                    System.out.println("Login successful.");
                    AdminAccess();
                }
                break;
            case 2:
                user = new Retailer();
                retailerLog();
                break;
            default:
                System.out.println("Invalid choice.");
                break;
        }

    }

    private void retailerLog() {
        boolean back = false;
        while (!back) {
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Back");

            switch (UserInputHandler.getInteger("Enter choice", 1, 2)) {
                case 1:
                    if (user.handleLogin()) {
                        RetailerAccess();
                        user.UserMenu();
                        System.out.println("Login successful.");
                    }
                    break;
                case 2:
                    (new Retailer()).Register();
                    break;
                case 3:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
    }

    private void RetailerAccess() {
        boolean logOut = false;
        while (!logOut) {
            System.out.println("1. Profile Management"); //Retailer
            System.out.println("2. Purchase Management"); // Sales Order
            System.out.println("3. log out");

            switch (UserInputHandler.getInteger("Enter choice", 1, 3)) {
                case 1:
                    user.UserMenu();
                    break;
                case 2:
                    ViewSalesManagement viewSalesManagement = new ViewSalesManagement(user);
                    viewSalesManagement.userMenu();
                    break;
                case 3:
                    logOut = true;
                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
    }

    private void AdminAccess() {
        boolean logOut = false;
        while (!logOut) {
            System.out.println("1. Admin & Retailer Management"); //Admin and Retailer
            System.out.println("2. Vendor Management"); //Vendor
            System.out.println("3. Item Management"); //Item，Auto Replenishment
            System.out.println("4. Purchase Management");//PO, GRN
            System.out.println("5. Schedule Management"); //DO, Schedule, Vehicle Management
            System.out.println("6. Report Management"); //Report
            System.out.println("7. log out");

            switch (UserInputHandler.getInteger("Enter choice", 1, 8)) {
                case 1: 
                    user.UserMenu();
                    break;
                case 2:
                    ViewVendor viewVendor = new ViewVendor(user);
                    viewVendor.menu();
                case 3:
                    ViewItem viewItem = new ViewItem(user);
                    viewItem.menu();
                    break;
                case 4:
                    ViewPurchaseManagement viewPurchaseManagement = new ViewPurchaseManagement(user);
                    viewPurchaseManagement.menu();
                    break;
                case 5:
                    ViewScheduleManagement viewScheduleManagement = new ViewScheduleManagement(user);
                    viewScheduleManagement.adminMenu();
                    break;
                case 6:
                    ViewReport viewReport = new ViewReport(user);
                    viewReport.menu();
                    break;
                case 7:
                    logOut = true;
                    break;
            }
        }
    }
}
