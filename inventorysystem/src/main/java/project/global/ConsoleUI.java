package project.global;

import java.io.IOException;
import java.util.Scanner;

public class ConsoleUI {

    private static final Scanner scanner = new Scanner(System.in);
    
    public static void clearScreen() {
    try {
        if (System.getProperty("os.name").contains("Windows")) {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } else {
            new ProcessBuilder("clear").inheritIO().start().waitFor();
        }
    } catch (IOException | InterruptedException ex) {
        System.out.println("Screen clearing error: " + ex.getMessage());
    }
    }


    public static void pause() {
        System.out.print("\n\nPress any key to continue...  ");
        scanner.nextLine();
    }

    public static void welcomeScreen() {
        ConsoleUI.clearScreen();
        ConsoleUI.welcomeText();
        ConsoleUI.loadingBar();
        ConsoleUI.showTitle();
        ConsoleUI.showCompanyName();
        ConsoleUI.spinnerAnimation();
        System.out.println("System Ready!");
        ConsoleUI.pause();
        System.out.println("\n\n");
    }
    
    
    @SuppressWarnings("BusyWait")
    // 1. Welcome Text
    private static void welcomeText() {
        System.out.println("  IIIIIII     N      N     V       V     EEEEE     N      N     TTTTTTT     OOOOO     RRRRRR      Y     Y");
        System.out.println("     I        NN     N     V       V     E         NN     N        T        O   O     R    R       Y   Y ");
        System.out.println("     I        N  N   N      V     V      EEEEE     N  N   N        T        O   O     RRRRRR         Y   ");
        System.out.println("     I        N    N N       V   V       E         N    N N        T        O   O     R    R         Y   ");
        System.out.println("  IIIIIII     N      N        V V        EEEEE     N      N        T        OOOOO     R    R         Y   ");

        System.out.println();

        System.out.println("   SSSSS      Y     Y     SSSSS     TTTTTTT     EEEEE     M      M");
        System.out.println("  S            Y   Y     S             T        E         MM    MM");
        System.out.println("   SSSSS         Y        SSSSS        T        EEEEE     M  MM  M");
        System.out.println("       S         Y            S        T        E         M      M");
        System.out.println("   SSSSS         Y        SSSSS        T        EEEEE     M      M");
    }
    
    @SuppressWarnings("BusyWait")
    // 2. Loading Bar Animation
    private static void loadingBar() {
        try {
            System.out.print("\n\n\nLoading: ");
            for (int i = 0; i < 50; i++) {
                System.out.print("#");
                Thread.sleep(25);  // Pause to simulate loading time
            }
            System.out.println(" Done!");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Preserve interrupt status
            System.out.println("Loading bar animation was interrupted.");
        }
    }
    
    @SuppressWarnings("BusyWait")
    // 3. Display System Title
    private static void showTitle() {
        try {
            String title = "\n\nINVENTORY MANAGEMENT SYSTEM";
            System.out.println();
            for (char ch : title.toCharArray()) {
                System.out.print(ch);
                Thread.sleep(80);  // Pause between each character
            }
            System.out.println();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Preserve interrupt status
            System.out.println("Displaying system title was interrupted.");
        }
    }
    
    @SuppressWarnings("BusyWait")
    // 4. Display Company Name
    private static void showCompanyName() {
        try {
            String company = "Powered by Inventory Solutions";
            for (char ch : company.toCharArray()) {
                System.out.print(ch);
                Thread.sleep(80);  // Pause between each character
            }
            System.out.println();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Preserve interrupt status
            System.out.println("Displaying company name was interrupted.");
        }
    }
    
    @SuppressWarnings("BusyWait")
    // 5. Spinner Animation
    private static void spinnerAnimation() {
        System.out.println();
        System.out.println();
        try {
            char[] spinner = {'|', '/', '-', '\\'};
            for (int i = 0; i < 30; i++) {
                System.out.print("\rLoading  " + spinner[i % spinner.length]);
                Thread.sleep(200);
            }
            System.out.print("      ");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Preserve interrupt status
            System.out.println("Spinner animation was interrupted.");
        }
    }

    public static void exitScreen() {
        ConsoleUI.clearScreen();
        ConsoleUI.welcomeText();
        ConsoleUI.countdownExit();
        ConsoleUI.scrollingTextExit();
        ConsoleUI.spiralFadeOut();
        System.out.println("\n\nGoodbye!\n\n");
    }

    @SuppressWarnings("BusyWait")
    public static void countdownExit() {
        try {
            System.out.print("\n\nExiting the Inventory System in:    ");
            for (int i = 5; i > 0; i--) {
                System.out.print(i + " ");
                Thread.sleep(1000); // Wait for 1 second
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Preserve interrupt status
            System.out.println("The countdown was interrupted.");
        }
    }

    @SuppressWarnings("BusyWait")
    public static void scrollingTextExit() {
        String message = "\n\nThank you for using Inventory System! ";
        try {
            for (int i = 0; i < message.length(); i++) {
                System.out.print(message.charAt(i));
                Thread.sleep(100); // Pause between each letter
            }
            System.out.println("\nSee you next time!");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Scrolling text was interrupted.");
        }
    }

    
    
    @SuppressWarnings("BusyWait")
    public static void spiralFadeOut() {
        char[] spinner = {'|', '/', '-', '\\'}; // Characters for a spinning effect
        try {
            System.out.print("\nShutting down ");
            for (int i = 0; i < 20; i++) {
                System.out.print("\rShutting down " + spinner[i % spinner.length]);
                Thread.sleep(200); // Pause for the spinning effect
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Spiral fade-out was interrupted.");
        }
    }
    
   
    
}
