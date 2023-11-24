package control;

import java.util.ArrayList;

import entity.CampFeedback;
import entity.CampSuggestion;
import util.Input;
import util.Log;

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
    public void viewAllSuggestions(int campId, Input input) {
        ArrayList<CampFeedback> suggestionList = new ArrayList<>();
        suggestionList = getCampFeedbacks(campId);
        int suggestions = 0;
        Log.println("===All Suggestions===");
        for (CampFeedback campFeedback : suggestionList) {
            if (!(campFeedback instanceof CampSuggestion))
                continue;
            CampSuggestion campSuggestion = (CampSuggestion) campFeedback;
            suggestions += 1;
            printSuggestion(campSuggestion);
        }
        if (suggestions == 0) {
            Log.println("No suggestions found. Directing back to menu...");
            return;
        }
    }

    /**
     * Displays unprocessed suggestions in the hashmap linked to the relevant camp.
     * 
     * @param campId campId of the camp to display suggestions.
     * @param input  Input object.
     */
    public void viewUnprocessedSuggestions(int campId, Input input) {
        ArrayList<CampFeedback> pendingSuggestionList = new ArrayList<>();
        pendingSuggestionList = getCampFeedbacks(campId);
        Log.println("===Unprocessed Suggestions===");
        int suggestions = 0;
        for (CampFeedback campFeedback : pendingSuggestionList) {
            if (campFeedback == null || !(campFeedback instanceof CampSuggestion))
                continue;
            CampSuggestion campSuggestion = (CampSuggestion) campFeedback;
            boolean processed = !campSuggestion.isPending();
            if (processed)
                continue;
            else {
                suggestions += 1;
                printSuggestion(campSuggestion);
            }
        }
        if (suggestions == 0) {
            Log.println("No unprocessed suggestions found. Directing back to menu...");
            return;
        }
    }

    /**
     * Displays processed suggestions in the hashmap linked to the relevant camp.
     * 
     * @param campCommitteeMemberId ID of CampCommiteeMember viewing processed
     *                              suggestions.
     * @param campId                campId of the camp to display suggestions.
     * @param input                 Input object.
     */
    public void viewProcessedSuggestions(String campCommitteeMemberId, int campId, Input input) {
        ArrayList<CampFeedback> processedSuggestionList = new ArrayList<>();
        processedSuggestionList = getCampFeedbacks(campId);
        int suggestions = 0;
        Log.println("===Processed Suggestions===");
        for (CampFeedback campFeedback : processedSuggestionList) {
            if (campFeedback == null || !(campFeedback instanceof CampSuggestion))
                continue;
            CampSuggestion campSuggestion = (CampSuggestion) campFeedback;
            boolean belongsToUser = campSuggestion.getOwnerId().equals(campCommitteeMemberId);
            boolean processed = !campSuggestion.isPending();
            if (!belongsToUser || !processed)
                continue;
            else {
                suggestions += 1;
                printSuggestion(campSuggestion);
            }
        }
        if (suggestions == 0) {
            Log.println("No processed suggestions found. Directing back to menu...");
            return;
        }
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
    public void viewEditDelSuggestions(String campCommitteeMemberId, int campId, Input input) {
        ArrayList<CampFeedback> comSuggestionList = new ArrayList<>();
        comSuggestionList = getCampFeedbacks(campId);
        int pending = 0;
        Log.println("===Pending Suggestions===");
        for (CampFeedback campFeedback : comSuggestionList) {
            if (campFeedback == null || !(campFeedback instanceof CampSuggestion))
                continue;
            CampSuggestion campSuggestion = (CampSuggestion) campFeedback;
            boolean belongsToUser = campSuggestion.getOwnerId().equals(campCommitteeMemberId);
            boolean processed = !campSuggestion.isPending();
            if (!belongsToUser || processed)
                continue;
            else {
                pending += 1;
                printSuggestion(campSuggestion);
            }
        }
        if (pending == 0) {
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
                int suggestionId = input.getInt("Please enter the suggestionId of the suggestion to edit: ");
                String newSuggestion = input.getLine("Please enter new suggestion: ");
                CampFeedback campFeedback = editCampFeedback(campId, suggestionId,
                        newSuggestion);
                if (campFeedback != null)
                    Log.println("Edit successful.");
                else
                    Log.println("Edit failed.");
                break;
            } else if (cChoice == 2) {
                int suggestionId = input.getInt("Please enter the suggestionId of the suggestion to delete: ");
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
     */
    public boolean processCampSuggestion(String staffId, int campId, int suggestionId, boolean decision) {
        CampFeedback campFeedback = findFeedbackById(suggestionId, campId);
        if (campFeedback == null || campFeedback instanceof CampSuggestion) {
            CampSuggestion campSuggestion = (CampSuggestion) campFeedback;
            campSuggestion.setApproval(staffId, decision);
            if (decision) {
                (campSuggestion.getCampCommitteeMember()).addPoints(2);
            } else {
                (campSuggestion.getCampCommitteeMember()).addPoints(1);
            }
            updateToDataStore(campSuggestion);
            dataStoreSystem.getUserDataStoreSubSystem()
                    .updateCommitteeMemberDetails(campSuggestion.getCampCommitteeMember());
            return true;
        } else {
            Log.error("Feedback not suggestion for some reason");
        }
        return false;
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
    public void printSuggestion(CampSuggestion campSuggestion) {
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
}