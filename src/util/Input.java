package util;

import java.util.Scanner;

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

    public static Input instance = null;

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
    * @return scanner
    */
    public Scanner getScanner() {
        return scanner;
    }

    public void close() {
        if (scanner != null)
        {
            scanner.close();
            scanner = null;
            //set instance to null, so that next getInstance creates the scanner
            instance = null; 
        }
        Log.info("Closing scanner");
    }

    //TODO below 
    public int getInt() {
        return scanner.nextInt(); 
    }

    public double getDouble() {
        return scanner.nextDouble();
    }

    public String getLine() {
        scanner.next(); //clear \n
        String ret = scanner.nextLine();
        return ret;
    }
};