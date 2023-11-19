package boundary;

import control.LoginSystem;
import entity.User;
import util.Log;

/**
 * <p>
 * Class defining the login menu functions
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 1-11-2023
 */
public class LoginMenu extends Menu {

    private final LoginSystem loginSystem;
    public LoginMenu(ConsoleUI ui, LoginSystem loginSystem) {
        super(ui);
        this.loginSystem = loginSystem;
    }

    @Override
    public boolean show() {
        Log.println("===Login to App===");
        Log.println("(1) Login as Student");
        Log.println("(2) Login as Staff");
        Log.println("(3) Exit");
        int choice = -1;
        while (choice < 0) {
            choice = getChoice(1, 2, 3);
            if (choice == 0) {
                Log.println("Exiting app...");
                return true;
            }
        }

        String usernameStr = ui.getInput().getLine("Enter User ID: ").trim().toUpperCase();
        String passwordStr = ui.getInput().getLine("Enter Password: ").trim();
        User user = loginSystem.login(usernameStr, passwordStr);
        if (user == null) {
            Log.println("Invalid user ID or password.");
            return false; // kick user back to menu selection
        } else if (user != null)
            ui.setStateDirty(true);
        return false;
    }
    
}
