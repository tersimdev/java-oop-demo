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
public abstract class CampEnquiry implements SerializeToCSV {

    int enquiryId;
    Student owner;
    CampCommitteeMember replier;
    private String enquiry;
    private String reply;

    public CampEnquiry() {
        owner = null;
        enquiry = "";
        replier = null;
        reply = null;
    }

    public CampEnquiry(Student student, String enquiry) {
        this.owner = student;
        this.enquiry = enquiry;
        replier = null;
        reply = null;
    }

    public Student getOwner() { return owner; }
    public String getEnquiry() { return enquiry; }
    public String getReply() { return reply; }

    public void reply(CampCommitteeMember campCommitteeMember, String reply) {
        this.reply = reply;
        replier = campCommitteeMember;
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
}
