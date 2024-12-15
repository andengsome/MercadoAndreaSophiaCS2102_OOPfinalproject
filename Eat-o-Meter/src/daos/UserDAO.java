package src.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import src.user.UserProfile;

public class UserDAO {
    private Connection connection;
    
    public UserDAO() {
        try {
            this.connection = DBConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to establish database connection.", e);
        }
    }

    public UserProfile createUser(String goal, String name, String sex, int age, double height, double startingWeight, double latestWeight, double goalWeight, String activeness, double calorieGoal, String username, String password) { 
        String query = "INSERT INTO users(username, password, name, age, sex, height, starting_weight, goal, current_weight, goal_weight, activeness, calorie_goal) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try(PreparedStatement s = connection.prepareStatement(query)) {
            s.setString(1, username);
            s.setString(2, password);
            s.setString(3, name);
            s.setInt(4, age);
            s.setString(5, sex);
            s.setDouble(6, height);
            s.setDouble(7, startingWeight);
            s.setString(8, goal);
            s.setDouble(9, latestWeight);
            s.setDouble(10, goalWeight);
            s.setString(11, activeness);
            s.setDouble(12, calorieGoal);
            s.executeUpdate();

            return getUserByUsername(username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public UserProfile validateLogIn(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement s = connection.prepareStatement(query)) {
            s.setString(1, username);
            s.setString(2, password);
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                return new UserProfile(
                    rs.getString("goal"),
                    rs.getString("name"),
                    rs.getString("sex"),
                    rs.getInt("age"),
                    rs.getDouble("height"),
                    rs.getDouble("starting_weight"),
                    rs.getDouble("current_weight"),
                    rs.getDouble("goal_weight"),
                    rs.getString("activeness"),
                    rs.getDouble("calorie_goal"),
                    rs.getString("username"),
                    rs.getString("password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isUsernameTaken(String username) {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (PreparedStatement s = connection.prepareStatement(query)) {
            s.setString(1, username);
            ResultSet rs = s.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true; //Username is TAKEN
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Username NOT TAKEN
    }

    public UserProfile getUserByUsername(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
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

    public boolean updateUser(UserProfile currentUser) {
        String query = "UPDATE users SET password = ?, name = ?, age = ?, sex = ?, height = ?, starting_weight = ?, goal = ?, current_weight = ?, goal_weight = ?, activeness = ?, calori_goal = ? WHERE username = ?";
        try(PreparedStatement s = connection.prepareStatement(query)) {
            s.setString(1, currentUser.getPassword());
            s.setString(2, currentUser.getName());
            s.setInt(3, currentUser.getAge());
            s.setString(4, currentUser.getSex());
            s.setDouble(5, currentUser.getHeight());
            s.setDouble(6, currentUser.getStartingWeight());
            s.setString(7, currentUser.getGoal());
            s.setDouble(8, currentUser.getLatestWeight());
            s.setDouble(9, currentUser.getGoalWeight());
            s.setString(10, currentUser.getActiveness());
            s.setDouble(11, currentUser.getCalorieGoal());
            s.setString(12, currentUser.getUsername());

            int rowsAffected = s.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(String username) {
        String query = "DELETE FROM users WHERE username = ?";
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