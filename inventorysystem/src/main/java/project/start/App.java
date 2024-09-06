package project.start;

import project.modules.transaction.AutoReplenishment;
import project.view.ViewUser;

public class App extends Thread{

    public static void main(String[] args) {
        ViewUser viewUser = new ViewUser();
        viewUser.menu();
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(60000); // 60,000 milliseconds = 1 minute
                AutoReplenishment.ExecuteAutomation();
            }
        } catch (InterruptedException e) {
            System.out.println("Thread Error: " + e.getMessage());
        }
    }
}
