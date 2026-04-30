package com.milestone.model;
import java.sql.Date;
import java.sql.Timestamp;

public class Goal {
    //Attributes
    private int goalId;
    private int userId;
    private String title;
    private String description;
    private Date deadline;
    private String status;
    private Timestamp createdAt;

    //Constructor for creating new goal
    public Goal(int userId, String title, String description, Date deadline) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.status = "Active";
    }

    //constructor for loading goal from the DB the id is known
    public Goal(int goalId, int userId, String title, String description,
                Date deadline, String status, Timestamp createdAt) {
        this.goalId = goalId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.status = status;
        this.createdAt = createdAt;
    }

    //Getter and Setters
    public int getGoalId() {return goalId;}
    public void setGoalId(int goalId) {this.goalId = goalId;}

    public int getUserId() {return userId;}
    public void setUserId(int userId) {this.userId = userId;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public Date getDeadline() {return deadline;}
    public void setDeadline(Date deadline) {this.deadline = deadline;}

    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}

    public Timestamp getCreatedAt() {return createdAt;}
    public void setCreatedAt(Timestamp createdAt) {this.createdAt = createdAt;}
}
