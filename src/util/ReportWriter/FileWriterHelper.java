package util.ReportWriter;

import java.io.FileWriter;
import java.io.IOException;

import control.ReportSystem.ReportWriteException;
import entity.Camp;
import entity.CampReportFilter;
import entity.CampReportOptions;

/**
 * <p>
 * An helper class for facilitating the writing of camp reports to files.
 * Adheres to Single Responsibility Principle, through encapsulation
 * </p>
 * 
 * @author Lim Jun Rong Ryan
 * @version 1.0
 * @since 23-11-2023
 */

public abstract class FileWriterHelper {

    protected void writeToFile(CampReportOptions reportOptions, StringBuilder reportContent)
            throws ReportWriteException, IOException {
        String fileName = reportOptions.getFilePath() + reportOptions.getFileName() + reportOptions.getFileType();
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(reportContent.toString());
        } catch (IOException exception) {
            throw exception;
        }
    }

    protected String getAttendeesAsString(Camp camp, String serparator) {
        StringBuilder ret = new StringBuilder(); 
        for (String attendee : camp.getAttendeeList()) {
            ret.append(attendee).append(serparator);
        }
        return ret.toString();
    }

    protected String getCommitteeListAsString(Camp camp, String separator) {
        StringBuilder committeeList = new StringBuilder();
        for (String committeeMember : camp.getCommitteeList()) {
            committeeList.append(committeeMember).append(separator);
        }
        return committeeList.toString();
    }

    protected String applyFilter(Camp camp, CampReportFilter filter) {
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
    
}