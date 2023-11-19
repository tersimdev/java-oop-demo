package entity;

import util.Log;
import util.DataStore.SerializeToCSV;

/**
 * <p>
 * This is a class to represent a camp enquiry
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 5-11-2023
 */
public class CampEnquiry implements SerializeToCSV {

    int enquiryId;
    String ownerId;
    String replierId;
    private String enquiry;
    private String reply;

    public CampEnquiry() {
        ownerId = null;
        enquiry = "";
        replierId = null;
        reply = null;
    }

    public CampEnquiry(Student student, String enquiry) {
        this.ownerId = student.getUserID();
        this.enquiry = enquiry;
        replierId = null;
        reply = null;
    }

    public String getOwner() { return ownerId; }
    public String getEnquiry() { return enquiry; }
    public String getReply() { return reply; }

    public void reply(CampCommitteeMember campCommitteeMember, String reply) {
        this.reply = reply;
        replierId = campCommitteeMember.getStudentId();
    }

    @Override
    public String toCSVLine() {
        String ret = "";
        //TOOD
        return ret;
    }

    @Override
    public void fromCSVLine(String csvLine) {
        String[] split = csvLine.split(",");
        // //TODO
        // if (split.length != 4) {
        //     Log.error("csvLine is invalid");
        // } else {
        // }
    }

    @Override
    public int getCSVLineLength() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCSVLineLength'");
    }
}
