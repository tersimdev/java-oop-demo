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
        return dataStoreSystem.getAllSuggestions();
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

}