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

    /**
     * User ID of the user who replied to the enquiry.
     */
    private String replierId;
    /**
     * The reply to the enquiry. Set to null if the enquiry is pending a response.
     */
    private String reply;

    /**
     * Default constructor for a camp enquiry.
     */
    public CampEnquiry() {
        super();
        replierId = null;
        reply = null;
    }

    /**
     * Constructor for a camp enquiry.
     * 
     * @param studentId ID of the student who submitted the enquiry.
     * @param enquiry   The enquiry.
     * @param campId    The ID of the camp the enquiry is about.
     */
    public CampEnquiry(String studentId, String enquiry, int campId) {
        super(studentId, enquiry, campId);
        replierId = null;
        reply = null;
    }

    /**
     * Getter for the reply.
     * 
     * @return The reply.
     */
    public String getReply() {
        return reply;
    }

    /**
     * Checks if a reply is pending a response.
     * 
     * @return True if the enquiry is pending a response.
     */
    public boolean isPending() {
        return reply == null;
    }

    /**
     * Setter for the reply to the enquiry.
     * 
     * @param commMemberId The ID of the committee member replying to the enquiry.
     * @param reply        The reply.
     */
    public void reply(String commMemberId, String reply) {
        this.reply = reply;
        replierId = commMemberId;
    }

    /**
     * Converts the enquiry's feedback ID, owner ID and enquiry itself into a string
     * in CSV format.
     * 
     * @return A string of comma separated values.
     */
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

    /**
     * Sets the enquiry's feedback ID, owner ID and enquiry itself based on the
     * information from a csvline.
     * 
     * @param csvLine The string containing all the enquiry's information.
     */
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

    /**
     * Gets the length of a csvline containing an enquiry's information.
     * 
     * @return The length of the csvline.
     */
    @Override
    public int getCSVLineLength() {
        return 5;
    }
}
