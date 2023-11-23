package control;

import util.Log;

/**
 * <p>
 * A class composed of XXDataStoreSubSystems.
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 21-11-2023
 */
public class DataStoreSystem {

    /**
     * Subsystem to handle user data store operations.
     */
    private UserDataStoreSubSystem userDataStoreSubSystem;
    /**
     * Subsystem to handle camp data store operations.
     */
    private CampDataStoreSubSystem campDataStoreSubSystem;
    /**
     * Subsystem to handle feedback data store operations.
     */
    private FeedbackDataStoreSubSystem feedbackDataStoreSubSystem;

    /**
     * Ensure to construct the object before other systems.
     */
    public DataStoreSystem() {
        userDataStoreSubSystem = new UserDataStoreSubSystem();
        campDataStoreSubSystem = new CampDataStoreSubSystem();
        feedbackDataStoreSubSystem = new FeedbackDataStoreSubSystem();
        //Log.info("creating data stores");
    }

    /**
     * Initializes all sub systems.
     */
    public void init() {
        Log.info("Creating data stores");
        userDataStoreSubSystem.init();
        campDataStoreSubSystem.init();
        feedbackDataStoreSubSystem.init();
    }

    /**
     * Cleanup datastore.
     */
    public void cleanup() {
        Log.info("Saving all data to CSVs");
        userDataStoreSubSystem.cleanup();
        campDataStoreSubSystem.cleanup();
        feedbackDataStoreSubSystem.cleanup();
    }

    /**
     * Gets the user data subsystem
     */
    public UserDataStoreSubSystem getUserDataStoreSubSystem() {
        return userDataStoreSubSystem;
    }

    /**
     * Gets the camp data subsystem
     */
    public CampDataStoreSubSystem getCampDataStoreSubSystem() {
        return campDataStoreSubSystem;
    }

    /**
     * Gets the feedback data subsystem
     */
    public FeedbackDataStoreSubSystem getFeedbackDataStoreSubSystem() {
        return feedbackDataStoreSubSystem;
    }
}
