package control;

import java.util.ArrayList;

import entity.Camp;
import entity.CampCommitteeMember;
import entity.CampEnquiry;
import entity.CampSuggestion;
import entity.User;
import util.DataStore.CampDataStoreCSVImpl;
import util.DataStore.CampDataStoreInterface;
import util.DataStore.FeedbackDataStoreCSVImpl;
import util.DataStore.FeedbackDataStoreInterface;
import util.DataStore.UserDataStoreCSVImpl;
import util.DataStore.UserDataStoreInterface;

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
     * Stores a concrete UserDataStoreInterface object.
     */
    private UserDataStoreInterface userDataStore = null;
    /**
     * Stores a concrete CampDataStoreInterface object.
     */
    private CampDataStoreInterface campDataStore = null;
    /**
     * Stores a concrete FeedbackDataStoreInterface object.
     */
    private FeedbackDataStoreInterface feedbackDataStore = null;

    /**
     * Calls <code>init()</code>.
     * Ensure to construct the object before other systems.
     * If future other DataStore implementations are created,
     * reccommended to create a Factory class to create the DataStoreInterface
     * object.
     */
    public DataStoreSystem() {
        userDataStore = new UserDataStoreCSVImpl();
        campDataStore = new CampDataStoreCSVImpl();
        feedbackDataStore = new FeedbackDataStoreCSVImpl();
        init();
    }

    /**
     * Initializes data store.
     * Loads data from initial sample csv if needed.
     */
    public void init() {
        userDataStore.init();
        campDataStore.init();
        feedbackDataStore.init();
    }

    /**
     * Cleanup datastore.
     */
    public void cleanup() {
        userDataStore.cleanup();
        campDataStore.cleanup();
        feedbackDataStore.cleanup();
    }

    /**
     * Get a Staff object from datastore.
     * 
     * @param userID id of user
     * @return staff object as User
     */
    public User queryStaff(String userID) {
        return userDataStore.queryStaff(userID);
    }

    /**
     * Get a Student object from datastore.
     * 
     * @param userID id of user
     * @return student object as User
     */
    public User queryStudent(String userID) {
        return userDataStore.queryStudent(userID);
    }

    /**
     * Set password field for user.
     * 
     * @param userID      id of user
     * @param newPassword new password to set to
     */
    public void updateUserPassword(String userID, String newPassword) {
        userDataStore.updateUserPassword(userID, newPassword);
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
        return userDataStore.queryCommitteeMembers(committeeMemberIDs);
    }

    /**
     * Create a camp entry in data store.
     * 
     * @param camp camp to serialize
     */
    public void addCamp(Camp camp) {
        campDataStore.addCamp(camp);
    }

    /**
     * Delete specified camp.
     * 
     * @param campId id of camp
     */
    public void deleteCamp(int campId) {
        campDataStore.deleteCamp(campId);
    }

    /**
     * Updates camp details in data store.
     * 
     * @param camp updated camp object
     */
    public void updateCampDetails(Camp camp) {
        campDataStore.updateCampDetails(camp);
    }

    /**
     * Get all camps in datastore.
     * 
     * @return array list of camps, sorted ascending by id
     */
    public ArrayList<Camp> getAllCamps() {
        return campDataStore.getAllCamps();
    }

    /**
     * Create a suggestion entry in data store
     * 
     * @param suggestion suggestion to serialize
     */
    public void addSuggestion(CampSuggestion suggestion) {
        feedbackDataStore.addSuggestion(suggestion);
    }

    /**
     * Delete specified suggestion.
     * 
     * @param suggestionId id of suggestion
     */
    public void deleteSuggestion(int suggestionId) {
        feedbackDataStore.deleteSuggestion(suggestionId);
    }

    /**
     * Updates suggestion details in data store.
     * 
     * @param suggestion updated suggestion object
     */
    public void updateSuggestion(CampSuggestion suggestion) {
        feedbackDataStore.updateSuggestion(suggestion);
    }

    /**
     * Get all suggestions in datastore.
     * 
     * @return array list of suggestions, sorted ascending by id
     */
    public ArrayList<CampSuggestion> getAllSuggestions() {
        return feedbackDataStore.getAllSuggestions();
    }

    /**
     * Create a enquiry entry in data store
     * 
     * @param enquiry enquiry to serialize
     */
    public void addEnquiry(CampEnquiry enquiry) {
        feedbackDataStore.addEnquiry(enquiry);
    }

    /**
     * Delete specified enquiry.
     * 
     * @param enquiryId id of enquiry
     */
    public void deleteEnquiry(int enquiryId) {
        feedbackDataStore.deleteEnquiry(enquiryId);
    }

    /**
     * Updates enquiry details in data store.
     * 
     * @param enquiry updated enquiryF object
     */
    public void updateEnquiry(CampEnquiry enquiry) {
        feedbackDataStore.updateEnquiry(enquiry);
    }

    /**
     * Get all enquiries in datastore.
     * 
     * @return array list of enquiries, sorted ascending by id
     */
    public ArrayList<CampEnquiry> getAllEnquiries() {
        return feedbackDataStore.getAllEnquiries();
    }

}
