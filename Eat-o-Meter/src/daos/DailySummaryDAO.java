package src.daos;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

import src.user.UserProfile;
import src.log.WeeklyReport.DailySummary;


public class DailySummaryDAO {
    private Connection connection;
    
    public DailySummaryDAO() {
        try {
            this.connection = DBConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to establish database connection.", e);
        }
    }

    public boolean addDailySummary(DailySummary day, UserProfile currentUser) {
        String checkQuery = "SELECT entry_id FROM diary_entries WHERE username = ? AND entry_date = ?";
        try(PreparedStatement check = connection.prepareStatement(checkQuery)) {
            check.setString(1, currentUser.getUsername());
            check.setString(2, day.getDate());

            ResultSet rs = check.executeQuery();
            if (rs.next()) {
               return updateSummary(day, currentUser);
            } else {
               String query = "INSERT INTO diary_entries (username, entry_date, calories_consumed, calories_burned) VALUES(?, ?, ?, ?)";
               try(PreparedStatement s = connection.prepareStatement(query)) {
                   s.setString(1, currentUser.getUsername());
                   s.setString(2, day.getDate());
                   s.setDouble(3, day.getCaloriesConsumed());
                   s.setDouble(4, day.getCaloriesBurned());

                   int rowsAffected = s.executeUpdate();
                    return rowsAffected > 0;
               }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<DailySummary> getEntriesByUsername(String username) {
        String query = "SELECT * FROM diary_entries WHERE username = ?";
        List<DailySummary> summaries = new ArrayList<>();
        try (PreparedStatement s = connection.prepareStatement(query)) {
            s.setString(1, username);
            ResultSet rs = s.executeQuery();

            if (rs.next()) {
                summaries.add(new DailySummary(
                    rs.getString("entry_date"), rs.getDouble("calories_consumed"), rs.getDouble("calories_burned")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return summaries;
    }

    public boolean updateSummary(DailySummary day, UserProfile currentUser) {
        String query = "UPDATE diary_entries SET calories_consumed = ?, calories_burned = ? WHERE username = ? AND entry_date = ?";
        try(PreparedStatement s = connection.prepareStatement(query)) {
            s.setDouble(1, day.getCaloriesConsumed());
            s.setDouble(2, day.getCaloriesBurned());
            s.setString(3, currentUser.getUsername());
            s.setString(4, day.getDate());

            int rowsAffected = s.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteSummary(String username) {
        String query = "DELETE FROM diary_entries WHERE username = ?";
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