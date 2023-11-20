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

    private int enquiryId;
    private int campId;
    private String ownerId;
    private String replierId;
    private String enquiry;
    private String reply;

    public CampEnquiry() {
        enquiryId = 0;
        campId = 0;
        ownerId = null;
        enquiry = "";
        replierId = null;
        reply = null;
    }

    public CampEnquiry(String studentID, String enquiry, int campId) {
        this.campId = campId;
        this.ownerId = studentID;
        this.enquiry = enquiry;
        replierId = null;
        reply = null;
    }

    public int getEnquiryId() {
        return enquiryId;
    }

    public int getCampId() {
        return campId;
    }

    public String getOwner() {
        return ownerId;
    }

    public String getEnquiry() {
        return enquiry;
    }

    public String getReply() {
        return reply;
    }

    public void setEnquiryId(int enquiryId) {
        this.enquiryId = enquiryId;
    }

    public void setEnquiry(String newEnquiry) {
        this.enquiry = newEnquiry;
    }

    public void reply(String commMemberId, String reply) {
        this.reply = reply;
        replierId = commMemberId;
    }

    @Override
    public String toCSVLine() {
        String ret = "";
        ret += enquiryId + ","
                + ownerId + ","
                + enquiry + ",";
        if (replierId != null)
            ret += replierId + "," + reply;
        else
            ret += "-1,-";
        return ret;
    }

    @Override
    public void fromCSVLine(String csvLine) {
        String[] split = csvLine.split(",");
        if (split.length != getCSVLineLength()) {
            Log.error("enquiry csvLine is invalid, expected " + getCSVLineLength() + " but got " + split.length);
            Log.error(csvLine);
        } else {
            enquiryId = Integer.parseInt(split[0]);
            ownerId = split[1];
            enquiry = split[2];
            if (split[3].isEmpty()) {
                replierId = null;
                reply = null;
            } else {
                replierId = split[3];
                reply = split[4];
            }
        }
    }

    @Override
    public int getCSVLineLength() {
        return 5;
    }
}
