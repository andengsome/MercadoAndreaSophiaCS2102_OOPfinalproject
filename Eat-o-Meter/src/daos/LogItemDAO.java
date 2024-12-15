package src.daos;

import java.sql.*;
import src.diary.DiaryEntry;
import src.diary.ExerciseEntry;
import src.diary.FoodEntry;
import src.user.UserProfile;
import java.util.List;
import java.util.ArrayList;

public class LogItemDAO {
    private Connection connection;
    
    public LogItemDAO() {
        try {
            this.connection = DBConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to establish database connection.", e);
        }
    }

    public boolean addLogItem(DiaryEntry entry, UserProfile currentUser) {
        String query = "INSERT INTO log_items (username, item_type, category, item_name, calories, quantity, unit, logged_date) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        try(PreparedStatement s = connection.prepareStatement(query)) {
            s.setString(1, currentUser.getUsername());
            s.setString(2, entry.getType());
            s.setString(3, entry.getCategory());
            s.setString(4, entry.getItemName());
            s.setDouble(5, entry.getCalories());

            if (entry instanceof FoodEntry) {
                FoodEntry food = (FoodEntry) entry;
                s.setInt(6, food.getQuantity());
                s.setString(7, food.getUnit());
            } else if (entry instanceof ExerciseEntry) {
                ExerciseEntry exercise = (ExerciseEntry) entry;
                s.setInt(6, exercise.getDuration());
                s.setString(7, exercise.getUnit());
            } else {
                throw new IllegalArgumentException("Unsupported DiaryEntry type");
            }

            s.setString(8, entry.getDate());
            int rowsAffected = s.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<DiaryEntry> getLogItemsByUsername(String username) {
        String query = "SELECT * FROM log_items WHERE username = ?";
        List<DiaryEntry> diaryEntries = new ArrayList<>();
        try (PreparedStatement s = connection.prepareStatement(query)) {
            s.setString(1, username);
            ResultSet rs = s.executeQuery();

            while (rs.next()) {
                String type = rs.getString("item_type");
                String category = rs.getString("category");
                String itemName = rs.getString("item_name");
                double calories = rs.getDouble("calories");
                String date = rs.getString("logged_date");

                if ("food".equalsIgnoreCase(type)) {
                    int quantity = rs.getInt("quantity");
                    String unit = rs.getString("unit");
                    diaryEntries.add(new FoodEntry(username, type, category, itemName, calories, quantity, unit, date));
                } else if ("exercise".equalsIgnoreCase(type)) {
                    int duration = rs.getInt("quantity");
                    String unit = rs.getString("unit");
                    diaryEntries.add(new ExerciseEntry(username, type, category, itemName, calories, duration, unit, date));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return diaryEntries;
    }

    public boolean updateLog(DiaryEntry entry, UserProfile currentUser) {
        String query = "UPDATE log_items SET item_type = ?, category = ?, item_name = ?, calories = ?, quantity = ?, unit = ? logged_date = ? WHERE username = ?";
        try(PreparedStatement s = connection.prepareStatement(query)) {
            
            s.setString(1, entry.getType());
            s.setString(2, entry.getCategory());
            s.setString(3, entry.getItemName());
            s.setDouble(4, entry.getCalories());

            if (entry instanceof FoodEntry) {
                FoodEntry food = (FoodEntry) entry;
                s.setInt(5, food.getQuantity());
                s.setString(6, food.getUnit());
            } else if (entry instanceof ExerciseEntry) {
                ExerciseEntry exercise = (ExerciseEntry) entry;
                s.setInt(5, exercise.getDuration());
                s.setString(6, exercise.getUnit());
            } else {
                throw new IllegalArgumentException("Unsupported DiaryEntry type");
            }

            s.setString(7, entry.getDate());
            s.setString(8, currentUser.getUsername());         
            int rowsAffected = s.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteLog(String username) {
        String query = "DELETE FROM log_items WHERE username = ?";
        try(PreparedStatement s = connection.prepareStatement(query)) {
            s.setString(1, username);

            int rowsAffected = s.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}