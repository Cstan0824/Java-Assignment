package project.start;

import project.modules.schedule.Schedule;
import project.view.ViewUser;
import project.modules.transaction.AutoReplenishment;


public class App implements Runnable{

    public static void main(String[] args) {
        //ConsoleUI.welcomeScreen();
        App app = new App(); // Create an instance of App (Thread)
        Thread thread = new Thread(app);
        thread.start(); // Start the thread
                
        ViewUser viewUser = new ViewUser();
        viewUser.menu();
    }

    @Override
    @SuppressWarnings("BusyWait")
    public void run() {
        while (true) {
            try {
                Schedule.StockOutProcess();
                AutoReplenishment.ExecuteAutomation();
                Thread.sleep(600000); // 600,000 milliseconds = 10 minute - 6000 milliseconds = 6 seconds
            } catch (InterruptedException e) {
                System.out.println("Thread Error: " + e.getMessage());
            }
        }
    }
}
