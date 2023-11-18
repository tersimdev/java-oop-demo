package entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.ArrayList;

import util.Log;
import util.DataStore.SerializeToCSV;

/**
 * <p>
 * This is a class to represent a camp
 * </p>
 * 
 * @author 
 * @version 1.0
 * @since 5-11-2023
 */
public class Camp implements SerializeToCSV {

    private int campId;
    private CampInformation campInfo;
    private ArrayList<Student> studentList; //store student ids
    private boolean visibility; //staff can set this to false to hide, if no one registered and stuff yet
    private static int totalNumberOfCamps = 1; //use this value to set campId when creating new camps !!!DOES THIS WORK AHAHAHA

    public Camp(int campId, CampInformation campInfo, ArrayList<Student> studentList) {
        this.campId = campId;
        this.campInfo = campInfo;
        this.studentList = null;
        this.visibility = true;
        totalNumberOfCamps++;
    }

    public int getCampId() {
        return campId;
    }

    public ArrayList<Student> getStudentList() {
        return studentList;
    }

    public CampInformation getCampInformation() {
        return campInfo;
    }

    public void registerStudent(Student student) {
        studentList.add(student);
    }

    // private void checkForDateClash(Student student) {
    //     ArrayList<LocalDateTime> dates = campInfo.getDates();
    // }

    private boolean checkRegistrationClosed() {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime deadline = campInfo.getRegistrationClosingDate();
        if (today.compareTo(deadline) >= 0) return true; // registration is closed
        return false;
    }

    private boolean checkCampFull() {
        if (studentList.size() >= campInfo.getTotalSlots()) return true; //camp is full
        return false;
    }

    public static int getTotalNumberOfCamps() {
        return totalNumberOfCamps;
    }

    @Override
    public String toCSVLine() {
        String ret = "";
        //TOOD
        return ret;
    }

    @Override
    public void fromCSVLine(String csvLine) {
        String[] split = csvLine.split(",");
        // //TODO
        // if (split.length != 4) {
        //     Log.error("csvLine is invalid");
        // } else {
        // }
    }
}
