package util;

import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;

/**
 * <p>
 * This is a singleton class to handle user console input
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 1-11-2023
 */
public class Input {

    private Scanner scanner;

    private static Input instance = null;

    private final static String INVALID_INPUT_MSG = " Invalid input, try again: ";

    private Input() {
        scanner = new Scanner(System.in);
        Log.info("Creating scanner");
    }

    public static Input getInstance() {
        if (instance == null)
            instance = new Input();
        return instance;
    }

    /*
     * returns the scanner object
     * 
     * @return scanner
     */
    public Scanner getScanner() {
        return scanner;
    }

    public void close() {
        if (scanner != null) {
            scanner.close();
            scanner = null;
            // set instance to null, so that next getInstance creates the scanner
            instance = null;
        }
        Log.info("Closing scanner");
    }

    public int getInt(String msg) {
        Log.print(msg);
        Integer ret = null; // use wrapper class to set to null
        while (ret == null) {
            try {
                ret = scanner.nextInt();
                scanner.nextLine(); //consume \n
            } catch (NoSuchElementException e) {
                Log.debug("invalid input to getInt");
                Log.print(INVALID_INPUT_MSG);
                scanner.nextLine(); //consume failed input
                //e.printStackTrace();
            }
        }
        return ret;
    }

    public double getDouble(String msg) {
        Log.print(msg);
        Double ret = null; // use wrapper class to set to null
        while (ret == null) {
            try {
                ret = scanner.nextDouble();
                scanner.nextLine(); //consume \n
            } catch (NoSuchElementException e) {
                Log.debug("invalid input to getDouble");
                Log.print(INVALID_INPUT_MSG);
                scanner.nextLine(); //consume failed input
                //e.printStackTrace();
            }
        }
        return ret;
    }

    public String getLine(String msg) {
        Log.print(msg);
        return scanner.nextLine();
    }

    public LocalDateTime getDate(String msg) {
        Log.print(msg);
        String dateStr = scanner.next();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("DD/MM/YYYY");  
        try {
            LocalDateTime date = LocalDateTime.parse(dateStr, formatter);
            return date;
        } catch (DateTimeParseException e) {
            Log.debug("Invalid date format. Please enter the date in the format DD/MM/YYYY.");
            return null; 
        }
    }
};
