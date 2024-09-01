package project.global;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.mail.imap.protocol.INTERNALDATE;

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

    public static String getString(String _message, int _requiredLength, String _regexPattern) {
        String input;
        Pattern pattern = Pattern.compile(_regexPattern);

        while (true) {
            System.out.print(_message + ": ");
            input = scanner.nextLine().trim();

            // Check if the input matches the required length and pattern
            if (input.length() != _requiredLength) {
                System.out.println("Invalid input length. Please enter exactly " + _requiredLength + " characters.");
                continue;
            }

            Matcher matcher = pattern.matcher(input);
            if (!matcher.matches()) {
                System.out.println("Input does not match the required pattern. Please try again.");
                continue;
            }

            // If valid, return the input
            return input;
        }
    }
}