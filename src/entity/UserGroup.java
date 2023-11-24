package entity;

import util.Log;
import util.DataStore.SerializeToCSV;

/**
 * <p>
 * This is a class to represent user group of a camp
 * It is either faculty, in which it has a faculty enum to specify,
 * or it is the whole NTU
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 1-11-2023
 */
public class UserGroup implements SerializeToCSV {
    /**
     * Boolean value to represent if a camp is open to the whole of NTU.
     */
    private boolean wholeNTU;
    /**
     * The faculty that a camp is open to.
     */
    private Faculty faculty;

    /**
     * Default constructor of a UserGroup object.
     */
    public UserGroup() {
        wholeNTU = true;
        faculty = Faculty.NULL;
    }

    /**
     * Checks if user group is set to the whole of NTU.
     * 
     * @return True if the user group is set to the whole of NTU.
     */
    public boolean isWholeNTU() {
        return wholeNTU;
    }

    /**
     * Getter for the faculty the user group is set to.
     * 
     * @return The faculty the user group is set to.
     */
    public Faculty getFaculty() {
        return faculty;
    }

    /**
     * Sets the user group to be open to the whole of NTU, and faculty to null.
     * 
     * @return A UserGroup object.
     */
    public UserGroup setWholeNTU() {
        wholeNTU = true;
        faculty = Faculty.NULL;
        return this;
    }

    /**
     * Sets the faculty of the user group, and wholeNTU to false.
     * 
     * @param faculty The faculty to be set.
     * @return A UserGroup object.
     */
    public UserGroup setFaculty(Faculty faculty) {
        this.faculty = faculty;
        wholeNTU = false;
        return this;
    }

    /**
     * Converts the user group's wholeNTU status and faculty into a string
     * in CSV format.
     * 
     * @return A string of comma separated values.
     */
    @Override
    public String toCSVLine() {
        String ret = "";
        ret += (wholeNTU ? "NTU" : "FACULTY") + ","
                + faculty.toString();
        return ret;
    }

    /**
     * Sets the user group's wholeNTU status and faculty based on the information
     * from a csvline.
     * 
     * @param csvLine The string containing all the user's information.
     */
    @Override
    public void fromCSVLine(String csvLine) {
        String[] split = csvLine.split(",");
        if (split.length != getCSVLineLength()) {
            Log.error("usergroup csvLine is invalid, expected " + getCSVLineLength() + " but got " + split.length);
            Log.error(csvLine);
        } else {
            wholeNTU = split[0].trim().equals("NTU");
            try {
                faculty = Faculty.valueOf(split[1]);
            } catch (IllegalArgumentException e) {
                Log.error("invalid faculty, doesnt correspond to enum: " + split[1]);
                faculty = Faculty.NULL; // give it null value
            }
        }
    }

    /**
     * Gets the length of a csvline containing a user group's information.
     * 
     * @return The length of the csvline.
     */
    @Override
    public int getCSVLineLength() {
        return 2;
    }
}
