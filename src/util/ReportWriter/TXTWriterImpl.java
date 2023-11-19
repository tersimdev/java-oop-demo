package util.ReportWriter;

import java.io.FileWriter;
import java.io.IOException;

/**
 * <p>
 *  An implementation of the {@link ReportWriterInterface} that writes reports in TXT format.
 * Uses strategy design pattern
 * </p>
 * 
 * @author Lim Jun Rong Ryan
 * @version 1.0
 * @since 19-11-2023
 */

public class TXTWriterImpl implements ReportWriterInterface {
    @Override
    public void writeReport(String fileName, String content) {
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException exception) {
            exception.printStackTrace();
            System.err.println("Error writing the report. Please try again");
        }
    }
}
