package project.start;

import project.modules.user.Admin;
import project.modules.user.User;


public class App {
    public static void main(String[] args) {
        //initialise a user details for system temporary use
        User user = new Admin();
        user.setUserId("A001");
        user.setUserType("Admin");

        //ViewPurchaseManagement view = new ViewPurchaseManagement(user);
        //view.menu();

        //ViewReport view = new ViewReport(user);
        //view.menu();
    }
}
