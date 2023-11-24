package control;

import entity.User;
import util.Log;

/**
 * <p>
 * Handles login logic.
 * Stores current user, null if none.
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 19-11-2023
 */
public class LoginSystem {

    private DataStoreSystem dataStoreSystem;
    /**
     * User object of the current user.
     */
    private User currentUser;
    private final static int MIN_PASSWORD_LEN = 8;

    /**
     * Constructor for a log in system.
     * 
     * @param dataStoreSystem A dataStore object.
     */
    public LoginSystem(DataStoreSystem dataStoreSystem) {
        currentUser = null;
        this.dataStoreSystem = dataStoreSystem;
    }

    /**
     * Gets the current user.
     * 
     * @return Returns user object of the current user.
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Logs in a staff by calling <code>loginUser</code>.
     * 
     * @param userID   The user ID of the staff logging in.
     * @param password The password of the staff logging in.
     * @return The user object of the staff logging in.
     */
    public User loginStaff(String userID, String password) {
        Log.info("logging in staff " + userID);
        // find user in staff datastore
        User user = dataStoreSystem.getUserDataStoreSubSystem().queryStaff(userID);
        return loginUser(user, password);
    }

    /**
     * Logs in a student by calling <code>loginUser</code>
     * 
     * @param userID   The user ID of the staff logging in.
     * @param password The password of the staff logging in.
     * @return The user object of the staff logging in.
     */
    public User loginStudent(String userID, String password) {
        Log.info("logging in student " + userID);
        // find user in student datastore
        User user = dataStoreSystem.getUserDataStoreSubSystem().queryStudent(userID);
        return loginUser(user, password);
    }

    /**
     * Checks if password is correct then sets current user
     * 
     * @param user     the user objecy to login
     * @param password the password entered
     * @return current user or null
     */
    private User loginUser(User user, String password) {
        currentUser = null;
        if (user != null) {
            if (user.getPassword().equals(password))
                currentUser = user;
        }
        return currentUser;
    }

    /**
     * Logs out the current user by setting <code>currentUser</code> to null.
     */
    public void logout() {
        if (currentUser != null)
            Log.info("logging out " + currentUser.getUserID());
        currentUser = null;
    }

    /**
     * Changes the current user's password.
     * 
     * @param newPassword New password to be set.
     * @return Returns true if successful.
     */
    public boolean changeUserPassword(String newPassword) {
        String oldPassword = currentUser.getPassword();
        if (oldPassword.equals(newPassword)) {
            Log.println("Error! New password same as old password!");
            return false;
        }
        if (!checkValidPassword(newPassword)) {
            Log.println("Password does not meet required length!");
            return false;
        }
        currentUser.setPassword(newPassword);
        dataStoreSystem.getUserDataStoreSubSystem().updateUserPassword(currentUser.getUserID(), newPassword);
        Log.println("Password changed.");
        return true;
    }

    /**
     * Checks if a password is longer than a minimum length.
     * 
     * @param password The password to be checked.
     * @return Returns true if the password is longer than the minimum length.
     */
    private boolean checkValidPassword(String password) {
        return password.length() >= MIN_PASSWORD_LEN;
    }
}
