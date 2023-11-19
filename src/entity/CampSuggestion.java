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
    private String ownerId; //owner of this suggestion
    private String approverId;
    private String suggestion; //the suggestion in plaintext
    private int approvalStatus; //0 for not viewd, 1 for approved, 2 for rejected

    public CampSuggestion() {
        ownerId = null;
        approverId = null;
        suggestion = "";
        approvalStatus = 0;
    }

    public CampSuggestion(String commMemberID, String suggestion) {
        this.ownerId = commMemberID;
        this.suggestion = suggestion;
        approvalStatus = 0;
    }
    
    public String getOwner() { return ownerId; }
    public String getSuggestion() {return suggestion; }
    public boolean isPending() {return approvalStatus == 0; }
    public boolean hasApproved() { return approvalStatus == 1; } 
    public boolean hasRejected() { return approvalStatus == 2; } 

    public void setApproval(Staff staff, boolean approve) {
        approverId = staff.getUserID();
        approvalStatus = approve ? 1 : 2;
    }

    @Override
    public String toCSVLine() {
        String ret = "";
        ret += "OWNER" + "," //TODO
            + suggestion + ","
            + approvalStatus;
        return ret;
    }

    @Override
    public void fromCSVLine(String csvLine) {
        String[] split = csvLine.split(",");
        if (split.length != 3) {
            Log.error("csvLine is invalid");
        } else {
            //owner = split[0].trim(); //TODO
            suggestion = split[1].trim();
            approvalStatus = Integer.parseInt(split[2]);
        }
    }

    @Override
    public int getCSVLineLength() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCSVLineLength'");
    }
}
