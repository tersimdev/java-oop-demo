package entity;

import java.util.ArrayList;
import java.time.LocalDateTime;
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
public abstract class CampInformation implements SerializeToCSV {

    private int campId;
    private String campName;
    private String descrription;
    private String location;
    private int totalSlots; //for attendees
    private int committeeSlots;

    private ArrayList<LocalDateTime> dates;
    private LocalDateTime registrationClosingDate;
    
    private Staff staffInCharge;
    private UserGroup userGroup;
    private Faculty organisingFaculty; //null if usergroup is wholeNTU;

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
