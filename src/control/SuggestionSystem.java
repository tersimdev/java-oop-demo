package control;

import entity.CampFeedback;
import entity.CampSuggestion;
import util.Log;

public class SuggestionSystem extends FeedbackSystem {

    public SuggestionSystem(DataStoreSystem dataStoreSystem) {
        super(dataStoreSystem);
        initFeedbackMap(dataStoreSystem.getFeedbackDataStoreSubSystem().getAllSuggestions());
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