package control;

import entity.CampReportOptions;
import java.io.FileWriter;
import java.io.IOException;

/**
 * <p>
 * A singleton class to generate reports for staff and committee memhers 
 * </p>
 * 
 * @author Lim Jun Rong Ryan
 * @version 1.0
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
            fileWriter.write(reportContent);
            fileWriter.close();
            System.out.println("Report Generated: " + fileName);
        } catch(IOException exception){
            exception.printStackTrace();
            System.err.println("Error generating the report. Please try again");
        }
    }
}
