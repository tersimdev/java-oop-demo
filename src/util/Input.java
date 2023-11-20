package util;

import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;

/**
 * <p>
 * This is a singleton class to handle user console input
 * Singleton allows it to maintain a single scanner instance for the entire app lifetime
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 1-11-2023
 */
public class Input {

    private Scanner scanner;
    private final static String INVALID_INPUT_MSG = " Invalid input, try again: ";

    public Input() {
        createScanner();
    }
    
    public void createScanner() {
        scanner = new Scanner(System.in);
        Log.info("Creating scanner");
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
                scanner.nextLine(); //consume failed input
                //e.printStackTrace();
            }
        }
        return ret;
    }

    public boolean getBool(String msg) {
        Log.print(msg);
        Boolean ret = null;
        while (ret == null) {
            try {
                String yesno = scanner.nextLine().toLowerCase();
                if (yesno == "y" || yesno == "yes") {
                    ret = true;
                } else if (yesno == "n" || yesno == "no") {
                    ret = false;
                }
            } catch (NoSuchElementException e) {
                Log.debug("invalid input to getBool");
                Log.print(INVALID_INPUT_MSG);
                scanner.nextLine(); //consume failed input
                //e.printStackTrace();
            }
        }
        return ret;
    }
};
