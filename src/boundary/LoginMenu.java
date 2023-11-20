package boundary;

import control.LoginSystem;
import entity.User;
import util.Log;

/**
 * <p>
 * Class defining the login menu
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 21-11-2023
 */
public class LoginMenu extends Menu {

    /**
     * DI of login system
     */
    private final LoginSystem loginSystem;

    /**
     * Uses dependency injection for params.
     * Menu function map is initialised here.
     * @param ui console ui object
     * @param loginSystem loginsystem object
     */
    public LoginMenu(ConsoleUI ui, LoginSystem loginSystem) {
        super(ui);
        this.loginSystem = loginSystem;

        // create function map corresponding to ui
        addMenuFunction(1, this::loginStudent);
        addMenuFunction(2, this::loginStaff);
    }

    /**
     * Polymorphisesd function to print out Ui for menu, 
     * and handle calling of appropriate functions.
     * Uses getChoice and runFunctionMap.
     * @return returns whether should exit app
     */
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
        // run specific login functions
        return runMenuFunction(choice);
    }

    /**
     * Function to login staff
     * @param menu login menu
     * @return returns shouldExit app, always false
     */
    private boolean loginStaff(Menu menu) {
        String[] userDetails = getUserDetails();
        User user = loginSystem.loginStaff(userDetails[0], userDetails[1]);
        handleLoginResult(user);
        return false;
    }

    /**
     * Function to login student
     * @param menu login menu
     * @return returns shouldExit app, always false
     */
    private boolean loginStudent(Menu menu) {
        String[] userDetails = getUserDetails();
        User user = loginSystem.loginStudent(userDetails[0], userDetails[1]);
        handleLoginResult(user);
        return false;
    }

    /**
     * prompts user for username and password
     * @return a pair of strings, first being username and second being password
     */
    private String[] getUserDetails() {
        String usernameStr = ui.getInput().getLine("Enter User ID: ").trim().toUpperCase();
        String passwordStr = ui.getInput().getLine("Enter Password: ").trim();
        return new String[] { usernameStr, passwordStr };
    }

    /**
     * checks if user logged in successfully
     * if logged in sets ui state dirty
     * else prints error message
     * @param user the user object to check
     */
    private void handleLoginResult(User user) {
        if (user == null) {
            Log.println("Invalid user ID or password.");
            return; //state remains unchanged
        } else if (user != null)
            ui.setStateDirty(true);
    }
}
