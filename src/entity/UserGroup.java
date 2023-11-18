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
    private boolean wholeNTU;
    private Faculty faculty;

    public UserGroup() {
        faculty = null;
    }

    public boolean isWholeNTU() {
        return wholeNTU;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public UserGroup setWholeNTU() {
        wholeNTU = true;
        faculty = null;
        return this;
    }

    public UserGroup setFaculty(Faculty faculty) {
        this.faculty = faculty;
        wholeNTU = false;
        return this;
    }

    @Override
    public String toCSVLine() {
        String ret = "";
        ret += (wholeNTU ? "NTU" : "FACULTY") + ","
                + faculty.toString();
        return ret;
    }

    @Override
    public void fromCSVLine(String csvLine) {
        String[] split = csvLine.split(",");
        if (split.length != getCSVLineLength()) {
            Log.error("csvLine is invalid");
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

    @Override
    public int getCSVLineLength() {
        return 2;
    }
}
