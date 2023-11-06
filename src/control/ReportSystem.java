package control;

import entity.CampReportFilter;
import entity.CampReportOptions;
import java.io.FileWriter;
import java.io.IOException;

/**
 * <p>
 * A singleton class to generate reports for staff and committee memhers 
 * </p>
 * 
 * @author Lim Jun Rong Ryan
 * @version 1.1
 * @since 7-11-2023
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

    public void generateReport(CampReportOptions reportOptions, String reportContent){
        String fileName = reportOptions.getFileName() + reportOptions.getFileType();
        try{
            FileWriter fileWriter = new FileWriter(fileName);
            String filteredReportContent = applyFilter(reportContent, reportOptions.getFilter());
            fileWriter.write(filteredReportContent);
            fileWriter.close();
            System.out.println("Report Generated: " + fileName);
        } catch(IOException exception){
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
