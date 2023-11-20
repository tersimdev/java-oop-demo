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
 * Singleton allows it to maintain a single state throughout the app lifetime 
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 19-11-2023
 */
public class DataStoreSystem {
    public DataStoreSystem() {
        dataStore = new DataStoreCSVImpl();
        init();
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

    public User queryStaff(String userID) {
        return dataStore.queryStaff(userID);
    }
    public User queryStudent(String userID) {
        return dataStore.queryStudent(userID);
    }

    public void updateUserPassword(String userID, String newPassword) {
        dataStore.updateUserPassword(userID, newPassword);
    }

    public void addCamp(Camp camp) {
        dataStore.addCamp(camp);
    }
    public void deleteCamp(int campId) {
        dataStore.deleteCamp(campId);
    }
    public void updateCampDetails(Camp camp) {
        //might split this up into multiple update
        //e.g. updateVisibility, updateDates, etc
        dataStore.updateCampDetails(camp);
    }
    public ArrayList<Camp> getAllCamps() {
        return dataStore.getAllCamps();
    }

    public void addSuggestion(CampSuggestion suggestion) {
        dataStore.addSuggestion(suggestion);
    }
    // public void updateSuggestion(int suggestionId) {
    //     dataStore.updateSuggestion(suggestionId);
    // }
    public ArrayList<CampSuggestion> getAllSuggestions() {
        return dataStore.getAllSuggestions();
    }
    public void addEnquiry(CampEnquiry enquiry) {
        dataStore.addEnquiry(enquiry);
    }
    // public void updateEnquiry(int enquiryId) {
    //     dataStore.updateEnquiry(enquiryId);
    // }
    public ArrayList<CampEnquiry> getAllEnquiries() {
        return dataStore.getAllEnquiries();
    }

}
