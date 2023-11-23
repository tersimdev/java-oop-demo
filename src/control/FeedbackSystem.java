package control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import entity.CampFeedback;
import entity.CampEnquiry;
import entity.CampSuggestion;
import util.Log;
import util.Input;

/**
 * <p>
 * A singleton class to store enquiries and suggestions
 * </p>
 * 
 * @author Yen Zhi Wei
 * @version 1.0
 * @since 1-11-2023
 */
public abstract class FeedbackSystem {
    protected DataStoreSystem dataStoreSystem;
    /*
     * private Map<Integer, ArrayList<CampEnquiry>> enquiriesMap;
     * private Map<Integer, ArrayList<CampSuggestion>> suggestionsMap;
     */
    protected Map<Integer, ArrayList<CampFeedback>> feedbacksMap;
    protected int nextFeedbackId;

    /*
     * private int nextEnquiryId;
     * private int nextSuggetionId;
     */

    public FeedbackSystem(DataStoreSystem dataStoreSystem) {
        this.feedbacksMap = new HashMap<>();
        this.dataStoreSystem = dataStoreSystem;

        ArrayList<CampFeedback> feedbackList = loadFeedbackFromDatastore();

        // store in system, use these as next id
        nextFeedbackId = 0;
        if (feedbackList.size() > 0)
            nextFeedbackId = feedbackList.get(feedbackList.size() - 1).getId() + 1;

        // add to map based on camp id
        for (CampFeedback cf : feedbackList) {
            if (!feedbacksMap.containsKey(cf.getCampId()))
                feedbacksMap.put(cf.getCampId(), new ArrayList<>());
            feedbacksMap.get(cf.getCampId()).add(cf);
        }
    }

    public abstract ArrayList<CampFeedback> loadFeedbackFromDatastore();

    public void addCampFeedback(int campId, CampFeedback feedback) {
        ArrayList<CampFeedback> feedbacks = feedbacksMap.computeIfAbsent(campId, k -> new ArrayList<>());
        // Index in the ArrayList used as enquiryID
        int feedbackId = nextFeedbackId++;
        feedback.setId(feedbackId);
        // Add the new feedback to the ArrayList
        feedbacks.add(feedback);
        // TODO dataStoreSystem.addFeedback(feedback);
    }

    public boolean editCampFeedback(int campId, int feedbackId, String newFeedback) {
        CampFeedback campFeedback = findFeedbackById(feedbackId, campId);
        if (campFeedback != null) {
            campFeedback.setFeedback(newFeedback);
            // TODO dataStoreSystem.edit(feedback);

            return true;
        }
        return false;
    }

    public boolean removeCampFeedback(int campId, int feedbackId) {
        CampFeedback campFeedback = findFeedbackById(feedbackId, campId);
        if (campFeedback != null) {
            getCampFeedbacks(campId).remove(campFeedback);
            // TODO remove from datastore
            return true;
        }
        return false;
    }

    public ArrayList<CampFeedback> getCampFeedbacks(int campId) {
        return feedbacksMap.getOrDefault(campId, new ArrayList<>());
    }

    public CampFeedback findFeedbackById(int feedbackId, int campId) {
        for (CampFeedback cf : getCampFeedbacks(campId)) {
            if (cf.getId() == feedbackId)
                return cf;
        }
        return null;
    }

    public boolean checkValidFeedbackId(int feedbackId, int campId) {
        return findFeedbackById(feedbackId, campId) != null;
    }

}
