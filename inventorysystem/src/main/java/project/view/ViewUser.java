package project.view;

import project.global.ConsoleUI;
import project.global.UserInputHandler;
import project.modules.user.Admin;
import project.modules.user.Retailer;
import project.modules.user.User;

public class ViewUser {

    private static User user;

    //method - menu
    public void menu() {
        
        while (true){
        ConsoleUI.clearScreen();    
        System.out.println("Are you ...");
        System.out.println("1. Admin");
        System.out.println("2. Retailer");
        System.out.println("3. Exit");
        int choice = UserInputHandler.getInteger("\nEnter choice (1-3): ", 1, 3);

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
                case 3:
                    ConsoleUI.exitScreen();
                    System.exit(1);
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }

    }

    private void retailerLog() {
        boolean back = false;
        while (!back) {
            ConsoleUI.clearScreen();
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Back");

            switch (UserInputHandler.getInteger("\nEnter choice: ", 1, 3)) {
                case 1:
                    if (user.handleLogin()) {
                        RetailerAccess();
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
            ConsoleUI.clearScreen();
            System.out.println("1. Profile Management"); //Retailer
            System.out.println("2. Purchase Management"); // Sales Order
            System.out.println("3. Schedule Management"); // Delivery Order
            System.out.println("4. log out");

            switch (UserInputHandler.getInteger("\nEnter choice: ", 1, 4)) {
                case 1:
                    user.UserMenu();
                    break;
                case 2:
                    ViewSalesManagement viewSalesManagement = new ViewSalesManagement(user);
                    viewSalesManagement.userMenu();
                    break;
                case 3:
                    ViewScheduleManagement viewScheduleManagement = new ViewScheduleManagement(user);
                    viewScheduleManagement.retailerMenu();
                    break;
                case 4:
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
            ConsoleUI.clearScreen();
            System.out.println("1. Admin & Retailer Management"); //Admin and Retailer
            System.out.println("2. Vendor Management"); //Vendor
            System.out.println("3. Item Management"); //Item，Auto Replenishment
            System.out.println("4. Purchase Management");//PO, GRN
            System.out.println("5. Sales Management");//SO
            System.out.println("6. Schedule Management"); //DO, Schedule, Vehicle Management
            System.out.println("7. Report Management"); //Report
            System.out.println("8. log out");

            switch (UserInputHandler.getInteger("\nEnter choice: ", 1, 8)) {
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
                    ViewSalesManagement viewSalesManagement = new ViewSalesManagement(user);
                    viewSalesManagement.userMenu();
                case 6:
                    ViewScheduleManagement viewScheduleManagement = new ViewScheduleManagement(user);
                    viewScheduleManagement.adminMenu();
                    break;
                case 7:
                    ViewReport viewReport = new ViewReport(user);
                    viewReport.menu();
                    break;
                case 8:
                    logOut = true;
                    break;
            }
        }
    }
}
