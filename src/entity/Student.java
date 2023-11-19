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
        ret = super.toCSVLine() + campCommitteeMember.toCSVLine();
        return ret;
    }

    @Override
    public void fromCSVLine(String csvLine) {
        String[] split = csvLine.split(",");
        if (split.length != getCSVLineLength()) {
            Log.error("csvLine is invalid");
        } else {
            int userCSVLen = super.getCSVLineLength();
            String userCSV = String.join(",", Arrays.copyOfRange(split, 0, userCSVLen - 1));
            String commCSV = String.join(",", Arrays.copyOfRange(split, userCSVLen, split.length - 1));
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
