import boundary.ConsoleUI;
import control.CampSystem;
import control.DataStoreSystem;
import control.FeedbackSystem;
import control.LoginSystem;
import control.ReportSystem;
import util.Input;

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
    private LoginSystem loginSystem;
    private CampSystem campSystem;
    private FeedbackSystem feedbackSystem;
    private ReportSystem reportSystem;

    public void init() {
        //init singletons
        DataStoreSystem.getInstance(); 
        
        //create systems
        loginSystem = new LoginSystem();
        campSystem = new CampSystem();
        feedbackSystem = new FeedbackSystem();
        reportSystem = new ReportSystem();

        //create ui
        consoleUI = new ConsoleUI();
        consoleUI.init(loginSystem, campSystem, feedbackSystem, reportSystem);
    }
    /**
     * run app main update loop
     * @return if app should exit
     */
    public boolean run() {
        return consoleUI.run();
    }
    public void cleanup() {
        consoleUI.cleanup();
        //cleanup systems here
    }
};