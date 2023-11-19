package entity;

import java.time.LocalDate;
import java.util.ArrayList;

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

    private ArrayList<LocalDate> dates;
    private LocalDate registrationClosingDate;
    
    private String staffInChargeId;
    private UserGroup userGroup;
    private Faculty organisingFaculty; //null if usergroup is wholeNTU;

    public CampInformation (String campName, String description, String location, int totalSlots, int committeeSlots, 
        ArrayList<LocalDate> dates, LocalDate registrationClosingDate, String staffInChargeId, UserGroup userGroup, Faculty organisingFaculty) {
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

    // getters and setters

    public String getCampName() {
        return campName;
    }

    public void setCampName(String campName) {
        this.campName = campName;
        return;
    }

    public ArrayList<LocalDate> getDates() {
        return dates;
    }

    public void setDates(ArrayList<LocalDate> dates) {
        this.dates = dates;
        return;
    }

    public LocalDate getRegistrationClosingDate() {
        return registrationClosingDate;
    }

    public void setRegistrationClosingDate(LocalDate registrationClosingDate) {
        this.registrationClosingDate = registrationClosingDate;
        return;
    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
        return;
    }

    public int getTotalSlots() {
        return totalSlots;
    }

    public void setTotalSlots(int totalSlots) {
        this.totalSlots = totalSlots;
        return;
    }

    public int getCommitteeSlots() {
        return committeeSlots;
    }

    public void setCommitteeSlots(int committeeSlots) {
        this.committeeSlots = committeeSlots;
        return;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        return;
    }

    public String getStaffInChargeId() {
        return staffInChargeId;
    }

    // data store functionality

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

    @Override
    public int getCSVLineLength() {
        return 1; //todo
    }
}
