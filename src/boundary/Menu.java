package boundary;

import java.util.HashMap;
import java.util.Map;

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

    /**
     * Interface to represent a function called by menu
     * e.g. calling loginsystem to login
     * Implement this interface to perform some function, 
     * with the menu calling passed as parameter
     */
    protected interface MenuFunctionInterface {
        boolean doFunction(Menu menu); 
    }
    /**
     * Map of user selected menu choice to function to execute
     * Allows for more extensible version of a switch statement 
     */ 
    private Map<Integer, MenuFunctionInterface> functionMap;

    public Menu(ConsoleUI ui) {
        this.ui = ui;
        functionMap = new HashMap<>();
    }

    public ConsoleUI getUi() {
        return ui;
    }

    /**
     * helper function to get input for a choice 0 exit, <0 invalid
     * @param lower the lower bound of choice number
     * @param upper the upper bound of choice number
     * @param exit the choice at which to exit
     * @return 0 for exit, <0 for invalid, else returns choice chosen
     */
    protected int getChoice(int lower, int upper, int exit) {
        int choice = ui.getInput().getInt("Enter choice: ");
        if (choice == exit) {
            return 0;
        } else if (choice < lower || choice > upper) {
            Log.println("Invalid choice, please input again.");
            return -1;
        } else
            return choice;
    }

    /**
     * function to add a menu function to functionMap
     * @param choice choice integer to map to
     * @param func menu function to call
     * 
     * @implNote
     * <p>
     * The following explanation is for how to use this function:
     * There are 3 ways to implement the MenuFunctionInterface
     * other than simply creating a new concrete class file.
     * </p>
     * <ol>
     * <li>Create the class inline.
     * <p><code>
     *      addMenuFunction(choice, new MenuFunctionInterface() {
     *          public void doFunction(Menu menu) {myFunc();}
     *      });
     * </code></p></li>
     * <li>Use lambda / arrow notation.
     * <p><code>
     *      addMenuFunction(choice, (menu) -> myFunc(menu));
     * </code></p></li>
     * <li>Use method reference.
     * Note this only works if myFunc matches signature
     * of doFunction() in the interface.
     * <p><code>
     *      addMenuFunction(choice, this::myFunc); 
     * </code></p></li>
     * </ol>
     */
    protected void addMenuFunction(int choice, MenuFunctionInterface func) {
        functionMap.put(choice, func);
    }
    
    /**
     * function to remove a menu function from functionMap
     * @param choice menu function to remove based on choice
     */
    protected void removeMenuFunction(int choice) {
        if (functionMap.containsKey(choice))
            functionMap.remove(choice);
    }

    /**
     * Call to run a menu function from functionMap.
     * Assumes choice is valid.
     * Use getChoice to ensure invalid choices are caught.
     * Depends on initialized function map that correspons to choices.
     * @param choice the choice chosen from e.g. getChoice
     * @return returns whether this menu should exit 
     */
    protected boolean runMenuFunction(int choice) {
        MenuFunctionInterface menuFunc = functionMap.get(choice);
        if (menuFunc == null) {
            Log.error("function map likely faulty, choice " + choice + " from"+ this);
            return true;
        }
        //else not null so do function
        return menuFunc.doFunction(this);
    }

    public abstract boolean show();
}
