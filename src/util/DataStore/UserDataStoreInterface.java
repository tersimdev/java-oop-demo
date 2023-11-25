package util.DataStore;

import java.util.ArrayList;

import entity.CampCommitteeMember;
import entity.User;

/**
 * <p>
 * An interface to handle user data storage queries
 * Uses strategy design pattern
 * Each function corresponds to <code>DataStoreSystem</code>,
 * hence refer to that class for documentation.
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 23-11-2023
 */
public interface UserDataStoreInterface {
    /**
     * Initialize datastore
     */
    public void init();

    /**
     * Cleanup datastore
     */
    public void cleanup();

    // user data functions
    /**
     * Get Staff object
     * 
     * @param userID id of staff
     * @return staff object
     */
    public User queryStaff(String userID);

    /**
     * Get Student object
     * 
     * @param userID id of student
     * @return student object
     */
    public User queryStudent(String userID);

    /**
     * Updates password of user object
     * 
     * @param userID      id of user
     * @param newPassword new password to set
     */
    public void updateUserPassword(String userID, String newPassword);

    /**
     * Gets CampCommitteeMember objects
     * 
     * @param committeeMemberIDs ids of committee members
     * @return array list of committee member object
     */
    public ArrayList<CampCommitteeMember> queryCommitteeMembers(ArrayList<String> committeeMemberIDs);

    /**
     * Updates camp committee member details
     * 
     * @param campCommitteeMember committee member object
     */
    public void updateCommitteeMemberDetails(CampCommitteeMember campCommitteeMember);
}
