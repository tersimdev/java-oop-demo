package entity;

import java.time.LocalDate;
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
    private ArrayList<String> attendeeList; //store attendees student ids
    private ArrayList<String> committeeList; //store committee member studentIds
    private boolean visibility; //staff can set this to false to hide, if no one registered and stuff yet

    public Camp() {
        this.campId = -1;
        this.campInformation = new CampInformationBuilder().build(); //build empty
        this.committeeList = new ArrayList<String>();
        this.attendeeList = new ArrayList<String>();
        this.visibility = true;
    }

    public Camp(int campId, CampInformation campInformation) {
        this.campId = campId;
        this.campInformation = campInformation;
        this.committeeList = new ArrayList<String>();
        this.attendeeList = new ArrayList<String>();
        this.visibility = true;
    }

    public int getCampId() {
        return campId;
    }

    public void setCampId(int campId) {
        this.campId = campId;
        return;
    }

    public String getCampName(){
        return campInformation.getCampName();
    }

    public ArrayList<String> getAttendeeList() {
        return attendeeList;
    }

    public ArrayList<String> getCommitteeList() {
        return committeeList;
    }

    public CampInformation getCampInformation() {
        return campInformation;
    }

    public boolean registerStudent(Student student) { // returns true if registration successful
        if (!checkCampFull() && !checkRegistrationClosed()) {
            attendeeList.add(student.getUserID());
            return true;
        }
        else if (checkCampFull()) {
            Log.println("The camp is full");
        }
        else if (checkRegistrationClosed()) {
            Log.println("Registration for this camp has closed");
        }
        return false;
    }

    public boolean registerCampCommitteeMember(Student student) { // returns true if registration successful
        CampCommitteeMember campCommitteeMember = student.getCampCommitteeMember();

        if (committeeList.size() < campInformation.getCommitteeSlots() && !checkRegistrationClosed()) {
            campCommitteeMember.setCampId(campId);
            campCommitteeMember.setMember(true);
            committeeList.add(campCommitteeMember.getStudentId());
            return true;
        }
        else if (committeeList.size() < campInformation.getCommitteeSlots()) {
            Log.println("There are no more camp committee slots for this camp");
        }
        else if (checkRegistrationClosed()) {
            Log.println("Registration for this camp has closed");
        }
        return false;
    }

    public void withdrawStudent(Student student) {
        if (!student.getCampCommitteeMember().isMember()) { // not a committee member
            // check if student is even registered
            for (String studentPointer : attendeeList) {
                if (studentPointer == student.getUserID()) {
                    attendeeList.remove(student.getUserID());
                    return;
                }
            }
        }
        else { // committee member
            // check if student is registered as committee member
            for (String studentPointer : committeeList) {
                if (studentPointer == student.getUserID()) {
                    student.getCampCommitteeMember().setCampId(-1);
                    student.getCampCommitteeMember().setMember(false);
                    committeeList.remove(student.getUserID());
                    return;
                }
            }
        }
        Log.println("This student is not registered to this camp.");
        return;
    }

    // private void checkForDateClash(Student student) {
    //    
    // }

    public boolean checkRegistrationClosed() {
        LocalDate today = LocalDate.now();
        LocalDate deadline = campInformation.getRegistrationClosingDate();
        if (today.compareTo(deadline) >= 0)
            return true; // registration is closed
        return false;
    }

    public boolean checkCampFull() {
        if (attendeeList.size() >= campInformation.getTotalSlots())
            return true; // camp is full
        return false;
    }

    public boolean checkVisibility() {
        return visibility;
    }

    public boolean toggleVisibility() {
        visibility = !visibility;
        return visibility;
    }

    @Override
    public String toCSVLine() {
        String ret = "";
        ret += campId + ","
                + (visibility ? "VISIBLE" : "HIDDEN") + ",";
        // add attendee list as one csv cell, separated by semicolon
        for (String s : attendeeList) {
            ret += s + ";";
        }
        ret += ",";
        // add committee list as one csv cell, separated by semicolon
        for (String s : committeeList) {
            ret += s + ";";
        }
        ret += "," + campInformation.toCSVLine();
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
            this.visibility = (split[1] == "VISIBLE" ? true : false);
            //get list of attendees
            String[] attendees = split[2].split(";");
            this.attendeeList = new ArrayList<>(Arrays.asList(attendees));
            //get list of committee members
            String[] committee = split[3].split(";");
            this.committeeList = new ArrayList<>(Arrays.asList(committee));
            //get the rest of the csv, join them with commas, and pass to campinfo 
            String campInfoCSV = String.join(",", Arrays.copyOfRange(split, 4, split.length));
            this.campInformation = new CampInformationBuilder().build(); //build empty instance
            this.campInformation.fromCSVLine(campInfoCSV);
        }
    }

    @Override
    public int getCSVLineLength() {
        return 4 + campInformation.getCSVLineLength();
    } 
}
