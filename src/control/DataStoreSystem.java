package control;

import entity.User;
import entity.UserGroup;

import java.time.LocalDateTime;
import java.util.ArrayList;

import entity.Camp;
import entity.Faculty;
import util.DataStore.DataStoreInterface;
import util.DataStore.DataStoreCSVImpl;

/**
 * <p>
 * A singleton class to handle all datastore operations
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 1-11-2023
 */
public class DataStoreSystem {
    private static DataStoreSystem instance = null;

    private DataStoreSystem() {
        dataStore = new DataStoreCSVImpl();
        init();
    }

    public static DataStoreSystem getInstance() {
        if (instance == null)
            instance = new DataStoreSystem();
        return instance;
    }

    private DataStoreInterface dataStore = null;

    /**
     * Initializes data store
     * Loads data from initial sample csv if needed
     */
    public void init() {
        dataStore.init();
    }

    public void cleanup() {
        dataStore.cleanup();
    }

    // query for users with username
    public User queryUser(String userID) {
        return dataStore.queryUser(userID);
    }

    public void updateUser(String userID, String newPassword) {
        dataStore.updateUser(userID, newPassword);
    }


    //todo add functions to update camps, enquiries, feedback

    public void createCamp(int campId, String campName, String description, String location, int totalSlots, int committeeSlots, 
        ArrayList<LocalDateTime> dates, LocalDateTime registrationClosingDate, String staffInChargeId, UserGroup userGroup, Faculty organisingFaculty) {
            dataStore.createCamp(campId, campName, description, location, totalSlots, committeeSlots, dates, registrationClosingDate, staffInChargeId, userGroup, organisingFaculty);
        }

    public void editCamp(int campId) {
        dataStore.editCamp(campId);
    }

    public void deleteCamp(int campId) {
        
    }

    public Camp queryCamp(int campId) {
        return dataStore.queryCamp(campId);
    }
}
