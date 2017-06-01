package edu.uw.dengz6.checkmate;

/**
 * Created by Quan Nguyen on 5/21/2017.
 */

public class User {
    public String name;
    public String email;
    public String password;
    public String createdOn;
    public int tasksAssigned;
    public int tasksCompleted;

    public User() {
        // Empty constructor required for Firebase
    }

    public User(String name, String email, String password, String createdOn){
        this(name, email, password, createdOn, 0, 0);
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdOn = createdOn;
    }

    public User(String name, String email, String password, String createdOn, int tasksAssigned, int tasksCompleted ){
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdOn = createdOn;
        this.tasksAssigned = tasksAssigned;
        this.tasksCompleted = tasksCompleted;
    }
}
