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
    public void init();
    public void cleanup();

    // user data functions
    public User queryStaff(String userID);
    public User queryStudent(String userID);
    public void updateUserPassword(String userID, String newPassword);
    public ArrayList<CampCommitteeMember> queryCommitteeMembers(ArrayList<String> committeeMemberIDs);
    public void updateCommitteeMemberDetails(CampCommitteeMember campCommitteeMember);
}
