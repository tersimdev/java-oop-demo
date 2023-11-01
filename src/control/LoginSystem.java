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

    public boolean login(String userID, String password) {
        // find user with username, then check for correct password
        User user = DataStoreSystem.getInstance().queryUsers(userID);
        if (user != null)
            return (user.getPassword().equals(password));
        return false;
    }

    public void changeUserPassword(User user, String newPassword) {
        String oldPassword = user.getPassword();
        if (oldPassword == newPassword) {
            Log.println("Error! New password same as old password!");
        }
        if (LoginSystem.getInstance().checkValidPassword(newPassword)) {
            user.setPassword(newPassword);
            Log.println("Password changed.");
        }
    }

    private boolean checkValidPassword(String password) {
        return password.length() < MIN_PASSWORD_LEN;
    }
}
