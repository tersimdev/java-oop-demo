package entity;

import control.LoginSystem;
import util.Log;

/**
 * <p>
 * This is an entity class to represent a user
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 1-11-2023
 */
public class User {
    private String name; //not mentioned in docs but makes sense to have
    private String userID;
    private String password;
    private Faculty faculty;

    public User(String name, String email, String faculty) {
        this.name = name;
        this.password = "password";
        //todo handle errors?
        this.userID = email.split("@")[0];
        this.faculty = Faculty.valueOf(faculty);
    }

    public String getName() { return name; }
    public String getUserID() { return userID; }
    public String getPassword() { return password; }
    public Faculty geFaculty() { return faculty; }

    public void setPassword(String password) {this.password = password;}
}
