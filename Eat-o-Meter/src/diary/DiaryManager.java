package src.diary;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import src.user.*;
import src.daos.LogItemDAO;
import src.log.WeeklyReport.*;

public class DiaryManager {
    private static final Scanner scanner = new Scanner(System.in);
    private static LogItemDAO logItemDAO = new LogItemDAO();
    private static double caloriesBurned = 0;
    private static double foodTotalCalories = 0;
    private static double calorieGoal = 0;

    public static void openDiary(UserProfile currentUser, double tdee, double customCalorieAdjustment) {
        calorieGoal = currentUser.getCalorieGoal();
 
        boolean isRunning = true;
        String currentDate = LocalDate.now().toString();

        while (isRunning) {
            System.out.println("\nCurrent logging date: " + currentDate);
            System.out.print("Is this a new day? (y/n): ");
            if (scanner.nextLine().equalsIgnoreCase("y")) {
                foodTotalCalories = 0;
                caloriesBurned = 0;
                currentDate = promptDate();
            }

            String username = currentUser.getUsername();

            displayEatoMeter(calorieGoal, foodTotalCalories, caloriesBurned);

            System.out.println("\nSelect an option:");
            System.out.println("----------------------------------------");
            System.out.println("1. Log food");
            System.out.println("2. Log Exercise");
            System.out.println("3. View History");
            System.out.println("4. Exit");
            System.out.println("----------------------------------------");
            System.out.print("Input: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    logItem("Food", currentDate, username, currentUser);
                    break;
                case 2:
                    logItem("Exercise", currentDate, username, currentUser);
                    break;
                case 3:
                    displayLogHistory(username);
                    break;                    
                case 4:
                    System.out.println("Closing diary...");
                    isRunning = false;
                    break;
                default:
                    System.out.println("Invalid input. Please try again.");
            }
        }
    }

    private static void displayEatoMeter(double calorieGoal, double foodTotalCalories, double caloriesBurned) {
        System.out.println("\n\n=====================================================================================");
        System.out.println("===============        M Y   E A T - O - M E T E R   D I A R Y        ===============");
        System.out.println("=====================================================================================");
        double caloriesLeft = calorieGoal - (foodTotalCalories - caloriesBurned);
        System.out.printf("\nCalorie Goal: %.2f%nCalories Consumed: %.2f%nCalories Burned: %.2f%nCalories Left: %.2f%n",
                          calorieGoal, foodTotalCalories, caloriesBurned, caloriesLeft);
        System.out.println("\n=====================================================================================");
    }

    private static void logItem(String type, String currentDate, String username, UserProfile currentUser) {
        boolean addMore = true;
        while (addMore) {
            displayEatoMeter(calorieGoal, foodTotalCalories, caloriesBurned);

            String category = type.equalsIgnoreCase("Food") ? displayMeal() : displayExercise();
        
            System.out.println("\n---------------------------- " + category + " ----------------------------");

            scanner.nextLine();
            System.out.print(type + " Name: ");
            String itemName = scanner.nextLine();

            double kcal = promptNumericInput(type.equals("Food") ?  "Calories per serving: " : "Calories burned: ");
            int quantity = promptIntegerInput(type.equalsIgnoreCase("Food") ? "Number of servings: " : "Duration (minutes): ");
            String unit = type.equalsIgnoreCase("Food") ? promptFoodUnit() : "minutes";
            double totalCalories = type.equalsIgnoreCase("Food") ? kcal * quantity : kcal;

            DiaryEntry entry = type.equalsIgnoreCase("Food") ?
                new FoodEntry(username,"Food", category, itemName, kcal, quantity, unit, currentDate) :
                new ExerciseEntry(username,"Exercise", category, itemName, kcal, quantity, unit, currentDate);

            boolean success = logItemDAO.addLogItem(entry, currentUser);
            if (success) {
                if (type.equalsIgnoreCase("Food")) {
                    foodTotalCalories += totalCalories;
                } else {
                    caloriesBurned += totalCalories;
                }
                System.out.println("\n=====================================================================================");
                System.out.println(type + " logged successfuly!");
            } else {
                System.out.println("\n=====================================================================================");
                System.out.println("Failed to log " + type + ". Please try again.");
                logItem(type, currentDate, username, currentUser);
            }
    
            System.out.print("\nWould you like to add another " + type + "? (y/n): ");
            addMore = scanner.nextLine().equalsIgnoreCase("y");
        }
    }

    public static List<DailySummary> getDailySummaries(String startDate, String endDate, String username) {
        List<DailySummary> summaries = new ArrayList<>();
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }

        LogItemDAO logItemDAO = new LogItemDAO();
        List<DiaryEntry> diaryEntries = logItemDAO.getLogItemsByUsername(username);
        
        start.datesUntil(end.plusDays(1)).forEach(date -> {
            double dayConsumed = diaryEntries.stream()
                .filter(entry -> entry.getDate().equals(date.toString()) && entry.getType().equalsIgnoreCase("Food"))
                .mapToDouble(DiaryEntry::getCalories).sum();

            double dayBurned = diaryEntries.stream()
                .filter(entry -> entry.getDate().equals(date.toString()) && entry.getType().equalsIgnoreCase("Exercise"))
                .mapToDouble(DiaryEntry::getCalories).sum();

            summaries.add(new DailySummary(date.toString(), dayConsumed, dayBurned));
        });
        
