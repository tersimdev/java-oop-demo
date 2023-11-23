package control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import entity.Camp;
import entity.CampCommitteeMember;
import entity.CampReportFilter;
import entity.CampReportOptions;
import entity.User;
import util.Log;
import util.ReportWriter.CSVWriterImpl;
import util.ReportWriter.BaseReportWriter;
import util.ReportWriter.TXTWriterImpl;

/**
 * A singleton class to generate reports for staff and committee members.
 * Uses the open-closed principle by allowing different implementations of
 * ReportWriter.
 * 
 * @author Lim Jun Rong Ryan
 * @version 1.2
 * @since 19-11-2023
 */
public class ReportSystem {
    // map of file extensions to writing implementation
    private Map<String, BaseReportWriter> reportWriters;

    private DataStoreSystem dataStoreSystem;

    // an exception class to catch and handle
    public static class ReportWriteException extends Exception {
        public ReportWriteException() {
            super();
            Log.error("Error! ReportWriteException.");
            Log.error("ReportWriter encountered an unspecified issue.");
        }

        public ReportWriteException(String message) {
            super();
            Log.error("ReportWriter encountered an issue:");
            Log.error(message);
        }
    }

    public ReportSystem(DataStoreSystem dataStoreSystem) {
        this.dataStoreSystem = dataStoreSystem;
        this.reportWriters = new HashMap<>();
        reportWriters.put(".txt", new TXTWriterImpl());
        reportWriters.put(".csv", new CSVWriterImpl());
    }

    public void generateCampReport(CampReportOptions reportOptions, CampReportFilter filter, User user, Camp camp) {
        String filetype = reportOptions.getFileType();
        BaseReportWriter writer = reportWriters.get(filetype);
        if (writer == null) {
            Log.error("writer implementation not found, file type is likely invalid");
            return;
        }

        try {
            writer.writeCampReport(reportOptions, filter, user, camp);
        } catch (ReportWriteException exception) {
            Log.println("Error! Failed to Generate Report.");
            // error msg for devs
            Log.error("ReportWriteException while writing report using " + filetype);
            Log.error(exception.getMessage());
        } catch (IOException exception) {
            Log.println("Error! Failed to write report to file.");
            Log.error("IOException while writing report using " + filetype); // error msg for devs
        }
    }

    public void writePerformanceReport(CampReportOptions reportOptions, User user, Camp camp) {
        String fileType = reportOptions.getFileType();
        BaseReportWriter writer = reportWriters.get(fileType);
        if (writer == null) {
            Log.error("writer implementation not found, file is likely invalid");
            return;
        }
        try {
            ArrayList<CampCommitteeMember> commmitteeMembers = dataStoreSystem.getUserDataStoreSubSystem().queryCommitteeMembers(camp.getCommitteeList());
            writer.writePerformanceReport(reportOptions, user, commmitteeMembers);
        } catch (ReportWriteException exception) {
            Log.println("Error! Failed to Generate Performance Report.");
            Log.error("ReportWriteException while writing performance report using " + fileType);
            Log.error(exception.getMessage());
        } catch (IOException exception) {
            Log.println("Error! Failed to write performance report to file.");
            Log.error("IOException while writing performance report using " + fileType);
            Log.error(exception.getMessage());
        }
    }
}
