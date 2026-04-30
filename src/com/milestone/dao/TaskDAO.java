package com.milestone.dao;

import com.milestone.model.Task;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {
    public boolean addTask(Task task) {
        String sql = "INSERT INTO tasks (user_id, goal_id, title, description, due_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {
            s.setInt(1, task.getUserId());
            if (task.getGoalId() != null)
                s.setInt(2, task.getGoalId());
            else
                s.setNull(2, Types.INTEGER); // goal_id is optional
            s.setString(3, task.getTitle());
            s.setString(4, task.getDescription());
            s.setDate(5, task.getDueDate());
            return s.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Task> getTasksByUser(int userId) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE user_id = ? ORDER BY due_date ASC";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {
            s.setInt(1, userId);
            ResultSet rs = s.executeQuery();
            while (rs.next()) {
                int goalIdVal = rs.getInt("goal_id");
                Integer goalId = rs.wasNull() ? null : goalIdVal;
                tasks.add(new Task(
                        rs.getInt("task_id"),
                        rs.getInt("user_id"),
                        goalId,
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getDate("due_date"),
                        rs.getBoolean("is_completed"),
                        rs.getTimestamp("created_at")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public boolean markComplete(int taskId) {
        String sql = "UPDATE tasks SET is_completed = TRUE WHERE task_id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {
            s.setInt(1, taskId);
            return s.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean markIncomplete(int taskId) {
        String sql = "UPDATE tasks SET is_completed = FALSE WHERE task_id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {
            s.setInt(1, taskId);
            return s.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean deleteTask(int taskId) {
        String sql = "DELETE FROM tasks WHERE task_id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {
            s.setInt(1, taskId);
            return s.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}
