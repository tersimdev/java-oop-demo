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
    /**
     * CampCommitteeMember object of the student.
     */
    private CampCommitteeMember campCommitteeMember;

    /**
     * Default constructor for a Student object.
     */
    public Student() {
        super();
        campCommitteeMember = new CampCommitteeMember(this);
    }

    /**
     * Constructor for a student object.
     * 
     * @param displayName The display name of the student.
     * @param userID      The user ID of the student.
     * @param faculty     The faculty of the student.
     */
    public Student(String displayName, String userID, Faculty faculty) {
        super(displayName, userID, faculty);
        campCommitteeMember = new CampCommitteeMember(this);
    }

    /**
     * Getter for the student's CampCommitteeMember object.
     * 
     * @return The student's CampCommitteeMember object.
     */
    public CampCommitteeMember getCampCommitteeMember() {
        return campCommitteeMember;
    }

    /**
     * Setter for the student's CampCommitteeMember object.
     * 
     * @param campCommitteeMember The student's CampCommitteeMember object to be
     *                            set.
     */
    public void setCampCommitteeMember(CampCommitteeMember campCommitteeMember) {
        this.campCommitteeMember = campCommitteeMember;
    }

    /**
     * Converts the student's display name, user ID, password, faculty and committee
     * member information into a string in CSV format.
     * 
     * @return A string of comma separated values.
     */
    @Override
    public String toCSVLine() {
        String ret = "";
        ret = super.toCSVLine() + "," + campCommitteeMember.toCSVLine();
        return ret;
    }

    /**
     * Sets the student's display name, user ID, password, faculty and committee
     * member information based on the information from a csvline.
     * 
     * @param csvLine The string containing all the student's information.
     */
    @Override
    public void fromCSVLine(String csvLine) {
        String[] split = csvLine.split(",");
        if (split.length != getCSVLineLength()) {
            Log.error("student csvLine is invalid, expected " + getCSVLineLength() + " but got " + split.length);
            Log.error(csvLine);
        } else {
            String userCSV = String.join(",", split); // pass in whole array because this is student, expects student
                                                      // len
            String commCSV = String.join(",", Arrays.copyOfRange(split, super.getCSVLineLength(), split.length));
            super.fromCSVLine(userCSV);
            campCommitteeMember = new CampCommitteeMember(this);
            campCommitteeMember.fromCSVLine(commCSV);
        }
    }

    /**
     * Gets the length of a csvline containing a student's information.
     * 
     * @return The length of the csvline.
     */
    @Override
    public int getCSVLineLength() {
        return super.getCSVLineLength() + campCommitteeMember.getCSVLineLength();
    }
}
