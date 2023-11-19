package control;

import entity.Camp;
import entity.CampReportFilter;
import entity.CampReportOptions;
import entity.User;
import util.ReportWriter.ReportWriterInterface;

import java.io.IOException;
import java.util.List;

/**
 * A singleton class to generate reports for staff and committee members.
 * Uses the open-closed principle by allowing different implementations of ReportWriter.
 * 
 * @author Lim Jun Rong Ryan
 * @version 1.2
 * @since 19-11-2023
 */
public class ReportSystem {
    private List<ReportWriterInterface> reportWriters;

    public ReportSystem(List<ReportWriterInterface> reportWriters) {
        this.reportWriters = reportWriters;
    }

    public void generateReport(CampReportOptions reportOptions, User user, Camp camp) {
        StringBuilder reportContent = generateCampReportContent(user, camp, reportOptions.getFilter());

        String fileName = reportOptions.getFilePath() + reportOptions.getFileName() + reportOptions.getFileType();
        try {
            for (ReportWriterInterface reportWriter : reportWriters) {
                reportWriter.writeReport(fileName, reportContent.toString());
            }
            System.out.println("Report Generated: " + fileName);
        } catch (IOException exception) {
            exception.printStackTrace();
            System.err.println("Error generating the report. Please try again");
        }
    }

    private StringBuilder generateCampReportContent(User user, Camp camp, CampReportFilter filter) {
        if (camp == null || camp.getCampInformation() == null) {
            return new StringBuilder("Invalid camp or camp information is missing.");
        }

        StringBuilder reportContent = new StringBuilder();

        reportContent.append("Camp Name: ").append(camp.getCampInformation().getCampName()).append("\n");
        reportContent.append("Dates: ").append(camp.getCampInformation().getDates()).append("\n");

        reportContent.append("\nCamp Attendees:\n");
        for (String attendee : camp.getAttendees()) {
            if (filter == null || 
                    (filter == CampReportFilter.ATTENDEE && attendee.contains("ATTENDEE")) ||
                    (filter == CampReportFilter.CAMP_COMMITTEE && attendee.contains("CAMP_COMMITTEE"))) {
                reportContent.append("- ").append(attendee).append("\n");
            }
        }
        return reportContent;
    }
}
