package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateStringHelper {

    private static DateTimeFormatter dateFormatter;
    private static DateTimeFormatter StringFormatter;
    
    public DateStringHelper() {
        dateFormatter = DateTimeFormatter.ofPattern("dd/MM/uuuu");
        StringFormatter = DateTimeFormatter.ofPattern("dd LLLL uuuu");
    }

    public static LocalDate StrToDateConverter(String input) {
        LocalDate ret = LocalDate.parse(input, dateFormatter);
        return ret;
    }

    public static String DateToStrConverter(LocalDate input) {
        String ret = StringFormatter.format(input);
        return ret;
    }

}
