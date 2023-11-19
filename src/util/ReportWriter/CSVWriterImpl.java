package util.ReportWriter;

import java.io.FileWriter;
import java.io.IOException;

import entity.Camp;
import entity.CampReportFilter;
import entity.CampReportOptions;
import entity.User;

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
    public void writeReport(CampReportOptions reportOptions, User user, Camp camp) throws IOException {
        if (camp == null || camp.getCampInformation() == null) {
            return;
        }
        StringBuilder reportContent = new StringBuilder();

        reportContent.append("Camp Name,").append(camp.getCampInformation().getCampName()).append("\n");
        reportContent.append("Dates,").append(camp.getCampInformation().getDates()).append("\n");

        reportContent.append("\nCamp Attendees,\n");
        for (String attendee : camp.getAttendees()) {
            //this filter doesnt work
            CampReportFilter filter = reportOptions.getFilter();
            if (filter == CampReportFilter.NONE ||
                    (filter == CampReportFilter.ATTENDEE && attendee.contains("ATTENDEE")) ||
                    (filter == CampReportFilter.CAMP_COMMITTEE && attendee.contains("CAMP_COMMITTEE"))) {
                reportContent.append(attendee).append("\n");
            }
        }
        String fileName = reportOptions.getFilePath() + reportOptions.getFileName() + reportOptions.getFileType();
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write(reportContent.toString());
            fileWriter.close();
        } catch (IOException exception) {
            throw exception;
        }
    }
}
