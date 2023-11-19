package control;

import entity.Camp;
import entity.CampReportFilter;
import entity.CampReportOptions;
import entity.User;
import util.Log;
import util.ReportWriter.CSVWriterImpl;
import util.ReportWriter.ReportWriterInterface;
import util.ReportWriter.TXTWriterImpl;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

/**
 * A singleton class to generate reports for staff and committee members.
 * Uses the open-closed principle by allowing different implementations of ReportWriter.
 * 
 * @author Lim Jun Rong Ryan
 * @version 1.2
 * @since 19-11-2023
 */
public class ReportSystem {
    //map of file extensions to writing implementation
    private Map<String, ReportWriterInterface> reportWriters;

    public ReportSystem() {
        this.reportWriters = new HashMap<>();
        reportWriters.put("txt", new TXTWriterImpl());
        reportWriters.put("csv", new CSVWriterImpl());
    }

    public void generateReport(CampReportOptions reportOptions, User user, Camp camp) {
        String filetype = reportOptions.getFileType();
        ReportWriterInterface writer = reportWriters.get(filetype);
        if (writer == null) {
            Log.error("writer implementation not found, file type is likely invalid");
            return;
        }

        try {
            writer.writeReport(reportOptions, user, camp);
        } catch (IOException exception) {
            Log.println("Error! Failed to Generate Report.");
            Log.error("IOException while writing report using " + filetype); //error msg for devs
        }
    }
}
