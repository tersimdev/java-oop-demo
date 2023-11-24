package control;

import java.util.ArrayList;

import entity.CampFeedback;
import entity.CampSuggestion;
import util.Input;
import util.Log;

public class SuggestionSystem extends FeedbackSystem {

    public SuggestionSystem(DataStoreSystem dataStoreSystem) {
        super(dataStoreSystem);
        initFeedbackMap(dataStoreSystem.getFeedbackDataStoreSubSystem().getAllSuggestions());
    }

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

    public boolean processCampSuggestion(String staffId, int campId, int suggestionId, boolean decision) {
        CampFeedback campFeedback = findFeedbackById(suggestionId, campId);
        if (campFeedback instanceof CampSuggestion) {
            CampSuggestion campSuggestion = (CampSuggestion) campFeedback;
            campSuggestion.setApproval(staffId, decision);
            if(decision) {
                (campSuggestion.getCampCommitteeMember()).addPoints(2);
            }
            else
                (campSuggestion.getCampCommitteeMember()).addPoints(1);
            return true;
        } else {
            Log.error("Feedback not suggestion for some reason");
        }
        return false;
    }

    @Override
    public void addToDataStore(CampFeedback feedback) {
        if (feedback instanceof CampSuggestion)
            dataStoreSystem.getFeedbackDataStoreSubSystem().addSuggestion((CampSuggestion) feedback);
        else
            Log.error("Tried to add a non suggestion");
    }

    @Override
    public void updateToDataStore(CampFeedback feedback) {
        if (feedback instanceof CampSuggestion)
            dataStoreSystem.getFeedbackDataStoreSubSystem().updateSuggestion((CampSuggestion) feedback);
        else
            Log.error("Tried to update a non suggestion");
    }

    @Override
    public void removeFromDataStore(int feedbackId) {
        dataStoreSystem.getFeedbackDataStoreSubSystem().deleteSuggestion(feedbackId);
    }
    
    public void printSuggestion(CampSuggestion campSuggestion) {
        Log.println("SuggestionID: " + campSuggestion.getId());
        Log.println("CampCommitteeMemberID: " + campSuggestion.getOwnerId());
        if(campSuggestion.isPending())
            Log.println("Suggestion Status: Pending");
        else if (campSuggestion.hasApproved())
            Log.println("Suggestion Status: Approved");
        else if (campSuggestion.hasRejected())
            Log.println("Suggestion Status: Rejected");
        Log.println("Suggestion: " + campSuggestion.getFeedback());
        Log.println("");
    }
}