package boundary;

import control.LoginSystem;
import util.Input;
import util.Log;
import entity.User;

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

    public LoginMenu(ConsoleUI ui) {
        super(ui);
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

        String usernameStr = Input.getInstance().getLine("Enter User ID: ").trim().toUpperCase();
        String passwordStr = Input.getInstance().getLine("Enter Password: ").trim();
        User user = LoginSystem.getInstance().login(usernameStr, passwordStr);
        ui.setUser(user);
        if (user == null) {
            Log.println("Invalid user ID or password.");
            return false; // kick user back to menu selection
        } else if (user != null)
            ui.setStateDirty(true);
        return false;
    }
    
}
