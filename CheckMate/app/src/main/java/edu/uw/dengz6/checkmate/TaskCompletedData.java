package edu.uw.dengz6.checkmate;

/**
 * Created by Leon on 5/29/17.
 */

public class TaskCompletedData {
    public String userName;
    public int totalCompletedTasks;
    public String since;

    public TaskCompletedData() {

    }

    public TaskCompletedData(String userName, int totalCompletedTasks, String since) {
        this.userName = userName;
        this.totalCompletedTasks = totalCompletedTasks;
        this.since = since;
    }
}
