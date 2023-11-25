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
 * @since 23-11-2023
 */
public abstract class BaseReportWriter {

    /**
     * Base Constructor
     */
    public BaseReportWriter() {}

    /**
     * Function which generates and writes to file a camp report in a specific
     * format.
     * Format is determined by concrete class.
     * 
     * @param reportOptions options like filename, filepath, etc
     * @param filter        filter to filter results in report
     * @param user          user that is requesting for this report
     * @param camp          camp this report is based on
     * @throws ReportWriteException when report generation fails
     * @throws IOException          when file write fails
     */
    public abstract void writeCampReport(CampReportOptions reportOptions, CampReportFilter filter, User user, Camp camp)
            throws ReportWriteException, IOException;

    /**
     * Function which generates and writes to file a performance report in a
     * specific
     * format.
     * Format is determined by concrete class.
     * 
     * @param reportOptions     options like filename, filepath, etc
     * @param user              user that is requesting for this report
     * @param commmitteeMembers the committee members to report on
     * @throws ReportWriteException when report generation fails
     * @throws IOException          when file write fails
     */
    public abstract void writePerformanceReport(CampReportOptions reportOptions, User user,
            ArrayList<CampCommitteeMember> commmitteeMembers) throws ReportWriteException, IOException;

    /**
     * Returns a list of camp attendees or committee members as a single string based on filter
     * @param camp camp to get the list from
     * @param filter whether to return attendee, committee, or both
     * @return string containing requested list
     */
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

    /**
     * Writes a string to file based on file name etc in report options.
     * Uses FileWriterHelper to do the writing.
     * @param reportOptions options like filename, filetype
     * @param reportContentStr content to write
     */
    protected void writeReportToFile(CampReportOptions reportOptions, String reportContentStr) {
        String fileName = reportOptions.getFilePath() + reportOptions.getFileName() + reportOptions.getFileType();
        FileWriterHelper.writeToFile(fileName, reportContentStr);
    }
}