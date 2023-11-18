package entity;

import util.Log;
import util.DataStore.SerializeToCSV;

/**
 * <p>
 * This is an entity class to represent a user
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 1-11-2023
 */
public abstract class User implements SerializeToCSV {
    private String displayName; // not mentioned in docs but makes sense to have
    private String userID;
    private String password;
    private Faculty faculty;

    public final static String defaultPassword = "password";

    public User() {
        this.displayName = "User";
        this.userID = "USER";
        this.faculty = Faculty.NULL;
        this.password = defaultPassword;
    }

    public User(String displayName, String userID, Faculty faculty) {
        this.displayName = displayName;
        this.userID = userID;
        this.faculty = faculty;
        this.password = defaultPassword;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getUserID() {
        return userID;
    }

    public String getPassword() {
        return password;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toCSVLine() {
        String ret = "";
        ret += displayName + ","
                + userID + ","
                + faculty.toString() + ","
                + password;
        return ret;
    }

    @Override
    public void fromCSVLine(String csvLine) {
        String[] split = csvLine.split(",");
        if (split.length != getCSVLineLength()) {
            Log.error("csvLine is invalid");
        } else {
            displayName = split[0].trim();
            userID = split[1].trim().toUpperCase();
            password = split[3].trim();
            try {
                faculty = Faculty.valueOf(split[2]);
            } catch (IllegalArgumentException e) {
                Log.error("invalid faculty, doesnt correspond to enum: " + split[2]);
                faculty = Faculty.NULL; // give it null value
            }
        }
    }

    @Override
    public int getCSVLineLength() {
        return 4;
    }
}
