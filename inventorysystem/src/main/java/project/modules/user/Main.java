package project.modules.user;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Are you ...");
        System.out.println("1. Admin");
        System.out.println("2. Retailer");
        System.out.println("Enter choice (1-2): ");
        int choice = scanner.nextInt();

        switch(choice){
            case 1:
                Admin admin = new Admin();
                admin.handleLogin();
                break;
            case 2:
                Retailer retailer = new Retailer();
                System.out.println("1. Login");
                System.out.println("2. Register");
                System.out.println("Enter choice (1-2): ");

                int choice2 = scanner.nextInt();

                switch(choice2){
                    case 1:
                        retailer.handleLogin();
                        break;
                    case 2:
                        retailer.Register();
                        break;
                    default:
                        System.out.println("Invalid choice.");
                        break;
                }
                break;
            default:
                System.out.println("Invalid choice.");
                break;
        }
        scanner.close();

    

        

        


        


    }
}