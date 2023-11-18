package util.DataStore;

import entity.User;
import entity.UserGroup;

import java.time.LocalDateTime;
import java.util.ArrayList;

import entity.Camp;
import entity.Faculty;

/**
 * <p>
 * An interface to handle storage queries needed by the app
 * Uses strategy design pattern
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 2.0
 * @since 18-11-2023
 */
public interface DataStoreInterface {
    public void init();
    public void cleanup();
    public boolean dataExists(String table);

    // user data functions
    public User queryUser(String userID);
    public void updateUser(String userID, String newPassword);

    //TODO, similar functions for camps etc

    // camp data functions
    public void createCamp(int campId, String campName, String description, String location, int totalSlots, int committeeSlots, 
        ArrayList<LocalDateTime> dates, LocalDateTime registrationClosingDate, String staffInChargeId, UserGroup userGroup, Faculty organisingFaculty);
    public void editCamp(int campId);
    public void deleteCamp(int campId);
    public Camp queryCamp(int campId);

}
