package control;

import entity.Camp;
import entity.CampReportFilter;
import entity.CampReportOptions;
import entity.Student;
import entity.User;

import java.io.FileWriter;
import java.io.IOException;

/**
 * A singleton class to generate reports for staff and committee members.
 * 
 * @author Lim Jun Rong Ryan
 * @version 1.2
 * @since 19-11-2023
 */
public class ReportSystem {
    private static ReportSystem instance = null;

    private ReportSystem() {
    }

    public static ReportSystem getInstance() {
        if (instance == null)
            instance = new ReportSystem();
        return instance;
    }

    public void generateReport(CampReportOptions reportOptions, User user, Camp camp) {
        String reportContent = generateCampReportContent(user, camp);

        String fileName = reportOptions.getFilePath() + reportOptions.getFileName() + reportOptions.getFileType();
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            String filteredReportContent = applyFilter(reportContent, reportOptions.getFilter());
            fileWriter.write(filteredReportContent);
            fileWriter.close();
            System.out.println("Report Generated: " + fileName);
        } catch (IOException exception) {
            exception.printStackTrace();
            System.err.println("Error generating the report. Please try again");
        }
    }

    private String generateCampReportContent(User user, Camp camp) {
        if (camp == null || camp.getCampInfo() == null) {
            return "Invalid camp or camp information is missing.";
        }

        StringBuilder reportContent = new StringBuilder();

        reportContent.append("Camp Name: ").append(camp.getCampInfo().getCampName()).append("\n");
        reportContent.append("Dates: ").append(camp.getCampInfo().getDates()).append("\n");

        reportContent.append("\nCamp Attendees:\n");
        for (Student attendee : camp.getAttendees()) {
            reportContent.append("- ").append(attendee.getDisplayName()).append("\n");
        }
        return reportContent.toString();
    }

    private String applyFilter(String reportContent, CampReportFilter filter){
        StringBuilder filteredContent = new StringBuilder();
        String[] lines = reportContent.split("\n");

        for(String line : lines){
            switch(filter){
                case ATTENDEE:
                    if(line.contains("ATTENDEE")){
                        filteredContent.append(line).append("\n");
                    }
                    break;
                case CAMP_COMMITTEE:
                    if(line.contains("CAMP_COMMITTEE")){
                        filteredContent.append(line).append("\n");
                    }
                    break;
                default:
                    filteredContent.append(line).append("\n");
            }
        }
        return filteredContent.toString();
    }
}
