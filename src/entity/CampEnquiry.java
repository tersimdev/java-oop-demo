package entity;

import util.Log;

/**
 * <p>
 * This is a class to represent a camp enquiry
 * </p>
 * 
 * @author Yen Zhi Wei
 * @version 1.0
 * @since 23-11-2023
 */
public class CampEnquiry extends CampFeedback {

    private String replierId;
    private String reply;

    public CampEnquiry() {
        super();
        replierId = null;
        reply = null;
    }

    public CampEnquiry(String studentId, String enquiry, int campId) {
        super(studentId, enquiry, campId);
        replierId = null;
        reply = null;
    }

    public String getReply() {
        return reply;
    }

    public void reply(String commMemberId, String reply) {
        this.reply = reply;
        replierId = commMemberId;
    }

    @Override
    public boolean isPending() {
        return reply == null;
    }


    @Override
    public String toCSVLine() {
        String ret = "";
        ret += feedbackId + ","
                + ownerId + ","
                + feedback + ",";
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
            feedbackId = Integer.parseInt(split[0]);
            ownerId = split[1];
            feedback = split[2];
            if (split[3].equals("-1")) {
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
