package control;

import java.util.ArrayList;

import entity.CampEnquiry;
import entity.CampSuggestion;
import util.DataStore.FeedbackDataStoreCSVImpl;
import util.DataStore.FeedbackDataStoreInterface;

/**
 * <p>
 * A class to handle all feedback datastore operations.
 * This class uses strategy pattern to call datastore operations.
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 21-11-2023
 */
public class FeedbackDataStoreSubSystem {

    /**
     * Stores a concrete FeedbackDataStoreInterface object.
     */
    private FeedbackDataStoreInterface dataStore = null;

    /**
     * Calls <code>init()</code>.
     * Ensure to construct the object before other systems.
     * If future other DataStore implementations are created,
     * reccommended to create a Factory class to create the DataStoreInterface
     * object.
     */
    public FeedbackDataStoreSubSystem() {
        dataStore = new FeedbackDataStoreCSVImpl();
        init();
    }

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
