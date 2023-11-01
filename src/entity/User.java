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
    private String userID;
    private String password = "password";
    private Faculty faculty;

    public boolean changePassword(String oldPassword, String newPassword) {
        if (oldPassword == newPassword) {
            Log.error("New password same as old password!");
            return false;
        }
        if (LoginSystem.getInstance().checkValidPassword(newPassword)) {
            oldPassword = newPassword;
            Log.info("Password changed.");
            return true;
        }
        return false;
    }
}
