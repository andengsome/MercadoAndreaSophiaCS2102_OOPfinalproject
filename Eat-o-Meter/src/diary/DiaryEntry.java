package src.diary;

public class DiaryEntry {
    private String username;
    private String type;
    private String category;
    private String itemName;
    private double calories;
    private String date;

    public DiaryEntry(String username, String type, String category, String itemName, double calories, String date) {
        this.username = username;  
        this.type = type;
        this.category = category;
        this.itemName = itemName;
        this.calories = calories;
        this.date = date;
        
    }

    public String getUsername() { return username; }
    public String getType() { return type; }
    public String getCategory() { return category; }
    public String getItemName() { return itemName; }
    public double getCalories() { return calories; }
    public String getDate() { return date; }    

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setCalories(double calories) { 
        if (calories < 0) {
            throw new IllegalArgumentException("Calories cannot be negative.");
        }
        this.calories = calories;
    }

    public void setDate(String date) {
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new IllegalArgumentException("Invalid date: " + date + ". Must be in the format yyyy-mm-dd.");
        }
        this.date = date;
    }
    
    @Override
    public String toString() {
        return String.format("Username: %s, Type: %s, Category: %s, Item: %s, Calories: %.2f, Date: %s", getUsername(), getType(), getCategory(), getItemName(), getCalories(), getDate());
    }
}