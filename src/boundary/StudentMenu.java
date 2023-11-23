package boundary;

import java.util.ArrayList;

import control.CampSystem;
import control.FeedbackSystem;
import control.EnquirySystem;
import control.SuggestionSystem;
import control.ReportSystem;
import entity.Camp;
import entity.CampCommitteeMember;
import entity.CampFeedback;
import entity.CampEnquiry;
import entity.CampReportFilter;
import entity.CampReportOptions;
import entity.CampSuggestion;
import entity.Student;
import util.InputHelper;
import util.Log;

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

    private final CampSystem campSystem;
    // private final FeedbackSystem feedbackSystem;
    private final EnquirySystem enquirySystem;
    private final SuggestionSystem suggestionSystem;
    private final ReportSystem reportSystem;

    private Student student;
    private CampCommitteeMember committeeMember;

    public StudentMenu(ConsoleUI ui, CampSystem campSystem, EnquirySystem enquirySystem,
            SuggestionSystem suggestionSystem, ReportSystem reportSystem) {
        super(ui);
        this.campSystem = campSystem;
        // this.feedbackSystem = feedbackSystem;
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
        addMenuFunction(12, this::viewSuggestions);
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

            runMenuFunction(choice); // use function map defined in constructor
        }
        return false;
    }

    private boolean viewAvailableCamps(Menu menu) {
        int sortChoice = menu.printCampSortOrderChoices();
        campSystem.getCampViewerSubSystem().viewAvailableCamps(student, sortChoice);
        return false;
    }

    private boolean registerAttendee(Menu menu) {
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "register for");
        campSystem.getCampRegistrationSubSystem().registerAsAttendee(student, selCampId);
        return false;
    }

    private boolean registerCommittee(Menu menu) {
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "register for");
        boolean yesno = ui.getInput().getBool(
                "You will not be able to quit this camp after registering. Are you sure you want to register (Y/N)? ");
        if (yesno)
            campSystem.getCampRegistrationSubSystem().registerAsCommittee(student, selCampId);
        return false;
    }

    private boolean viewRegisteredCamps(Menu menu) {
        int sortChoice = menu.printCampSortOrderChoices();
        campSystem.getCampViewerSubSystem().viewRegisteredCamps(student, sortChoice);
        return false;
    }

    private boolean submitEnquiry(Menu menu) {
        // Submit Enquiry
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "submit enquiry");
        String enquiryStr = ui.getInput().getLine("Please enter enquiry: ");
        CampEnquiry enquiry = new CampEnquiry(student.getUserID(), enquiryStr, selCampId);
        enquirySystem.addCampFeedback(selCampId, enquiry);
        Log.println("Enquiry submitted.");
        return false;
    }

    private boolean viewEditDelPendingEnquiries(Menu menu) {
        // View/Edit/Delete Pending Enquiries
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "view/edit/delete your enquiries");
        ArrayList<CampFeedback> studentEnquiryList = new ArrayList<>();
        studentEnquiryList = enquirySystem.getCampFeedbacks(selCampId);
        int pending = 0;
        Log.println("===Pending Enquiries===");
        for (CampFeedback campFeedback : studentEnquiryList) {
            if (campFeedback == null || !(campFeedback instanceof CampEnquiry))
                continue;
            CampEnquiry campEnquiry = (CampEnquiry) campFeedback;
            boolean belongsToUser = campEnquiry.getOwner().equals(student.getUserID());
            boolean processed = campEnquiry.getReply() != null;
            if (!belongsToUser || processed)
                continue;
            else {
                pending += 1;
                Log.println("EnquiryID: " + campEnquiry.getFeedbackId());
                Log.println("StudentID: " + campEnquiry.getOwner());
                Log.println("Enquiry Status: Pending");
                Log.println("Enquiry: " + campEnquiry.getFeedback());
                Log.println("");
            }
        }
        if (pending == 0) {
            Log.println("No pending enquiries found. Directing back to menu...");
            return false;
        }
        Log.println("===Please select the following options=== ");
        Log.println("(1) Edit Enquiry");
        Log.println("(2) Delete Enquiry");
        Log.println("(3) Back to Student Menu");
        int sChoice = -1;
        while (sChoice < 0) {
            sChoice = ui.getInput().getInt("Enter choice: ");
            if (sChoice == 1) {
                int enquiryId = ui.getInput().getInt("Please enter the enquiryId of the enquiry to edit: ");
                String newEnquiry = ui.getInput().getLine("Please enter new enquiry: ");
                Boolean result = enquirySystem.editCampFeedback(selCampId, enquiryId,
                        newEnquiry);
                if (result)
                    Log.println("Edit successful.");
                else
                    Log.println("Edit failed.");
            } else if (sChoice == 2) {
                int enquiryId = ui.getInput().getInt("Please enter the enquiryId of the enquiry to delete: ");
                Boolean result = enquirySystem.removeCampFeedback(selCampId, enquiryId);
                if (result)
                    Log.println("Deletion successful.");
                else
                    Log.println("Deletion failed.");
            }
        }
        return false;
    }

    private boolean viewEnquiryReplies(Menu menu) {
        // View Processed Enquiry Replies
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "view processed enquiries");

        ArrayList<CampFeedback> processedEnquiryList = new ArrayList<>();
        processedEnquiryList = enquirySystem.getCampFeedbacks(selCampId);
        Log.println("===Processed Enquiries===");
        for (CampFeedback temp : processedEnquiryList) {
            if (!(temp instanceof CampEnquiry))
                continue;
            CampEnquiry campEnquiry = (CampEnquiry) temp;
            if (campEnquiry == null || !campEnquiry.getOwner().equals(student.getUserID()) || campEnquiry.isPending())
                continue;
            else {
                Log.println("EnquiryID: " + campEnquiry.getFeedbackId());
                Log.println("StudentID: " + campEnquiry.getOwner());
                Log.println("Enquiry Status: Processed");
                Log.println("Enquiry: " + campEnquiry.getFeedback());
                Log.println("Reply: " + campEnquiry.getReply());
                Log.println("");
            }
        }
        return false;
    }

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

    private boolean submitSuggestion(Menu menu) {
        // Submit Suggestions
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "submit suggestion");
        String suggestionStr = ui.getInput().getLine("Please enter suggestion: ");
        CampSuggestion suggestion = new CampSuggestion(student.getUserID(), suggestionStr, selCampId);
        suggestionSystem.addCampFeedback(selCampId, suggestion);
        Log.println("Suggestion submitted.");
        return false;
    }

    private boolean viewEditDelPendingSuggestions(Menu menu) {
        // View/Edit/Delete Pending Suggestions
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "view/edit/delete your suggestions");

        ArrayList<CampFeedback> comSuggestionList = new ArrayList<>();
        comSuggestionList = suggestionSystem.getCampFeedbacks(selCampId);
        int pending = 0;
        Log.println("===Pending Suggestions===");
        for (CampFeedback campFeedback : comSuggestionList) {
             if (!(campFeedback instanceof CampSuggestion))
                continue;
            CampSuggestion campSuggestion = (CampSuggestion) campFeedback;
            if (campSuggestion == null || !campSuggestion.getOwner().equals(student.getUserID()) || !campSuggestion.isPending())
                continue;
            else {
                pending += 1;
                Log.println("SuggestionID: " + campSuggestion.getFeedbackId());
                Log.println("CampCommitteeMemberID: " + campSuggestion.getOwner());
                Log.println("Suggestion Status: Pending");
                Log.println("Suggestion: " + campSuggestion.getFeedback());
                Log.println("");
            }
        }
        if (pending == 0) {
            Log.println("No pending suggestions found. Directing back to menu...");
            return false;
        }
        Log.println("===Please select the following options=== ");
        Log.println("(1) Edit Suggestion");
        Log.println("(2) Delete Suggestion");
        Log.println("(3) Back to Student Menu");
        int cChoice = -1;
        while (cChoice < 0) {
            cChoice = ui.getInput().getInt("Enter choice: ");
            if (cChoice == 1) {
                int suggestionId = ui.getInput().getInt("Please enter the suggestionId of the suggestion to edit: ");
                String newSuggestion = ui.getInput().getLine("Please enter new suggestion: ");
                Boolean result = suggestionSystem.editCampFeedback(selCampId, suggestionId,
                        newSuggestion);
                if (result)
                    Log.println("Edit successful.");
                else
                    Log.println("Edit failed.");
                break;
            } else if (cChoice == 2) {
                int suggestionId = ui.getInput().getInt("Please enter the suggestionId of the suggestion to delete: ");
                Boolean result = suggestionSystem.removeCampFeedback(selCampId,
                        suggestionId);
                if (result)
                    Log.println("Deletion successful.");
                else
                    Log.println("Deletion failed.");
                break;
            } else if (cChoice == 3)
                break;
            else {
                Log.println("Invalid choice! Try again.");
                cChoice = -1;
            }
        }
        return false;
    }

    private boolean viewSuggestions(Menu menu) {
        // View Processed Suggestions
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "view processed suggestions");
        ArrayList<CampFeedback> processedSuggestionList = new ArrayList<>();
        processedSuggestionList = suggestionSystem.getCampFeedbacks(selCampId);
        Log.println("===Processed Suggestions===");
        for (CampFeedback campFeedback : processedSuggestionList) {
            if (!(campFeedback instanceof CampSuggestion))
                continue;
            CampSuggestion campSuggestion = (CampSuggestion) campFeedback;
            if (campSuggestion == null || !campSuggestion.getOwner().equals(student.getUserID()) || campSuggestion.isPending())
                continue;
            else {
                Log.println("SuggestionID: " + campSuggestion.getFeedbackId());
                Log.println("CampCommitteeMemberID: " + campSuggestion.getOwner());
                if (campSuggestion.hasApproved())
                    Log.println("Suggestion Status: Approved");
                else if (campSuggestion.hasRejected())
                    Log.println("Suggestion Status: Rejected");
                Log.println("Suggestion: " + campSuggestion.getFeedback());
                Log.println("");
            }
        }
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
        Boolean result = enquirySystem.processCampEnquiry(student.getUserID(), selCampId, enquiryId,
                reply);
        if (result) {
            Log.println("Enquiry successfully processed.");
            student.getCampCommitteeMember().addPoints();
        } else
            Log.println("Enquiry processing failed.");
        return false;
    }

    private boolean generateCampReport(Menu menu) {
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "Generate camp report");
        Camp camp = campSystem.getCampById(selCampId);

        CampReportFilter[] filterChoicesOptions = {
            CampReportFilter.ATTENDEE,
            CampReportFilter.CAMP_COMMITTEE,
            CampReportFilter.NONE,
        };
    
        if (camp != null) {
            String fileName = ui.getInput().getLine("Please enter the file name: ");

            //CampReportOptions reportOptions = ReportInputHelper.getOptionsFromUser();

            int filterChoice = ui.getInput().getInt("Please enter the filter (1 for ATTENDEE, 2 for CAMP_COMMITTEE, 3 for no filter): ");
            CampReportFilter filter = filterChoicesOptions[filterChoice-1];

            String[] fileTypeOptions = {
                ".txt",
                ".csv",
            };

            int fileTypeChoice = ui.getInput().getInt("Choose your filetype((1 for TXT, 2 for CSV): ");
            String fileType = fileTypeOptions[fileTypeChoice -1];
    
            CampReportOptions reportOptions = new CampReportOptions();
            reportOptions.setCampId(camp.getCampId());
            reportOptions.setFileName(fileName);
            reportOptions.setFileType(fileType);
    
            reportSystem.generateCampReport(reportOptions, filter, student, camp);
        } else {
            Log.println("Camp not found " + selCampId);
        }
        return false;
    }

    private boolean viewCampAttendeeList(Menu menu) {
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "inspect");
        campSystem.getCampViewerSubSystem().viewAttendeeList(student.getCampCommitteeMember(), selCampId);
        return false;
    }

    private boolean viewCampCommitteeList(Menu menu) {
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "inspect");
        campSystem.getCampViewerSubSystem().viewCampCommitteeList(student.getCampCommitteeMember(), selCampId);
        return false;
    }
}
