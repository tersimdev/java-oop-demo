package entity;

import java.time.LocalDate;
import java.util.ArrayList;

import util.Log;
import util.DataStore.SerializeToCSV;
import util.helpers.DateStringHelper;

/**
 * <p>
 * This is a class to represent a camp. It stores information such as camp name,
 * camp dates, total slots and more.
 * Implemented using builder design pattern.
 * </p>
 * 
 * @author Team 2
 * @version 1.0
 * @since 19-11-2023
 */
public class CampInformation implements SerializeToCSV {

    /**
     * ID of the camp
     */
    private int campId;

    // required parameters
    /**
     * Name of the camp
     */
    private String campName;
    /**
     * Dates the camp is held
     */
    private ArrayList<LocalDate> dates;
    /**
     * Date registration closes
     */
    private LocalDate registrationClosingDate;
    /**
     * Total slots, which equals attendee slots + committee slots
     */
    private int totalSlots;
    /**
     * Number of committee slots
     */
    private int committeeSlots;
    /**
     * Staff userId that made this camp
     */
    private String staffInChargeId;
    /**
     * User group that this camp is available to
     */
    private UserGroup userGroup;
    /**
     * Faculty this camp is available to
     * Null if userGroup is wholeNTU
     */
    private Faculty organisingFaculty;

    // optional parameters
    /**
     * Description of camp
     */
    private String description;
    /**
     * Location of camp
     */
    private String location;

    /**
     * Private constructor for camp information in order to use builder design
     * pattern.
     * 
     * @param builder The camp information builder.
     */
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

    /**
     * A static nested class that allows step-by-step construction of
     * <code>CampInformation</code> objects.
     * The default
     */
    public static class CampInformationBuilder {

        //essentially same as CampInformation
        // required parameters
        /**
         * Name of the camp
         */
        private String campName;
        /**
         * Dates the camp is held
         */
        private ArrayList<LocalDate> dates;
        /**
         * Date registration closes
         */
        private LocalDate registrationClosingDate;
        /**
         * Total slots, which equals attendee slots + committee slots
         */
        private int totalSlots;
        /**
         * Number of committee slots
         */
        private int committeeSlots;
        /**
         * Staff userId that made this camp
         */
        private String staffInChargeId;
        /**
         * User group that this camp is available to
         */
        private UserGroup userGroup;
        /**
         * Faculty this camp is available to
         * Null if userGroup is wholeNTU
         */
        private Faculty organisingFaculty;

        // optional parameters
        /**
         * Description of camp
         */
        private String description;
        /**
         * Location of camp
         */
        private String location;

        /**
         * Public constructor that sets default camp information. All camp information
         * will have to bet through the appropriate setter.
         */
        public CampInformationBuilder() { // set default values here
            campName = "";
            dates = new ArrayList<>();
            totalSlots = 0;
            committeeSlots = 0;
            staffInChargeId = "";
            userGroup = new UserGroup();
            organisingFaculty = Faculty.NULL;
        }

        /**
         * Builder setter for camp name.
         * 
         * @param campName The camp name to be set.
         * @return Returns a camp information builder object.
         */
        public CampInformationBuilder setCampName(String campName) {
            this.campName = campName;
            return this;
        }

        /**
         * Builder setter for dates.
         * 
         * @param dates The dates to be set.
         * @return Returns a camp information builder object.
         */
        public CampInformationBuilder setDates(ArrayList<LocalDate> dates) {
            this.dates = dates;
            return this;
        }

        /**
         * Alternative builder setter for dates.
         * Overloads setDates function.
         * 
         * @param firstDate The first date of the camp.
         * @param duration  The duration of the camp.
         * @return Returns a camp information builder object.
         */
        public CampInformationBuilder setDates(LocalDate firstDate, int duration) {
            for (int i = 0; i < duration; i++) {
                dates.add(i, firstDate);
                firstDate = firstDate.plusDays(1);
            }
            return this;
        }

        /**
         * Builder setter for registration deadline.
         * 
         * @param registrationClosingDate The registration deadline.
         * @return Returns a camp information builder object.
         */
        public CampInformationBuilder setRegistrationClosingDate(LocalDate registrationClosingDate) {
            this.registrationClosingDate = registrationClosingDate;
            return this;
        }

        /**
         * Builder setter for total slots (attendee + committee).
         * 
         * @param totalSlots The total slots.
         * @return Returns a camp information builder object.
         */
        public CampInformationBuilder setTotalSlots(int totalSlots) {
            this.totalSlots = totalSlots;
            return this;
        }

        /**
         * Builder setter for total committee slots.
         * 
         * @param committeeSlots The total committee slots.
         * @return Returns a camp information builder object.
         */
        public CampInformationBuilder setCommitteeSlots(int committeeSlots) {
            this.committeeSlots = committeeSlots;
            return this;
        }

        /**
         * Builder setter for the staff in charge.
         * 
         * @param staffInChargeId The ID of the staff who created the camp.
         * @return Returns a camp information builder object.
         */
        public CampInformationBuilder setStaffInChargeId(String staffInChargeId) {
            this.staffInChargeId = staffInChargeId;
            return this;
        }

        /**
         * Builder setter for the user group the camp is open to.
         * 
         * @param userGroup The user group the camp is open to.
         * @return Returns a camp information builder object.
         */
        public CampInformationBuilder setUserGroup(UserGroup userGroup) {
            this.userGroup = userGroup;
            return this;
        }

