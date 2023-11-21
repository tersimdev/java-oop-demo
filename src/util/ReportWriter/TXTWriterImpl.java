package util.ReportWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import control.ReportSystem.ReportWriteException;
import entity.Camp;
import entity.CampCommitteeMember;
import entity.CampReportFilter;
import entity.CampReportOptions;
import entity.User;

/**
 * <p>
 * An implementation of the {@link ReportWriterInterface} that writes reports in
 * TXT format.
 * Uses strategy design pattern
 * </p>
 * 
 * @author Lim Jun Rong Ryan
 * @version 1.0
 * @since 19-11-2023
 */

public class TXTWriterImpl implements ReportWriterInterface {
    @Override
    public void writeCampReport(CampReportOptions reportOptions, User user, Camp camp)
            throws ReportWriteException, IOException {
        if (camp == null || camp.getCampInformation() == null) {
            throw new ReportWriteException("Camp information is invalid");
        }
        StringBuilder reportContent = new StringBuilder();

        reportContent.append("Camp Name: ").append(camp.getCampInformation().getCampName()).append("\n");
        reportContent.append("Dates: ").append(camp.getCampInformation().getDates()).append("\n");

        CampReportFilter filter = reportOptions.getFilter();
        boolean printAttendees = (filter == CampReportFilter.ATTENDEE || filter  == CampReportFilter.NONE);
        boolean printCommittee = (filter == CampReportFilter.CAMP_COMMITTEE || filter == CampReportFilter.NONE);
        if (printAttendees) {
            reportContent.append("\nCamp Attendees: \n");
            for (String attendee : camp.getAttendeeList()) {
                reportContent.append("- ").append(attendee).append("\n");
            }
        }
        if (printCommittee) {
            reportContent.append("\nCamp Committee: \n");            
            reportContent.append(camp.getCommitteeList());
            for (String comm : camp.getCommitteeList()) {
                reportContent.append("- ").append(comm).append("\n");
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

    @Override
public void writePerformanceReport(CampReportOptions reportOptions, User user, ArrayList<CampCommitteeMember> committeeMembers)
        throws ReportWriteException, IOException {
    if (user == null) {
        throw new ReportWriteException("User information is invalid");
    }

    StringBuilder reportContent = new StringBuilder();

    reportContent.append("Performance Report for all Camp Committee Members\n");

    for (CampCommitteeMember committeeMember : committeeMembers) {
        reportContent.append("User ID: ").append(committeeMember.getStudentId()).append("\n");
        reportContent.append("- Total Points Earned: ").append(committeeMember.getPoints()).append("\n");
        reportContent.append("\n");
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
