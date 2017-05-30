package edu.uw.dengz6.checkmate;

public class TaskData {
    public String title;
    public String detail;
    public String dueOn;
    public String createdOn;
    public String assigner;
    public String assignee;
    public boolean isCompleted;
    public String taskID;

    public TaskData() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public TaskData(String title, String detail, String dueOn, String createdOn, String assigner, String assignee, boolean isCompleted, String taskID) {
        this.title = title;
        this.detail = detail;
        this.dueOn = dueOn;
        this.createdOn = createdOn;
        this.assigner = assigner;
        this.assignee = assignee;
        this.isCompleted = isCompleted;
        this.taskID = taskID;
    }
}
