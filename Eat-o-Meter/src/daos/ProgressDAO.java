package src.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import src.user.UserProfile;

public class ProgressDAO {
    private Connection connection;
    
    public ProgressDAO() {
        try {
            this.connection = DBConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to establish database connection.", e);
        }
    }

    public boolean addWeightUpdate (UserProfile currentUser) {
        String query = "INSERT INTO weight_progress (username, goal, current_weight, progress, goal_weight) VALUES(?, ?, ?, ?, ?)";
        try(PreparedStatement s = connection.prepareStatement(query)) {
            s.setString(1, currentUser.getUsername()); 
            s.setString(2, currentUser.getGoal());
            s.setDouble(3, currentUser.getLatestWeight());
            s.setDouble(4, currentUser.getWeightProgress());
            s.setDouble(5, currentUser.getGoalWeight());  

            int rowsAffected = s.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public UserProfile getProgressByUsername(String username) {
        String query = "SELECT * FROM weight_progress WHERE username = ?";
        try (PreparedStatement s = connection.prepareStatement(query)) {
            s.setString(1, username);
            ResultSet rs = s.executeQuery();

            if (rs.next()) {
                UserProfile user = new UserProfile(
                    rs.getString("goal"), rs.getString("name"), rs.getString("sex"), rs.getInt("age"), rs.getDouble("height"),
                    rs.getDouble("starting_weight"), rs.getDouble("current_weight"), rs.getDouble("goal_weight"), rs.getString("activeness"), 
                    rs.getDouble("calorie_goal"), rs.getString("username"), rs.getString("password")
                );
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateProgress(UserProfile currentUser) {
        String query = "UPDATE weight_progress SET goal = ?, current_weight = ?, progress = ?, goal_weight = ? WHERE username = ?";
        try(PreparedStatement s = connection.prepareStatement(query)) {
            s.setString(1, currentUser.getGoal());
            s.setDouble(2, currentUser.getLatestWeight());
            s.setDouble(3, currentUser.getWeightProgress());
            s.setDouble(4, currentUser.getGoalWeight());

            int rowsAffected = s.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(String username) {
        String query = "DELETE FROM weight_progress WHERE username = ?";
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