package util.ReportWriter;

import entity.Camp;
import entity.CampCommitteeMember;
import entity.CampReportFilter;
import entity.CampReportOptions;
import entity.User;
import util.helpers.FileWriterHelper;
import control.ReportSystem.ReportWriteException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * <p>
 * An abstract base class to handle report generation needed by the app
 * Uses Polymorphism
 * </p>
 * 
 * @author Lim Jun Rong Ryan
 * @version 1.0
 * @since 19-11-2023
 */
public abstract class BaseReportWriter {

    public abstract void writeCampReport(CampReportOptions reportOptions, CampReportFilter filter, User user, Camp camp)
            throws ReportWriteException, IOException;

    public abstract void writePerformanceReport(CampReportOptions reportOptions, User user,
            ArrayList<CampCommitteeMember> commmitteeMembers) throws ReportWriteException, IOException;

    protected String getStudentListAsString(Camp camp, CampReportFilter filter) {
        StringBuilder reportContent = new StringBuilder();

        boolean printAttendees = (filter == CampReportFilter.ATTENDEE || filter == CampReportFilter.NONE);
        boolean printCommittee = (filter == CampReportFilter.CAMP_COMMITTEE || filter == CampReportFilter.NONE);

        if (printAttendees) {
            reportContent.append("\nCamp Attendees: \n");
            for (String attendee : camp.getAttendeeList()) {
                reportContent.append("- ").append(attendee).append("\n");
            }
        }
        if (printCommittee) {
            reportContent.append("\nCamp Committee: \n");
            for (String comm : camp.getCommitteeList()) {
                reportContent.append("- ").append(comm).append("\n");
            }
        }

        return reportContent.toString();
    }

    protected void writeReportToFile(CampReportOptions reportOptions, String reportContentStr) {
        String fileName = reportOptions.getFilePath() + reportOptions.getFileName() + reportOptions.getFileType();
        FileWriterHelper.writeToFile(fileName, reportContentStr);
    }
}