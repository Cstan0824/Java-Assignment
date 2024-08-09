package com.example;


import java.util.Scanner;

public class Menu {
    
    public void MainMenu(){
        Scanner scanner = new Scanner(System.in);
        User user ;

        System.out.println("Welcome to the Inventory System");
        System.out.println("Are you ...");
        System.out.println("1. Admin");
        System.out.println("2. Retailer");
        int choice = scanner.nextInt(); 


        switch(choice){
            case 1: 
                user = new Admin();
                user.login();  // already implemented admin menu
                break;
            case 2:
                 user = new Retailer();
                user.login();  // already implemented retailer menu
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }
    }


   
}
