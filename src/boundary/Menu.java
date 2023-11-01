package boundary;

import util.Input;
import util.Log;

public abstract class Menu {
    protected ConsoleUI ui;

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
