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
public class CampInformation implements SerializeToCSV {

    private int campId;
    private String campName;
    private String description;
    private String location;
    private int totalSlots; //for attendees
    private int committeeSlots;

    private ArrayList<LocalDateTime> dates;
    private LocalDateTime registrationClosingDate;
    
    private String staffInChargeId;
    private UserGroup userGroup;
    private Faculty organisingFaculty; //null if usergroup is wholeNTU;

    public CampInformation (String campName, String description, String location, int totalSlots, int committeeSlots, 
        ArrayList<LocalDateTime> dates, LocalDateTime registrationClosingDate, String staffInChargeId, UserGroup userGroup, Faculty organisingFaculty) {
            this.campName = campName;
            this.description = description;
            this.location = location;
            this.totalSlots = totalSlots;
            this.committeeSlots = committeeSlots;
            this.dates = dates;
            this.registrationClosingDate = registrationClosingDate;
            this.staffInChargeId = staffInChargeId;
            this.userGroup = userGroup;
            this.organisingFaculty = organisingFaculty;
        }

    public String getCampName() {
        return campName;
    }

    public ArrayList<LocalDateTime> getDates() {
        return dates;
    }

    public LocalDateTime getRegistrationClosingDate() {
        return registrationClosingDate;
    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public String getLocation() {
        return location;
    }

    public int getTotalSlots() {
        return totalSlots;
    }

    public int getCommitteeSlots() {
        return committeeSlots;
    }

    public String getDescription() {
        return description;
    }

    public String getStaffInChargeId() {
        return staffInChargeId;
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
