package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import entity.CampInformation.CampInformationBuilder;
import util.Log;
import util.DataStore.SerializeToCSV;

/**
 * <p>
 * This is a class to represent a camp
 * </p>
 * 
 * @author Lim Jun Rong Ryan
 * @version 1.1
 * @since 19-11-2023
 */
public class Camp implements SerializeToCSV {

    private int campId;
    private CampInformation campInformation;
    private ArrayList<String> studentList; //store student ids
    private ArrayList<Student> attendees; //store student objects
    private ArrayList<CampCommitteeMember> committeeList; //store committee member objects
    private boolean visibility; //staff can set this to false to hide, if no one registered and stuff yet

    public Camp() {
        //todo default vals
    }

    public Camp(int campId, CampInformation campInformation) {
        this.campId = campId;
        this.campInformation = campInformation;
        this.attendees = new ArrayList<Student>();
        this.committeeList = new ArrayList<CampCommitteeMember>();
        this.studentList = new ArrayList<String>();
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

    public ArrayList<Student> getAttendees(){
        return attendees;
    }

    public ArrayList<CampCommitteeMember> getCampCommitteeMembers() {
        return committeeList;
    }

    public ArrayList<String> getStudentList() {
        return studentList;
    }

    public CampInformation getCampInformation() {
        return campInformation;
    }

    public boolean registerStudent(Student student) { // returns true if registration successful
        if (!checkCampFull() && !checkRegistrationClosed()) {
            studentList.add(student.getUserID());
            attendees.add(student);
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
            committeeList.add(campCommitteeMember);
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
        studentList.remove(student.getUserID());
        attendees.remove(student);
    }

    // private void checkForDateClash(Student student) {
    // ArrayList<LocalDateTime> dates = campInfo.getDates();
    // }

    public boolean checkRegistrationClosed() {
        LocalDate today = LocalDate.now();
        LocalDate deadline = campInformation.getRegistrationClosingDate();
        if (today.compareTo(deadline) >= 0)
            return true; // registration is closed
        return false;
    }

    public boolean checkCampFull() {
        if (attendees.size() >= campInformation.getTotalSlots())
            return true; // camp is full
        return false;
    }

    public boolean isVisibility() {
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
        // add student list as one csv cell, separated by semicolon
        for (String s : studentList) {
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
            String[] students = split[2].split(";");
            this.studentList = new ArrayList<>(Arrays.asList(students));
            String campInfoCSV = "";
            for (int i = 3; i < split.length; ++i)
                campInfoCSV += split[i] + ",";
            this.campInformation = new CampInformationBuilder().build();
            this.campInformation.fromCSVLine(campInfoCSV);
        }
    }

    @Override
    public int getCSVLineLength() {
        return 3 + campInformation.getCSVLineLength();
    }

    
}