        return summaries;
    }

    public static void clearDiaryForUser(String username) {
        System.out.print("\nAre you sure you want to delete all entries for this user? (y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            LogItemDAO logItemDAO = new LogItemDAO();
            boolean success = logItemDAO.deleteLog(username);

            if (success) {
                System.out.println("All diary entries associated with this acount have been deleted.");
            } else {
                System.out.println("Failed to delete diary entries.");

            }            
        }
    }

    private static void displayLogHistory(String username) {
        System.out.println("\n\n=========================================================================================================================");
        System.out.println("=====================                        D A I L Y  L O G  H I S T O R Y                        =====================");
        System.out.println("=========================================================================================================================");

        List<DiaryEntry> entries = logItemDAO.getLogItemsByUsername(username);
        if (entries == null || entries.isEmpty()) {
            System.out.println("No entries available in your diary.");
            return;
        }

        System.out.println("\n------------------------------------------------------------------------------------------------------------------------");
        System.out.println("Date          | Type       | Category           | Item Name                 | Details                   | Calories");
        System.out.println("------------------------------------------------------------------------------------------------------------------------");
        for (DiaryEntry entry : entries) {
            String details = "";
            if (entry instanceof FoodEntry) {
                FoodEntry foodEntry = (FoodEntry) entry;
                details = "Quantity: " + foodEntry.getQuantity() + " " + foodEntry.getUnit();
            } else if (entry instanceof ExerciseEntry) {
                ExerciseEntry exerciseEntry = (ExerciseEntry) entry;
                details = "Duration: " + exerciseEntry.getDuration() + " " + exerciseEntry.getUnit();
            }
            System.out.printf("%-13s | %-10s | %-18s | %-25s | %-25s | %.2f kcal%n",
                entry.getDate(), entry.getType(), entry.getCategory(), entry.getItemName(), details, entry.getCalories()); 
        }

        System.out.println("------------------------------------------------------------------------------------------------------------------------");
        System.out.println("\n=========================================================================================================================");
    }

    private static String displayMeal() {
        System.out.println("\nSelect a meal category:");
        System.out.println("----------------------------------------");
        System.out.println("1. Breakfast");
        System.out.println("2. Lunch");
        System.out.println("3. Dinner");
        System.out.println("4. Snacks");
        System.out.println("----------------------------------------");
        System.out.print("Category: ");
        int choice = scanner.nextInt();
        
        switch (choice) {
            case 1: return "BREAKFAST";
            case 2: return "LUNCH";
            case 3: return "DINNER";
            case 4: return "SNACKS";
            default:
                System.out.println("Invalid choice. Please select a valid exercise category.\n");;
                return displayMeal();
        }
    }

    private static String displayExercise() {
        System.out.println("\nSelect an exercise category:");
        System.out.println("----------------------------------------");
        System.out.println("1. Cardiovascular");
        System.out.println("2. Strength");
        System.out.println("3. Workout Routines");
        System.out.println("----------------------------------------");
        System.out.print("Category: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1: return "CARDIOVASCULAR";
            case 2: return "STRENGTH";
            case 3: return "WORKOUT ROUTINES";
            default:
                System.out.println("Invalid choice. Please select a valid exercise category.\n");;
                return displayExercise();
        }
    }

    private static double promptNumericInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = Double.parseDouble(scanner.nextLine());
                if (value > 0) return value;
                System.out.println("Value must be greater than zero. Please try again.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a numeric value.");
            }
        }
    }

    private static int promptIntegerInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine());
                if (value > 0) return value;
                System.out.println("Value must be greater than zero. Please try again.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }

    private static String promptFoodUnit() {
        String[] units = {"grams", "pieces", "cups", "ounces", "slices", "numbers"};
        System.out.println("\nSelect the unit of serving:");
        System.out.println("----------------------------------------");
        for (int i = 0; i < units.length; i++) {
            System.out.printf("%d. %s%n", i + 1, units[i]);
        }
        System.out.println("----------------------------------------");

        while (true) {
            System.out.print("Unit: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= units.length) return units[choice - 1];
                System.out.println("Invalid selection. Please choose a valid unit.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a numeric choice.");
            }
        }
    }

    public static String promptDate() {
        System.out.print("\nDate (yyyy-mm-dd): ");
        String date;
        while (true) {
            date = scanner.nextLine();
            try {
                LocalDate parsedDate = LocalDate.parse(date);
                if (!parsedDate.isAfter(LocalDate.now())) {
                    return date;
                } else {
                    System.out.print("Date cannot be in the future. Enter the date today (yyyy-mm-dd): ");
                }            
            } catch (DateTimeParseException e) {
                System.out.print("Invalid date format. Enter date (yyyy-mm-dd): ");
            }
        }
    }
}  
