package project.start;

import project.modules.schedule.Schedule;
import project.view.ViewUser;


public class App extends Thread{

    public static void main(String[] args) {

        //ConsoleUI.welcomeScreen();

        App appThread = new App();  // Create an instance of App (Thread)
        appThread.start();         // Start the thread

        ViewUser viewUser = new ViewUser();
        viewUser.menu();
    }

    @Override
    @SuppressWarnings("BusyWait")
    public void run() {
        try {
            while (true) {
                Schedule.StockOutProcess();
                Thread.sleep(60000); // 60,000 milliseconds = 1 minute
                //AutoReplenishment.ExecuteAutomation();

            }
        } catch (InterruptedException e) {
            System.out.println("Thread Error: " + e.getMessage());
        }
    }
}
