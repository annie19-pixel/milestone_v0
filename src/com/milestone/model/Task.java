package com.milestone.model;
import java.sql.Date;
import java.sql.Timestamp;

public class Task {
    private int taskId;
    private int userId;
    private Integer goalId;
    private String title;
    private String description;
    private Date dueDate;
    private boolean isCompleted;
    private Timestamp createdAt;

    //constructor for creating anew task
    public Task(int userId, Integer goalId,String title,
                String description, Date dueDate) {
        this.userId = userId;
        this.goalId = goalId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.isCompleted = false;
    }

    //Constructor for loading from DB
    public Task(int taskId, int userId, Integer goalId,String title,
                String description, Date dueDate, boolean isCompleted, Timestamp createdAt) {
        this.taskId = taskId;
        this.userId = userId;
        this.goalId = goalId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.isCompleted = isCompleted;
        this.createdAt = createdAt;
    }

    //Getters ans Setters
    public int getTaskId() {return taskId;}
    public void setTaskId(int taskId) {this.taskId = taskId;}

    public int getUserId() {return userId;}
    public void setUserId(int userId) {this.userId = userId;}

    public Integer getGoalId() {return goalId;}
    public void setGoalId(Integer goalId) {this.goalId = goalId;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public Date getDueDate() {return dueDate;}
    public void setDueDate(Date dueDate) {this.dueDate = dueDate;}

    public boolean isCompleted() {return isCompleted;}
    public void setCompleted(boolean completed) {this.isCompleted = completed;}

    public Timestamp getCreatedAt() {return createdAt;}
    public void setCreatedAt(Timestamp createdAt) {this.createdAt = createdAt;}
}
