package util.ReportWriter;

import java.io.FileWriter;
import java.io.IOException;

/**
 * <p>
 *  An implementation of the {@link ReportWriterInterface} that writes reports in CSV format.
 * Uses strategy design pattern
 * </p>
 * 
 * @author Lim Jun Rong Ryan
 * @version 1.0
 * @since 19-11-2023
 */

public class CSVWriterImpl implements ReportWriterInterface {
    @Override
    public void writeReport(String fileName, String content) {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            String[] lines = content.split("\n");

            for (String line : lines) {
                String[] values = line.split(",");
    
                for (int i = 0; i < values.length; i++) {
                    fileWriter.append(values[i]);
                    if (i < values.length - 1) {
                        fileWriter.append(',');
                    }
                }
                fileWriter.append('\n');
            }

            System.out.println("CSV report generated: " + fileName);
        } catch (IOException exception) {
            exception.printStackTrace();
            System.err.println("Error writing the report. Please try again");
        }
    }
}
