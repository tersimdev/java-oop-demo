package util.DataStore;

import java.util.ArrayList;

import entity.CampEnquiry;
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
    public void init();
    public void cleanup();

    // feedback data functions
    public void addSuggestion(CampSuggestion suggestion);
    public void deleteSuggestion(int suggestionId);
    public void updateSuggestion(CampSuggestion suggestion);
    public ArrayList<CampSuggestion> getAllSuggestions();
    public void addEnquiry(CampEnquiry enquiry);
    public void deleteEnquiry(int enquiryId);
    public void updateEnquiry(CampEnquiry enquiry);
    public ArrayList<CampEnquiry> getAllEnquiries();
}
