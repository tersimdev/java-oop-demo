package entity;

import java.util.Arrays;

import util.Log;

/**
 * <p>
 * This is an entity class to represent a student
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 19-11-2023
 */
public class Student extends User {
    private CampCommitteeMember campCommitteeMember;

    public Student() {
        super();
        campCommitteeMember = new CampCommitteeMember(this);
    }

    public Student(String displayName, String userID, Faculty faculty) {
        super(displayName, userID, faculty);
        campCommitteeMember = new CampCommitteeMember(this);
    }

    public CampCommitteeMember getCampCommitteeMember() {
        return campCommitteeMember;
    }

    public void setCampCommitteeMember(CampCommitteeMember campCommitteeMember) {
        this.campCommitteeMember = campCommitteeMember;
    }

    @Override
    public String toCSVLine() {
        String ret = "";
        ret = super.toCSVLine() + "," + campCommitteeMember.toCSVLine();
        return ret;
    }

    @Override
    public void fromCSVLine(String csvLine) {
        String[] split = csvLine.split(",");
        if (split.length != getCSVLineLength()) {
            Log.error("student csvLine is invalid, expected " + getCSVLineLength()  + " but got " + split.length);
            Log.error(csvLine);
        } else {
            String userCSV = String.join(",", split); // pass in whole array because this is student, expects student len 
            String commCSV = String.join(",", Arrays.copyOfRange(split, super.getCSVLineLength(), split.length));
            super.fromCSVLine(userCSV);
            campCommitteeMember = new CampCommitteeMember(this);
            campCommitteeMember.fromCSVLine(commCSV);
        }
    }

    @Override
    public int getCSVLineLength() {
        return super.getCSVLineLength() + campCommitteeMember.getCSVLineLength();
    }
}
