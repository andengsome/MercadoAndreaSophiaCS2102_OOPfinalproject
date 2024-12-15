package src.user;

import src.daos.*;
import src.log.*;
import src.calories.CalculateCalories;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import src.diary.DiaryManager;
import java.util.List;
import java.util.Scanner;

public class UserManager {
    private static final Scanner scanner = new Scanner(System.in);
    private static UserProfile currentUser;
    private static boolean continueLoop = true;  
    private static UserDAO userDAO = new UserDAO();

    public static void signUp(double tdee, double customCalorieAdjustment) {
        System.out.println("\n\n---------------------------------| S I G N   U P |---------------------------------");
        
        System.out.println("\nWhat is your goal of using Eat-o-Meter?");
        System.out.println("1. Lose Weight");
        System.out.println("2. Gain Weight");
        System.out.print("Input: ");
        int choiceGoal = scanner.nextInt();        
        scanner.nextLine();
    
        String goal = (choiceGoal == 1) ? "Lose Weight" : "Gain Weight";

        System.out.print("\nName of the User (FN, LN): ");
        String name = scanner.nextLine();
        while (!name.matches("^[A-Za-z ]+$")) {
            System.out.print("*Name must only contain alphabetic characters.\nPlease enter a valid name: ");
            name = scanner.nextLine();
        }

        System.out.print("\nSex\n  1. Male\n  2. Female\nInput: ");
        int sexChoice = scanner.nextInt();
        scanner.nextLine();
        String sex = (sexChoice == 1) ? "Male" : "Female";   

        System.out.print("\nAge: ");
        int age = scanner.nextInt();
        
        System.out.print("Height (cm): ");
        double height = scanner.nextDouble();

        System.out.print("Weight (kg): ");
        double startingWeight = scanner.nextDouble();
        double latestWeight = startingWeight;

        System.out.println("\nHow active are you?");
        System.out.println("1. Sedentary (little to no exercise)");
        System.out.println("2. Lightly active (light exercise/sports 1-3 days/week)");
        System.out.println("3. Moderately active (moderate exercise/sports 3-5 days/week)");
        System.out.println("4. Very active (hard exercise/sports 6-7 days/week)");
        System.out.println("5. Extremely active (very hard exercise/physical job)");
        System.out.print("Input: ");
        int choiceActivity = scanner.nextInt();
        scanner.nextLine();

        String activeness = activityLevel(choiceActivity);
        if (activeness.equals("Invalid input.")) {
            System.out.println("Invalid activity level. Please try again.");
            return;
        }

        UserProfile tempUser = new UserProfile(goal, name, sex, age, height, startingWeight, latestWeight, 0, activeness, 0, "", "");

        tdee = CalculateCalories.calculateTDEE(tempUser);

        System.out.println("\nNote: -----------------------------------------------------------------------------");
        System.out.println("The default calories per day for adjustment (lose or gain) is 500 calories.");
        System.out.println("However, this can vary based on individual needs and goals.");
        System.out.println("-----------------------------------------------------------------------------------");
        System.out.print("Would you like to use the default adjustment? (y/n): ");
        String customChoice = scanner.next();

        if (customChoice.equalsIgnoreCase("n")) {
            System.out.print("Enter the number of calories for adjustment (at least 250): ");
            customCalorieAdjustment = scanner.nextDouble();
            while (customCalorieAdjustment < 250) {
                System.out.print("Invalid input. Enter a value of at least 250: ");
                customCalorieAdjustment = scanner.nextDouble();
            }
        } else if (customChoice.equalsIgnoreCase("y")) {
            customCalorieAdjustment = 500;
        } else {
            System.out.print("Invalid input. Please enter a valid input (y/n): ");
            scanner.next();
        }
        System.out.println("Your calorie adjustment: " + customCalorieAdjustment + " kcal/day");

        double calorieGoal = CalculateCalories.dailyCalorieGoal(tempUser, tdee, customCalorieAdjustment);

        System.out.print("\nGoal weight (kg): ");
        double goalWeight = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("\nUsername: ");
        String username = scanner.nextLine();

        while (userDAO.isUsernameTaken(username)) {
            System.out.println("\n+----------------------------+");
            System.out.println("|  USERNAME ALREADY IN USE!  |");
            System.out.println("+----------------------------+");
            System.out.print("Please try another.\n\nUsername: ");
            username = scanner.nextLine();
        }

        System.out.print("Password (at least 8 characters): ");
        String password = scanner.nextLine();

        while (!password.matches(".*[0-9].*") || !password.matches(".*[A-Za-z].*") || password.length() < 8) {
            System.out.print("***Password must be at least 8 characters and contain a number.\n\nPlease enter a valid password: ");
            password = scanner.nextLine();
        }

        System.out.println("-----------------------------------------------------------------------------------");

        //UserProfile newUser = new UserProfile(goal, name, sex, age, height, startingWeight, latestWeight, goalWeight, activeness, calorieGoal, username, password);

        UserProfile newUser = userDAO.createUser(goal, name, sex, age, height, startingWeight, latestWeight, goalWeight, activeness, calorieGoal, username, password);

        if (newUser != null) {
            newUser.displayProfile();
            userDAO.getUserByUsername(username);
            System.out.println("Sign up successful. You can now log in.");
            logIn();
        } else {
            System.out.println("An error occured during sign up. Please try again.");
        }
    }
    
