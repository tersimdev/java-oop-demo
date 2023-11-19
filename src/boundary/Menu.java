package boundary;

import util.Input;
import util.Log;

/**
 * <p>
 * Abstract class to define a menu class
 * A menu class is responsible for showing the ui for a specific menu
 * It also has a helper function to help with menu input
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 1-11-2023
 */
public abstract class Menu {
    protected final ConsoleUI ui;

    public Menu(ConsoleUI ui) {
        this.ui = ui;
    }

    // helper to get input for a choice 0 exit, <0 invalid
    protected int getChoice(int lower, int upper, int exit) {
        int choice = Input.getInstance().getInt("Enter choice: ");
        if (choice == exit) {
            return 0;
        } else if (choice < lower || choice > upper) {
            Log.println("Invalid choice, please input again.");
            return -1;
        } else
            return choice;
    }

    public abstract boolean show();
}
