package util.DataStore;

import java.util.ArrayList;

import entity.CampEnquiry;
import entity.CampFeedback;
import entity.CampSuggestion;

/**
 * <p>
 * An interface to handle feedback data storage queries
 * Uses strategy design pattern
 * Each function corresponds to <code>DataStoreSystem</code>,
 * hence refer to that class for documentation.
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 23-11-2023
 */
public interface FeedbackDataStoreInterface {
    /**
     * Initialize datastore
     */
    public void init();

    /**
     * Cleanup datastore
     */
    public void cleanup();

    // feedback data functions
    /**
     * Add suggestion
     * 
     * @param suggestion suggestion
     */
    public void addSuggestion(CampSuggestion suggestion);

    /**
     * Delete suggestion
     * 
     * @param suggestionId id of suggestion
     */
    public void deleteSuggestion(int suggestionId);

    /**
     * Update suggestion
     * 
     * @param suggestion suggestion
     */
    public void updateSuggestion(CampSuggestion suggestion);

    /**
     * Gets all suggestions
     * 
     * @return array list of suggestions
     */
    public ArrayList<CampFeedback> getAllSuggestions();

    /**
     * Add enquiry
     * 
     * @param enquiry enquiry
     */
    public void addEnquiry(CampEnquiry enquiry);

    /**
     * Delete enquiry
     * 
     * @param enquiryId id of enquiry
     */
    public void deleteEnquiry(int enquiryId);

    /**
     * Update enquiry
     * 
     * @param enquiry enquiry
     */
    public void updateEnquiry(CampEnquiry enquiry);

    /**
     * Gets all enquiries
     * 
     * @return array list of enquiries
     */
    public ArrayList<CampFeedback> getAllEnquiries();
}
