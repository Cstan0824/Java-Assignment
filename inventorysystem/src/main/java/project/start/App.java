package project.start;

import project.modules.user.Admin;
import project.modules.user.User;
import project.view.ViewUser;
import project.view.ViewVendor;


public class App {
    // public static void main(String[] args) {
    //     //initialise a user details for system temporary use
    //     User user = new Admin();
    //     user.setUserId("A001");
    //     user.setUserType("Admin");

    //     //ViewSalesManagement view = new ViewSalesManagement(user);
    //     //view.userMenu();

    //     ViewVendor view = new ViewVendor(user);
    //     view.menu();
    // }

    // public static void menu() {
    //     System.out.println("1. Sales Management");
    //     System.out.println("2. Inventory Management");
    //     System.out.println("3. Report Management");
    //     System.out.println("4. Exit");

  





    // }



    public static void main(String[] args) {
        ViewUser viewUser = new ViewUser();
        viewUser.menu();
    }
}
