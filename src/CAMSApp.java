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
 * Creates UI and System objects,
 * with exception of DataStoreSystem which is a singleton
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 19-11-2023
 */
public class CAMSApp {
    private ConsoleUI consoleUI;
    private DataStoreSystem dataStoreSystem;
    private LoginSystem loginSystem;
    private CampSystem campSystem;
    private EnquirySystem enquirySystem;
    private SuggestionSystem suggestionSystem;
    private ReportSystem reportSystem;

    private boolean running = false;

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
     * run app main update loop
     * 
     * @return if app should exit
     */
    public boolean run() {
        return consoleUI.run();
    }

    public void cleanup() {
        if (!running)
            return; // alr cleaned
        // cleanup stuff here
        consoleUI.cleanup();
        dataStoreSystem.cleanup(); // crucial, as it saves data
        running = false;
    }

    public boolean isRunning() {
        return running;
    }
};