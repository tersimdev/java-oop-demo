package util.DataStore;

import java.util.ArrayList;

import entity.Camp;
import entity.CampEnquiry;
import entity.CampSuggestion;
import entity.User;

/**
 * <p>
 * An interface to handle storage queries needed by the app
 * Uses strategy design pattern
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 19-11-2023
 */
public interface DataStoreInterface {
    public void init();
    public void cleanup();
    public boolean dataExists(String table);

    // user data functions
    public User queryStaff(String userID);
    public User queryStudent(String userID);
    public void updateUserPassword(String userID, String newPassword);

    //TODO, similar functions for camps etc

    // camp data functions
    public void addCamp(Camp camp);
    public void deleteCamp(int campId);
    public void updateCampDetails(Camp camp);
    public ArrayList<Camp> getAllCamps();

    // feedback data functions
    public void addSuggestion(CampSuggestion suggestion);
    //public void updateSuggestion(int suggestionId);
    public ArrayList<CampSuggestion> getAllSuggestions();
    public void addEnquiry(CampEnquiry enquiry);
    //public void updateEnquiry(int enquiryId);
    public ArrayList<CampEnquiry> getAllEnquiries();
}
