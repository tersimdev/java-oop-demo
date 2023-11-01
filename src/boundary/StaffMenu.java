package boundary;

import entity.Staff;
import util.Log;

/**
 * <p>
 * Class defining the staff menu functions
 * </p>
 * 
 * @author 
 * @version 1.0
 * @since 1-11-2023
 */
public class StaffMenu extends Menu {

    public StaffMenu(ConsoleUI ui) {
        super(ui);
    }

    @Override
    public boolean show() {
        // assume safe, check handled by state machine
        Staff staff = (Staff) ui.getUser();
        Log.println("===Staff Menu===");
        Log.println("(1) Create Camp");
        Log.println("(2) Edit Camp");
        Log.println("(3) View All Camps");
        Log.println("(4) View Camp Student List");
        Log.println("(5) View Camp Committee List");
        Log.println("(6) View Camp Suggestions");
        Log.println("(7) Accept/Reject Suggestion");
        Log.println("(8) Generate Camp Report");
        Log.println("(9) Generate Performance Report");
        Log.println("(10) Generate Enquiry Report");
        Log.println("(11) Back to Start");
        int choice = -1;
        while (choice < 0) {
            choice = getChoice(1, 10, 11);
            if (choice == 0) {
                ui.setStateDirty(true);
            }
        }
        return false;
    }

}
