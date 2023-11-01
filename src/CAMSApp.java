import boundary.ConsoleUI;

/**
 * <p>
 * This is a singleton class to handle user console input
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 1-11-2023
 */
public class CAMSApp {
    public static void main(String[] args) {
        ConsoleUI consoleUI = new ConsoleUI();
        consoleUI.init();
        boolean shouldExit = false;
        while(!shouldExit) {
           shouldExit = consoleUI.run();
        }
        consoleUI.cleanup();
    }
};