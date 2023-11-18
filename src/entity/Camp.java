package entity;

import java.util.ArrayList;
import java.util.Arrays;

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
    private ArrayList<String> studentList; //store student ids
    private boolean visibility; //staff can set this to false to hide, if no one registered and stuff yet

    public Camp() {
        //todo default vals
    }

    public Camp(int campId, CampInformation campInfo, ArrayList<String> studentList) {
        this.campId = campId;
        this.campInfo = campInfo;
        this.studentList = null;
        this.visibility = true;
    }

    public int getCampId() {
        return campId;
    }

    public CampInformation getCampInfo() {
        return campInfo;
    }

    public ArrayList<String> getStudentList() {
        return studentList;
    }

    public boolean isVisibility() {
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
        ret += "," + campInfo.toCSVLine();
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
            this.campInfo.fromCSVLine(campInfoCSV);
        }
    }
    
    @Override
    public int getCSVLineLength() {
        return 3 + campInfo.getCSVLineLength();
    }
}
