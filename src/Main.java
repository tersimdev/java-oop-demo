import util.Log;

/**
 * <p>
 * Main class with main function
 * Creates the CAMSApp object
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 19-11-2023
 */
public class Main {
    public static void main(String[] args) {
        Log.enableLogging(true); // enable this for dev work

        //create the app and run it
        CAMSApp app = new CAMSApp();
        app.init();
        boolean shouldExit = false;
        while(!shouldExit) {
           shouldExit = app.run();
        }
        app.cleanup();
    }
}
