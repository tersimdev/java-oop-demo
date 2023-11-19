package entity;

import java.time.LocalDate;
import java.util.ArrayList;

import util.DateStringHelper;
import util.Log;
import util.DataStore.SerializeToCSV;

/**
 * <p>
 * This is a class to represent a camp
 * </p>
 * 
 * @author Jon Daniel Acu Kang
 * @version 1.0
 * @since 19-11-2023
 */
public class CampInformation implements SerializeToCSV {

    private int campId; // used internally
    
    // required parameters
    private String campName; 
    private ArrayList<LocalDate> dates;
    private LocalDate registrationClosingDate;
    private int totalSlots; //for attendees
    private int committeeSlots;
    private String staffInChargeId;
    private UserGroup userGroup;
    private Faculty organisingFaculty; //null if usergroup is wholeNTU;

    // optional parameters
    private String description;
    private String location;

    private CampInformation(CampInformationBuilder builder) {
        this.campName = builder.campName;
        this.dates = builder.dates;
        this.registrationClosingDate = builder.registrationClosingDate;
        this.totalSlots = builder.totalSlots;
        this.committeeSlots = builder.committeeSlots;
        this.staffInChargeId = builder.staffInChargeId;
        this.userGroup = builder.userGroup;
        this.organisingFaculty = builder.organisingFaculty;
        this.description = builder.description;
        this.location = builder.location;
    }

    // builder class
    public static class CampInformationBuilder {

        // required parameters
        private String campName; 
        private ArrayList<LocalDate> dates;
        private LocalDate registrationClosingDate;
        private int totalSlots; //for attendees
        private int committeeSlots;
        private String staffInChargeId;
        private UserGroup userGroup;
        private Faculty organisingFaculty; //null if usergroup is wholeNTU;

        // optional parameters
        private String description;
        private String location;

        public CampInformationBuilder() { // required public constructor
        }

        public CampInformationBuilder setCampName(String campName) {
            this.campName = campName;
            return this;
        }

        public CampInformationBuilder setDates(ArrayList<LocalDate> dates) {
            this.dates = dates;
            return this;
        }

        public CampInformationBuilder setDates(String startDate, int duration) {
            LocalDate firstDate = DateStringHelper.StrToDateConverter(startDate);
            for (int i = 0; i < duration; i++) {
                dates.add(i, firstDate);
                firstDate = firstDate.plusDays(1);
            }
            return this;
        }

        public CampInformationBuilder setRegistrationClosingDate(LocalDate registrationClosingDate) {
            this.registrationClosingDate = registrationClosingDate;
            return this;
        }

        public CampInformationBuilder setTotalSlots(int totalSlots) {
            this.totalSlots = totalSlots;
            return this;
        }

        public CampInformationBuilder setCommitteeSlots(int committeeSlots) {
            this.committeeSlots = committeeSlots;
            return this;
        }

        public CampInformationBuilder setStaffInChargeId(String staffInChargeId) {
            this.staffInChargeId = staffInChargeId;
            return this;
        }

        public CampInformationBuilder setUserGroup(UserGroup userGroup) {
            this.userGroup = userGroup;
            return this;
        }

        public CampInformationBuilder setOrganisingFaculty(Faculty organisingFaculty) {
            this.organisingFaculty = organisingFaculty;
            return this;
        }

        public CampInformationBuilder setLocation(String location) {
            this.location = location;
            return this;
        }

        public CampInformationBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public CampInformation build() {
            return new CampInformation(this);
        }
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

    public void setDates(String startDate, int duration) {
        LocalDate firstDate = DateStringHelper.StrToDateConverter(startDate);
        for (int i = 0; i < duration; i++) {
            dates.add(i, firstDate);
            firstDate = firstDate.plusDays(1);
        }
        return;
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

    public String getStaffInChargeId() {
        return staffInChargeId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        return;
    }

    

    // data store functionality
    @Override
    public String toCSVLine() {
        String ret = "";
        ret += campId + ","
        + campName + ","
        + description + ","
        + location + ","
        + totalSlots + ","
        + committeeSlots + ","
        + staffInChargeId + ","
        + organisingFaculty + ","
        //handle dates last
        + registrationClosingDate + ","
        + dates.get(0) + "," //start date
        + dates.size() + "," //duration of camp
        + userGroup.toCSVLine(); // do this last for simplicity
        return ret;
    }

    @Override
    public void fromCSVLine(String csvLine) {
        String[] split = csvLine.split(",");
        if (split.length != getCSVLineLength()) {
            Log.error("csvLine is invalid");
        } else {
            campId = Integer.parseInt(split[0]);
            campName = split[1];
            description = split[2];
            location = split[3];
            totalSlots = Integer.parseInt(split[4]);
            committeeSlots = Integer.parseInt(split[5]);
            staffInChargeId = split[6];
            organisingFaculty = Faculty.valueOf(split[7]);
            //TODO 
            // use helper convert this string to date format
            registrationClosingDate = DateStringHelper.StrToDateConverter(split[8]);
            String startDate = split[9];
            int duration = Integer.parseInt(split[10]);
            //TODO
            setDates(startDate, duration);
            userGroup = new UserGroup();
            userGroup.fromCSVLine(split[11]);
        }
    }

    @Override
    public int getCSVLineLength() {
        return 11 + userGroup.getCSVLineLength();
    }
}
