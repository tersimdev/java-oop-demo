package control;

import entity.User;
import util.Log;

/**
 * <p>
 * Handles login logic
 * Stores current user, null if none
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 19-11-2023
 */
public class LoginSystem {

    private DataStoreSystem dataStoreSystem;
    private User currentUser;
    private final static int MIN_PASSWORD_LEN = 8;

    public LoginSystem(DataStoreSystem dataStoreSystem) {
        currentUser = null;
        this.dataStoreSystem = dataStoreSystem;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public User loginStaff(String userID, String password) {
        Log.info("logging in staff " + userID);
        //find user in staff datastore
        User user = dataStoreSystem.queryStaff(userID);
        return loginUser(user, password);
    }

    public User loginStudent(String userID, String password) {
        Log.info("logging in student " + userID);
        //find user in student datastore
        User user = dataStoreSystem.queryStudent(userID);
        return loginUser(user, password);
    }

    /**
     * checks password same then sets current user
     * returns current user, else returns null
     */
    private User loginUser(User user, String password) {
        currentUser = null;
        if (user != null) {
            if (user.getPassword().equals(password))
                currentUser = user;
        }
        return currentUser;
    }

    public void logout() {
        if (currentUser != null)
            Log.info("logging out " + currentUser.getUserID());
        currentUser = null;
    }

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
        dataStoreSystem.updateUserPassword(currentUser.getUserID(), newPassword);
        Log.println("Password changed.");
        return true;
    }

    private boolean checkValidPassword(String password) {
        return password.length() >= MIN_PASSWORD_LEN;
    }
}
