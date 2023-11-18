package control;

import java.util.ArrayList;

import entity.Camp;
import entity.CampEnquiry;
import entity.CampSuggestion;
import entity.User;
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

    public void addCamp(Camp camp) {
        dataStore.addCamp(camp);
    }
    public void deleteCamp(int campId) {
        dataStore.deleteCamp(campId);
    }
    public void updateCamp(int campId) {
        dataStore.updateCamp(campId);
    }
    public ArrayList<Camp> getAllCamps() {
        return dataStore.getAllCamps();
    }

    public void addSuggestion(CampSuggestion suggestion) {
        dataStore.addSuggestion(suggestion);
    }
    public void updateSuggestion(int suggestionId) {
        dataStore.updateSuggestion(suggestionId);
    }
    public ArrayList<CampSuggestion> getAllSuggestions() {
        return dataStore.getAllSuggestions();
    }
    public void addEnquiry(CampEnquiry enquiry) {
        dataStore.addEnquiry(enquiry);
    }
    public void updateEnquiry(int enquiryId) {
        dataStore.updateEnquiry(enquiryId);
    }
    public ArrayList<CampEnquiry> getAllEnquiries() {
        return dataStore.getAllEnquiries();
    }

}
