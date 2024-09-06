package project.view;

import java.awt.Desktop;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Scanner;

import project.global.UserInputHandler;
import project.modules.transaction.Report;
import project.modules.user.User;

public class ViewReport {
    private static final Scanner sc = new Scanner(System.in);
    private static User user;
    private Report report;

    public ViewReport(User _user) {
        user = _user;
    }

    public void menu() {
        //Only admin can access this feature
        if (!user.getUserType().equals("Admin")) {
            System.out.println("You do not have permission to access this feature.");
            return;
        }

        // Simulate user input

        boolean exit = false;
        while (!exit) {
            boolean reportGenerated = false;
            System.out.println("============== Report Menu ==============");
            System.out.println("1. Generate Purchase Report");
            System.out.println("2. Generate Sales Report");
            System.out.println("3. Exit");
            System.out.println("=========================================");
            switch (UserInputHandler.validateInteger(sc, "Enter choice", 1, 3)) {
                case 1:
                    System.out.println("Generating Purchase Report...");
                    report = new Report("Yearly Purchase Report", "This is a report for all purchases made in a year.",
                            LocalDateTime.now());
                    if (!report.generateReport(Report.ReportType.YEARLY_PURCHASE)) {
                        System.out.println("Error generating purchase report.");
                    }
                    reportGenerated = true;

                    break;
                case 2:
                    System.out.println("Generating Sales Report...");
                    report = new Report("Yearly Sales Report", "This is a report for all sales made in a year.",
                            LocalDateTime.now());
                    if (!report.generateReport(Report.ReportType.YEARLY_SALES)) {
                        System.out.println("Error generating sales report.");
                    }
                    reportGenerated = true;
                    break;
                case 3:
                    exit = true;
                    break;
                default:
                    break;
            }

            if (reportGenerated == false) {
                continue;
            }
            if (!UserInputHandler.getConfirmation("Do you want to open the file?").equalsIgnoreCase("Y")) {
                return;
            }

            //open report file 
            if (Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                try {
                    Desktop.getDesktop().open(report.getFile());
                } catch (IOException e) {
                    System.out.println("Error opening file: " + e.getMessage());
                }
            } else {
                System.out.println("Opening file is not supported on this platform.");
            }
        }
    }
}
