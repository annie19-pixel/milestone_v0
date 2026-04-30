package com.milestone.dao;

import com.milestone.model.Goal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GoalDAO {
    public boolean addGoal(Goal goal) {
        String sql = "INSERT INTO goals (user_id, title, description, deadline) VALUES (?, ?, ?, ?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {
            s.setInt(1, goal.getUserId());
            s.setString(2, goal.getTitle());
            s.setString(3, goal.getDescription());
            s.setDate(4, goal.getDeadline()); // null is fine here
            return s.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Goal> getGoalsByUser(int userId) {
        List<Goal> goals = new ArrayList<>();
        String sql = "SELECT * FROM goals WHERE user_id = ? ORDER BY deadline ASC";
        try (Connection c= DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {
            s.setInt(1, userId);
            ResultSet rs = s.executeQuery();
            while (rs.next()) {
                goals.add(new Goal(
                        rs.getInt("goal_id"),
                        rs.getInt("user_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getDate("deadline"),
                        rs.getString("status"),
                        rs.getTimestamp("created_at")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return goals;
    }

    public boolean deleteGoal(int goalId) {
        String sql = "DELETE FROM goals WHERE goal_id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {
            s.setInt(1, goalId);
            return s.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}



