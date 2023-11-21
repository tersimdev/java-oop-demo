package control;

import java.util.ArrayList;

import entity.Camp;
import entity.CampCommitteeMember;
import entity.CampEnquiry;
import entity.CampSuggestion;
import entity.User;
import util.DataStore.DataStoreInterface;
import util.DataStore.DataStoreCSVImpl;

/**
 * <p>
 * A class to handle all datastore operations.
 * This class uses strategy pattern to call datastore operations.
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 21-11-2023
 */
public class DataStoreSystem {

    /**
     * Stores a concrete DataStoreInterface object.
     */
    private DataStoreInterface dataStore = null;

    /**
     * Calls <code>init()</code>.
     * Ensure to construct the object before other systems.
     * If future other DataStore implementations are created,
     * reccommended to create a Factory class to create the DataStoreInterface
     * object.
     */
    public DataStoreSystem() {
        dataStore = new DataStoreCSVImpl();
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
     * Get a list of camp committee member objects from datastore given the ids.
     * Note, this does not check if the student is a camp committee member,
     * assumes that given ids are members.
     * Use <code>CampCommitteeMember.getIsMember()</code> to check.
     * 
     * @param committeeMemberIDs the ids of the members to retrieve
     * @return array list of <code>CampCommitteeMember</code> objects.
     */
    public ArrayList<CampCommitteeMember> queryCommitteeMembers(ArrayList<String> committeeMemberIDs) {
        return dataStore.queryCommitteeMembers(committeeMemberIDs);
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
