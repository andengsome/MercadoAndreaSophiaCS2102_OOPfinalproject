package src.calories;

import src.user.*;

public class CalculateCalories {
   
    //Basal Metabolic Rate (BMR)- calories or energy your body need to function
    public static double calculateBMR(UserProfile currentUser) {      
        if (currentUser.getSex().equalsIgnoreCase("Male")) {
            return 10 * currentUser.getLatestWeight() + 6.25 * currentUser.getHeight() - 5 * currentUser.getAge() + 5;
        } else if (currentUser.getSex().equalsIgnoreCase("Female")) {
            return 10 * currentUser.getLatestWeight() + 6.25 * currentUser.getHeight() - 5 * currentUser.getAge() - 161;
        } else {
            throw new IllegalArgumentException("Invalid input. Try again");
        }
    }

    public static double getActivityFactor(String activityLevel) {
        switch (activityLevel) {
            case "Sedentary": return 1.2;
            case "Lightly Active": return 1.375;
            case "Moderately Active": return 1.55;
            case "Very Active": return 1.725;
            case "Extra Active": return 1.9;
            default: throw new IllegalArgumentException("Invalid input. Try again");
        }
    }

    private static double calculateCalorieDeficit(UserProfile currentUser, double tdee, double customCalorieAdjustment) {
        return tdee - customCalorieAdjustment;
    }

    private static double calculateCalorieSurplus(UserProfile currentUser, double tdee, double customCalorieAdjustment) {
        return tdee + customCalorieAdjustment;
    }

    //Total Daily Energy Expenditure (TDEE) - total energy that a person uses in a day
    public static double calculateTDEE(UserProfile currentUser) {
        double bmr = calculateBMR(currentUser);
        double activityFactor = getActivityFactor(currentUser.getActiveness());
        return bmr * activityFactor;
    }
    
    public static double dailyCalorieGoal(UserProfile currentUser, double tdee, double customCalorieAdjustment) {
        double adjustedCalories ;
        if (currentUser.getGoal().equals("Lose Weight")) {
            adjustedCalories = calculateCalorieDeficit(currentUser, tdee, customCalorieAdjustment);
        } else if (currentUser.getGoal().equals("Gain Weight")) {
            adjustedCalories = calculateCalorieSurplus(currentUser, tdee, customCalorieAdjustment);
        } else {
            throw new IllegalArgumentException("Invalid input. Try again");
        }
        return adjustedCalories;
    }
}