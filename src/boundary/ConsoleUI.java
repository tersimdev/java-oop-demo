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
 * each UI state has a respective Menu class
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 1-11-2023
 */
public class ConsoleUI {

    private enum STATE {
        LOGIN_MENU,
        START_MENU,
        STAFF_MENU,
        STUDENT_MENU,
    }

    private Map<STATE, Menu> menuMap;

    private STATE state = STATE.LOGIN_MENU;
    private User user = null;
    private boolean stateDirty = false; // tracks if state needs to be refreshed

    public void init() {
        Log.enableLogging(true); // enable this for dev work

        // init singletons
        Input.getInstance();
        DataStoreSystem.getInstance();
        LoginSystem.getInstance();
        CampSystem.getInstance();
        FeedbackSystem.getInstance();
        ReportSystem.getInstance();

        // init menus
        menuMap = new HashMap<>();
        menuMap.put(STATE.LOGIN_MENU, new LoginMenu(this));
        menuMap.put(STATE.START_MENU, new StartMenu(this));
        menuMap.put(STATE.STAFF_MENU, new StaffMenu(this));
        menuMap.put(STATE.STUDENT_MENU, new StudentMenu(this));

        Log.printLogo("data/logo.txt");
        Log.println("======================================================================");
        Log.println("Welcome to Camp Application and Management System (CAMS).");
        Log.println("> Made by Team 2: Terence, Ryan, Jon, Zhi Wei\n");
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setStateDirty(boolean dirty) {
        this.stateDirty = dirty;
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
        Input.getInstance().close();
        DataStoreSystem.getInstance().cleanup();
    }

    private void switchState() {
        switch (state) {
            case LOGIN_MENU:
                if (user != null) // check just incase
                    state = STATE.START_MENU;
                break;
            case START_MENU:
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
