package boundary;

import control.LoginSystem;
import entity.User;
import util.Log;

/**
 * <p>
 * Class defining the start menu.
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 21-11-2023
 */
public class StartMenu extends Menu {

    /**
     * DI of login system
     * Used to change password
     */
    private final LoginSystem loginSystem;

    /**
     * Uses dependency injection for params.
     * Menu function map is initialised here.
     * 
     * @param ui
     * @param loginSystem
     */
    public StartMenu(ConsoleUI ui, LoginSystem loginSystem) {
        super(ui);
        this.loginSystem = loginSystem;

        addMenuFunction(1, this::changeUserPassword);
        addMenuFunction(2, this::viewCommands);
    }

    /**
     * Polymorphisesd function to print out Ui for menu,
     * and handle calling of appropriate functions.
     * Uses getChoice and runFunctionMap.
     * 
     * @return returns whether should exit app
     */
    @Override
    public boolean show() {
        User user = ui.getUser();
        Log.println("===Welcome, " + user.getDisplayName() + "===");
        // check if new user, force to change password!
        boolean newUser = user.getPassword().equals(User.defaultPassword);

        if (!newUser) {
            Log.println("(1) Change Password");
            Log.println("(2) View App Commands");
            Log.println("(3) Logout");
        }
        int choice = -1;
        if (newUser) {
            Log.println("New user detected, please change your password.");
            choice = 1; // make menu choice
        }
        while (choice < 0) {
            choice = getChoice(1, 2, 3);
            if (choice == 0) {
                loginSystem.logout();
                ui.setStateDirty(true);
                return false; // dont exit
            }
        }
        return runMenuFunction(choice);
    }

    /**
     * Function change user password.
     * User will be logged out after changing password.
     * @param menu start menu
     * @return returns shouldExit app, always false
     */
    private boolean changeUserPassword(Menu menu) {
        String oldPasswordStr = ui.getInput().getLine("Enter Old Password: ").trim();
        String newPasswordStr = ui.getInput().getLine("Enter New Password: ").trim();
        if (oldPasswordStr.equals(newPasswordStr)) {
            Log.println("Can't set to same password.");
            return false;
        }
        boolean success = loginSystem.changeUserPassword(newPasswordStr);
        if (!success) {
            Log.println("Password change failed.");
            return false;
        } else {
            // log out user
            Log.println("Please login with new password.");
            loginSystem.logout();
            ui.setStateDirty(true);
            // return false;
        }
        return false;
    }

    /**
     * Function to view app commands.
     * Simply sets state to dirty for state machine to change menus.
     * @param menu start menu
     * @return returns shouldExit app, always false
     */
    private boolean viewCommands(Menu menu) {
        ui.setStateDirty(true);
        return false;
    }
}
