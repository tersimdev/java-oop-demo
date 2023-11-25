package util.ReportWriter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import control.ReportSystem.ReportWriteException;
import entity.Camp;
import entity.CampCommitteeMember;
import entity.CampReportFilter;
import entity.CampReportOptions;
import entity.User;
import util.helpers.DateStringHelper;

/**
 * An implementation of the {@link BaseReportWriter} that writes reports in
 * TXT format.
 * 
 * @author Lim Jun Rong Ryan
 * @version 1.0
 * @since 19-11-2023
 */
public class TXTWriterImpl extends BaseReportWriter {

    /**
     * Default Constructor
     */
    public TXTWriterImpl() {
        super();
    }

    /**
     * Function which generates and writes to file a camp report in a
     * txt format.
     * 
     * @param reportOptions options like filename, filepath, etc
     * @param filter        filter to filter results in report
     * @param user          user that is requesting for this report
     * @param camp          camp this report is based on
     * @throws ReportWriteException when report generation fails
     * @throws IOException          when file write fails
     */
    @Override
    public void writeCampReport(CampReportOptions reportOptions, CampReportFilter filter, User user, Camp camp)
            throws ReportWriteException, IOException {
        if (camp == null || camp.getCampInformation() == null) {
            throw new ReportWriteException("Camp information is invalid");
        }

        StringBuilder reportContent = new StringBuilder();

        reportContent.append("Camp Name: ").append(camp.getCampInformation().getCampName()).append("\n");
        reportContent.append("Dates: \n");
        for (LocalDate d : camp.getCampInformation().getDates()) {
            reportContent.append(DateStringHelper.DateToPrintableStrConverter(d)).append("\n");
        }

        reportContent.append(getStudentListAsString(camp, filter, "\n", "- "));
        writeReportToFile(reportOptions, reportContent.toString());
    }

    /**
     * Function which generates and writes to file a performance report in a
     * txt format.
     * 
     * @param reportOptions     options like filename, filepath, etc
     * @param user              user that is requesting for this report
     * @param committeeMembers the committee members to report on
     * @throws ReportWriteException when report generation fails
     * @throws IOException          when file write fails
     */
    @Override
    public void writePerformanceReport(CampReportOptions reportOptions, User user,
            ArrayList<CampCommitteeMember> committeeMembers) throws ReportWriteException, IOException {
        if (user == null) {
            throw new ReportWriteException("User information is invalid");
        }

        StringBuilder reportContent = new StringBuilder();

        reportContent.append("Performance Report for all Camp Committee Members\n");

        for (CampCommitteeMember committeeMember : committeeMembers) {
            reportContent.append("User ID: ").append(committeeMember.getStudentId()).append("\n");
            reportContent.append("Total Points Earned: ").append(committeeMember.getPoints()).append("\n");
            reportContent.append("\n\n");
        }
        writeReportToFile(reportOptions, reportContent.toString());
    }
}
