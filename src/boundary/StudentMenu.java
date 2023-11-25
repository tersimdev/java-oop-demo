package boundary;

import control.CampSystem;
import control.EnquirySystem;
import control.ReportSystem;
import control.SuggestionSystem;
import entity.Camp;
import entity.CampCommitteeMember;
import entity.CampEnquiry;
import entity.CampReportFilter;
import entity.CampReportOptions;
import entity.CampSuggestion;
import entity.Student;
import util.Log;
import util.helpers.InputHelper;

/**
 * <p>
 * Class defining the student menu.
 * </p>
 * 
 * @author Team 2
 * @version 1.0
 * @since 1-11-2023
 */
public class StudentMenu extends Menu {
    /**
     * Dependency Injection
     */
    private final CampSystem campSystem;
    /**
     * Dependency Injection
     */
    private final EnquirySystem enquirySystem;
    /**
     * Dependency Injection
     */
    private final SuggestionSystem suggestionSystem;
    /**
     * Dependency Injection
     */
    private final ReportSystem reportSystem;

    /**
     * Current student object using the menu
     */
    private Student student;
    /**
     * Current committee object belonging to <code>student</code>
     */
    private CampCommitteeMember committeeMember;

    /**
     * Constructor with DI
     * 
     * @param ui               ui object
     * @param campSystem       campSystem object
     * @param enquirySystem    enquirySystem object
     * @param suggestionSystem suggestionSystem object
     * @param reportSystem     reportSystem object
     */
    public StudentMenu(ConsoleUI ui, CampSystem campSystem, EnquirySystem enquirySystem,
            SuggestionSystem suggestionSystem, ReportSystem reportSystem) {
        super(ui);
        this.campSystem = campSystem;
        this.enquirySystem = enquirySystem;
        this.suggestionSystem = suggestionSystem;
        this.reportSystem = reportSystem;

        // define which choice triggers which function
        addMenuFunction(1, this::viewAvailableCamps);
        addMenuFunction(2, this::registerAttendee);
        addMenuFunction(3, this::registerCommittee);
        addMenuFunction(4, this::viewRegisteredCamps);
        addMenuFunction(5, this::submitEnquiry);
        addMenuFunction(6, this::viewEditDelPendingEnquiries);
        addMenuFunction(7, this::viewEnquiryReplies);
        addMenuFunction(8, this::withdrawFromCamp);
        // 9 is exit
        addMenuFunction(10, this::submitSuggestion);
        addMenuFunction(11, this::viewEditDelPendingSuggestions);
        addMenuFunction(12, this::viewSuggestionsApproval);
        addMenuFunction(13, this::viewEnquiries);
        addMenuFunction(14, this::replyEnquiries);
        addMenuFunction(15, this::generateCampReport);
        addMenuFunction(16, this::viewCampAttendeeList);
        addMenuFunction(17, this::viewCampCommitteeList);
    }

    @Override
    public boolean show() {
        // assume safe, check handled by state machine
        student = (Student) ui.getUser();
        committeeMember = student.getCampCommitteeMember();
        boolean isCommittee = committeeMember.getIsMember();

        Log.println("===Student Menu===");
        Log.println("(1) View Available Camps");
        Log.println("(2) Register as Attendee");
        Log.println("(3) Register as Committee");
        Log.println("(4) View Registered Camps");
        Log.println("(5) Submit Enquiries");
        Log.println("(6) View/Edit/Delete Pending Enquiries");
        Log.println("(7) View Processed Enquiry Replies");
        Log.println("(8) Withdraw from Camp");
        if (!isCommittee)
            Log.println("(9) Back to Start");
        else {
            Log.println("==Committee Member Menu==");
            Log.println("(10) Submit Suggestions");
            Log.println("(11) View/Edit/Delete Pending Suggestions");
            Log.println("(12) View Processed Suggestions");
            Log.println("(13) View Enquiries");
            Log.println("(14) Reply Unprocessed Enquiries");
            Log.println("(15) Generate Camp Report");
            Log.println("(16) View Camp Attendee List");
            Log.println("(17) View Camp Committee Members List");
            Log.println("(18) Back to Start");
        }
        int choice = -1;
        while (choice < 0) {
            if (isCommittee)
                choice = getChoice(1, 17, 18);
            else
                choice = getChoice(1, 8, 9);
            if (choice == 0) {
                ui.setStateDirty(true);
                return false;
            }
        }
        return runMenuFunction(choice); // use function map defined in constructor
    }

