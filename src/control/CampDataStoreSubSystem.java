package control;

import java.util.ArrayList;

import entity.Camp;
import util.DataStore.CampDataStoreCSVImpl;
import util.DataStore.CampDataStoreInterface;

/**
 * <p>
 * A class to handle all camp datastore operations.
 * This class uses strategy pattern to call datastore operations.
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 23-11-2023
 */
public class CampDataStoreSubSystem {

    /**
     * Stores a concrete CampDataStoreInterface object.
     */
    private CampDataStoreInterface dataStore = null;

    /**
     * Calls <code>init()</code>.
     * Ensure to construct the object before other systems.
     * If future other DataStore implementations are created,
     * reccommended to create a Factory class to create the DataStoreInterface
     * object.
     */
    public CampDataStoreSubSystem() {
        dataStore = new CampDataStoreCSVImpl();
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
}
