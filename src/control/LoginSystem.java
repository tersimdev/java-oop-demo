package control;

import util.Log;
import entity.User;

/**
 * <p>
 * A singleton class to handle login logic
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 1-11-2023
 */
public class LoginSystem {
    private static LoginSystem instance = null;

    private LoginSystem() {
    }

    public static LoginSystem getInstance() {
        if (instance == null)
            instance = new LoginSystem();
        return instance;
    }

    private final static int MIN_PASSWORD_LEN = 8;

    public User login(String userID, String password) {
        // find user with username, then check for correct password
        User user = DataStoreSystem.getInstance().queryUsers(userID);
        if (user != null) {
            if (user.getPassword().equals(password))
                return user;
        }
        return null;
    }

    public boolean changeUserPassword(User user, String newPassword) {
        String oldPassword = user.getPassword();
        if (oldPassword.equals(newPassword)) {
            Log.println("Error! New password same as old password!");
            return false;
        }
        if (!LoginSystem.getInstance().checkValidPassword(newPassword)) {
            Log.println("Password does not meet required length!");
            return false;
        }
        user.setPassword(newPassword);
        DataStoreSystem.getInstance().updateUser(user.getUserID(), newPassword);
        Log.println("Password changed.");
        return true;
    }

    private boolean checkValidPassword(String password) {
        return password.length() >= MIN_PASSWORD_LEN;
    }
}
