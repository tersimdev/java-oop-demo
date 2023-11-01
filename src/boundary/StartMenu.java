package boundary;

import control.LoginSystem;
import util.Input;
import util.Log;
import entity.User;

public class StartMenu extends Menu {

    public StartMenu(ConsoleUI ui) {
        super(ui);
    }

    @Override
    public boolean show() {
        Log.println("===Welcome, " + ui.getUser().getDisplayName() + "===");
        Log.println("(1) Change Password");
        Log.println("(2) View App Commands");
        Log.println("(3) Logout");
        int choice = -1;
        while (choice < 0) {
            choice = getChoice(1, 2, 3);
            if (choice == 0) {
                ui.setUser(null); // destruct user
                ui.setStateDirty(true);
                return false; // dont exit
            }
        }

        switch (choice) {
            case 1:
                String oldPasswordStr = Input.getInstance().getLine("Enter Old Password: ").trim();
                String newPasswordStr = Input.getInstance().getLine("Enter New Password: ").trim();
                User user = ui.getUser();
                if (!oldPasswordStr.equals(user.getPassword())) {
                    Log.println("Password is wrong, please try again.");
                    return false;
                }
                boolean success = LoginSystem.getInstance().changeUserPassword(user, newPasswordStr);
                if (!success) {
                    Log.println("Password change failed.");
                    return false;
                } else {
                    // log out user
                    ui.setUser(null);
                    ui.setStateDirty(true);
                    // return false;
                }
                break;
            case 2:
                ui.setStateDirty(true);
                break;
        }
        return false;
    }

}
