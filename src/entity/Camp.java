package entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

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
    private boolean visibility; //staff can set this to false to hide, if no one registered and stuff yet

    public Camp() {
        //todo default vals
    }

    public Camp(int campId, CampInformation campInformation, ArrayList<String> studentList) {
        this.campId = campId;
        this.campInformation = campInformation;
        this.studentList = new ArrayList<>();
        this.visibility = true;
    }

    public int getCampId() {
        return campId;
    }

    public ArrayList<String> getStudentList() {
        return studentList;
    }

    public CampInformation getCampInformation() {
        return campInformation;
    }

    public void registerStudent(String student) {
        studentList.add(student);
    }

    // private void checkForDateClash(Student student) {
    //     ArrayList<LocalDateTime> dates = campInfo.getDates();
    // }

    private boolean checkRegistrationClosed() {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime deadline = campInformation.getRegistrationClosingDate();
        if (today.compareTo(deadline) >= 0) return true; // registration is closed
        return false;
    }

    private boolean checkCampFull() {
        if (studentList.size() >= campInformation.getTotalSlots()) return true; //camp is full
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
        //add student list as one csv cell, separated by semicolon
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
            Log.error("csvLine is invalid");
        } else {
            this.campId = Integer.parseInt(split[0]);
            this.visibility = (split[1] == "VISIBLE" ? true : false);
            String[] students = split[2].split(";");
            this.studentList = new ArrayList<>(Arrays.asList(students));
            String campInfoCSV = "";
            for (int i = 3; i < split.length; ++i)
                campInfoCSV += split[i] + ",";
            this.campInformation.fromCSVLine(campInfoCSV);
        }
    }
    
    @Override
    public int getCSVLineLength() {
        return 3 + campInformation.getCSVLineLength();
    }

    public String getCampName(){
        return campInformation.getCampName();
    }

    public List<String> getAttendees(){
        return studentList;
    }
}
