package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateStringHelper {

    private static DateStringHelper instance = null;
    private DateTimeFormatter dateFormatter;
    private DateTimeFormatter StringFormatter;
    
    private DateStringHelper() {
        dateFormatter = DateTimeFormatter.ofPattern("dd/MM/uuuu");
        StringFormatter = DateTimeFormatter.ofPattern("dd LLLL uuuu");
    }

    public static DateStringHelper getInstance() {
        if (instance == null)
            instance = new DateStringHelper();
        return instance;
    }

    public LocalDate StrToDateConverter(String input) {
        LocalDate ret = LocalDate.parse(input, dateFormatter);
        return ret;
    }

    public String DateToStrConverter(LocalDate input) {
        String ret = StringFormatter.format(input);
        return ret;
    }

}
