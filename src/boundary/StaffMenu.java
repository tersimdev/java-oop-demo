package boundary;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import control.CampSystem;
import control.FeedbackSystem;
import control.ReportSystem;
import entity.CampEnquiry;
import entity.CampInformation;
import entity.CampSuggestion;
import entity.Faculty;
import entity.Staff;
import entity.Student;
import entity.UserGroup;
import entity.User;
import util.Input;
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

    private final CampSystem campSystem;
    private final FeedbackSystem feedbackSystem;
    private final ReportSystem reportSystem;

    public StaffMenu(ConsoleUI ui, CampSystem campSystem, FeedbackSystem feedbackSystem, ReportSystem reportSystem) {
        super(ui);
        this.campSystem = campSystem;
        this.feedbackSystem = feedbackSystem;
        this.reportSystem = reportSystem;
    }

    @Override
    public boolean show() {
        String selCampName;

        // assume safe, check handled by state machine
        Staff staff = (Staff) ui.getUser();
        Log.println("===Staff Menu===");
        Log.println("(1) Create Camp");
        Log.println("(2) Edit Camp");
        Log.println("(3) Delete Camp");
        Log.println("(4) View All Camps");
        Log.println("(5) View Camp Student List");
        Log.println("(6) View Camp Committee List");
        Log.println("(7) View Camp Suggestions");
        Log.println("(8) Accept/Reject Suggestion");
        Log.println("(9) Generate Camp Report");
        Log.println("(10) Generate Performance Report");
        Log.println("(11) Generate Enquiry Report");
        Log.println("(12) Back to Start");
        int choice = -1;
        while (choice < 0) {
            choice = getChoice(1, 11, 12);
            if (choice == 0) {
                ui.setStateDirty(true);
            }

            switch (choice) {
                case 1:
                    // Create Camp
                    // get camp info from user
                    String campName = ui.getInput().getLine("Please enter the camp name: ");
                    String description = ui.getInput().getLine("Please enter the camp's description: ");
                    String location = ui.getInput().getLine("Please enter the camp's location: ");
                    int totalSlots = ui.getInput().getInt("Please enter the camp's total number of slots: ");
                    int committeeSlots = ui.getInput()
                            .getInt("Please enter the camp's number of committee slots: ");
                    int duration = ui.getInput()
                            .getInt("Please enter the number of days the camp will be held: ");
                    LocalDate firstDate = ui.getInput()
                            .getDate("Please enter the date of day number 1 (DD/MM/YYYY): ");
                    LocalDate registrationClosingDate = ui.getInput()
                            .getDate("Please enter the closing date for registration (DD/MM/YYYY): ");
                    ArrayList<LocalDate> dates = new ArrayList<LocalDate>();
                    for (int i = 0; i < duration; i++) {
                        dates.add(firstDate);
                        firstDate = firstDate.plusDays(1);
                    }
                    
                    ArrayList<String> studentList = new ArrayList<>();
                    String staffInChargeId = staff.getUserID();
                    Faculty organisingFaculty = staff.getFaculty();
                    UserGroup userGroup = new UserGroup().setFaculty(organisingFaculty);

                    // create the camp
                    int campId = campSystem.generateNewCampId();
                    CampInformation campInformation = new CampInformation(campName, description, location, totalSlots, committeeSlots, dates, registrationClosingDate, staffInChargeId, userGroup, organisingFaculty);

                    campSystem.createCamp(campId, campInformation, studentList);

                    break;

                case 2:
                    // Edit Camp
                    selCampName = ui.getInput()
                            .getLine("Please enter the name of the camp you would like to edit: ");

                    Log.println("===What would you like to edit?===");
                    Log.println("(1) Camp name");
                    Log.println("(2) Camp description");
                    Log.println("(3) Camp location");
                    Log.println("(4) Camp's total number of slots");
                    Log.println("(5) Camp's number of committee slots");
                    Log.println("(6) Camp dates");
                    Log.println("(7) Camp registration closing date");
                    Log.println("(8) Toggle camp visibility");
                    Log.println("(9) Back to Staff Menu");
                    
                    int editChoice = -1;
                    while (editChoice < 0) {
                        editChoice = getChoice(1, 8, 9);
                        if (editChoice == 0) {
                            choice = -1;
                            break;
                        }
                        campSystem.editCamp(selCampName, editChoice, ui.getInput());
                    }
                    break;

                case 3:
                    // Delete Camp
                    selCampName = ui.getInput()
                            .getLine("Please enter the name of the camp you would like to delete: ");
                    campSystem.deleteCamp(selCampName);
                    break;

                case 4:
                    // View All Camps
                    campSystem.viewAllCamps();
                    break;

                case 5:
                    // View Camp Student List
                    selCampName = ui.getInput()
                            .getLine("Please enter the name of the camp you would like to inspect: ");
                    campSystem.viewCampStudentList(selCampName);
                    break;

                case 6:
                    // View Camp Committee List
                    selCampName = ui.getInput()
                            .getLine("Please enter the name of the camp you would like to inspect: ");
                    campSystem.viewCampCommitteeList(selCampName);
                    break;

                case 7:
                    // View Camp Suggestions
                    selCampName = ui.getInput()
                            .getLine("Please enter the name of the camp you would like to view suggestions: ");
                    ArrayList<CampSuggestion> suggestionList = new ArrayList<>();
                    suggestionList = feedbackSystem.getCampSuggestions(selCampName);
                    int size = suggestionList.size();
                    for (int i = 0; i < size; i++) {
                        CampSuggestion temp = suggestionList.get(i);
                        Log.println("CampCommitteeMemberID: " + temp.getOwner());

                        if (temp.hasApproved())
                            Log.println("Approval status: Approved");
                        else if (temp.hasRejected())
                            Log.println("Approval status: Rejected");
                        else
                            Log.println("Approval status: Pending");

                        Log.println("Suggestion: " + temp.getSuggestion());
                        Log.println("");
                    }

                    break;

                case 8:
                    // Accept/Reject Suggestion
                    selCampName = ui.getInput()
                            .getLine("Please enter the name of the camp you would like to inspect: ");

                    break;

                case 9:
                    // Generate Camp Report
                    selCampName = ui.getInput()
                            .getLine("Please enter the name of the camp you would like to inspect: ");

                    break;

                case 10:
                    // Generate Performance Report
                    selCampName = ui.getInput()
                            .getLine("Please enter the name of the camp you would like to inspect: ");

                    break;

                case 11:
                    // Generate Enquiry Report
                    selCampName = ui.getInput()
                            .getLine("Please enter the name of the camp you would like to inspect: ");

                    break;

                default:
                    break;
            }
        }
        return false;
    }

}
