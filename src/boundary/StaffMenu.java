package boundary;

import java.time.LocalDate;
import java.util.ArrayList;

import control.CampSystem;
import control.FeedbackSystem;
import control.EnquirySystem;
import control.SuggestionSystem;
import control.ReportSystem;
import entity.CampEnquiry;
import entity.CampFeedback;
import entity.CampInformation;
import entity.CampSuggestion;
import entity.Faculty;
import entity.Staff;
import entity.UserGroup;
import util.InputHelper;
import util.Log;

/**
 * <p>
 * Class defining the staff menu.
 * </p>
 * 
 * @author Team 2
 * @version 1.0
 * @since 1-11-2023
 */
public class StaffMenu extends Menu {

    private final CampSystem campSystem;
    //private final FeedbackSystem feedbackSystem;
    private final EnquirySystem enquirySystem;
    private final SuggestionSystem suggestionSystem;
    private final ReportSystem reportSystem;

    private Staff staff;

    public StaffMenu(ConsoleUI ui, CampSystem campSystem, EnquirySystem enquirySystem, SuggestionSystem suggestionSystem, ReportSystem reportSystem) {
        super(ui);
        this.campSystem = campSystem;
        //this.feedbackSystem = feedbackSystem;
        this.enquirySystem = enquirySystem;
        this.suggestionSystem = suggestionSystem;
        this.reportSystem = reportSystem;

        // define which choice triggers which function
        addMenuFunction(1, this::createCamp);
        addMenuFunction(2, this::editCamp);
        addMenuFunction(3, this::deleteCamp);
        addMenuFunction(4, this::viewAllCamps);
        addMenuFunction(5, this::viewCampAttendeeList);
        addMenuFunction(6, this::viewCampCommitteeList);
        addMenuFunction(7, this::viewEnquiries);
        addMenuFunction(8, this::replyEnquiries);
        addMenuFunction(9, this::viewSuggestions);
        addMenuFunction(10, this::acceptRejectSuggestions);
        addMenuFunction(11, this::generateCampReport);
        addMenuFunction(12, this::generatePerformanceReport);
        addMenuFunction(13, this::generateEnquiryReport);
    }

