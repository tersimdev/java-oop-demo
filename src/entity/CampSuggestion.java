package entity;

import util.Log;
import entity.CampCommitteeMember;

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

    private CampCommitteeMember campCommitteeMember;
    private String responderId;
    private int approvalStatus; // 0 for not viewed, 1 for approved, 2 for rejected

    public CampSuggestion() {
        super();
        approvalStatus = 0;
    }

    public CampSuggestion(CampCommitteeMember campCommitteeMember, String campCommitteeMemberId, String suggestion, int campId) {
        super(campCommitteeMemberId, suggestion, campId);
        this.campCommitteeMember = campCommitteeMember;
        approvalStatus = 0;
        responderId = null;
    }

    public String getResponderId() {
        return responderId;
    }

    public CampCommitteeMember getCampCommitteeMember() {
        return campCommitteeMember;
    }

    public boolean isPending() {
        return approvalStatus == 0;
    }

    public boolean hasApproved() {
        return approvalStatus == 1;
    }

    public boolean hasRejected() {
        return approvalStatus == 2;
    }

    public void setApproval(String staffID, boolean approve) {
        responderId = staffID;
        approvalStatus = approve ? 1 : 2;
    }

    @Override
    public String toCSVLine() {
        String ret = "";
        ret += feedbackId + ","
                + ownerId + ","
                + feedback + ",";
        if (responderId != null)
            ret += responderId + "," + approvalStatus;
        else
            ret += "-1,0";
        return ret;
    }

    @Override
    public void fromCSVLine(String csvLine) {
        String[] split = csvLine.split(",");
        if (split.length != getCSVLineLength()) {
            Log.error("suggestion csvLine is invalid, expected " + getCSVLineLength() + " but got " + split.length);
            Log.error(csvLine);
        } else {
            feedbackId = Integer.parseInt(split[0]);
            ownerId = split[1];
            feedback = split[2];
            approvalStatus = Integer.parseInt(split[4]);
            if (split[3].equals("-1")) {
                responderId = null;
            } else {
                responderId = split[3];
            }
        }
    }

    @Override
    public int getCSVLineLength() {
        return 5;
    }
}
