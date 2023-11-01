package boundary;

import control.LoginSystem;
import util.Input;
import util.Log;

/**
 * <p>
 * This class handles ui of the console
 * e.g. printing out the menu, asking user for input
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 1-11-2023
 */
public class ConsoleUI {

    private boolean loggedIn = false;;

    public void init() {
        Log.enableLogging(true);
        Input.getInstance(); // init input
        Log.println("=========================================================");
        Log.println("Welcome to Camp Application and Management System (CAMS).");
        Log.println(">Made by Team 2: Terence, Ryan, Jon, Zhi Wei");
    }

    // returns if should exit app
    public boolean run() {
        if (!loggedIn)
            return showLoginMenu();

        return false;
    }

    public void cleanup() {
        Input.getInstance().close();
    }

    private boolean showLoginMenu() {
        Log.println("===Login to App===");
        Log.println("(1) Login as Student");
        Log.println("(2) Login as Staff");
        Log.println("(3) Exit");
        int choice = -1;
        while(choice < 0) {
            choice = getChoice(1,2,3);
            if (choice == 0)
                return true;   
        }

        Log.println("Enter usename:");
        String usernameStr = Input.getInstance().getLine();
        Log.println("Enter password:");
        String passwordStr = Input.getInstance().getLine();

        //LoginSystem.getInstance().login();
        loggedIn = true;

        return false;
    }

    // 0 exit, <0 invalid
    private int getChoice(int lower, int upper, int exit) {
        int choice = Input.getInstance().getInt();
        if (choice == exit) {
            Log.println("Exiting app...");
            return 0;
        } else if (choice < lower || choice > upper) {
            Log.println("Invalid choice, please input again.");
            return -1;
        } else
            return choice;
    }
}
