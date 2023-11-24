import boundary.ConsoleUI;
import control.CampSystem;
import control.DataStoreSystem;
import control.EnquirySystem;
import control.LoginSystem;
import control.ReportSystem;
import control.SuggestionSystem;

/**
 * <p>
 * Entry point for our application.
 * Creates UI and System objects and passes 
 * them in using Dependency Injection
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 19-11-2023
 */
public class CAMSApp {
    /**
     * UI to display to user
     */
    private ConsoleUI consoleUI;
    /**
     * System to store data for data permanence
     */
    private DataStoreSystem dataStoreSystem;
    /**
     * System to handle login logic
     */
    private LoginSystem loginSystem;
    /**
     * System to handle camp logic
     */
    private CampSystem campSystem;
    /**
     * System to handle enquiry logic
     */
    private EnquirySystem enquirySystem;
    /**
     * System to handle suggestion logic
     */
    private SuggestionSystem suggestionSystem;
    /**
     * System to handle report generation logic
     */
    private ReportSystem reportSystem;

    /**
     * Keeps track whether app has been cleaned up
     */
    private boolean running = false;

    /**
     * Intializes all systems and creates the UI.
     * Sets running to true.
     */
    public void init() {
        
        //data store should be created first!
        dataStoreSystem = new DataStoreSystem();
        dataStoreSystem.init();
        
        // create systems
        loginSystem = new LoginSystem(dataStoreSystem);
        campSystem = new CampSystem(dataStoreSystem);
        enquirySystem = new EnquirySystem(dataStoreSystem);
        suggestionSystem = new SuggestionSystem(dataStoreSystem);
        reportSystem = new ReportSystem(dataStoreSystem);

        // create ui
        consoleUI = new ConsoleUI();
        consoleUI.init(loginSystem, campSystem, enquirySystem, suggestionSystem, reportSystem);

        running = true;
    }

    /**
     * run app main update loop.
     * 
     * @return if app should exit
     */
    public boolean run() {
        return consoleUI.run();
    }

    /**
     * Does systems cleanup if app was running.
     */
    public void cleanup() {
        if (!running)
            return; // alr cleaned
        // cleanup stuff here
        consoleUI.cleanup();
        dataStoreSystem.cleanup(); // crucial, as it saves data
        running = false;
    }

    /**
     * Returns whether app did init and ran. 
     * Used to determine if cleanup needs to be done.
     * 
     * @return whether app is running
     */
    public boolean isRunning() {
        return running;
    }
};