package edu.uw.dengz6.checkmate;

public class TaskData {
    public String title;
    public String details;
    public String dueOn;
    public String createdOn;
    public String assigner;
    public String assignee;

    public TaskData(String title, String details, String dueOn, String createdOn, String assigner, String assignee) {
        this.title = title;
        this.details = details;
        this.dueOn = dueOn;
        this.createdOn = createdOn;
        this.assigner = assigner;
        this.assignee = assignee;
    }
}
