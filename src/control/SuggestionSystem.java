package control;

import entity.CampCommitteeMember;
import entity.CampFeedback;
import entity.CampSuggestion;
import entity.Student;
import entity.User;
import util.Input;
import util.Log;
import util.helpers.InputHelper;

/**
 * <p>
 * A class to handle suggestion operations.
 * </p>
 * 
 * @author Yen Zhi Wei
 * @version 1.0
 * @since 23-11-2023
 */
public class SuggestionSystem extends FeedbackSystem {

    /**
     * Constructor for the suggestion system.
     * 
     * @param dataStoreSystem A class to handle all datastore operations.
     */
    public SuggestionSystem(DataStoreSystem dataStoreSystem) {
        super(dataStoreSystem);
        initFeedbackMap(dataStoreSystem.getFeedbackDataStoreSubSystem().getAllSuggestions());
    }

    /**
     * Displays all suggestions in the hashmap linked to the relevant camp.
     * 
     * @param campId campId of the camp to display suggestions.
     * @param input  Input object.
     */
    @Override
    public int printAllFeedback(int campId) {
        Log.println("===All Suggestions===");
        int size = super.printAllFeedback(campId);
        if (size == 0)
            Log.println("No suggestions found. Directing back to menu...");
        return size;
    }

    /**
     * Adds CampSuggestion object to the system.
     * 
     * @param feedback CampFeedback object to be added to the system.
     */
    @Override
    protected void addToDataStore(CampFeedback feedback) {
        if (feedback instanceof CampSuggestion)
            dataStoreSystem.getFeedbackDataStoreSubSystem().addSuggestion((CampSuggestion) feedback);
        else
            Log.error("Tried to add a non suggestion");
    }

    /**
     * Updates existing CampSuggestion object in the system.
     * 
     * @param feedback CampFeedback object to be updated to the system.
     */
    @Override
    protected void updateToDataStore(CampFeedback feedback) {
        if (feedback instanceof CampSuggestion)
            dataStoreSystem.getFeedbackDataStoreSubSystem().updateSuggestion((CampSuggestion) feedback);
        else
            Log.error("Tried to update a non suggestion");
    }

    /**
     * Deletes existing CampSuggestion object from the system.
     * 
     * @param feedback CampFeedback object to be deleted from the system.
     */
    @Override
    protected void removeFromDataStore(int feedbackId) {
        dataStoreSystem.getFeedbackDataStoreSubSystem().deleteSuggestion(feedbackId);
    }

    /**
     * Prints details of CampSuggestion
     * 
     * @param feedback CampSuggestion object to be printed.
     */
    @Override
    public void printFeedback(CampFeedback campFeedback) {
        if (!(campFeedback instanceof CampSuggestion)) {
            Log.error("Tried to print feedback of wrong type");
            return;
        }
        CampSuggestion campSuggestion = (CampSuggestion) campFeedback;
        Log.println("SuggestionID: " + campSuggestion.getId());
        Log.println("CampCommitteeMemberID: " + campSuggestion.getOwnerId());
        if (campSuggestion.isPending())
            Log.println("Suggestion Status: Pending");
        else if (campSuggestion.hasApproved())
            Log.println("Suggestion Status: Approved");
        else if (campSuggestion.hasRejected())
            Log.println("Suggestion Status: Rejected");
        Log.println("Suggestion: " + campSuggestion.getFeedback());
        Log.println("");
    }

    /**
     * Displays pending or processed suggestions in the hashmap linked to the
     * relevant camp.
     * 
     * @param campId campId of the camp to display suggestions.
     * @return size of printed list
     */
    @Override
    public int printPendingFeedback(int campId) {
        Log.println("===Unprocessed Suggestions===");
        int size = super.printPendingFeedback(campId);
        if (size == 0)
            Log.println("No unprocessed suggestions found. Directing back to menu...");
        return size;
    }

    /**
     * Displays processed suggestions belonging to camp committee member
     * in the hashmap linked to the relevant camp.
     * 
     * @param campCommitteeMemberId ID of CampCommiteeMember viewing processed
     *                              suggestions.
     * @param campId                campId of the camp to display suggestions.
     * @return size of printed list
     */
    @Override
    public int printProcessedFeedbackByOwner(String campCommitteeMemberId, int campId) {
        Log.println("===Processed Suggestions===");
        int size = super.printProcessedFeedbackByOwner(campCommitteeMemberId, campId);
        if (size == 0)
            Log.println("No processed suggestions found. Directing back to menu...");
        return size;
    }

    /**
     * View, edit and delete unprocessed suggestions in the hashmap linked to the
     * relevant camp.
     * 
     * @param campCommitteeMemberId ID of CampCommiteeMember viewing, editing and
     *                              deleting unprocessed suggestions.
     * @param campId                campId of the camp to view, edit and delete
     *                              suggestions.
     * @param input                 Input object.
     */
    public void viewEditDelFeedback(String campCommitteeMemberId, int campId, Input input) {
        Log.println("===Pending Suggestions by You===");
        int size = printPendingFeedbackByOwner(campCommitteeMemberId, campId);
        if (size == 0) {
            Log.println("No pending suggestions found. Directing back to menu...");
            return;
        }
        Log.println("===Please select the following options=== ");
        Log.println("(1) Edit Suggestion");
        Log.println("(2) Delete Suggestion");
        Log.println("(3) Back to Student Menu");
        int cChoice = -1;
        while (cChoice < 0) {
            cChoice = input.getInt("Enter choice: ");
            if (cChoice == 1) {
                int suggestionId = InputHelper.getSuggestionIdFromUser(input, this, "edit", campId);
                String newSuggestion = input.getLine("Please enter new suggestion: ");
                CampFeedback campFeedback = editCampFeedback(campId, suggestionId,
                        newSuggestion);
                if (campFeedback != null)
                    Log.println("Edit successful.");
                else
                    Log.println("Edit failed.");
                break;
            } else if (cChoice == 2) {
                int suggestionId = InputHelper.getSuggestionIdFromUser(input, this, "delete", campId);
                Boolean result = removeCampFeedback(campId, suggestionId);
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
    }

    /**
     * Process suggestions in the hashmap linked to the relevant camp.
     * 
     * @param staffId      ID of Staff processing the suggestions.
     * @param campId       campId of the camp to process suggestions.
     * @param suggestionId ID of suggesstion to be processed.
     * @param decision     Decision of staff to approve/reject suggestion.
     * @return success of request
     */
    public boolean processCampSuggestion(String staffId, int campId, int suggestionId, boolean decision) {
        CampFeedback campFeedback = findFeedbackById(suggestionId, campId);
        if (campFeedback == null || campFeedback instanceof CampSuggestion) {
            CampSuggestion campSuggestion = (CampSuggestion) campFeedback;
            campSuggestion.setApproval(staffId, decision);
            // find committe member from data store
            User user = dataStoreSystem.getUserDataStoreSubSystem().queryStudent(campSuggestion.getOwnerId());
            if (!(user instanceof Student)) {
                Log.error("Committee member not found while trying to award points for suggestion.");
                return false;
            }
            //retrieve it from datastore since user is currently staff
            CampCommitteeMember commmitteeMember = ((Student) user).getCampCommitteeMember();
            // award points
            if (decision) {
                commmitteeMember.addPoints(2);
            } else {
                commmitteeMember.addPoints(1);
            }
            updateToDataStore(campSuggestion);
            dataStoreSystem.getUserDataStoreSubSystem()
                    .updateCommitteeMemberDetails(commmitteeMember);
            return true;
        } else {
            Log.error("Feedback not suggestion for some reason");
        }
        return false;
    }
}