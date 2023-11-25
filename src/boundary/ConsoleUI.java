package boundary;

import java.util.HashMap;
import java.util.Map;

import control.CampSystem;
import control.EnquirySystem;
import control.LoginSystem;
import control.ReportSystem;
import control.SuggestionSystem;
import entity.Staff;
import entity.Student;
import entity.User;
import util.Input;
import util.Log;

/**
 * <p>
 * This class handles ui of the console.
 * Implements a simple state machine with a STATE enum to keep track of UI
 * state.
 * Each UI state has a respective Menu class.
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 19-11-2023
 */
public class ConsoleUI {

    /**
     * Enum class used to represent
     * states in state machine.
     */
    private enum STATE {
        /**
         * LOGIN_MENU
         */
        LOGIN_MENU,
        /**
         * START_MENU
         */
        START_MENU,
        /**
         * STAFF_MENU
         */
        STAFF_MENU,
        /**
         * STUDENT_MENU
         */
        STUDENT_MENU,
    }

    // state machine attribs
    /**
     * A map containing all states and their corresponding menu
     */
    private Map<STATE, Menu> menuMap;
    /**
     * Represents the current state
     */
    private STATE state;
    /**
     * Tracks if state needs to be refreshed.
     * Dirty means to check to change states.
     */
    private boolean stateDirty;

    /**
     * Input class to handle input using Scanner
     */
    private Input input;

    /**
     * DI for login system
     * ui depends on loginsystemm to know state
     */
    private LoginSystem loginSystem;

    /**
     * Simple constructor to set default values. Call init() to initialize logic.
     */
    public ConsoleUI() {
        menuMap = null;
        state = STATE.LOGIN_MENU;
        stateDirty = false;
        input = null;
    }

    /**
     * Setter for stateDirty.
     * 
     * @param dirty whether state is dirty
     */
    public void setStateDirty(boolean dirty) {
        this.stateDirty = dirty;
    }

    /**
     * Getter for user object.
     * 
     * @return user object
     */
    public User getUser() {
        return loginSystem.getCurrentUser();
    }

    /**
     * Getter for input object.
     * Call this to do user input.
     * 
     * @return input object
     */
    public Input getInput() {
        return input;
    }

    /**
     * Initializes all menus, creates state map.
     * Uses dependency injection to pass systems to menus.
     * 
     * @param loginSystem      loginSystem object
     * @param campSystem       campSystem object
     * @param enquirySystem    enquirySystem object
     * @param suggestionSystem suggestionSystem object
     * @param reportSystem     reportSystem object
     */
    public void init(LoginSystem loginSystem, CampSystem campSystem, EnquirySystem enquirySystem,
            SuggestionSystem suggestionSystem,
            ReportSystem reportSystem) {
        this.loginSystem = loginSystem;

        input = new Input(System.in);

        // init menus with dependency injection
        menuMap = new HashMap<>();
        menuMap.put(STATE.LOGIN_MENU, new LoginMenu(this, loginSystem));
        menuMap.put(STATE.START_MENU, new StartMenu(this, loginSystem));
        menuMap.put(STATE.STAFF_MENU, new StaffMenu(this, campSystem, enquirySystem, suggestionSystem, reportSystem));
        menuMap.put(STATE.STUDENT_MENU,
                new StudentMenu(this, campSystem, enquirySystem, suggestionSystem, reportSystem));

        Log.printLogo("data/logo.txt");
        Log.println("=========================================================================================");
        Log.println("Welcome to Camp Application and Management System (CAMS).");
        Log.println("> Made by Team 2: Terence, Ryan, Jon, Zhi Wei\n");
    }

    /**
     * Main update loop of app.
     * Uses state machine design.
     * Checks if state is dirty and calls switchStates
     * 
     * @return returns if should exit app
     */
    public boolean run() {
        // return implicitly does break
        boolean shouldExit = false;
        Menu menu = menuMap.get(state);
        if (menu == null) {
            Log.error("unknown state, exiting...");
            shouldExit = true;
        } else
            shouldExit = menu.show();
        if (stateDirty)
            switchState();
        return shouldExit;
    }

    /**
     * Function to cleanup systems
     */
    public void cleanup() {
        input.close();
    }

    /**
     * Function to change states,
     * by setting state variable and checking preconditions
     */
    private void switchState() {
        switch (state) {
            case LOGIN_MENU:
                if (loginSystem.getCurrentUser() != null) // check just incase
                    state = STATE.START_MENU;
                break;
            case START_MENU:
                User user = loginSystem.getCurrentUser();
                if (user == null)
                    state = STATE.LOGIN_MENU;
                else if (user instanceof Staff)
                    state = STATE.STAFF_MENU;
                else if (user instanceof Student)
                    state = STATE.STUDENT_MENU;
                break;
            case STAFF_MENU:
                state = STATE.START_MENU;
                break;
            case STUDENT_MENU:
                state = STATE.START_MENU;
                break;
            default:
                // UNKNOWN STATE!
                break;
        }
        stateDirty = false;
    }
}
