package boundary;

import entity.Staff;
import util.Log;

public class StaffMenu extends Menu {

    public StaffMenu(ConsoleUI ui) {
        super(ui);
    }

    @Override
    public boolean show() {
        // assume safe, check handled by state machine
        Staff staff = (Staff) ui.getUser();
        Log.println("===Staff Menu===");
        Log.println("(1) Do things");
        Log.println("(2) Do other things");
        Log.println("(10) Back to Start");
        int choice = -1;
        while (choice < 0) {
            choice = getChoice(1, 2, 10);
            if (choice == 0) {
                ui.setStateDirty(true);
            }
        }
        return false;
    }

}
