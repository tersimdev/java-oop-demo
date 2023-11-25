package control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import entity.CampFeedback;
import util.Input;

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

    /**
     * Hashmap of campIds to list of feedbacks
     */
    protected Map<Integer, ArrayList<CampFeedback>> feedbacksMap;
    /**
     * Stores current biggest feedback id, used to get next id
     */
    protected int currFeedbackId;
    /**
     * Dependency Injection 
     */
    protected DataStoreSystem dataStoreSystem;

    /**
     * Constructor for the feedback system.
     * 
     * @param dataStoreSystem A class to handle all datastore operations.
     */
    public FeedbackSystem(DataStoreSystem dataStoreSystem) {
        this.feedbacksMap = new HashMap<>();
        this.dataStoreSystem = dataStoreSystem;
    }

    /**
     * Adds CampFeedback object to the system.
     * 
     * @param feedback CampFeedback object to be added to the system.
     */
    protected abstract void addToDataStore(CampFeedback feedback);

    /**
     * Updates existing CampFeedback object in the system.
     * 
     * @param feedback CampFeedback object to be updated to the system.
     */
    protected abstract void updateToDataStore(CampFeedback feedback);

    /**
     * Deletes existing CampFeedback object from the system.
     * 
     * @param feedbackId id of the CampFeedback object to be deleted from the
     *                   system.
     */
    protected abstract void removeFromDataStore(int feedbackId);

    /**
     * Prints details of CampFeedback
     * 
     * @param feedback CampFeedback object to be printed.
     */
    public abstract void printFeedback(CampFeedback feedback);

    /**
     * View, edit and delete unprocessed feedback in the hashmap linked to the
     * relevant camp.
     * 
     * @param ownerId ID of owner viewing, editing and
     *                deleting unprocessed feedback.
     * @param campId  campId of the camp to view, edit and delete
     *                suggestions.
     * @param input   Input object.
     */
    public abstract void viewEditDelFeedback(String ownerId, int campId, Input input);

    // below are functions common to subclasses. They can be extended, by overriding
    // and calling super.
    /**
     * Creates the mapping of the feedback to relevant campIds.
     * 
     * @param feedbackList feedbackList where feedback is stored.
     */
    protected void initFeedbackMap(ArrayList<CampFeedback> feedbackList) {
        // Store in system, use these as next id
        currFeedbackId = 0;
        if (feedbackList.size() > 0)
            currFeedbackId = feedbackList.get(feedbackList.size() - 1).getId() + 1;

        // Add to map based on camp id
        for (CampFeedback cf : feedbackList) {
            if (!feedbacksMap.containsKey(cf.getCampId()))
                feedbacksMap.put(cf.getCampId(), new ArrayList<>());
            feedbacksMap.get(cf.getCampId()).add(cf);
        }
    }

    /**
     * Adds a new feedback to the hashmap.
     * 
     * @param campId   campId of the camp to submit feedback.
     * @param feedback CampFeedback submitted by the student.
     */
    public void addCampFeedback(int campId, CampFeedback feedback) {
        ArrayList<CampFeedback> feedbacks = feedbacksMap.computeIfAbsent(campId, k -> new ArrayList<>());
        // Index in the ArrayList used as enquiryID
        int feedbackId = currFeedbackId++;
        feedback.setId(feedbackId);
        // Add the new feedback to the ArrayList
        feedbacks.add(feedback);
        addToDataStore(feedback);
    }

    /**
     * Edits an existing feedback in the hashmap.
     * 
     * @param campId      campId of the camp to edit feedback.
     * @param feedbackId  feedbackId of the feedback to be edited.
     * @param newFeedback new CampFeedback submitted by the student.
     * @return edited feedback object
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
     * 
     * @param campId     campId of the camp to delete feedback.
     * @param feedbackId feedbackId of the feedback to be deleted.
     * @return whether feedback could be removed
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
     * Returns all feedback
     * 
     * @param campId campId of the camp to retrieve feedback.
     * @return array list of feedbacks
     */
    public ArrayList<CampFeedback> getCampFeedbacks(int campId) {
        return feedbacksMap.getOrDefault(campId, new ArrayList<>());
    }

    /**
     * Retrieves the hashmap containing the feedback.
     * Returns either processed or unprocessed feedback.
     * 
     * @param campId  campId of the camp to retrieve feedback.
     * @param pending true to retrieve only unprocessed, false to receive only
     *                processed
     * @return array list of feedbacks
     */
    public ArrayList<CampFeedback> getCampFeedbacks(int campId, boolean pending) {
        ArrayList<CampFeedback> ret = new ArrayList<>();
        ArrayList<CampFeedback> allFeedback = getCampFeedbacks(campId);
        for (CampFeedback cf : allFeedback) {
            if (pending && cf.isPending()) // add processed
                ret.add(cf);
            else if (!pending && !cf.isPending()) // add unprocessed
                ret.add(cf);
        }

        return ret;
    }

    /**
     * Retrieves the hashmap containing the feedback.
     * Returns either processed or unprocessed feedback, that
     * only belongs to the given ownerId.
     * 
     * @param campId  campId of the camp to retrieve feedback.
     * @param pending true to retrieve only unprocessed, false to receive only
     *                processed
     * @param ownerId Id that the feedback belongs to
     * @return array list of feedbacks
     */
    public ArrayList<CampFeedback> getCampFeedbacks(int campId, boolean pending, String ownerId) {
        ArrayList<CampFeedback> ret = new ArrayList<>();
        ArrayList<CampFeedback> pendingFeedback = getCampFeedbacks(campId, pending);
        for (CampFeedback cf : pendingFeedback) {
            if (ownerId.equals(cf.getOwnerId())) // check if id matches
                ret.add(cf);
        }

        return ret;
    }

    /**
     * Displays all feedback in the hashmap linked to the relevant camp.
     * Override this and call super() should you need to print additional things.
     * 
     * @param campId campId of the camp to display feedback.
     * @return size of printed list
     */
    public int printAllFeedback(int campId) {
        ArrayList<CampFeedback> feedbacks = getCampFeedbacks(campId);
        for (CampFeedback cf : feedbacks)
            printFeedback(cf);
        return feedbacks.size();
    }

    /**
     * Displays pending feedback in the hashmap linked to the relevant camp.
     * 
     * @param campId campId of the camp to display suggestions.
     * @return size of printed list
     */
    public int printPendingFeedback(int campId) {
        ArrayList<CampFeedback> feedbacks = getCampFeedbacks(campId, true);
        for (CampFeedback cf : feedbacks)
            printFeedback(cf);
        return feedbacks.size();
    }

    /**
     * Displays pending feedback belonging to ownerId
     * in the hashmap linked to the relevant camp.
     * 
     * @param ownerId ownerId of the feedback to show
     * @param campId  campId of the camp to display suggestions.
     * @return size of printed list
     */
    public int printPendingFeedbackByOwner(String ownerId, int campId) {
        ArrayList<CampFeedback> feedbacks = getCampFeedbacks(campId, true, ownerId);
        for (CampFeedback cf : feedbacks)
            printFeedback(cf);
        return feedbacks.size();
    }

    /**
     * Displays processed feedback belonging to ownerId
     * in the hashmap linked to the relevant camp.
     * 
     * @param ownerId ownerId of the feedback to show
     * @param campId  campId of the camp to display suggestions.
     * @return size of printed list
     */
    public int printProcessedFeedbackByOwner(String ownerId, int campId) {
        ArrayList<CampFeedback> feedbacks = getCampFeedbacks(campId, false, ownerId);
        for (CampFeedback cf : feedbacks)
            printFeedback(cf);
        return feedbacks.size();
    }

    /**
     * Searches the hashmap containing the feedback by feedbackId.
     * 
     * @param feedbackId feedbackId of the feedback to be searched.
     * @param campId     campId of the camp to search feedback.
     * @return feedback object found
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
     * 
     * @param feedbackId feedbackId of the feedback to be checked.
     * @param campId     campId of the camp to check feedback.
     * @return whether feedback object is not null
     */
    public boolean checkValidFeedbackId(int feedbackId, int campId) {
        return findFeedbackById(feedbackId, campId) != null;
    }
}
