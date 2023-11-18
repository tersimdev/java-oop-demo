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
 * @version 1.1
 * @since 18-11-2023
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

    public static class ReportGenerator {

        public static String generateCampReport(User user, Camp camp) {
            if (camp == null || camp.getCampInfo() == null) {
                return "Invalid camp or camp information is missing.";
            }

            StringBuilder reportContent = new StringBuilder();
        
            reportContent.append("Camp Name: ").append(camp.getCampId()).append("\n");
            reportContent.append("Dates: ").append(camp.getCampInfo().getDates()).append("\n");
        
            reportContent.append("\nCamp Attendees:\n");
            for (Student attendee : camp.getAttendees()) {
                reportContent.append("- ").append(attendee.getDisplayName()).append("\n");
            }
        
            reportContent.append("\nCamp Committee Members:\n");
            for (Student committeeMember : camp.getCommitteeMembers()) {
                reportContent.append("- ").append(committeeMember.getDisplayName()).append("\n");
            }
        
            return reportContent.toString();
        }
    }

    public void generateReport(CampReportOptions reportOptions, User user, Camp camp) {
        String reportContent = ReportGenerator.generateCampReport(user, camp);

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
