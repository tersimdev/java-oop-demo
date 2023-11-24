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

    protected int feedbackId;
    protected int campId;
    protected String ownerId; //owner of feedback
    protected String feedback; //feedback in plaintext

    public CampFeedback() {
        feedbackId = 0;
        campId = 0;
        ownerId = null;
        feedback = "";
    }

    public CampFeedback(String studentId, String feedback, int campId) {
        this.campId = campId;
        this.ownerId = studentId;
        this.feedback = feedback;
    }

    public int getId() {
        return feedbackId;
    }

    public int getCampId() {
        return campId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public void setFeedback(String newFeedback) {
        this.feedback = newFeedback;
    }
}
