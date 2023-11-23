package control;

import entity.CampFeedback;
import java.util.ArrayList;

import entity.CampSuggestion;
import util.Log;

public class SuggestionSystem extends FeedbackSystem {

    public SuggestionSystem(DataStoreSystem dataStoreSystem) {
        super(dataStoreSystem);
    }

    @Override
    public ArrayList<CampFeedback> loadFeedbackFromDatastore() {
        return dataStoreSystem.getFeedbackDataStoreSubSystem().getAllSuggestions();
    }

    public boolean processCampSuggestion(String staffId, int campId, int suggestionId, boolean decision) {
        CampFeedback campFeedback = findFeedbackById(suggestionId, campId);
        if (campFeedback instanceof CampSuggestion) {
            CampSuggestion campSuggestion = (CampSuggestion) campFeedback;
            campSuggestion.setApproval(staffId, decision);
            return true;
        }
        else {
            Log.error("Feedback not suggestion for some reason");
        }
        return false;
    }

    public void printSuggestion(CampSuggestion campSuggestion) {
        Log.println("SuggestionID: " + campSuggestion.getId());
        Log.println("CampCommitteeMemberID: " + campSuggestion.getOwner());
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