package util.ReportWriter;

import java.io.FileWriter;
import java.io.IOException;

import control.ReportSystem.ReportWriteException;
import entity.Camp;
import entity.CampReportFilter;
import entity.CampReportOptions;
import entity.User;

/**
 * <p>
 * An implementation of the {@link ReportWriterInterface} that writes reports in
 * CSV format.
 * Uses strategy design pattern
 * </p>
 * 
 * @author Lim Jun Rong Ryan
 * @version 1.0
 * @since 19-11-2023
 */

public class CSVWriterImpl implements ReportWriterInterface {
    @Override
    public void writeCampReport(CampReportOptions reportOptions, User user, Camp camp)
            throws ReportWriteException, IOException {
        if (camp == null || camp.getCampInformation() == null) {
            throw new ReportWriteException("Camp information is invalid");
        }
        StringBuilder reportContent = new StringBuilder();

        reportContent.append("Camp Name,").append(camp.getCampInformation().getCampName()).append("\n");
        reportContent.append("Dates,").append(camp.getCampInformation().getDates()).append("\n");

        CampReportFilter filter = reportOptions.getFilter();
        boolean printAttendees = (filter == CampReportFilter.ATTENDEE || filter  == CampReportFilter.NONE);
        boolean printCommittee = (filter == CampReportFilter.CAMP_COMMITTEE || filter == CampReportFilter.NONE);
        if (printAttendees) {
            reportContent.append("\nCamp Attendees,\n");
            for (String attendee : camp.getAttendees()) {
                reportContent.append(attendee).append(",");
            }
        }
        if (printCommittee) {
            reportContent.append("\nCamp Committee,\n");
            //TODO
            reportContent.append("TODO");
            // for (String comm : camp.getCommittee()) {
            //     reportContent.append(comm).append(",");
            // }
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
    public void writeEnquiryReport() throws ReportWriteException, IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'writeEnquiryReport'");
    }
}