        /**
         * Builder setter for the organising faculty of the camp (faculty of the staff
         * in charge).
         * 
         * @param organisingFaculty The organising faculty of the camp.
         * @return Returns a camp information builder object.
         */
        public CampInformationBuilder setOrganisingFaculty(Faculty organisingFaculty) {
            this.organisingFaculty = organisingFaculty;
            return this;
        }

        /**
         * Builder setter for the location of the camp.
         * 
         * @param location The location of the camp.
         * @return Returns a camp information builder object.
         */
        public CampInformationBuilder setLocation(String location) {
            this.location = location;
            return this;
        }

        /**
         * Builder setter for the description of the camp.
         * 
         * @param description The description of the camp.
         * @return Returns a camp information builder object.
         */
        public CampInformationBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        /**
         * Method to return the CampInformation object that has been created.
         * 
         * @return Returns a CampInformation object.
         */
        public CampInformation build() {
            return new CampInformation(this);
        }
    }

    // getters and setters

    /**
     * Getter for the camp name.
     * 
     * @return Returns the camp name.
     */
    public String getCampName() {
        return campName;
    }

    /**
     * Setter for the camp name.
     * 
     * @param campName Camp name to be set.
     */
    public void setCampName(String campName) {
        this.campName = campName;
        return;
    }

    /**
     * Getter for the camp dates.
     * 
     * @return Returns a list of all the camp dates.
     */
    public ArrayList<LocalDate> getDates() {
        return dates;
    }

    /**
     * Alternative setter for the camp dates.
     * Overloads <code>setDates</code>.
     * 
     * @param firstDate The first date of the camp.
     * @param duration  The duration of the camp.
     */
    public void setDates(LocalDate firstDate, int duration) {
        for (int i = 0; i < duration; i++) {
            dates.add(i, firstDate);
            firstDate = firstDate.plusDays(1);
        }
        return;
    }

    /**
     * Setter for the camp dates.
     * 
     * @param dates A list of the camp dates.
     */
    public void setDates(ArrayList<LocalDate> dates) {
        this.dates = dates;
        return;
    }

    /**
     * Getter for the camp registration deadline.
     * 
     * @return Returns the camp registration deadline.
     */
    public LocalDate getRegistrationClosingDate() {
        return registrationClosingDate;
    }

    /**
     * Setter for the camp registration deadline.
     * 
     * @param registrationClosingDate The camp registration deadline to be set.
     */
    public void setRegistrationClosingDate(LocalDate registrationClosingDate) {
        this.registrationClosingDate = registrationClosingDate;
        return;
    }

    /**
     * Getter for the camp's total slots (attendee + committee)
     * 
     * @return Returns the camp's total slots.
     */
    public int getTotalSlots() {
        return totalSlots;
    }

    /**
     * Setter for the camp's total slots (attendee + committee).
     * 
     * @param totalSlots The camp's total slots to be set.
     */
    public void setTotalSlots(int totalSlots) {
        this.totalSlots = totalSlots;
        return;
    }

    /**
     * Getter for the camp's total committee slots.
     * 
     * @return The camp's total commitee slots.
     */
    public int getCommitteeSlots() {
        return committeeSlots;
    }

    /**
     * Setter for the camp's total committee slots.
     * 
     * @param committeeSlots The camp's total committee slots to be set.
     */
    public void setCommitteeSlots(int committeeSlots) {
        this.committeeSlots = committeeSlots;
        return;
    }

    /**
     * Getter for the camp's staff in charge.
     * 
     * @return Returns the ID of the camp's staff in charge.
     */
    public String getStaffInChargeId() {
        return staffInChargeId;
    }

    /**
     * Getter for the user group the camp is open to.
     * 
     * @return Returns the user group the camp is open to.
     */
    public UserGroup getUserGroup() {
        return userGroup;
    }

    /**
     * Getter of the camp's organising faculty (facult of the staff in charge).
     * 
     * @return Returns the camp's organising faculty.
     */
    public Faculty getOrganisingFaculty() {
        return organisingFaculty;
    }

    /**
     * Getter for the camp's location.
     * 
     * @return Returns the camp's location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Setter for the camp's location.
     * 
     * @param location The location to be set.
     */
    public void setLocation(String location) {
        this.location = location;
        return;
    }

    /**
     * Getter for the camp's description.
     * 
     * @return Returns the camp's description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for the camp's description.
     * 
     * @param description Returns the camp's description.
     */
    public void setDescription(String description) {
        this.description = description;
        return;
    }

    // data store functionality
    /**
     * Converts the camp information into a string in CSV format.
     * 
     * @return A string of comma separated values.
     */
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
                // handle dates last
                + DateStringHelper.DateToStrConverter(registrationClosingDate) + ","
                + DateStringHelper.DateToStrConverter(dates.get(0)) + "," // start date
                + dates.size() + "," // duration of camp
                + userGroup.toCSVLine(); // do this last for simplicity
        return ret;
    }

    /**
     * Sets the camp information based on the information from a csvline.
     * 
     * @param csvLine The string containing all the camp's information.
     */
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
            registrationClosingDate = DateStringHelper.StrToDateConverter(split[8]);
            String startDate = split[9];
            int duration = Integer.parseInt(split[10]);
            setDates(DateStringHelper.StrToDateConverter(startDate), duration);
            userGroup = new UserGroup();
            String userGrpStr = "";
            for (int i = 0; i < userGroup.getCSVLineLength(); ++i) {
                userGrpStr += split[11 + i] + ",";
            }
            userGroup.fromCSVLine(userGrpStr);
        }
    }

    /**
     * Gets the length of a csvline containing a camp's information.
     * 
     * @return The length of the csvline.
     */
    @Override
    public int getCSVLineLength() {
        return 11 + userGroup.getCSVLineLength();
    }
}
