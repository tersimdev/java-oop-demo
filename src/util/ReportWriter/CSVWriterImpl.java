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
 * <p>
 * An implementation of the {@link BaseReportWriter} that writes reports in
 * CSV format.
 * </p>
 * 
 * @author Lim Jun Rong Ryan
 * @version 1.0
 * @since 19-11-2023
 */

public class CSVWriterImpl extends BaseReportWriter {

    /** Default Constructor */
    public CSVWriterImpl() {
        super();
    }

    @Override
    public void writeCampReport(CampReportOptions reportOptions, CampReportFilter filter, User user, Camp camp)
            throws ReportWriteException, IOException {
        if (camp == null || camp.getCampInformation() == null) {
            throw new ReportWriteException("Camp information is invalid");
        }

        StringBuilder reportContent = new StringBuilder();

        reportContent.append("Camp Name,").append(camp.getCampInformation().getCampName()).append(",");
        reportContent.append("Dates,");
        for (LocalDate d : camp.getCampInformation().getDates()) {
            reportContent.append(DateStringHelper.DateToPrintableStrConverter(d)).append(",");
        }
        reportContent.append("\n");

        reportContent.append(getStudentListAsString(camp, filter, ",", ""));
        writeReportToFile(reportOptions, reportContent.toString());
    }

    @Override
    public void writePerformanceReport(CampReportOptions reportOptions, User user,
            ArrayList<CampCommitteeMember> committeeMembers) throws ReportWriteException, IOException {
        if (user == null) {
            throw new ReportWriteException("User information is invalid");
        }

        StringBuilder reportContent = new StringBuilder();

        reportContent.append("Performance Report for all Camp Committee Members\n");
        reportContent.append("User ID,");
        reportContent.append("Total Points Earned");
        reportContent.append("\n");

        for (CampCommitteeMember committeeMember : committeeMembers) {
            reportContent.append(committeeMember.getStudentId()).append(",");
            reportContent.append(committeeMember.getPoints()).append(",");
            reportContent.append("\n");
        }
        writeReportToFile(reportOptions, reportContent.toString());
    }
}
