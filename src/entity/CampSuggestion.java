package entity;

import util.Log;
import util.DataStore.SerializeToCSV;

/**
 * <p>
 * This is a class to represent a camp suggestion
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 5-11-2023
 */
public class CampSuggestion extends CampFeedback implements SerializeToCSV {

    private String approverId;
    private int approvalStatus; // 0 for not viewed, 1 for approved, 2 for rejected

    public CampSuggestion() {
        super();
        approvalStatus = 0;
    }

    public CampSuggestion(String commMemberID, String suggestion, int campId) {
        super();
        approvalStatus = 0;
        approverId = null;
    }

    /*public int getSuggestionId() {
        return suggestionId;
    }

    public int getCampId() {
        return campId;
    }

    public String getOwner() {
        return ownerId;
    }

    public String getSuggestion() {
        return suggestion;
    }
    
    public void setSuggestionId(int suggestionId) {
        this.suggestionId = suggestionId;
    }

    public void setSuggestion(String newSuggestion) {
        this.suggestion = newSuggestion;
    }*/

    public String getApproverId() {
        return approverId;
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
        approverId = staffID;
        approvalStatus = approve ? 1 : 2;
    }

    @Override
    public String toCSVLine() {
        String ret = "";
        ret += feedbackId + ","
                + ownerId + ","
                + feedback + ",";
        if (approverId != null)
            ret += approverId + "," + approvalStatus;
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
                approverId = null;
            } else {
                approverId = split[3];
            }
        }
    }

    @Override
    public int getCSVLineLength() {
        return 5;
    }
}
