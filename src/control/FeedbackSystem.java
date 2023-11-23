package control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import entity.CampFeedback;

/**
 * <p>
 * A singleton class to store enquiries and suggestions
 * </p>
 * 
 * @author Yen Zhi Wei
 * @version 1.0
 * @since 23-11-2023
 */
public abstract class FeedbackSystem {
    protected DataStoreSystem dataStoreSystem;
    protected Map<Integer, ArrayList<CampFeedback>> feedbacksMap;
    protected int nextFeedbackId;

    public FeedbackSystem(DataStoreSystem dataStoreSystem) {
        this.feedbacksMap = new HashMap<>();
        this.dataStoreSystem = dataStoreSystem;
    }

    public abstract void addToDataStore(CampFeedback feedback);
    public abstract void updateToDataStore(CampFeedback feedback);
    public abstract void removeFromDataStore(int feedbackId);

    protected void initFeedbackMap(ArrayList<CampFeedback> feedbackList) {
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

    public void addCampFeedback(int campId, CampFeedback feedback) {
        ArrayList<CampFeedback> feedbacks = feedbacksMap.computeIfAbsent(campId, k -> new ArrayList<>());
        // Index in the ArrayList used as enquiryID
        int feedbackId = nextFeedbackId++;
        feedback.setId(feedbackId);
        // Add the new feedback to the ArrayList
        feedbacks.add(feedback);
        addToDataStore(feedback);
    }

    public CampFeedback editCampFeedback(int campId, int feedbackId, String newFeedback) {
        CampFeedback campFeedback = findFeedbackById(feedbackId, campId);
        if (campFeedback != null) {
            campFeedback.setFeedback(newFeedback);
            updateToDataStore(campFeedback);
        }
        return campFeedback;
    }

    public boolean removeCampFeedback(int campId, int feedbackId) {
        CampFeedback campFeedback = findFeedbackById(feedbackId, campId);
        if (campFeedback != null) {
            getCampFeedbacks(campId).remove(campFeedback);
            removeFromDataStore(feedbackId);
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
