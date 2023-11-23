package util.ReportWriter;

import java.io.IOException;
import java.util.ArrayList;

import control.ReportSystem.ReportWriteException;
import entity.Camp;
import entity.CampCommitteeMember;
import entity.CampReportFilter;
import entity.CampReportOptions;
import entity.User;

/**
 * An implementation of the {@link BaseReportWriter} that writes reports in
 * TXT format.
 * 
 * @author Lim Jun Rong Ryan
 * @version 1.0
 * @since 19-11-2023
 */
public class TXTWriterImpl extends BaseReportWriter {

    @Override
    public void writeCampReport(CampReportOptions reportOptions, CampReportFilter filter, User user, Camp camp)
            throws ReportWriteException, IOException {
        if (camp == null || camp.getCampInformation() == null) {
            throw new ReportWriteException("Camp information is invalid");
        }

        StringBuilder reportContent = new StringBuilder();

        reportContent.append("Camp Name: ").append(camp.getCampInformation().getCampName()).append("\n");
        reportContent.append("Dates: ").append(camp.getCampInformation().getDates()).append("\n");
        
        reportContent.append(getStudentListAsString(camp, filter));
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

        for (CampCommitteeMember committeeMember : committeeMembers) {
            reportContent.append("User ID: ").append(committeeMember.getStudentId()).append("\n");
            reportContent.append("Total Points Earned: ").append(committeeMember.getPoints()).append("\n");
            reportContent.append("\n");
        }
        writeReportToFile(reportOptions, reportContent.toString());
    }
}
