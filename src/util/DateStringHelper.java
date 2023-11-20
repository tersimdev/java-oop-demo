package util;

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

    private static DateTimeFormatter Formatter;
    private final static String localDateFormat = "dd/MM/uuuu";
    private final static String stringDateFormat = "dd LLLL uuuu";

    public DateStringHelper() {
    }

    public static LocalDate StrToDateConverter(String input) {
        Formatter = DateTimeFormatter.ofPattern(localDateFormat);
        LocalDate ret = LocalDate.parse(input, Formatter);
        return ret;
    }

    public static String DateToStrConverter(LocalDate input) {
        Formatter = DateTimeFormatter.ofPattern(stringDateFormat);
        String ret = Formatter.format(input);
        return ret;
    }

}
