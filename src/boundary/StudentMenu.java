package boundary;

import java.util.ArrayList;

import control.CampSystem;
import control.FeedbackSystem;
import control.ReportSystem;
import entity.Camp;
import entity.CampCommitteeMember;
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
    private final FeedbackSystem feedbackSystem;
    private final ReportSystem reportSystem;

    private Student student;
    private CampCommitteeMember committeeMember;

    public StudentMenu(ConsoleUI ui, CampSystem campSystem, FeedbackSystem feedbackSystem, ReportSystem reportSystem) {
        super(ui);
        this.campSystem = campSystem;
        this.feedbackSystem = feedbackSystem;
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
        addMenuFunction(16, this::generatePerformanceReport);
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
            Log.println("(16) Generate Enquiry Report");
            Log.println("(17) Back to Start");
        }
        int choice = -1;
        while (choice < 0) {
            if (isCommittee)
                choice = getChoice(1, 16, 17);
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
        campSystem.viewAvailableCamps(student, sortChoice);
        return false;
    }

    private boolean registerAttendee(Menu menu) {
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "register for");
        campSystem.registerAsAttendee(student, selCampId);
        return false;
    }

    private boolean registerCommittee(Menu menu) {
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "register for");
        boolean yesno = ui.getInput().getBool(
                "You will not be able to quit this camp after registering. Are you sure you want to register (Y/N)? ");
        if (yesno)
            campSystem.registerAsCommittee(student, selCampId);
        return false;
    }

    private boolean viewRegisteredCamps(Menu menu) {
        int sortChoice = menu.printCampSortOrderChoices();
        campSystem.viewRegisteredCamps(student, sortChoice);
        return false;
    }

    private boolean submitEnquiry(Menu menu) {
        // Submit Enquiry
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "submit enquiry");
        String enquiryStr = ui.getInput().getLine("Please enter enquiry: ");
        CampEnquiry enquiry = new CampEnquiry(student.getUserID(), enquiryStr, selCampId);
        feedbackSystem.addCampEnquiry(selCampId, enquiry);
        Log.println("Enquiry submitted.");
        return false;
    }

    private boolean viewEditDelPendingEnquiries(Menu menu) {
        // View/Edit/Delete Pending Enquiries
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "view/edit/delete your enquiries");
        ArrayList<CampEnquiry> studentEnquiryList = new ArrayList<>();
        studentEnquiryList = feedbackSystem.getCampEnquiries(selCampId);
        int pending = 0;
        Log.println("===Pending Enquiries===");
        for (CampEnquiry temp : studentEnquiryList) {
            boolean belongsToUser = temp.getOwner().equals(student.getUserID());
            boolean processed = temp.getReply() != null;
            if (!belongsToUser || processed)
                continue;
            else {
                pending += 1;
                Log.println("EnquiryID: " + temp.getEnquiryId());
                Log.println("StudentID: " + temp.getOwner());
                Log.println("Enquiry Status: Pending");
                Log.println("Enquiry: " + temp.getEnquiry());
                Log.println("");
            }
        }
        if (pending==0) {
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
                Boolean result = feedbackSystem.editCampEnquiry(selCampId, enquiryId,
                        newEnquiry);
                if (result)
                    Log.println("Edit successful.");
                else
                    Log.println("Edit failed.");
            }
            else if (sChoice == 2) {
                int enquiryId = ui.getInput().getInt("Please enter the enquiryId of the enquiry to delete: ");
                Boolean result = feedbackSystem.removeCampEnquiry(selCampId, enquiryId);
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

        ArrayList<CampEnquiry> processedEnquiryList = new ArrayList<>();
        processedEnquiryList = feedbackSystem.getCampEnquiries(selCampId);
        Log.println("===Processed Enquiries===");
        for (CampEnquiry temp : processedEnquiryList) {
            if (temp == null || !temp.getOwner().equals(student.getUserID()) || temp.isPending())
                continue;
            else {
                Log.println("EnquiryID: " + temp.getEnquiryId());
                Log.println("StudentID: " + temp.getOwner());
                Log.println("Enquiry Status: Processed");
                Log.println("Enquiry: " + temp.getEnquiry());
                Log.println("Reply: " + temp.getReply());
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
            campSystem.withdrawFromCamp(student, selCampId);
        return false;
    }

    // committee functions below

    private boolean submitSuggestion(Menu menu) {
        // Submit Suggestions
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "submit suggestion");
        String suggestionStr = ui.getInput().getLine("Please enter suggestion: ");
        CampSuggestion suggestion = new CampSuggestion(student.getUserID(), suggestionStr, selCampId);
        feedbackSystem.addCampSuggestion(selCampId, suggestion);
        Log.println("Suggestion submitted.");
        return false;
    }

    private boolean viewEditDelPendingSuggestions(Menu menu) {
        // View/Edit/Delete Pending Suggestions
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "view/edit/delete your suggestions");

        ArrayList<CampSuggestion> comSuggestionList = new ArrayList<>();
        comSuggestionList = feedbackSystem.getCampSuggestions(selCampId);
        int pending = 0;
        Log.println("===Pending Suggestions===");
        for (CampSuggestion temp : comSuggestionList) {
            if (temp == null || !temp.getOwner().equals(student.getUserID()) || !temp.isPending())
                continue;
            else {
                pending += 1;
                Log.println("SuggestionID: " + temp.getSuggestionId());
                Log.println("CampCommitteeMemberID: " + temp.getOwner());
                Log.println("Suggestion Status: Pending");
                Log.println("Suggestion: " + temp.getSuggestion());
                Log.println("");
            }
        }
        if (pending==0) {
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
                Boolean result = feedbackSystem.editCampSuggestion(selCampId, suggestionId,
                        newSuggestion);
                if (result)
                    Log.println("Edit successful.");
                else
                    Log.println("Edit failed.");
                break;
            } else if (cChoice == 2) {
                int suggestionId = ui.getInput().getInt("Please enter the suggestionId of the suggestion to delete: ");
                Boolean result = feedbackSystem.removeCampSuggestion(selCampId,
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
        ArrayList<CampSuggestion> processedSuggestionList = new ArrayList<>();
        processedSuggestionList = feedbackSystem.getCampSuggestions(selCampId);
        Log.println("===Processed Suggestions===");
        for (CampSuggestion temp : processedSuggestionList) {
            if (temp == null || !temp.getOwner().equals(student.getUserID()) || temp.isPending())
                continue;
            else {
                Log.println("SuggestionID: " + temp.getSuggestionId());
                Log.println("CampCommitteeMemberID: " + temp.getOwner());
                if (temp.hasApproved())
                    Log.println("Suggestion Status: Approved");
                else if (temp.hasRejected())
                    Log.println("Suggestion Status: Rejected");
                Log.println("Suggestion: " + temp.getSuggestion());
                Log.println("");
            }
        }
        return false;
    }

    private boolean viewEnquiries(Menu menu) {
        // View Camp Enquiries
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "view enquiries");

        ArrayList<CampEnquiry> enquiryList = new ArrayList<>();
        enquiryList = feedbackSystem.getCampEnquiries(selCampId);
        Log.println("===All Enquiries===");
        for (CampEnquiry temp : enquiryList) {
            if (temp == null) continue;
            Log.println("EnquiryID: " + temp.getEnquiryId());
            Log.println("StudentID: " + temp.getOwner());
            if (temp.getReply() == null) {
                Log.println("Enquiry Status: Pending");
                Log.println("Enquiry: " + temp.getEnquiry());
                Log.println("Reply: Null");
            } else {
                Log.println("Enquiry Status: Processed");
                Log.println("Enquiry: " + temp.getEnquiry());
                Log.println("Reply: " + temp.getReply());
            }
            Log.println("");
        }
        return false;
    }

    private boolean replyEnquiries(Menu menu) {
        // Reply Unprocessed Enquiries
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "reply unprocessed enquiries");

        ArrayList<CampEnquiry> pendingEnquiryList = new ArrayList<>();
        pendingEnquiryList = feedbackSystem.getCampEnquiries(selCampId);
        Log.println("===Unprocessed Enquiries===");
        for (CampEnquiry temp : pendingEnquiryList) {
            if (temp == null || !temp.isPending())
                continue;
            Log.println("EnquiryID: " + temp.getEnquiryId());
            Log.println("StudentID: " + temp.getOwner());
            Log.println("Enquiry Status: Pending");
            Log.println("Enquiry: " + temp.getEnquiry());
            Log.println("Reply: Null");
            Log.println("");
        }
        int enquiryId = ui.getInput().getInt("Please enter the enquiryId of the enquiry to reply: ");
        String reply = ui.getInput().getLine("Please enter reply: ");
        Boolean result = feedbackSystem.processCampEnquiry(student.getUserID(), selCampId, enquiryId,
                reply);
        if (result) {
            Log.println("Enquiry successfully processed.");
            student.getCampCommitteeMember().addPoints();
        } else
            Log.println("Enquiry processing failed.");
        return false;
    }

    private boolean generateCampReport(Menu menu) {

        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "generate camp report");

        Camp camp = campSystem.getCampById(selCampId);

        if (camp != null) {
            String fileName = ui.getInput().getLine("Please enter the file name: ");

            int filterChoice = ui.getInput()
                    .getInt("Please enter the filter (1 for ATTENDEE, 2 for CAMP_COMMITTEE, 3 for no filter): ");
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

            reportSystem.generateCampReport(reportOptions, student, camp);
        } else {
            Log.println("Camp not found " + selCampId);
        }
        return false;
    }

    private boolean generatePerformanceReport(Menu menu) {
        return false;
    }
}
