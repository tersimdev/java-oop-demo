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
    /**
     * Initialize datastore
     */
    public void init();

    /**
     * Cleanup datastore
     */
    public void cleanup();

    // camp data functions
    /**
     * Add camp
     * 
     * @param camp camp object
     */
    public void addCamp(Camp camp);

    /**
     * Delete camp
     * 
     * @param campId id of camp
     */
    public void deleteCamp(int campId);

    /**
     * Update camp
     * 
     * @param camp camp object
     */
    public void updateCampDetails(Camp camp);

    /**
     * Returns all camps in datastore
     * 
     * @return array list of camps
     */
    public ArrayList<Camp> getAllCamps();
}
