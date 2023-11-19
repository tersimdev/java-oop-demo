package boundary;

import java.util.HashMap;
import java.util.Map;
import control.CampSystem;
import control.DataStoreSystem;
import control.FeedbackSystem;
import control.LoginSystem;
import control.ReportSystem;
import entity.User;
import entity.Staff;
import entity.Student;
import util.Input;
import util.Log;

/**
 * <p>
 * This class handles ui of the console
 * Implements a simple state machine with a STATE enum to keep track of UI state
 * Each UI state has a respective Menu class
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 19-11-2023
 */
public class ConsoleUI {

    private enum STATE {
        LOGIN_MENU,
        START_MENU,
        STAFF_MENU,
        STUDENT_MENU,
    }

    // state machine attribs
    private Map<STATE, Menu> menuMap;
    private STATE state;
    private boolean stateDirty;// tracks if state needs to be refreshed

    //input class to handle input using Scanner
    private Input input;

    // ui depends on loginsystemm to know state
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

    public void setStateDirty(boolean dirty) {
        this.stateDirty = dirty;
    }

    public User getUser() {
        return loginSystem.getCurrentUser();
    }

    public Input getInput() {
        return input;
    }

    /**
     * Initializes all menus, creates state map.
     * Uses dependency injection to pass systems to menus
     */
    public void init(LoginSystem loginSystem, CampSystem campSystem, FeedbackSystem feedbackSystem,
            ReportSystem reportSystem) {
        this.loginSystem = loginSystem;
        
        input = new Input();
        
        // init menus with dependency injection
        menuMap = new HashMap<>();
        menuMap.put(STATE.LOGIN_MENU, new LoginMenu(this, loginSystem));
        menuMap.put(STATE.START_MENU, new StartMenu(this, loginSystem));
        menuMap.put(STATE.STAFF_MENU, new StaffMenu(this, campSystem, feedbackSystem, reportSystem));
        menuMap.put(STATE.STUDENT_MENU, new StudentMenu(this, campSystem, feedbackSystem, reportSystem));

        Log.printLogo("data/logo.txt");
        Log.println("======================================================================");
        Log.println("Welcome to Camp Application and Management System (CAMS).");
        Log.println("> Made by Team 2: Terence, Ryan, Jon, Zhi Wei\n");
    }

    // returns if should exit app
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

    public void cleanup() {
        input.close();
        DataStoreSystem.getInstance().cleanup();
    }

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
