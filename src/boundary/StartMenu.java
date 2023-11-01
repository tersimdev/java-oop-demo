package boundary;

import control.LoginSystem;
import util.Input;
import util.Log;
import entity.User;

/**
 * <p>
 * Class defining the start menu functions
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 1-11-2023
 */
public class StartMenu extends Menu {

    public StartMenu(ConsoleUI ui) {
        super(ui);
    }

    @Override
    public boolean show() {
        Log.println("===Welcome, " + ui.getUser().getDisplayName() + "===");
        // check if new user, force to change password!
        boolean newUser = ui.getUser().getPassword().equals(User.defaultPassword);

        if (!newUser) {
            Log.println("(1) Change Password");
            Log.println("(2) View App Commands");
            Log.println("(3) Logout");
        }
        int choice = -1;
        if (newUser) {
            Log.println("New user detected, please change your password.");
            choice = 1; //make menu choice
        }
        while (choice < 0) {
            choice = getChoice(1, 2, 3);
            if (choice == 0) {
                ui.setUser(null); // destruct user
                ui.setStateDirty(true);
                return false; // dont exit
            }
        }

        switch (choice) {
            case 1:
                String oldPasswordStr = Input.getInstance().getLine("Enter Old Password: ").trim();
                String newPasswordStr = Input.getInstance().getLine("Enter New Password: ").trim();
                User user = ui.getUser();
                if (!oldPasswordStr.equals(user.getPassword())) {
                    Log.println("Password is wrong, please try again.");
                    return false;
                }
                boolean success = LoginSystem.getInstance().changeUserPassword(user, newPasswordStr);
                if (!success) {
                    Log.println("Password change failed.");
                    return false;
                } else {
                    // log out user
                    ui.setUser(null);
                    ui.setStateDirty(true);
                    // return false;
                }
                break;
            case 2:
                ui.setStateDirty(true);
                break;
        }
        return false;
    }

}
