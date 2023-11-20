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
    private boolean isMember;
    private int campId;
    private int points;
    private String studentId;

    // inject dependency
    public CampCommitteeMember(Student student) {
        this.studentId = student.getUserID();
        isMember = false;
        campId = -1;
        points = 0;
    }

    public boolean isMember() {
        return isMember;
    }

    public void setMember(boolean isMember) {
        this.isMember = isMember;
    }

    public int getCampId() {
        return campId;
    }

    public void setCampId(int campId) {
        this.campId = campId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentId() {
        return studentId;
    }

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

    @Override
    public void fromCSVLine(String csvLine) {
        String[] split = csvLine.split(",");
        if (split.length != getCSVLineLength()) {
            Log.error("camp committee csvLine is invalid, expected " + getCSVLineLength() + " but got " + split.length);
            Log.error(csvLine);
        } else {
            isMember = split[0] == "MEMBER";
            if (isMember) {
                campId = Integer.parseInt(split[1]);
                points = Integer.parseInt(split[2]);
                studentId = split[3];
            } // else do nothing
        }

    }

    @Override
    public int getCSVLineLength() {
        return 4;
    }
}
