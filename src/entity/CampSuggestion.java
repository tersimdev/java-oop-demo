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
public abstract class CampSuggestion implements SerializeToCSV {

    private CampCommitteeMember owner; //owner of this suggestion
    private Staff approver;
    private String suggestion; //the suggestion in plaintext
    private int approvalStatus; //0 for not viewd, 1 for approved, 2 for rejected

    public CampSuggestion() {
        owner = null;
        approver = null;
        suggestion = "";
        approvalStatus = 0;
    }

    public CampSuggestion(CampCommitteeMember owner, String suggestion) {
        this.owner = owner;
        this.suggestion = suggestion;
        approvalStatus = 0;
    }
    
    public CampCommitteeMember getOwner() { return owner; }
    public String getSuggestion() {return suggestion; }
    public boolean hasApproved() { return approvalStatus == 1; } 
    public boolean hasRejected() { return approvalStatus == 2; } 

    public void setApproval(Staff staff, boolean approve) {
        approver = staff;
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
}
