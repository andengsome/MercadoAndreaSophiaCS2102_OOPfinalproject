package src.log;

import java.util.List;
import src.user.UserProfile;
import src.daos.*;

public class WeeklyReport {
    public static class DailySummary {
        public String date;
        public double caloriesConsumed;
        public double caloriesBurned;

        public DailySummary (String date, double caloriesConsumed, double caloriesBurned) {
            this.date = date;
            this.caloriesConsumed = caloriesConsumed;
            this.caloriesBurned = caloriesBurned;
        }

        public String getDate() { return date; }
        public double getCaloriesConsumed() { return caloriesConsumed; }
        public double getCaloriesBurned() { return caloriesBurned; }

        public void setDate(String date) {
            if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                throw new IllegalArgumentException("Invalid date: " + date + ". Must be in the format yyyy-mm-dd.");
            }
            this.date = date;
        }

        public void setCaloriesConsumed(double caloriesConsumed) {
            if (caloriesConsumed < 0) {
                throw new IllegalArgumentException("Calories cannot be negative.");
            }
            this.caloriesConsumed = caloriesConsumed;
        }

        public void setCaloriesBurned(double caloriesBurned) {
            if (caloriesBurned < 0) {
                throw new IllegalArgumentException("Calories cannot be negative.");
            }
            this.caloriesBurned = caloriesBurned;
        }
    }

    public static void generateWeeklyReport(UserProfile currentUser, String startDate, String endDate, List<DailySummary> dailySummaries, double calorieGoal) {

        double totalCaloriesConsumed = 0;
        double totalCaloriesBurned = 0;

        if (dailySummaries.isEmpty()) {
            System.out.println("\nNo data available for the specifies date range.");
            return;
        }

        DailySummaryDAO dailySummaryDAO = new DailySummaryDAO();
        ProgressDAO progressDAO = new ProgressDAO();

        System.out.println("\n\n                              +---------------------------------+");
        System.out.println("==============================| M Y   W E E K L Y   R E P O R T |==============================");
        System.out.println("                              +---------------------------------+");
        System.out.println("\n+-----------------------------------+");
        System.out.printf(" Week of: %s to %s%n", startDate, endDate);
        System.out.println("+-----------------------------------+");
      
        System.out.println("\n| DAILY CALORIE SUMMARY |----------------------------------------------------------------------\n");
        
        for(int i = 0; i < dailySummaries.size(); i++) {
            DailySummary day = dailySummaries.get(i);
            System.out.printf(" -> Day %d: Calories Consumed %.1f, Calories Burned %.1f", i + 1, day.caloriesConsumed, day.caloriesBurned);
            
            boolean isSaved = dailySummaryDAO.addDailySummary(day, currentUser);
            if (isSaved) {
                System.out.printf("[ Day %d saved. ]%n", i + 1);
            } else {
                System.out.printf("\t[ Failed to save Day %d. ]%n", i + 1);
            }

            totalCaloriesConsumed += day.getCaloriesConsumed();
            totalCaloriesBurned += day.getCaloriesBurned();
        }

        System.out.println("\n-----------------------------------------------------------------------------------------------");
        
        double weeklyCalorieGoal = calorieGoal * 7;
        double averageCaloriesLeft = (weeklyCalorieGoal - (totalCaloriesConsumed - totalCaloriesBurned)) / 7;

        System.out.println("\n\n| TOTAL WEEKLY STATS |-------------------------------------------------------------------------\n");
        System.out.printf("%-25s: %.2f kcal%n", "Weekly Calorie Goal", weeklyCalorieGoal);
        System.out.printf("%-25s: %.2f kcal%n", "Total Calories Consumed", totalCaloriesConsumed);
        System.out.printf("%-25s: %.2f kcal%n", "Total Calories Burned", totalCaloriesBurned);
        System.out.printf("%-25s: %.2f kcal%n", "Average Calories Left", averageCaloriesLeft);
        if (averageCaloriesLeft <= 10.0) {
            System.out.println("Great job! You are on track to achieve your goal.");
        } else {
            System.out.println("You did well but you may need to adjust your calorie intake or activity levels.");
        }

        System.out.println("\n-----------------------------------------------------------------------------------------------");

        System.out.println("\n\n| WEIGHT PROGRESS |----------------------------------------------------------------------------");

        System.out.println("\nStarting Weight: " + currentUser.getStartingWeight() + " kg");
        System.out.println("Current Weight: " + currentUser.getLatestWeight() + " kg");

        if (currentUser.getLastDateOfWeightUpdate() != null) {
            System.out.println("Last date of weight update: " + currentUser.getLastDateOfWeightUpdate());
        } else {
            System.out.println("No weight updates yet.");
        }

        double progressPercentage = calculateProgressPercentage(currentUser);

        System.out.println("Weight " + (currentUser.getGoal().equalsIgnoreCase("Lose Weight") ? "Lost" : "Gained") + ": " + currentUser.getWeightProgress() + " kg");
        System.out.printf("\nProgress Toward Goal: %.2f%%%n", progressPercentage);
        
        if (progressPercentage == 100.0) {
            System.out.println("Congratulations! You have achieve your weight goal.");
        } else if (progressPercentage > 0) {
            System.out.println("Keep going! You're making steady progress toward your goal.");
        } else {
            System.out.println("Let's get started! Every step counts toward achieving your goal. ");
        }
        
        System.out.println("\n-----------------------------------------------------------------------------------------------");
        
        boolean isSaved = progressDAO.addWeightUpdate(currentUser);
        if (isSaved) {
            System.out.println("Weight progress saved successfully!");
        } else {
            System.out.println("\nFailed to save weight progress.");
        
        }

        System.out.println("\n===============================================================================================");
    }

    public static double calculateProgressPercentage(UserProfile currentUser) {
        double progress = 0.0;
        if (currentUser.getGoal().equalsIgnoreCase("Lose Weight")) {
            progress = (currentUser.getStartingWeight() - currentUser.getLatestWeight()) / (currentUser.getStartingWeight() - currentUser.getGoalWeight()) * 100;
        } else if (currentUser.getGoal().equalsIgnoreCase("Gain Weight")) {
            progress = (currentUser.getLatestWeight() - currentUser.getStartingWeight()) / (currentUser.getGoalWeight() - currentUser.getStartingWeight()) * 100;
        }
        return Math.min(progress, 100.0);
    }
}