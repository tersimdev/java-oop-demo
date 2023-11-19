package boundary;

import java.time.LocalDate;
import java.util.ArrayList;

import control.CampSystem;
import control.FeedbackSystem;
import control.ReportSystem;
import entity.CampEnquiry;
import entity.CampInformation;
import entity.CampSuggestion;
import entity.Faculty;
import entity.Staff;
import entity.UserGroup;
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
        int selCampId;
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
        Log.println("(7) View Enquiries");
        Log.println("(8) Reply Unprocessed Enquiries");
        Log.println("(9) View Suggestions");
        Log.println("(10) Accept/Reject Unprocessed Suggestions");
        Log.println("(11) Generate Camp Report");
        Log.println("(12) Generate Performance Report");
        Log.println("(13) Generate Enquiry Report");
        Log.println("(14) Back to Start");
        int choice = -1;
        while (choice < 0) {
            choice = getChoice(1, 13, 14);
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
                    
                    String staffInChargeId = staff.getUserID();
                    Faculty organisingFaculty = staff.getFaculty();
                    UserGroup userGroup = new UserGroup().setFaculty(organisingFaculty);

                    // create the camp
                    int campId = campSystem.generateNewCampId();
                    CampInformation campInformation = new CampInformation(campName, description, location, totalSlots, committeeSlots, dates, registrationClosingDate, staffInChargeId, userGroup, organisingFaculty);

                    campSystem.createCamp(campId, campInformation);

                    break;

                case 2:
                    // Edit Camp
                    selCampId = ui.getInput()
                            .getInt("Please enter the ID of the camp you would like to edit: ");

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
                        campSystem.editCamp(selCampId, editChoice, ui.getInput());
                    }
                    break;

                case 3:
                    // Delete Camp
                    selCampId = ui.getInput()
                            .getInt("Please enter the ID of the camp you would like to delete: ");
                    campSystem.deleteCamp(selCampId);
                    break;

                case 4:
                    // View All Camps
                    campSystem.viewAllCamps();
                    break;

                case 5:
                    // View Camp Student List
                    selCampId = ui.getInput()
                            .getInt("Please enter the ID of the camp you would like to inspect: ");
                    campSystem.viewCampStudentList(selCampId);
                    break;

                case 6:
                    // View Camp Committee List
                    selCampId = ui.getInput()
                            .getInt("Please enter the ID of the camp you would like to inspect: ");
                    campSystem.viewCampCommitteeList(selCampId);
                    break;

                case 7:
                    // View Camp Enquiries
                    selCampName = ui.getInput()
                            .getLine("Please enter the name of the camp you would like to view enquiries: ");
                    ArrayList<CampEnquiry> enquiryList = new ArrayList<>();
                    enquiryList = feedbackSystem.getCampEnquiries(selCampName);
                    int size2 = enquiryList.size();
                    Log.println("===All Enquiries===");
                    for (int i = 0; i < size2; i++) {
                        CampEnquiry temp = enquiryList.get(i);
                        Log.println("EnquiryID: " + temp.getEnquiryId());
                        Log.println("StudentID: " + temp.getOwner());
                        if (temp.getReply()==null){
                            Log.println("Enquiry Status: Pending");
                            Log.println("Enquiry: " + temp.getEnquiry());
                            Log.println("Reply: Null");
                        }
                        else{
                            Log.println("Enquiry Status: Processed");
                            Log.println("Enquiry: " + temp.getEnquiry());
                            Log.println("Reply: " +temp.getReply());
                        }
                        Log.println("");
                    }

                    break;
                case 8:
                    // Reply Unprocessed Enquiries
                    selCampName = ui.getInput()
                            .getLine("Please enter the name of the camp you would like to reply unprocessed enquiries: ");
                    ArrayList<CampEnquiry> pendingEnquiryList = new ArrayList<>();
                    pendingEnquiryList = feedbackSystem.getCampEnquiries(selCampName);
                    int size4 = pendingEnquiryList.size();
                    Log.println("===Unprocessed Enquiries===");
                    for (int i = 0; i < size4; i++) {
                        CampEnquiry temp = pendingEnquiryList.get(i);
                        if (temp.getReply()!=null) continue;
                        else{
                            Log.println("EnquiryID: " + temp.getEnquiryId());
                            Log.println("StudentID: " + temp.getOwner());
                            Log.println("Enquiry Status: Pending");
                            Log.println("Enquiry: " + temp.getEnquiry());
                            Log.println("Reply: Null");
                            Log.println("");
                        }
                    }
                    int enquiryId = ui.getInput().getInt("Please enter the enquiryId of the enquiry to reply: ");
                    String reply = ui.getInput().getLine("Please enter reply: ");
                    Boolean result = feedbackSystem.replyCampEnquiry(staff.getUserID(),selCampName, enquiryId, reply);
                    if(result) 
                            Log.println("Enquiry successfully processed.");
                        else 
                            Log.println("Enquiry processing failed.");
                    break;
                case 9:
                    // View Suggestions
                    selCampName = ui.getInput()
                            .getLine("Please enter the name of the camp you would like to view suggestions: ");
                    ArrayList<CampSuggestion> suggestionList = new ArrayList<>();
                    suggestionList = feedbackSystem.getCampSuggestions(selCampName);
                    int size = suggestionList.size();
                    Log.println("===All Suggestions===");
                    for (int i = 0; i < size; i++) {
                        CampSuggestion temp = suggestionList.get(i);
                        Log.println("SuggestionID: " + temp.getSuggestionId());
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

                case 10:
                    // Accept/Reject Unprocessed Suggestions
                    selCampName = ui.getInput()
                            .getLine("Please enter the name of the camp you would like to approve/reject unprocessed suggestions: ");
                    ArrayList<CampSuggestion> pendingSuggestionList = new ArrayList<>();
                    pendingSuggestionList = feedbackSystem.getCampSuggestions(selCampName);
                    int size1 = pendingSuggestionList.size();
                    Log.println("===Unprocessed Suggestions===");
                    for (int i = 0; i < size1; i++) {
                        CampSuggestion temp = pendingSuggestionList.get(i);
                        if(!temp.isPending()) continue;
                        else {
                            Log.println("SuggestionID: " + temp.getSuggestionId());
                            Log.println("CampCommitteeMemberID: " + temp.getOwner());
                            Log.println("Approval status: Pending");
                            Log.println("Suggestion: " + temp.getSuggestion());
                            Log.println("");
                        }
                    }
                    int suggestionId = ui.getInput().getInt("Please enter the suggestionId of the suggestion to approve/reject: ");
                    Log.println("===Please select the following options===");
                    Log.println("(1) Accept suggestion");
                    Log.println("(2) Reject suggestion");
                    int decision = -1;
                    Boolean result1 = false;
                    while(decision < 0) {
                        decision = ui.getInput().getInt("Enter choice: ");
                        if(decision==1)
                            result1 = feedbackSystem.processCampSuggestion(staff.getUserID(),selCampName, suggestionId, true);
                        else if(decision==2)
                            result1 = feedbackSystem.processCampSuggestion(staff.getUserID(),selCampName, suggestionId, false);
                        else {
                            Log.println("Invalid choice! Try again.");
                            decision = -1;
                        }
                    }
                    if(result1)
                        Log.println("Suggestion successfully processed.");
                    else 
                        Log.println("Suggestion processing failed.");
                    break;

                case 11:
                    // Generate Camp Report
                    selCampName = ui.getInput()
                            .getLine("Please enter the name of the camp you would like to inspect: ");

                    break;

                case 12:
                    // Generate Performance Report
                    selCampName = ui.getInput()
                            .getLine("Please enter the name of the camp you would like to inspect: ");

                    break;

                case 13:
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
