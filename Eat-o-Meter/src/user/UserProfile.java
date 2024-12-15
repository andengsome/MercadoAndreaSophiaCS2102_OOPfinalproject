package src.user;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserProfile {
    private String goal;
    private String name;
    private String sex;
    private int age;
    private double height;
    private double startingWeight;
    private double weight;
    private double goalWeight;
    private String activeness;
    private double calorieGoal;
    private String username;
    private String password;
    private List<Double> weeklyWeights = new ArrayList<>();
    private LocalDate lastWeightUpdateDate;

    public UserProfile(String goal, String name, String sex, int age, double height, double startingWeight,
                       double weight, double goalWeight, String activeness, double calorieGoal,  String username, String password) {
        this.goal = goal;
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.height = height;
        this.startingWeight = startingWeight;
        this.weight = weight;
        this.goalWeight = goalWeight;
        this.activeness = activeness;
        this.calorieGoal = calorieGoal;
        this.username = username;
        this.password = password;
    }

    public String getGoal() { return goal; }
    public String getName() { return name; }
    public String getSex() { return sex; }
    public int getAge() { return age; }
    public double getHeight() { return height; }
    public double getStartingWeight() { return startingWeight; }
    public double getGoalWeight() { return goalWeight; }  
    public String getActiveness() { return activeness; }
    public double getCalorieGoal() { return calorieGoal; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    

    public double getLatestWeight() {
        return weeklyWeights.isEmpty() ? weight : weeklyWeights.get(weeklyWeights.size() - 1);
    }

    public LocalDate getLastDateOfWeightUpdate() {
        return lastWeightUpdateDate;
    }

    public List<Double> getWeeklyWeights() {
        return weeklyWeights;
    }

    public void displayProfile() {
        System.out.println("\n+-+-+-+-+-+-+-+-+-| MY EAT-O-METER PROFILE |-+-+-+-+-+-+-+-+-+");
        System.out.println("Username: " + username);
        System.out.println("Name: " + name);
        System.out.println("Age: " + age + " yrs old");
        System.out.println("Sex: " + sex);
        System.out.printf("Height: %.1f cm%n", height);

        System.out.println("\n+-+-+-+-+-+-+-+-+-+-+-| M Y  G O A L S |-+-+-+-+-+-+-+-+-+-+-+");
        System.out.println("Goal: " + goal);
        System.out.printf("Starting Weight: %.1f kg%n", startingWeight);
        System.out.printf("Current Weight: %.1f kg%n", weight);
        System.out.printf("Goal Weight: %.1f kg%n", goalWeight);
        System.out.println("Activity Level: " + activeness);
        System.out.printf("Daily Calorie Goal: %.2f kcal/day%n", calorieGoal);
        System.out.println("+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-++-+-+-+");
    }

    public boolean canUpdateWeight() {
        if (lastWeightUpdateDate == null) {
            return true;
        }
        LocalDate today = LocalDate.now();
        return today.isAfter(lastWeightUpdateDate.plusDays(6));
    }

    public void updateWeight(double latestWeight) {
        if (latestWeight <= 0) {
            System.out.println("Invalid weight. Please enter a positie value.");
            return;
        }

        if (!canUpdateWeight()) {
            System.out.println("\nYou can only update your weight once a week. Try again next time.");
            return;
        }
        this.weeklyWeights.add(latestWeight);
        this.weight = latestWeight;
        this.lastWeightUpdateDate = LocalDate.now();
        System.out.println("Weight updated successfully!");
    }

    public double getWeightProgress() {
        if (weeklyWeights.isEmpty()) {
            return 0;
        }
        switch (goal.toLowerCase()) {
            case "lose weight": return getStartingWeight() - getLatestWeight();
            case "gain weight": return getLatestWeight() - getStartingWeight();
            default:
                System.out.println("Invalid input. Try again");
                return 0;
        } 
    }

    public void recalculateCalories(double customCalorieAdjustment) {
        double newTDEE = src.calories.CalculateCalories.calculateTDEE(this);
        this.calorieGoal = src.calories.CalculateCalories.dailyCalorieGoal(this, newTDEE, customCalorieAdjustment);

        System.out.println("\nCalorie goals have been updated based on your newly recorded weight!");
        System.out.println("Your new TDEE: " + newTDEE + " kcal");
        System.out.println("Your new daily calorie goal: " + this.calorieGoal + " kcal");
    }
}