    public static void logIn() {
        boolean logInLoop = true;
        while (logInLoop) {
            System.out.println("\n\n---------------------| L O G  I N |---------------------");
            System.out.print("Username: ");
            String username = scanner.nextLine();

            System.out.print("Password: ");
            String password = scanner.nextLine();
            System.out.println("--------------------------------------------------------");

            UserProfile user = userDAO.validateLogIn(username, password);
            if (user != null) {
                System.out.println("Log in successful. Proceeding to home menu...");
                    currentUser = user;
                    homeMenu();
                    logInLoop = false;
            } else {
                System.out.println("Invalid username or password. Please try again.");
                System.out.print("Do you want to try again? (y/n): ");
                String choice = scanner.nextLine().toLowerCase();
                if (!choice.equals("y")) {
                        logInLoop = false;
                }
            }
        }
    }

    public static void homeMenu() {
        if (currentUser == null) {
            System.out.println("No user is currently logged in. Please log in first.");
            return;
        }
        
        double tdee = CalculateCalories.calculateTDEE(currentUser);
        double customCalorieAdjustment = 500;

        while (continueLoop) {
            System.out.println("\n\nHey there, " + currentUser.getUsername() + "!");
            System.out.println("\n----------- H O M E   M E N U -----------");
            System.out.println("1. My Eat-o-Meter Diary");
            System.out.println("2. My Weekly Report");
            System.out.println("3. Update Weight");
            System.out.println("4. Settings");
            System.out.println("-----------------------------------------");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
    
            switch (choice) {
                case 1:
                    src.diary.DiaryManager.openDiary(currentUser, tdee, customCalorieAdjustment);
                    break;
                case 2:
                    LocalDate endDate = LocalDate.now();
                    LocalDate startDate = endDate.minusDays(6);

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String formattedStartDate = startDate.format(formatter);
                    String formattedEndDate = endDate.format(formatter);
                
                    List<WeeklyReport.DailySummary> dailySummaries = DiaryManager.getDailySummaries(formattedStartDate, formattedEndDate, currentUser.getUsername());
                    double calorieGoal = currentUser.getCalorieGoal();
                
                    src.log.WeeklyReport.generateWeeklyReport(currentUser, formattedStartDate, formattedEndDate, dailySummaries, calorieGoal);
                    break;
                case 3:
                    updateWeight(customCalorieAdjustment);
                    break;
                case 4:
                    settings();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void settings() {
        System.out.println("\n\n------------ S E T T I N G S ------------");
        System.out.println("1. My Profile");
        System.out.println("2. Delete Account");
        System.out.println("3. Log Out");
        System.out.println("4. Return");
        System.out.println("-----------------------------------------");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
    
        switch (choice) {
            case 1:
                currentUser.displayProfile(); 
                break;
            case 2:
                if (confirmAction("Are you sure you want to delete your account? This action cannot be undone.")) {
                    UserManager.deleteAccount(currentUser);
                    continueLoop = false;
                }
                break;
            case 3:
                logOut();
                continueLoop = false;
                break;
            case 4:
                continueLoop = false;
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }

    }

    public static void deleteAccount(UserProfile currentUser) {
        userDAO.deleteUser(currentUser.getUsername());

        src.diary.DiaryManager.clearDiaryForUser(currentUser.getUsername());

        System.out.println("\nYour account has been successfully deleted.");
    }

    public static boolean confirmAction(String message) {
        System.out.println("\n" + message);
        System.out.print("Type 'yes' to confirm or 'no' to cancel: ");
        String confirmation = scanner.nextLine().toLowerCase();
        return confirmation.equals("yes");
    }

    private static String activityLevel (int choiceActivity) {
        switch (choiceActivity) {
            case 1: return "Sedentary";
            case 2: return "Lightly Active";
            case 3: return "Moderately Active";
            case 4: return "Very Active";
            case 5: return "Extra Active"; 
            default: throw new IllegalArgumentException();
        }
    }

    private static void updateWeight(double customCalorieAdjustment) {
            if (!currentUser.canUpdateWeight()) {
                LocalDate nextAllowedDate = currentUser.getLastDateOfWeightUpdate().plusDays(7);
                System.out.println("You can only update your weight once a week.");
                System.out.println("Next allowed update date: " + nextAllowedDate);
                return;
            }
    
            System.out.println("\n------------------------------- M Y   P R O G R E S S -------------------------------");
            System.out.print("Enter your current weight (kg): ");
            double latestWeight = scanner.nextDouble();
    
            if (latestWeight <= 0 || latestWeight > 500) {
                System.out.print("Invalid weight input. Please enter a realistic weight (kg): ");
                latestWeight = scanner.nextDouble();
            }
    
            currentUser.updateWeight(latestWeight);
            currentUser.recalculateCalories(customCalorieAdjustment);
        
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("Weight updated successfully! Your updated calorie goal will now reflect in the your Diary.");
    }

    public static void logOut() {
        currentUser = null;
        System.out.println("You have successfully logged out.");
    }
}
