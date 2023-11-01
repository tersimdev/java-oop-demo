package util;

/**
 * <p>
 * This class has static methods to output to console
 * It's the sole place where System.out is used.
 * use println to print output to console for user feedback
 * use info, debug and error to output dev feedback
 * loggingEnabled controls whether to show dev feedback
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 1-11-2023
 */
public class Log {
    private static boolean loggingEnabled = true;

    public static void enableLogging(boolean enabled) {
        loggingEnabled = enabled;
    }

    public static void println(String msg) {
        System.out.println(msg);
    }
    public static void print(String msg) {
        System.out.print(msg);
    }
    
    public static void info(String msg) {
        if (loggingEnabled) System.out.println("[INFO]: " + msg);
    }
    public static void debug(String msg) {
        if (loggingEnabled) System.out.println("[DEBUG]: " + msg);
    }
    public static void error(String msg) {
        if (loggingEnabled) System.out.println("[ERROR]: " + msg);
    }
}
