package boundary;

import control.FeedbackSystem;
import entity.CampSuggestion;
import entity.Student;
import util.Input;
import util.Log;

/**
 * <p>
 * Class defining the student menu functions
 * </p>
 * 
 * @author 
 * @version 1.0
 * @since 1-11-2023
 */
public class StudentMenu extends Menu {

    public StudentMenu(ConsoleUI ui) {
        super(ui);
    }

    @Override
    public boolean show() {
        String selCampName;

        // assume safe, check handled by state machine
        Student student = (Student) ui.getLoginSystem().getCurrentUser();
        boolean isCommittee = student.getCampCommitteeMember() != null;
        Log.println("===Student Menu===");
        Log.println("(1) View Available Camps");
        Log.println("(2) Register as Attendee");
        Log.println("(3) Register as Committee");
        Log.println("(4) View Registered Camps");
        Log.println("(5) Submit Enquiries");
        Log.println("(6) View Enquiry Replies");
        Log.println("(7) Withdraw from Camp");
        if (!isCommittee)
            Log.println("(8) Back to Start");
        else {
            Log.println("==Committee Member Menu==");
            Log.println("(9) Submit Suggestions");
            Log.println("(10) View Enquiries");
            Log.println("(11) Reply Enquiries");
            Log.println("(12) View Suggestions");
            Log.println("(13) Edit Suggestions");
            Log.println("(14) Delete Suggestions");
            Log.println("(15) Generate Camp Report");
            Log.println("(16) Generate Enquiry Report");
            Log.println("(17) Back to Start");
        }
        int choice = -1;
        while (choice < 0) {
            if (isCommittee)
                choice = getChoice(1, 16, 17);
            else
                choice = getChoice(1, 7, 8);
            if (choice == 0) {
                ui.setStateDirty(true);
            }
            switch (choice) {
                case 9:
                    selCampName = Input.getInstance().getLine("Please enter the camp name to submit suggestion: ");
                    String suggestionStr = Input.getInstance().getLine("Please enter suggestion: ");
                    CampSuggestion suggestion = new CampSuggestion(student.getCampCommitteeMember(),suggestionStr);
                    ui.getFeedbackSystem().addCampSuggestion(selCampName, suggestion);
                    Log.println("Suggestion submitted.");
            }
        }
        return false;
    }

}
