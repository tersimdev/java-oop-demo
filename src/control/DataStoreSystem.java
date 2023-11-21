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
     * Initializes data store.
     * Loads data from initial sample csv if needed.
     */
    public void init() {
        dataStore.init();
    }

    /**
     * Cleanup datastore.
     */
    public void cleanup() {
        dataStore.cleanup();
    }

    /**
     * Get a Staff object from datastore.
     * 
     * @param userID id of user
     * @return staff object as User
     */
    public User queryStaff(String userID) {
        return dataStore.queryStaff(userID);
    }

    /**
     * Get a Student object from datastore.
     * 
     * @param userID id of user
     * @return student object as User
     */
    public User queryStudent(String userID) {
        return dataStore.queryStudent(userID);
    }

    /**
     * Set password field for user.
     * 
     * @param userID      id of user
     * @param newPassword new password to set to
     */
    public void updateUserPassword(String userID, String newPassword) {
        dataStore.updateUserPassword(userID, newPassword);
    }

    /**
     * Create a camp entry in data store.
     * 
     * @param camp camp to serialize
     */
    public void addCamp(Camp camp) {
        dataStore.addCamp(camp);
    }

    /**
     * Delete specified camp.
     * 
     * @param campId id of camp
     */
    public void deleteCamp(int campId) {
        dataStore.deleteCamp(campId);
    }

    /**
     * Updates camp details in data store.
     * 
     * @param camp updated camp object
     */
    public void updateCampDetails(Camp camp) {
        dataStore.updateCampDetails(camp);
    }

    /**
     * Get all camps in datastore.
     * 
     * @return array list of camps, sorted ascending by id
     */
    public ArrayList<Camp> getAllCamps() {
        return dataStore.getAllCamps();
    }

    /**
     * Create a suggestion entry in data store
     * 
     * @param suggestion suggestion to serialize
     */
    public void addSuggestion(CampSuggestion suggestion) {
        dataStore.addSuggestion(suggestion);
    }

    /**
     * Delete specified suggestion.
     * 
     * @param suggestionId id of suggestion
     */
    public void deleteSuggestion(int suggestionId) {
        dataStore.deleteSuggestion(suggestionId);
    }

    /**
     * Updates suggestion details in data store.
     * 
     * @param suggestion updated suggestion object
     */
    public void updateSuggestion(CampSuggestion suggestion) {
        dataStore.updateSuggestion(suggestion);
    }

    /**
     * Get all suggestions in datastore.
     * 
     * @return array list of suggestions, sorted ascending by id
     */
    public ArrayList<CampSuggestion> getAllSuggestions() {
        return dataStore.getAllSuggestions();
    }

    /**
     * Create a enquiry entry in data store
     * 
     * @param enquiry enquiry to serialize
     */
    public void addEnquiry(CampEnquiry enquiry) {
        dataStore.addEnquiry(enquiry);
    }

    /**
     * Delete specified enquiry.
     * 
     * @param enquiryId id of enquiry
     */
    public void deleteEnquiry(int enquiryId) {
        dataStore.deleteEnquiry(enquiryId);
    }

    /**
     * Updates enquiry details in data store.
     * 
     * @param enquiry updated enquiryF object
     */
    public void updateEnquiry(CampEnquiry enquiry) {
        dataStore.updateEnquiry(enquiry);
    }

    /**
     * Get all enquiries in datastore.
     * 
     * @return array list of enquiries, sorted ascending by id
     */
    public ArrayList<CampEnquiry> getAllEnquiries() {
        return dataStore.getAllEnquiries();
    }

}
