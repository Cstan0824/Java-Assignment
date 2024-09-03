package project.start;

import project.modules.user.Admin;
import project.modules.user.User;
import project.view.ViewSalesManagement;


public class App {
    public static void main(String[] args) {
        //initialise a user details for system temporary use
        User user = new Admin();
        user.setUserId("A001");
        user.setUserType("Admin");

        ViewSalesManagement view = new ViewSalesManagement(user);
        view.userMenu();

        //ViewReport view = new ViewReport(user);
        //view.menu();

        //ViewVendor view = new ViewVendor(user);
        //view.menu();

        //ViewItem viewItem = new ViewItem(user);
        //viewItem.menu();

    }
}
