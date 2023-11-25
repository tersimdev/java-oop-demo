package entity;

import util.Log;
import util.DataStore.SerializeToCSV;

/**
 * <p>
 * This is an entity class to represent a camp committee member
 * </p>
 * 
 * @author Jon Daniel Acu Kang
 * @version 1.0
 * @since 19-11-2023
 */
public class CampCommitteeMember implements SerializeToCSV {
    /**
     * True if the student is a committee member.
     */
    private boolean isMember;
    /**
     * The camp ID of the camp the committee member is registered for.
     */
    private int campId;
    /**
     * A score reflecting how much the committee member has contributed. Points are
     * added when a camp committee member replies enquiries and gives or accepts
     * suggestions.
     */
    private int points;
    /**
     * The student ID of the camp committee member.
     */
    private String studentId;

    /**
     * Constructor for a camp committee member.
     * 
     * @param student student object
     */
    public CampCommitteeMember(Student student) {
        this.studentId = student.getUserID();
        isMember = false;
        campId = -1;
        points = 0;
    }

    /**
     * Getter for <code>isMember</code>
     * 
     * @return true if the student is a committee member.
     */
    public boolean getIsMember() {
        return isMember;
    }

    /**
     * Setter for <code>isMember</code>
     * 
     * @param isMember new member status of the committee member.
     */
    public void setIsMember(boolean isMember) {
        this.isMember = isMember;
    }

    /**
     * Getter for the camp ID of the registered camp.
     * 
     * @return The camp ID.
     */
    public int getCampId() {
        return campId;
    }

    /**
     * Setter for the camp ID of the registered camp.
     * 
     * @param campId The camp ID of the camp the committee member is registered for.
     */
    public void setCampId(int campId) {
        this.campId = campId;
    }

    /**
     * Getter for the committee member's points.
     * 
     * @return The committee member's points.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Method to give the commitee member more points.
     * 
     * @param additionalPoints The number of points to be added.
     */
    public void addPoints(int additionalPoints) {
        this.points += additionalPoints;
    }

    /**
     * Setter for the committee member's points.
     * 
     * @param points The number of points to be set.
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * Setter for the committee member's student ID.
     * 
     * @param studentId The student ID to be set.
     */
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    /**
     * Getter for the committee member's student ID.
     * 
     * @return The student ID of the committee member.
     */
    public String getStudentId() {
        return studentId;
    }

    /**
     * Converts the committee member's camp ID, points and student ID into a string
     * in CSV format.
     * 
     * @return A string of comma separated values.
     */
    @Override
    public String toCSVLine() {
        String ret = "";
        if (isMember) {
            ret += "MEMBER,"
                    + campId + ","
                    + points + ","
                    + studentId;
        } else {
            ret += "NA,-1,0,-1"; // give some defaults
        }
        return ret;
    }

    /**
     * Sets the committee member's camp ID, points and student ID based on the
     * information from a csvline.
     * 
     * @param csvLine The string containing all the committee member's information.
     */
    @Override
    public void fromCSVLine(String csvLine) {
        String[] split = csvLine.split(",");
        if (split.length != getCSVLineLength()) {
            Log.error("camp committee csvLine is invalid, expected " + getCSVLineLength() + " but got " + split.length);
            Log.error(csvLine);
        } else {
            isMember = split[0].trim().equals("MEMBER");
            if (isMember) {
                campId = Integer.parseInt(split[1]);
                points = Integer.parseInt(split[2]);
                studentId = split[3];
            } // else do nothing
        }

    }

    /**
     * Gets the length of a csvline containing a committee member's information.
     * 
     * @return The length of the csvline.
     */
    @Override
    public int getCSVLineLength() {
        return 4;
    }
}
