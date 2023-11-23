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
 * @author Lim Jun Rong Ryan, Jon Daniel Acu Kang
 * @version 1.1
 * @since 19-11-2023
 */
public class Camp implements SerializeToCSV {

    private int campId;
    private CampInformation campInformation;
    private ArrayList<String> attendeeList; // store attendees student ids
    private ArrayList<String> committeeList; // store committee member studentIds
    private ArrayList<String> withdrawnList; // store students who have withdrawn from this camp previously
    private boolean visibility; // staff can set this to false to hide, if no one registered and stuff yet

    private final static String EMPTY_STUD_LIST = "NONE";
    private final static String STUD_LIST_SEP = ";";

    public Camp() {
        this.campId = -1;
        this.campInformation = new CampInformationBuilder().build(); // build empty
        this.committeeList = new ArrayList<String>();
        this.attendeeList = new ArrayList<String>();
        this.withdrawnList = new ArrayList<String>();
        this.visibility = true;
    }

    public Camp(int campId, CampInformation campInformation) {
        this.campId = campId;
        this.campInformation = campInformation;
        this.committeeList = new ArrayList<String>();
        this.attendeeList = new ArrayList<String>();
        this.withdrawnList = new ArrayList<String>();
        this.visibility = true;
    }

    public int getCampId() {
        return campId;
    }

    public void setCampId(int campId) {
        this.campId = campId;
        return;
    }

    public String getCampName() {
        return campInformation.getCampName();
    }

    public ArrayList<String> getAttendeeList() {
        return attendeeList;
    }

    public ArrayList<String> getCommitteeList() {
        return committeeList;
    }

    public ArrayList<String> getWithdrawnList() {
        return withdrawnList;
    }

    public CampInformation getCampInformation() {
        return campInformation;
    }

    public void addAttendee(Student student) {
        attendeeList.add(student.getUserID());
    }

    public void addCampCommitteeMember(CampCommitteeMember campCommitteeMember) {
        committeeList.add(campCommitteeMember.getStudentId());
    }

    public void removeAttendee(Student student) {
        attendeeList.remove(student.getUserID());
        withdrawnList.add(student.getUserID());
    }

    public boolean checkVisibility() {
        return visibility;
    }

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
     * Helper function to a single string to an array list of string
     * 
     * @param ids string to convert
     * @return empty array list if string is EMPTY_STUD_LIST, else array list of
     *         string
     */
    private ArrayList<String> getStringAsIDList(String str) {
        if (str.trim().equals(EMPTY_STUD_LIST))
            return new ArrayList<>();
        String[] arr = str.split(STUD_LIST_SEP);
        return new ArrayList<>(Arrays.asList(arr));
    }

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

        campInformation.toCSVLine();
        return ret;
    }

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

    @Override
    public int getCSVLineLength() {
        return 5 + campInformation.getCSVLineLength();
    }
}
