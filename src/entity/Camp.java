package entity;

import java.util.ArrayList;
import java.util.Arrays;

import entity.CampInformation.CampInformationBuilder;
import util.Log;
import util.DataStore.SerializeToCSV;

/**
 * <p>
 * This is a class to represent a camp
 * </p>
 * 
 * @author Team 2
 * @version 1.0
 * @since 24-11-2023
 */
public class Camp implements SerializeToCSV {

    /**
     * ID of a camp.
     */
    private int campId;
    /**
     * CampInformation object.
     */
    private CampInformation campInformation;
    /**
     * List of student IDs of attendees registered for this camp.
     */
    private ArrayList<String> attendeeList;
    /**
     * List of student IDs of committee members registered for this camp.
     */
    private ArrayList<String> committeeList;
    /**
     * List of student IDs of students who previously withdrew from this camp.
     */
    private ArrayList<String> withdrawnList;
    /**
     * Staff can set this to false to hide the camp.
     * Hiding the camp would prevent students from viewing the camp.
     */
    private boolean visibility; // staff can set this to false to hide, if no one registered and stuff yet

    /**
     * Constant to represent an empty student list
     */
    private final static String EMPTY_STUD_LIST = "NONE";
    /**
     * Constant to represent the separator used in student list
     */
    private final static String STUD_LIST_SEP = ";";

    /**
     * Default camp constructor
     */
    public Camp() {
        this.campId = -1;
        this.campInformation = new CampInformationBuilder().build(); // build empty
        this.committeeList = new ArrayList<String>();
        this.attendeeList = new ArrayList<String>();
        this.withdrawnList = new ArrayList<String>();
        this.visibility = true;
    }

    /**
     * Camp constructor.
     * 
     * @param campId          The camp ID.
     * @param campInformation The camp information.
     */
    public Camp(int campId, CampInformation campInformation) {
        this.campId = campId;
        this.campInformation = campInformation;
        this.committeeList = new ArrayList<String>();
        this.attendeeList = new ArrayList<String>();
        this.withdrawnList = new ArrayList<String>();
        this.visibility = true;
    }

    /**
     * Getter for the camp ID.
     * 
     * @return Returns the camp ID.
     */
    public int getCampId() {
        return campId;
    }

    /**
     * Setter for the camp ID.
     * 
     * @param campId Camp ID to be set.
     */
    public void setCampId(int campId) {
        this.campId = campId;
        return;
    }

    /**
     * Getter for the camp name.
     * 
     * @return Returns the camp name.
     */
    public String getCampName() {
        return campInformation.getCampName();
    }

    /**
     * Getter for the list of attendees.
     * 
     * @return Returns the list of attendees.
     */
    public ArrayList<String> getAttendeeList() {
        return attendeeList;
    }

    /**
     * Getter for the list of committee members.
     * 
     * @return Returns the list of committee members.
     */
    public ArrayList<String> getCommitteeList() {
        return committeeList;
    }

    /**
     * Getter for the withdrawn list.
     * 
     * @return Returns the list of students who previously withdrew.
     */
    public ArrayList<String> getWithdrawnList() {
        return withdrawnList;
    }

    /**
     * Getter for the camp information.
     * 
     * @return Returns a CampInformation object.
     */
    public CampInformation getCampInformation() {
        return campInformation;
    }

    /**
     * Adds an attendee to the attendee list
     * 
     * @param student The student to be added.
     */
    public void addAttendee(Student student) {
        attendeeList.add(student.getUserID());
    }

    /**
     * Adds a camp committee member to the committee list.
     * 
     * @param campCommitteeMember The camp committee member to be added.
     */
    public void addCampCommitteeMember(CampCommitteeMember campCommitteeMember) {
        committeeList.add(campCommitteeMember.getStudentId());
    }

    /**
     * Removes an attendee from the attendee list and adds them to the withdrawn
     * list.
     * 
     * @param student The student being withdrawn.
     */
    public void removeAttendee(Student student) {
        attendeeList.remove(student.getUserID());
        withdrawnList.add(student.getUserID());
    }

    /**
     * Checks if the camp is visible (true by default).
     * 
     * @return Returns true if the camp is visible.
     */
    public boolean checkVisibility() {
        return visibility;
    }

    /**
     * Toggles the visibility of the camp.
     * 
     * @return Returns the new camp visibility.
     */
    public boolean toggleVisibility() {
        visibility = !visibility;
        return visibility;
    }

    /**
     * Helper function to convert an array list of string to a single string
     * 
     * @param ids list of user ids
     * @return string of ids separated by STUD_LIST_SEP, returns EMPTY_STUD_LIST if
     *         size is empty
     */
    private String getIDListAsString(ArrayList<String> ids) {
        String ret = "";
        for (String s : ids) {
            ret += s + STUD_LIST_SEP;
        }
        if (!ret.isEmpty())
            return ret;
        return EMPTY_STUD_LIST;
    }

    /**
     * Helper function to convert a single string to an array list of string
     * 
     * @param str string to convert
     * @return empty array list if string is EMPTY_STUD_LIST, else array list of
     *         string
     */
    private ArrayList<String> getStringAsIDList(String str) {
        if (str.trim().equals(EMPTY_STUD_LIST))
            return new ArrayList<>();
        String[] arr = str.split(STUD_LIST_SEP);
        return new ArrayList<>(Arrays.asList(arr));
    }

    /**
     * Converts the camp's visibility, attendee list, committee list, withdrawn list
     * and camp information into a string in CSV format.
     * 
     * @return A string of comma separated values.
     */
    @Override
    public String toCSVLine() {
        String ret = "";
        ret += campId + ","
                + (visibility ? "VISIBLE" : "HIDDEN") + ",";
        // add attendee list as one csv cell, separated by semicolon
        ret += getIDListAsString(attendeeList) + ",";
        // add committee list as one csv cell, separated by semicolon
        ret += getIDListAsString(committeeList) + ",";
        // add withdrawn list as one csv cell, separated by semicolon
        ret += getIDListAsString(withdrawnList) + ",";

        ret += campInformation.toCSVLine();
        return ret;
    }

    /**
     * Sets the camp's visibility, attendee list, committee list, withdrawn list
     * and camp information based on the information from a csvline.
     * 
     * @param csvLine The string containing all the camp's information.
     */
    @Override
    public void fromCSVLine(String csvLine) {
        String[] split = csvLine.split(",");
        if (split.length != getCSVLineLength()) {
            Log.error("camp csvLine is invalid, expected " + getCSVLineLength() + " but got " + split.length);
            Log.error(csvLine);
        } else {
            this.campId = Integer.parseInt(split[0]);
            this.visibility = (split[1].equals("VISIBLE"));
            // get list of attendees
            this.attendeeList = getStringAsIDList(split[2]);
            // get list of committee members
            this.committeeList = getStringAsIDList(split[3]);
            // get list of withdraw students
            this.withdrawnList = getStringAsIDList(split[4]);
            // get the rest of the csv, join them with commas, and pass to campinfo
            String campInfoCSV = String.join(",", Arrays.copyOfRange(split, 5, split.length));
            this.campInformation = new CampInformationBuilder().build(); // build empty instance
            this.campInformation.fromCSVLine(campInfoCSV);
        }
    }

    /**
     * Gets the length of a csvline containing a camp's information.
     * 
     * @return The length of the csvline.
     */
    @Override
    public int getCSVLineLength() {
        return 5 + campInformation.getCSVLineLength();
    }
}
