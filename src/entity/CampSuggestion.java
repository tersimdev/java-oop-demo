package entity;

import util.Log;

/**
 * <p>
 * This is a class to represent a camp suggestion
 * </p>
 * 
 * @author Yen Zhi Wei
 * @version 1.0
 * @since 23-11-2023
 */
public class CampSuggestion extends CampFeedback {

    /**
     * The ID of the staff who responded to the suggestion, if any. Set to null if
     * there has been no response.
     */
    private String responderId;
    /**
     * The approval status of the suggestion. Set to 0 for not viewed, 1 for
     * approved, 2 for rejected.
     */
    private int approvalStatus;

    /**
     * Default constructor for a camp suggestion object.
     */
    public CampSuggestion() {
        super();
        approvalStatus = 0;
    }

    /**
     * Constructor for a camp suggestion object.
     * 
     * @param campCommitteeMember   CampCommitteeMember object of the committee
     *                              member who made the suggestion.
     * @param campCommitteeMemberId ID of the committee member who made the
     *                              suggestion.
     * @param suggestion            The suggestion in plaintext.
     * @param campId                ID of the camp the suggestion is for.
     */
    public CampSuggestion(String campCommitteeMemberId, String suggestion,
            int campId) {
        super(campCommitteeMemberId, suggestion, campId);
        approvalStatus = 0;
        responderId = null;
    }

    /**
     * Getter for responder ID.
     * 
     * @return ID of the responder, null if no response yet.
     */
    public String getResponderId() {
        return responderId;
    }


    /**
     * Checks if the suggestion's approval status is pending.
     * 
     * @return True if the suggestion is pending a response.
     */
    public boolean isPending() {
        return approvalStatus == 0;
    }

    /**
     * Checks if the suggestion's approval status is approved.
     * 
     * @return True if the suggestion has been approved.
     */
    public boolean hasApproved() {
        return approvalStatus == 1;
    }

    /**
     * Checks if the suggestion's approval status is rejected.
     * 
     * @return True if the suggestion has been rejected.
     */
    public boolean hasRejected() {
        return approvalStatus == 2;
    }

    /**
     * Sets for the approval status of the suggestion to either approved or
     * rejected.
     * 
     * @param staffID ID of the staff responding to the suggestion.
     * @param approve Whether the suggestion is approved or not.
     */
    public void setApproval(String staffID, boolean approve) {
        responderId = staffID;
        approvalStatus = approve ? 1 : 2;
    }

    /**
     * Converts the suggestion's feedback ID, owner ID, suggestion itself, responder
     * ID and approval status into a string in CSV format.
     * 
     * @return A string of comma separated values.
     */
    @Override
    public String toCSVLine() {
        String ret = "";
        ret += feedbackId + ","
                + ownerId + ","
                + campId + ","
                + feedback + ",";
        if (responderId != null)
            ret += responderId + "," + approvalStatus;
        else
            ret += "-1,0";
        return ret;
    }

    /**
     * Sets the feedback ID, owner ID, suggestion itself, responder
     * ID and approval status based on the information from a csvline.
     * 
     * @param csvLine The string containing all the suggestion's information.
     */
    @Override
    public void fromCSVLine(String csvLine) {
        String[] split = csvLine.split(",");
        if (split.length != getCSVLineLength()) {
            Log.error("suggestion csvLine is invalid, expected " + getCSVLineLength() + " but got " + split.length);
            Log.error(csvLine);
        } else {
            feedbackId = Integer.parseInt(split[0]);
            ownerId = split[1];
            campId = Integer.parseInt(split[2]);

            feedback = split[3];
            approvalStatus = Integer.parseInt(split[5]);
            if (split[4].equals("-1")) {
                responderId = null;
            } else {
                responderId = split[3];
            }
        }
    }

    /**
     * Gets the length of a csvline containing a suggestion's information.
     * 
     * @return The length of the csvline.
     */
    @Override
    public int getCSVLineLength() {
        return 6;
    }
}
