package project.global;

import java.util.Scanner;

public class UserInputHandler {
    private static final Scanner scanner = new Scanner(System.in);

    public static String getConfirmation(String _message) {
        String choice;
        do {
            System.out.print(_message + " [Y/N]: ");
            choice = scanner.nextLine().trim();
            if (!choice.equalsIgnoreCase("Y") && !choice.equalsIgnoreCase("N")) {
                System.out.println("Invalid input. Please enter 'Y' or 'N'.");
            }
        } while (!choice.equalsIgnoreCase("Y") && !choice.equalsIgnoreCase("N"));
        return choice;
    }

    public static int getInteger(String _message, int _min, int _max) {
        int number;
        do {
            System.out.print(_message + " (" + _min + " - " + _max + "): ");
            number = scanner.nextInt();
            scanner.nextLine();
            if (number < _min || number > _max) {
                System.out.println("Invalid input. Please enter a number between " + _min + " and " + _max + ".");
            }
        } while (number < _min || number > _max);
        return number;
    }
}