    /**
     * Menu function
     * 
     * @param menu menu object
     * @return true if should quit app
     */
    private boolean viewAvailableCamps(Menu menu) {
        int sortChoice = menu.printCampSortOrderChoices();
        campSystem.getCampViewerSubSystem().viewAvailableCamps(student, sortChoice);
        return false;
    }

    /**
     * Menu function
     * 
     * @param menu menu object
     * @return true if should quit app
     */
    private boolean registerAttendee(Menu menu) {
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "register for");
        campSystem.getCampRegistrationSubSystem().registerAsAttendee(student, selCampId);
        return false;
    }

    /**
     * Menu function
     * 
     * @param menu menu object
     * @return true if should quit app
     */
    private boolean registerCommittee(Menu menu) {
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "register for");
        boolean yesno = ui.getInput().getBool(
                "You will not be able to quit this camp after registering. Are you sure you want to register (Y/N)? ");
        if (yesno)
            campSystem.getCampRegistrationSubSystem().registerAsCommittee(student, selCampId);
        return false;
    }

    /**
     * Menu function
     * 
     * @param menu menu object
     * @return true if should quit app
     */
    private boolean viewRegisteredCamps(Menu menu) {
        int sortChoice = menu.printCampSortOrderChoices();
        campSystem.getCampViewerSubSystem().viewRegisteredCamps(student, sortChoice);
        return false;
    }

    /**
     * Menu function
     * 
     * @param menu menu object
     * @return true if should quit app
     */
    private boolean submitEnquiry(Menu menu) {
        // Submit Enquiry
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "submit enquiry");
        String enquiryStr = ui.getInput().getLine("Please enter enquiry: ");
        CampEnquiry enquiry = new CampEnquiry(student.getUserID(), enquiryStr, selCampId);
        enquirySystem.addCampFeedback(selCampId, enquiry);
        Log.println("Enquiry submitted.");
        return false;
    }

    /**
     * Menu function
     * 
     * @param menu menu object
     * @return true if should quit app
     */
    private boolean viewEditDelPendingEnquiries(Menu menu) {
        // View/Edit/Delete Pending Enquiries
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "view/edit/delete your enquiries");
        enquirySystem.viewEditDelFeedback(student.getUserID(), selCampId, ui.getInput());
        return false;
    }

    /**
     * Menu function
     * 
     * @param menu menu object
     * @return true if should quit app
     */
    private boolean viewEnquiryReplies(Menu menu) {
        // View Processed Enquiry Replies of a specific student
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "view processed enquiries");
        enquirySystem.printProcessedFeedbackByOwner(student.getUserID(), selCampId);
        return false;
    }

    /**
     * Menu function
     * 
     * @param menu menu object
     * @return true if should quit app
     */
    private boolean withdrawFromCamp(Menu menu) {
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "withdraw from");
        boolean yesno = ui.getInput()
                .getBool(
                        "You will not be able to register for this camp after withdrawing from it. Are you sure you want to withdraw (Y/N)? ");
        if (yesno)
            campSystem.getCampRegistrationSubSystem().withdrawFromCamp(student, selCampId);
        return false;
    }

    // committee functions below
    /**
     * Menu function
     * 
     * @param menu menu object
     * @return true if should quit app
     */
    private boolean submitSuggestion(Menu menu) {
        // Submit Suggestions
        Camp camp = campSystem.getCampsByCommittee(student.getUserID()).get(0);
        int selCampId = camp.getCampId();

        String suggestionStr = ui.getInput().getLine("Please enter suggestion: ");
        CampSuggestion suggestion = new CampSuggestion(student.getUserID(),
                suggestionStr, selCampId);
        suggestionSystem.addCampFeedback(selCampId, suggestion);
        Log.println("Suggestion submitted.");
        return false;
    }

    /**
     * Menu function
     * 
     * @param menu menu object
     * @return true if should quit app
     */
    private boolean viewEditDelPendingSuggestions(Menu menu) {
        // View/Edit/Delete Pending Suggestions
        Camp camp = campSystem.getCampsByCommittee(student.getUserID()).get(0);
        int selCampId = camp.getCampId();

        suggestionSystem.viewEditDelFeedback(student.getUserID(), selCampId, ui.getInput());
        return false;
    }

    /**
     * Menu function
     * 
     * @param menu menu object
     * @return true if should quit app
     */
    private boolean viewSuggestionsApproval(Menu menu) {
        // View Processed Suggestions of a specific student
        Camp camp = campSystem.getCampsByCommittee(student.getUserID()).get(0);
        int selCampId = camp.getCampId();
        suggestionSystem.printProcessedFeedbackByOwner(student.getUserID(), selCampId);
        return false;
    }

    /**
     * Menu function
     * 
     * @param menu menu object
     * @return true if should quit app
     */
    private boolean viewEnquiries(Menu menu) {
        // View Camp Enquiries
        Camp camp = campSystem.getCampsByCommittee(student.getUserID()).get(0);
        int selCampId = camp.getCampId();

        enquirySystem.printAllFeedback(selCampId);
        return false;
    }

    /**
     * Menu function
     * 
     * @param menu menu object
     * @return true if should quit app
     */
    private boolean replyEnquiries(Menu menu) {
        // Reply Unprocessed Enquiries
        Camp camp = campSystem.getCampsByCommittee(student.getUserID()).get(0);
        int selCampId = camp.getCampId();

        int size = enquirySystem.printPendingFeedback(selCampId);
        if (size == 0) {
            Log.println("No pending enquiries found. Directing back to menu...");
            return false;
        }

        int enquiryId = InputHelper.getEnquiryIdFromUser(ui.getInput(), enquirySystem, "reply", selCampId);
        String reply = ui.getInput().getLine("Please enter reply: ");
        Boolean result = enquirySystem.processCampEnquiry(student.getCampCommitteeMember(), student.getUserID(),
                selCampId, enquiryId,
                reply);
        if (result) {
            Log.println("Enquiry successfully processed.");
            student.getCampCommitteeMember().addPoints(1);
        } else
            Log.println("Enquiry processing failed.");
        return false;
    }

    /**
     * Menu function
     * 
     * @param menu menu object
     * @return true if should quit app
     */
    private boolean generateCampReport(Menu menu) {
        Camp camp = campSystem.getCampsByCommittee(student.getUserID()).get(0);

        CampReportFilter[] filterChoicesOptions = {
                CampReportFilter.ATTENDEE,
                CampReportFilter.CAMP_COMMITTEE,
                CampReportFilter.NONE,
        };

        String fileName = ui.getInput().getLine("Please enter the file name: ");

        // CampReportOptions reportOptions = ReportInputHelper.getOptionsFromUser();

        int filterChoice = ui.getInput()
                .getInt("Please enter the filter (1 for ATTENDEE, 2 for CAMP_COMMITTEE, 3 for no filter): ");
        CampReportFilter filter = filterChoicesOptions[filterChoice - 1];

        String[] fileTypeOptions = {
                ".txt",
                ".csv",
        };

        int fileTypeChoice = ui.getInput().getInt("Choose your filetype((1 for TXT, 2 for CSV): ");
        String fileType = fileTypeOptions[fileTypeChoice - 1];

        CampReportOptions reportOptions = new CampReportOptions();
        reportOptions.setCampId(camp.getCampId());
        reportOptions.setFileName(fileName);
        reportOptions.setFileType(fileType);

        reportSystem.writeCampReport(reportOptions, filter, student, camp);
        return false;
    }

    /**
     * Menu function
     * 
     * @param menu menu object
     * @return true if should quit app
     */
    private boolean viewCampAttendeeList(Menu menu) {
        Camp camp = campSystem.getCampsByCommittee(student.getUserID()).get(0);
        int selCampId = camp.getCampId();
        campSystem.getCampViewerSubSystem().viewAttendeeList(student.getCampCommitteeMember(), selCampId);
        return false;
    }

    /**
     * Menu function
     * 
     * @param menu menu object
     * @return true if should quit app
     */
    private boolean viewCampCommitteeList(Menu menu) {
        Camp camp = campSystem.getCampsByCommittee(student.getUserID()).get(0);
        int selCampId = camp.getCampId();
        campSystem.getCampViewerSubSystem().viewCampCommitteeList(student.getCampCommitteeMember(), selCampId);
        return false;
    }
}
