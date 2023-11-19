package boundary;

import control.FeedbackSystem;
import control.ReportSystem;
import control.CampSystem;
import entity.Camp;
import entity.CampReportFilter;
import entity.CampReportOptions;
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

    private final CampSystem campSystem;
    private final FeedbackSystem feedbackSystem;
    private final ReportSystem reportSystem;

    public StudentMenu(ConsoleUI ui, CampSystem campSystem, FeedbackSystem feedbackSystem, ReportSystem reportSystem) {
        super(ui);
        this.campSystem = campSystem;
        this.feedbackSystem = feedbackSystem;
        this.reportSystem = reportSystem;
    }

    @Override
    public boolean show() {
        String selCampName;

        // assume safe, check handled by state machine
        Student student = (Student) ui.getUser();
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
                    feedbackSystem.addCampSuggestion(selCampName, suggestion);
                    Log.println("Suggestion submitted.");
                    break;
                case 15:
                    selCampName = Input.getInstance().getLine("Please enter the camp name for report generation: ");
                
                    Camp camp = campSystem.getCampByName(selCampName);
                
                    if (camp != null) {
                        String fileName = Input.getInstance().getLine("Please enter the file name: ");
                
                        int filterChoice = Input.getInstance().getInt("Please enter the filter (1 for ATTENDEE, 2 for CAMP_COMMITTEE, 3 for no filter): ");
                        CampReportFilter filter = null;
                        switch (filterChoice) {
                            case 1:
                                filter = CampReportFilter.ATTENDEE;
                                break;
                            case 2:
                                filter = CampReportFilter.CAMP_COMMITTEE;
                                break;
                            default:
                                break;
                        }

                        CampReportOptions reportOptions = new CampReportOptions();
                        reportOptions.setCampId(camp.getCampId());
                        reportOptions.setFileName(fileName);
                        reportOptions.setFilter(filter);
            
                        reportSystem.generateReport(reportOptions, student, camp);
                    } else {
                        Log.println("Camp not found with the given name: " + selCampName);
                    }
                    break;

            }
        }
        return false;
    }

}
