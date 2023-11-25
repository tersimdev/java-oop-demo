package util;

import java.util.Scanner;

import util.helpers.DateStringHelper;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.HashMap;
import java.util.NoSuchElementException;

/**
 * <p>
 * This is a class to store and maintain a Scanner object.
 * Depends on Log class to print messages
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 1-11-2023
 */
public class Input {

    /**
     * The scanner object.
     * Should only have one for each inputStream open at once.
     */
    private Scanner scanner;

    /**
     * Message to display to user when invalid input is keyed in
     */
    private final static String INVALID_INPUT_MSG = " Invalid input, try again: ";

    /**
     * Calls createScanner
     * 
     * @param inputStream input stream to create scanner with
     */
    public Input(InputStream inputStream) {
        createScanner(inputStream);
    }

    /**
     * Creates the scanner object with provided input stream.
     * 
     * @param inputStream provided input stream
     */
    private void createScanner(InputStream inputStream) {
        scanner = new Scanner(inputStream);
        Log.info("Creating scanner");
    }

    /**
     * Returns the scanner object
     * 
     * @return scanner object
     */
    public Scanner getScanner() {
        return scanner;
    }

    /**
     * Closes the scanner object if not null.
     * Sets scanner to null upon close.
     */
    public void close() {
        if (scanner != null) {
            scanner.close();
            scanner = null;
        }
        Log.info("Closing scanner");
    }

    /**
     * Gets an int from the user.
     * Handles invalid non-int input
     * 
     * @param msg msg to cue user
     * @return gotten int
     */
    public int getInt(String msg) {
        Log.print(msg);
        Integer ret = null; // use wrapper class to set to null
        while (ret == null) {
            try {
                ret = scanner.nextInt();
                scanner.nextLine(); // consume \n
            } catch (NoSuchElementException e) {
                Log.debug("invalid input to getInt");
                Log.print(INVALID_INPUT_MSG);
                scanner.nextLine(); // consume failed input
                // e.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * Gets an double from the user.
     * Handles invalid non-double input
     * 
     * @param msg msg to cue user
     * @return gotten double
     */
    public double getDouble(String msg) {
        Log.print(msg);
        Double ret = null; // use wrapper class to set to null
        while (ret == null) {
            try {
                ret = scanner.nextDouble();
                scanner.nextLine(); // consume \n
            } catch (NoSuchElementException e) {
                Log.debug("invalid input to getDouble");
                Log.print(INVALID_INPUT_MSG);
                scanner.nextLine(); // consume failed input
                // e.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * Gets an string(line) from the user.
     * No checking done.
     * 
     * @param msg msg to cue user
     * @return gotten string
     */
    public String getLine(String msg) {
        Log.print(msg);
        return scanner.nextLine();
    }

    /**
     * Gets an date from the user.
     * Handles invalid non-date input in wrong format.
     * 
     * @param msg msg to cue user
     * @return gotten date object
     */
    public LocalDate getDate(String msg) {
        Log.print(msg);
        LocalDate ret = null;
        while (ret == null) {
            try {
                String dateStr = scanner.nextLine();
                ret = DateStringHelper.StrToDateConverter(dateStr);
            } catch (DateTimeParseException e) {
                Log.debug("invalid date format. Please enter the date in the format DD/MM/YYYY.");
                Log.print(INVALID_INPUT_MSG);
                // e.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * Gets an boolean from the user.
     * Has a hashmap of responses, handles invalid input if user keys
     * in something not in hashmap.
     * 
     * @param msg msg to cue user
     * @return gotten boolean
     */
    public boolean getBool(String msg) {
        Log.print(msg);
        // fancy way of doing to put more options + efficient lookup
        Map<String, Boolean> responseMap = new HashMap<>();
        responseMap.put("y", true);
        responseMap.put("yes", true);
        responseMap.put("ya", true);
        responseMap.put("yeboi", true);
        responseMap.put("n", false);
        responseMap.put("no", false);
        responseMap.put("nay", false);
        responseMap.put("nah", false);
        Boolean ret = null;
        while (ret == null) {
            try {
                String yesno = scanner.nextLine().trim().toLowerCase();
                ret = responseMap.getOrDefault(yesno, null);
            } catch (NoSuchElementException e) {
                Log.debug("invalid input to getBool");
                Log.print(INVALID_INPUT_MSG);
                scanner.nextLine(); // consume failed input
                // e.printStackTrace();
            }
        }
        return ret;
    }
};
