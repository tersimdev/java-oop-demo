package entity;

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
public abstract class Camp implements SerializeToCSV {

    private int campId;
    private CampInformation campInfo;
    private ArrayList<String> studentList; //store student ids
    private boolean visibility; //staff can set this to false to hide, if no one registered and stuff yet

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