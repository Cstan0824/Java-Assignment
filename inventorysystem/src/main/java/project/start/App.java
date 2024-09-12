package project.start;

import project.global.ConsoleUI;
import project.modules.schedule.Schedule;
import project.modules.transaction.AutoReplenishment;
import project.view.ViewUser;


public class App implements Runnable{

    public static void main(String[] args) {
        //System.out.print("\033[47m"); - white screen
        ConsoleUI.welcomeScreen();
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
