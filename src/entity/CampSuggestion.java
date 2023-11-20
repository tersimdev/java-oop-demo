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
public class CampSuggestion implements SerializeToCSV {

    private int suggestionId;
    private String ownerId; // owner of this suggestion
    private String approverId;
    private String suggestion; // the suggestion in plaintext
    private int approvalStatus; // 0 for not viewd, 1 for approved, 2 for rejected

    public CampSuggestion() {
        suggestionId = 0;
        ownerId = null;
        approverId = null;
        suggestion = "";
        approvalStatus = 0;
    }

    public CampSuggestion(String commMemberID, String suggestion) {
        this.ownerId = commMemberID;
        this.suggestion = suggestion;
        approvalStatus = 0;
        approverId = null;
    }

    public int getSuggestionId() {
        return suggestionId;
    }

    public String getOwner() {
        return ownerId;
    }

    public String getSuggestion() {
        return suggestion;
    }

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

    public void setSuggestionId(int suggestionId) {
        this.suggestionId = suggestionId;
    }

    public void setSuggestion(String newSuggestion) {
        this.suggestion = newSuggestion;
    }

    public void setApproval(String staffID, boolean approve) {
        approverId = staffID;
        approvalStatus = approve ? 1 : 2;
    }

    @Override
    public String toCSVLine() {
        String ret = "";
        ret += suggestionId + ","
                + ownerId + ","
                + suggestion + ",";
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
            suggestionId = Integer.parseInt(split[0]);
            ownerId = split[1];
            suggestion = split[2];
            approvalStatus = Integer.parseInt(split[4]);
            if (split[3].isEmpty()) {
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
