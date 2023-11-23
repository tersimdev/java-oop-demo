package util.DataStore;

import java.util.ArrayList;

import entity.Camp;

/**
 * <p>
 * An interface to handle storage queries of camp data
 * Uses strategy design pattern
 * Each function corresponds to <code>DataStoreSystem</code>,
 * hence refer to that class for documentation.
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 23-11-2023
 */
public interface CampDataStoreInterface {
    public void init();
    public void cleanup();

    // camp data functions
    public void addCamp(Camp camp);
    public void deleteCamp(int campId);
    public void updateCampDetails(Camp camp);
    public ArrayList<Camp> getAllCamps();
}
