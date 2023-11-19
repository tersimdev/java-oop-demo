package util.ReportWriter;


import entity.Camp;
import entity.CampReportOptions;
import entity.User;
import control.ReportSystem.ReportWriteException;
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
    void writeCampReport(CampReportOptions reportOptions, User user, Camp camp) throws ReportWriteException, IOException;
    void writeEnquiryReport(/*TODO */) throws ReportWriteException, IOException;
}