    @Override
    public boolean show() {
        // assume safe, check handled by state machine
        staff = (Staff) ui.getUser();

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
                return false;
            }
            runMenuFunction(choice);
        }
        return false;
    }

    // menu functions defineed below

    private boolean createCamp(Menu menu) {
        // Create Camp
        // get camp info from user
        String campName = ui.getInput().getLine("Please enter the camp name: ");
        String description = ui.getInput().getLine("Please enter the camp's description: ");
        String location = ui.getInput().getLine("Please enter the camp's location: ");
        int committeeSlots = InputHelper.getBoundedInt(ui.getInput(), 0, 10,
                "Please enter the camp's number of committee slots (MAX 10): ");
        int totalSlots = InputHelper.getBoundedInt(ui.getInput(), committeeSlots + 10, 999,
                "Please enter the camp's total number of slots (including committee members): ");
        int duration = InputHelper.getBoundedInt(ui.getInput(), 1, 9999,
                "Please enter the number of days the camp will be held: ");
        LocalDate firstDate = ui.getInput()
                .getDate("Please enter the date of the first day of the camp (DD/MM/YYYY): ");
        LocalDate registrationClosingDate = ui.getInput()
                .getDate("Please enter the registration deadline (DD/MM/YYYY): ");
        ArrayList<LocalDate> dates = new ArrayList<LocalDate>();
        for (int i = 0; i < duration; i++) {
            dates.add(firstDate);
            firstDate = firstDate.plusDays(1);
        }
        boolean isWholeNTU = ui.getInput()
                .getBool("Will this camp be open to the whole of NTU (Y/N)? ");
        Faculty organisingFaculty = staff.getFaculty();
        UserGroup userGroup = new UserGroup();
        if (isWholeNTU)
            userGroup.setWholeNTU();
        else
            userGroup.setFaculty(organisingFaculty);
        String staffInChargeId = staff.getUserID();

        // create the camp
        CampInformation campInformation = new CampInformation.CampInformationBuilder().setCampName(campName)
                .setDates(dates)
                .setRegistrationClosingDate(registrationClosingDate).setTotalSlots(totalSlots)
                .setCommitteeSlots(committeeSlots)
                .setLocation(location).setDescription(description).setStaffInChargeId(staffInChargeId)
                .setUserGroup(userGroup)
                .setOrganisingFaculty(organisingFaculty).build();

        campSystem.createCamp(campInformation);
        return false;
    }

    private boolean editCamp(Menu menu) {
        // Edit Camp
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "edit");

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
                return false;
            }
            campSystem.editCamp(selCampId, editChoice, ui.getInput());
        }
        return false;
    }

    private boolean deleteCamp(Menu menu) {
        // Delete Camp
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "delete");
        campSystem.deleteCamp(selCampId);
        return false;
    }

    private boolean viewAllCamps(Menu menu) {
        boolean yesno = ui.getInput()
                .getBool("Would you like to view only the camps you created?(Y/N) ");
        int sortChoice = menu.printCampSortOrderChoices();
        if (yesno == true) campSystem.viewCampsOfStaff(staff, sortChoice);
        else campSystem.viewAllCamps(sortChoice);
        return false;
    }

    private boolean viewCampAttendeeList(Menu menu) {
        // View Camp Attendee List
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "inspect");
        campSystem.viewAttendeeList(selCampId, staff);
        return false;
    }

    private boolean viewCampCommitteeList(Menu menu) {
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "inspect");
        campSystem.viewCampCommitteeList(selCampId, staff);
        return false;
    }

    private boolean viewEnquiries(Menu menu) {
        // View Camp Enquiries
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "view enquiries");

        ArrayList<CampFeedback> enquiryList = new ArrayList<>();
        enquiryList = enquirySystem.getCampFeedbacks(selCampId);
        Log.println("===All Enquiries===");
        for (CampFeedback campFeedback : enquiryList) {
            if (!(campFeedback instanceof CampEnquiry))
                continue;
            CampEnquiry campEnquiry = (CampEnquiry) campFeedback;
            Log.println("EnquiryID: " + campEnquiry.getFeedbackId());
            Log.println("StudentID: " + campEnquiry.getOwner());
            if (campEnquiry.getReply() == null) {
                Log.println("Enquiry Status: Pending");
                Log.println("Enquiry: " + campEnquiry.getFeedback());
                Log.println("Reply: Null");
            } else {
                Log.println("Enquiry Status: Processed");
                Log.println("Enquiry: " + campEnquiry.getFeedback());
                Log.println("Reply: " + campEnquiry.getReply());
            }
            Log.println("");
        }
        return false;
    }

    private boolean replyEnquiries(Menu menu) {
        // Reply Unprocessed Enquiries
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "reply unprocessed enquiries");

        ArrayList<CampFeedback> pendingEnquiryList = new ArrayList<>();
        pendingEnquiryList = enquirySystem.getCampFeedbacks(selCampId);
        Log.println("===Unprocessed Enquiries===");
        for (CampFeedback campFeedback : pendingEnquiryList) {
            if (!(campFeedback instanceof CampEnquiry))
                continue;
            CampEnquiry campEnquiry = (CampEnquiry) campFeedback;
            if (campEnquiry == null || !campEnquiry.isPending())
                continue;
            Log.println("EnquiryID: " + campEnquiry.getFeedbackId());
            Log.println("StudentID: " + campEnquiry.getOwner());
            Log.println("Enquiry Status: Pending");
            Log.println("Enquiry: " + campEnquiry.getFeedback());
            Log.println("Reply: Null");
            Log.println("");
        }
        int enquiryId = ui.getInput().getInt("Please enter the enquiryId of the enquiry to reply: ");
        String reply = ui.getInput().getLine("Please enter reply: ");
        Boolean result = enquirySystem.processCampEnquiry(staff.getUserID(), selCampId, enquiryId, reply);
        if (result) {
            Log.println("Enquiry successfully processed.");
        } else
            Log.println("Enquiry processing failed.");
        return false;
    }

    private boolean viewSuggestions(Menu menu) {
        // View Suggestions
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "view suggestions");

        ArrayList<CampFeedback> suggestionList = new ArrayList<>();
        suggestionList = suggestionSystem.getCampFeedbacks(selCampId);
        Log.println("===All Suggestions===");
        for (CampFeedback campFeedback : suggestionList) {
            if (!(campFeedback instanceof CampSuggestion))
                continue;
            CampSuggestion campSuggestion = (CampSuggestion) campFeedback;
            Log.println("SuggestionID: " + campSuggestion.getFeedbackId());
            Log.println("CampCommitteeMemberID: " + campSuggestion.getOwner());

            if (campSuggestion.hasApproved())
                Log.println("Approval status: Approved");
            else if (campSuggestion.hasRejected())
                Log.println("Approval status: Rejected");
            else
                Log.println("Approval status: Pending");

            Log.println("Suggestion: " + campSuggestion.getFeedback());
            Log.println("");
        }
        return false;
    }

    private boolean acceptRejectSuggestions(Menu menu) {
        // Accept/Reject Unprocessed Suggestions
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem,
                "approve/reject unprocessed suggestions");

        ArrayList<CampFeedback> pendingSuggestionList = new ArrayList<>();
        pendingSuggestionList = suggestionSystem.getCampFeedbacks(selCampId);
        Log.println("===Unprocessed Suggestions===");
        
        for (CampFeedback campFeedback : pendingSuggestionList) {
            if (!(campFeedback instanceof CampSuggestion))
                continue;
            CampSuggestion campSuggestion = (CampSuggestion) campFeedback;
            if (campSuggestion == null || !campSuggestion.isPending())
                continue;
            else {
                Log.println("SuggestionID: " + campSuggestion.getFeedbackId());
                Log.println("CampCommitteeMemberID: " + campSuggestion.getOwner());
                Log.println("Approval status: Pending");
                Log.println("Suggestion: " + campSuggestion.getFeedback());
                Log.println("");
            }
        }
        int suggestionId = ui.getInput()
                .getInt("Please enter the suggestionId of the suggestion to approve/reject: ");
        Log.println("===Please select the following options===");
        Log.println("(1) Accept suggestion");
        Log.println("(2) Reject suggestion");
        int decision = -1;
        Boolean result1 = false;
        while (decision < 0) {
            decision = ui.getInput().getInt("Enter choice: ");
            if (decision == 1)
                result1 = suggestionSystem.processCampSuggestion(staff.getUserID(), selCampId, suggestionId,
                        true);
            else if (decision == 2)
                result1 = suggestionSystem.processCampSuggestion(staff.getUserID(), selCampId, suggestionId,
                        false);
            else {
                Log.println("Invalid choice! Try again.");
                decision = -1;
            }
        }
        if (result1)
            Log.println("Suggestion successfully processed.");
        else
            Log.println("Suggestion processing failed.");
        return false;
    }

    private boolean generateCampReport(Menu menu) {
        // Generate Camp Report
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "generate camp report");

        return false;
    }

    private boolean generatePerformanceReport(Menu menu) {
        // Generate Performance Report
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "generate performance report");

        return false;
    }

    private boolean generateEnquiryReport(Menu menu) {
        // Generate Enquiry Report
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "generate enquiry report");

        return false;
    }

}
