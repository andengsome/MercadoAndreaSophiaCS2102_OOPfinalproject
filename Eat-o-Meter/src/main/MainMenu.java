package src.main;

import src.user.*;
import java.util.Scanner;

public class MainMenu {
    public static void main(String[] args) {
        displayTitle();
        
        Scanner scanner = new Scanner(System.in);
        //UserManager userManager = new UserManager();
        boolean isRunning = true;

        while (isRunning) {
            System.out.println("\n\n---------- MAIN MENU ----------");
            System.out.println("1. Log in");
            System.out.println("2. Sign up");
            System.out.println("3. Exit program");
            System.out.println("-------------------------------");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    UserManager.logIn();
                    break;
                case 2:
                    double customCalorieAdjustment = 0;
                    double tdee = 0;
                    UserManager.signUp(tdee, customCalorieAdjustment);
                    break;
                case 3:
                    System.out.println("Thank you for using Eat-o-Meter. Goodbye!");
                    isRunning = false;
                    break;    
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }
   
    private static void displayTitle() {

        System.out.println(
            " .--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--. \n" +
            "/ .. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\\n" +
            "\\ \\/\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\/ /\n" +
            " \\/ /`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'\\/ / \n" +
            " / /\\   _______  _______  _______         _______         __   __  _______  _______  ___ ____  ______     / /\\ \n" +
            "/ /\\ \\ |       ||   _   ||       |       |       |       |  |_|  ||       ||       ||       ||    _ |   / /\\ \\\n" +
            "\\ \\/ / |    ___||  |_|  ||_     _| ____  |   _   | ____  |       ||    ___||_     _||    ___||   | ||   \\ \\/ /\n" +
            " \\/ /  |   |___ |       |  |   |  |____| |  | |  ||____| |       ||   |___   |   |  |   |___ |   |_||_   \\/ / \n" +
            " / /\\  |    ___||       |  |   |         |  |_|  |       |       ||    ___|  |   |  |    ___||    __  |  / /\\ \n" +
            "/ /\\ \\ |   |___ |   _   |  |   |         |       |       | ||_|| ||   |___   |   |  |   |___ |   |  | | / /\\ \\\n" +
            "\\ \\/ / |_______||__| |__|  |___|         |_______|       |_|   |_||_______|  |___|  |_______||___|  |_| \\ \\/ /\n" +
            " \\/ /                                                                                                    \\/ / \n" +
            " / /\\.--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--./ /\\ \n" +
            "/ /\\ \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\/\\ \\\n" +
            "\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `' /\n" +
            " `--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--' "
        );
        System.out.println("  ---------------  Y O U R   P E R S O N A L   C A L O R I E   T R A C K I N G   B U D D Y  ---------------");
    }
}