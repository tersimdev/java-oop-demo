package control;

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
     * Calls <code>init()</code>.
     * Ensure to construct the object before other systems.
     */
    public DataStoreSystem() {
        userDataStoreSubSystem = new UserDataStoreSubSystem();
        campDataStoreSubSystem = new CampDataStoreSubSystem();
        feedbackDataStoreSubSystem = new FeedbackDataStoreSubSystem();
        init();
    }

    /**
     * Initializes all sub systems.
     */
    public void init() {
        userDataStoreSubSystem.init();
        campDataStoreSubSystem.init();
        feedbackDataStoreSubSystem.init();
    }

    /**
     * Cleanup datastore.
     */
    public void cleanup() {
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
