package boundary;

import control.LoginSystem;
import entity.User;
import entity.Faculty;
import entity.Staff;
import entity.Student;
import util.Input;
import util.Log;

/**
 * <p>
 * This class handles ui of the console
 * Implements a simple state machine with a STATE enum to keep track of UI state
 * e.g. printing out the menu, asking user for input
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 1-11-2023
 */
public class ConsoleUI {

    private enum STATE {
        LOGIN,
        START_MENU,
        STAFF_MENU,
        STUDENT_MENU,
    }

    private STATE state = STATE.LOGIN;
    private User user = null;
    private boolean stateDirty = false; // tracks if state needs to be refreshed

    public void init() {
        Log.enableLogging(true);
        Input.getInstance(); // init input
        Log.println("=========================================================");
        Log.println("Welcome to Camp Application and Management System (CAMS).");
        Log.println(">Made by Team 2: Terence, Ryan, Jon, Zhi Wei");
    }

    // returns if should exit app
    public boolean run() {
        // return implicitly does break
        boolean shouldExit = false;
        switch (state) {
            case LOGIN:
                shouldExit = showLoginMenu();
            case START_MENU:
                shouldExit = showStartMenu(user);
            case STAFF_MENU:
                shouldExit = showStaffMenu((Staff) user);
            case STUDENT_MENU:
                shouldExit = showStudentMenu((Student) user);
            default:
                Log.error("unknown state, exiting...");
                shouldExit = true;
        }
        if (stateDirty)
            switchState();
        return shouldExit;
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
        while (choice < 0) {
            choice = getChoice(1, 2, 3);
            if (choice == 0) {
                Log.println("Exiting app...");
                return true;
            }
        }

        String usernameStr = Input.getInstance().getLine("Enter username: ", true);
        // todo login things
        // this.user = LoginSystem.getInstance().login(usernameStr, passwordStr);
        this.user = new User(usernameStr, "TEST", Faculty.SCSE); // TEMP
        if (user != null)
            stateDirty = true;
        return false;
    }

    private boolean showStartMenu(User user) {
        Log.println("===Welcome, " + user.getDisplayName() + "===");
        Log.println("(1) Change Password");
        Log.println("(2) View App Commands");
        Log.println("(3) Logout");
        int choice = -1;
        while (choice < 0) {
            choice = getChoice(1, 2, 3);
            if (choice == 0) {
                user = null; // destruct user
                stateDirty = true;
                return false; // dont exit
            }
        }

        switch (choice) {
            case 1:
                // todo change password
                break;
            case 2:
                stateDirty = true;
        }
        return false;
    }

    private boolean showStudentMenu(Student student) {
        return false;
    }

    private boolean showStaffMenu(Staff staff) {
        return false;
    }

    // 0 exit, <0 invalid
    private int getChoice(int lower, int upper, int exit) {
        int choice = Input.getInstance().getInt("Enter choice: ");
        if (choice == exit) {
            return 0;
        } else if (choice < lower || choice > upper) {
            Log.println("Invalid choice, please input again.");
            return -1;
        } else
            return choice;
    }

    private void switchState() {
        switch (state) {
            case LOGIN:
                if (user != null) // check just incase
                    state = STATE.START_MENU;
                break;
            case START_MENU:
                if (user instanceof Staff)
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
                //UNKNOWN STATE!
                break;
        }
        stateDirty = false;
    }
}
