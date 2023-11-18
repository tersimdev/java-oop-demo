package control;

import entity.User;

import java.util.ArrayList;

import entity.Camp;
import entity.CampInformation;

/**
 * <p>
 * A singleton class that stores all camps, and controls access to them 
 * </p>
 * 
 * @author 
 * @version 2.0
 * @since 18-11-2023
 */
public class CampSystem {
    private static CampSystem instance = null;

    private CampSystem() {
    }

    public static CampSystem getInstance() {
        if (instance == null)
            instance = new CampSystem();
        return instance;
    }

    // Staff functions
    public Camp createCamp(User user) {

        int campId;
        CampInformation campInfo;
        ArrayList<String> studentList; //store student ids




        return newCamp;
    }

    public Camp editCamp() {

    }

    public Camp deleteCamp() {

    }

    public Camp toggleCampVisibility() {

    }
}
