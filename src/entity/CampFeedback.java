package entity;

import util.DataStore.SerializeToCSV;

/**
 * <p>
 * This is a class to represent a camp feedback
 * </p>
 * 
 * @author Yen Zhi Wei
 * @version 1.0
 * @since 23-11-2023
 */

public abstract class CampFeedback implements SerializeToCSV {

    /**
     * Identification number of the feedback.
     */
    protected int feedbackId;
    /**
     * ID of the camp the feedback is about.
     */
    protected int campId;
    /**
     * User ID of the user who submitted the feedback.
     */
    protected String ownerId;
    /**
     * The feedback in plaintext.
     */
    protected String feedback;

    /**
     * Default constructor for camp feedback.
     */
    public CampFeedback() {
        feedbackId = 0;
        campId = 0;
        ownerId = null;
        feedback = "";
    }

    /**
     * Constructor for camp feedback.
     * 
     * @param studentId ID of the student who submitted the feedback.
     * @param feedback  The feedback.
     * @param campId    ID of the camp the feedback is about.
     */
    public CampFeedback(String studentId, String feedback, int campId) {
        this.campId = campId;
        this.ownerId = studentId;
        this.feedback = feedback;
    }

    /**
     * Getter for the feedback ID.
     * 
     * @return The feedback ID.
     */
    public int getId() {
        return feedbackId;
    }

    /**
     * Getter for the camp ID.
     * 
     * @return The ID of the camp the feedback is about.
     */
    public int getCampId() {
        return campId;
    }

    /**
     * Getter for the owner ID.
     * 
     * @return The ID of the user who submitted the feedback.
     */
    public String getOwnerId() {
        return ownerId;
    }

    /**
     * Getter for the feedback.
     * 
     * @return The feedback.
     */
    public String getFeedback() {
        return feedback;
    }

    /**
     * Setter for the feedback ID.
     * 
     * @param feedbackId The feedback ID to be set.
     */
    public void setId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    /**
     * Setter for the feedback.
     * 
     * @param newFeedback The feedback.
     */
    public void setFeedback(String newFeedback) {
        this.feedback = newFeedback;
    }
}
