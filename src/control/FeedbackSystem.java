package control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import entity.CampFeedback;

/**
 * <p>
 * A class to handle feedback operations.
 * </p>
 * 
 * @author Yen Zhi Wei
 * @version 1.0
 * @since 23-11-2023
 */
public abstract class FeedbackSystem {
    
    protected Map<Integer, ArrayList<CampFeedback>> feedbacksMap;
    protected int nextFeedbackId;
    protected DataStoreSystem dataStoreSystem;

    /**
     * Constructor for the feedback system.
     * @param dataStoreSystem A class to handle all datastore operations.
     */
    public FeedbackSystem(DataStoreSystem dataStoreSystem) {
        this.feedbacksMap = new HashMap<>();
        this.dataStoreSystem = dataStoreSystem;
    }

    /**
     * Adds CampFeedback object to the system.
     * @param feedback CampFeedback object to be added to the system.
     */
    public abstract void addToDataStore(CampFeedback feedback);

    /**
     * Updates existing CampFeedback object in the system.
     * @param feedback CampFeedback object to be updated to the system.
     */
    public abstract void updateToDataStore(CampFeedback feedback);

    /**
     * Deletes existing CampFeedback object from the system.
     * @param feedback CampFeedback object to be deleted from the system.
     */    
    public abstract void removeFromDataStore(int feedbackId);

    /**
     * Creates the mapping of the feedback to relevant campIds.
     * @param feedbackList feedbackList where feedback is stored.
     */
    protected void initFeedbackMap(ArrayList<CampFeedback> feedbackList) {
        // Store in system, use these as next id
        nextFeedbackId = 0;
        if (feedbackList.size() > 0)
            nextFeedbackId = feedbackList.get(feedbackList.size() - 1).getId() + 1;

        // Add to map based on camp id
        for (CampFeedback cf : feedbackList) {
            if (!feedbacksMap.containsKey(cf.getCampId()))
                feedbacksMap.put(cf.getCampId(), new ArrayList<>());
            feedbacksMap.get(cf.getCampId()).add(cf);
        }
    }

    /**
     * Adds a new feedback to the hashmap.
     * @param campId campId of the camp to submit feedback.
     * @param feedback CampFeedback submitted by the student.
     */
    public void addCampFeedback(int campId, CampFeedback feedback) {
        ArrayList<CampFeedback> feedbacks = feedbacksMap.computeIfAbsent(campId, k -> new ArrayList<>());
        // Index in the ArrayList used as enquiryID
        int feedbackId = nextFeedbackId++;
        feedback.setId(feedbackId);
        // Add the new feedback to the ArrayList
        feedbacks.add(feedback);
        addToDataStore(feedback);
    }

    /**
     * Edits an existing feedback in the hashmap.
     * @param campId campId of the camp to edit feedback.
     * @param feedbackId feedbackId of the feedback to be edited.
     * @param feedback new CampFeedback submitted by the student.
     */
    public CampFeedback editCampFeedback(int campId, int feedbackId, String newFeedback) {
        CampFeedback campFeedback = findFeedbackById(feedbackId, campId);
        if (campFeedback != null) {
            campFeedback.setFeedback(newFeedback);
            updateToDataStore(campFeedback);
        }
        return campFeedback;
    }

    /**
     * Deletes an existing feedback from the hashmap
     * @param campId campId of the camp to delete feedback.
     * @param feedbackId feedbackId of the feedback to be deleted.
     */
    public boolean removeCampFeedback(int campId, int feedbackId) {
        CampFeedback campFeedback = findFeedbackById(feedbackId, campId);
        if (campFeedback != null) {
            getCampFeedbacks(campId).remove(campFeedback);
            removeFromDataStore(feedbackId);
            return true;
        }
        return false;
    }

    /**
     * Retrieves the hashmap containing the feedback.
     * @param campId campId of the camp to retrieve feedback.
     */
    public ArrayList<CampFeedback> getCampFeedbacks(int campId) {
        return feedbacksMap.getOrDefault(campId, new ArrayList<>());
    }

    /**
     * Searches the hashmap containing the feedback by feedbackId.
     * @param feedbackId feedbackId of the feedback to be searched.
     * @param campId campId of the camp to search feedback.
     */
    public CampFeedback findFeedbackById(int feedbackId, int campId) {
        for (CampFeedback cf : getCampFeedbacks(campId)) {
            if (cf.getId() == feedbackId)
                return cf;
        }
        return null;
    }

    /**
     * Checks whether a feedback is valid and exists in the hashmap.
     * @param feedbackId feedbackId of the feedback to be checked.
     * @param campId campId of the camp to check feedback.
     */
    public boolean checkValidFeedbackId(int feedbackId, int campId) {
        return findFeedbackById(feedbackId, campId) != null;
    }
}
