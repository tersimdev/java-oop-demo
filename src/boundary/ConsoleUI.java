package boundary;

import control.CampSystem;
import control.DataStoreSystem;
import control.FeedbackSystem;
import control.LoginSystem;
import control.ReportSystem;
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
        Log.enableLogging(true); // enable this for dev work
        // init singletons
        Input.getInstance();
        DataStoreSystem.getInstance();
        LoginSystem.getInstance();
        // CampSystem.getInstance();
        // FeedbackSystem.getInstance();
        // ReportSystem.getInstance();
        Log.printLogo("data/logo.txt");
        Log.println("======================================================================");
        Log.println("Welcome to Camp Application and Management System (CAMS).");
        Log.println("> Made by Team 2: Terence, Ryan, Jon, Zhi Wei\n");
    }

    // returns if should exit app
    public boolean run() {
        // return implicitly does break
        boolean shouldExit = false;
        switch (state) {
            case LOGIN:
                shouldExit = showLoginMenu();
                break;
            case START_MENU:
                shouldExit = showStartMenu();
                break;
            case STAFF_MENU:
                shouldExit = showStaffMenu();
                break;
            case STUDENT_MENU:
                shouldExit = showStudentMenu();
                break;
            default:
                Log.error("unknown state, exiting...");
                shouldExit = true;
                break;
        }
        if (stateDirty)
            switchState();
        return shouldExit;
    }

    public void cleanup() {
        Input.getInstance().close();
        DataStoreSystem.getInstance().cleanup();
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

        String usernameStr = Input.getInstance().getLine("Enter User ID: ").trim().toUpperCase();
        String passwordStr = Input.getInstance().getLine("Enter Password: ").trim();
        this.user = LoginSystem.getInstance().login(usernameStr, passwordStr);
        if (user == null) {
            Log.println("Invalid user ID or password.");
            return false; // kick user back to menu selection
        }
        else if (user != null)
            stateDirty = true;
        return false;
    }

    private boolean showStartMenu() {
        Log.println("===Welcome, " + user.getDisplayName() + "===");
        Log.println("(1) Change Password");
        Log.println("(2) View App Commands");
        Log.println("(3) Logout");
        int choice = -1;
        while (choice < 0) {
            choice = getChoice(1, 2, 3);
            if (choice == 0) {
                this.user = null; // destruct user
                stateDirty = true;
                return false; // dont exit
            }
        }

        switch (choice) {
            case 1:
                String oldPasswordStr = Input.getInstance().getLine("Enter Old Password: ").trim();
                String newPasswordStr = Input.getInstance().getLine("Enter New Password: ").trim();
                if (!oldPasswordStr.equals(user.getPassword())) {
                    Log.println("Password is wrong, please try again.");
                    return false;
                }
                boolean success = LoginSystem.getInstance().changeUserPassword(user, newPasswordStr);
                if (!success) {
                    Log.println("Password change failed.");
                    return false;
                } else {
                    //log out user
                    this.user = null;
                    stateDirty = true;
                    //return false; 
                }
                break;
            case 2:
                stateDirty = true;
                break;
        }
        return false;
    }

    private boolean showStudentMenu() {
        // assume safe, check handled by state machine
        Student student = (Student) user;
        Log.println("===Student Menu===");
        Log.println("(1) Do things");
        Log.println("(2) Do other things");
        Log.println("(10) Back to Start");
        int choice = -1;
        while (choice < 0) {
            choice = getChoice(1, 2, 10);
            if (choice == 0) {
                stateDirty = true;
            }
        }
        return false;
    }

    private boolean showStaffMenu() {
        // assume safe, check handled by state machine
        Staff staff = (Staff) user;
        Log.println("===Staff Menu===");
        Log.println("(1) Do things");
        Log.println("(2) Do other things");
        Log.println("(10) Back to Start");
        int choice = -1;
        while (choice < 0) {
            choice = getChoice(1, 2, 10);
            if (choice == 0) {
                stateDirty = true;
            }
        }
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
                if (user == null)
                    state = STATE.LOGIN;
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
