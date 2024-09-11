package project.start;

import project.global.ConsoleUI;
import project.modules.schedule.Schedule;
import project.modules.transaction.AutoReplenishment;
import project.view.ViewUser;


public class App implements Runnable{

    public static void main(String[] args) {
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
        System.out.println("Auto Replenishment Process Started");
        int count = 0;
        while (true) {
            try {

                Thread.sleep(6000); // 60,000 milliseconds = 1 minute
                Schedule.StockOutProcess();
                AutoReplenishment.ExecuteAutomation();
                System.out.println("Auto Replenishment Process Ended");

                
            } catch (InterruptedException e) {
                System.out.println("Thread Error: " + e.getMessage());
            }
            count++;
            System.out.println("Auto Replenishment Process: " + count);
        }
    }
}
