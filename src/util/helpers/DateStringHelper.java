package util.helpers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Helper class to provide static functions to convert
 * <code>LocalDate</code> to and from <code>Strings</code>.
 * 
 * @author Jon Daniel Acu Kang
 * @version 1.0
 * @since 20-11-2023
 */
public class DateStringHelper {
    /**
     * Default Constructor
     * Not used, this class has no state.
     */
    public DateStringHelper() {
    }

    /**
     * Format of the date to convert
     */
    private final static String localDateFormat = "dd/MM/uuuu";
    /**
     * Format of the date to print
     */
    private final static String printableDateFormat = "dd LLLL uuuu";

    /**
     * Parses string to date object
     * 
     * @param input string to parse
     * @return LocalDate object
     */
    public static LocalDate StrToDateConverter(String input) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(localDateFormat);
        LocalDate ret = LocalDate.parse(input, formatter);
        return ret;
    }

    /**
     * Formats a date object into a string
     * 
     * @param input LocalDate object to format
     * @return date as string
     */
    public static String DateToStrConverter(LocalDate input) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(localDateFormat);
        String ret = formatter.format(input);
        return ret;
    }

    /**
     * Formats a date object into a printable string
     * 
     * @param input LocalDate object to format
     * @return date as string in nice printable format
     */
    public static String DateToPrintableStrConverter(LocalDate input) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(printableDateFormat);
        String ret = formatter.format(input);
        return ret;
    }
}
