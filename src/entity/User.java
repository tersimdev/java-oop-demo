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
    /**
     * Display name of the user.
     */
    private String displayName; // not mentioned in docs but makes sense to have
    /**
     * The NTU network user ID of the user; the part before @ in the user's email
     * address.
     */
    private String userID;
    /**
     * The user's password.
     */
    private String password;
    /**
     * The user's faculty.
     */
    private Faculty faculty;

    /**
     * Constant string to define a default password.
     * Used to compare if it is a new user.
     */
    public final static String defaultPassword = "password";

    /**
     * Default constructor for a user object.
     */
    public User() {
        this.displayName = "User";
        this.userID = "USER";
        this.faculty = Faculty.NULL;
        this.password = defaultPassword;
    }

    /**
     * Constructor for a user object.
     * 
     * @param displayName Display name of the user.
     * @param userID      User ID of the user.
     * @param faculty     Faculty of the user.
     */
    public User(String displayName, String userID, Faculty faculty) {
        this.displayName = displayName;
        this.userID = userID;
        this.faculty = faculty;
        this.password = defaultPassword;
    }

    /**
     * Getter for the display name of the user.
     * 
     * @return The display name of the user.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Getter for the user ID of the user.
     * 
     * @return The user ID of the user.
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Getter for the password of the user.
     * 
     * @return The password of the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Getter for the faculty of the user.
     * 
     * @return The the faculty of the user.
     */
    public Faculty getFaculty() {
        return faculty;
    }

    /**
     * Setter for the password of the user.
     * 
     * @param password The password to be set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Converts the user's display name, user ID, password and faculty into a string
     * in CSV format.
     * 
     * @return A string of comma separated values.
     */
    @Override
    public String toCSVLine() {
        String ret = "";
        ret += displayName + ","
                + userID + ","
                + faculty.toString() + ","
                + password;
        return ret;
    }

    /**
     * Sets the user's display name, user ID, password and faculty based on the
     * information from a csvline.
     * 
     * @param csvLine The string containing all the user's information.
     */
    @Override
    public void fromCSVLine(String csvLine) {
        String[] split = csvLine.split(",");
        if (split.length != User.this.getCSVLineLength()) {
            Log.error("user csvLine is invalid, expected " + User.this.getCSVLineLength() + " but got " + split.length);
            Log.error(csvLine);
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

    /**
     * Gets the length of a csvline containing a user's information.
     * 
     * @return The length of the csvline.
     */
    @Override
    public int getCSVLineLength() {
        return 4;
    }
}
