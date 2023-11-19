package util.ReportWriter;

import java.io.IOException;

/**
 * <p>
 * An interface to handle report generation needed by the app
 * Uses strategy design pattern
 * </p>
 * 
 * @author Lim Jun Rong Ryan
 * @version 1.0
 * @since 19-11-2023
 */
public interface ReportWriterInterface {
    void writeReport(String fileName, String content) throws IOException;
}