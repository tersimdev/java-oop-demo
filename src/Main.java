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

        //create a runtimem hook to trigger cleanup on crash
        Runtime.getRuntime().addShutdownHook( new Thread() { 
            @Override 
            public void run() { 
                if (app.isRunning())
                    app.cleanup();
            }
        });
        
        boolean shouldExit = false;
        while(!shouldExit) {
           shouldExit = app.run();
        }
        app.cleanup();
    }
}
