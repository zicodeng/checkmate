package edu.uw.dengz6.checkmate;

/**
 * Created by Quan Nguyen on 5/21/2017.
 */

public class User {
    public String name;
    public String email;
    public String password;
    public String createdOn;
    public int totalTasks;

    public User() {
        // Empty constructor required for Firebase
    }

    public User(String name, String email, String password, String createdOn){
        this(name, email, password, createdOn, 0);
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdOn = createdOn;
    }

    public User(String name, String email, String password, String createdOn, int totalTasks){
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdOn = createdOn;
        this.totalTasks = totalTasks;
    }
}
