package control;

import java.util.ArrayList;

import entity.CampCommitteeMember;
import entity.User;
import util.DataStore.UserDataStoreCSVImpl;
import util.DataStore.UserDataStoreInterface;

/**
 * <p>
 * A class to handle all user datastore operations.
 * This class uses strategy pattern to call datastore operations.
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 23-11-2023
 */
public class UserDataStoreSubSystem {

    /**
     * Stores a concrete UserDataStoreInterface object.
     */
    private UserDataStoreInterface dataStore = null;

    /**
     * Calls <code>init()</code>.
     * Ensure to construct the object before other systems.
     * If future other DataStore implementations are created,
     * reccommended to create a Factory class to create the DataStoreInterface
     * object.
     */
    public UserDataStoreSubSystem() {
        dataStore = new UserDataStoreCSVImpl();
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
     * Updates camp commitee member details in data store.
     * @param campCommitteeMember updated camp committee member object.
     */
    public void updateCommitteeMemberDetails(CampCommitteeMember campCommitteeMember) {
        dataStore.updateCommitteeMemberDetails(campCommitteeMember);
    }
}
