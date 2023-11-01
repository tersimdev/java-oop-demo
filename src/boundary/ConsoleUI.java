package boundary;

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
    public void init() {
        Log.enableLogging(true);
        Input.getInstance(); //init input
        Log.println("Welcome to Camp Application and Management System (CAMS).");
        Log.println("  Made by Team 2: Terence, Ryan, Jon, Zhi Wei");
    }
    public boolean run() {
        showMenu();
        return true;
    }
    public void cleanup() {
        Input.getInstance().close();
    }

    private void showMenu() {
        Log.println("(1) Do something");
    }
}
