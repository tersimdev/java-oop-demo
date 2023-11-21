package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BLACK = "\u001B[30m";

    public static void enableLogging(boolean enabled) {
        loggingEnabled = enabled;
    }

    public static void println(String msg) {
        System.out.println(msg);
    }

    public static void print(String msg) {
        System.out.print(msg);
    }

    /**
     * Prints a colored line. No \n character.
     * 
     * @param msg   message to print
     * @param color use the provided ANSI_XXX constants. e.g. ANSI_WHITE
     */
    public static void printColor(String msg, String color) {
        System.out.print(color + msg + ANSI_RESET);
    }

    /**
     * function to print out ascii art line by line from .txt file
     * 
     * @param filepath filepath too txt file containing logo
     */
    public static void printLogo(String filepath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            for (String line; (line = reader.readLine()) != null;) {
                Log.println(line);
            }
        } catch (IOException e) {
            Log.error("cant find file");
            // e.printStackTrace();
            Log.println(""); // print empty
        }
    }

    public static void info(String msg) {
        if (loggingEnabled)
            System.out.println(ANSI_GREEN + "[INFO]: " + msg + ANSI_RESET);
    }

    public static void debug(String msg) {
        if (loggingEnabled)
            System.out.println(ANSI_YELLOW + "[DEBUG]: " + msg + ANSI_RESET);
    }

    public static void error(String msg) {
        if (loggingEnabled)
            System.out.println(ANSI_RED + "[ERROR]: " + msg + ANSI_RESET);
    }
}
