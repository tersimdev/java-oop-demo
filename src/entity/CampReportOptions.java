package entity;

/**
 * <p>
 * Class to group together camp options for report generation
 * </p>
 * 
 * @author Lim Jun Rong Ryan
 * @version 1.1
 * @since 19-11-2023
 */

/**
 * A class to store the options for a camp report. Includes the ID of the camp
 * the report is about and the report file's type, name and path.
 */
public class CampReportOptions {
    /**
     * ID of the camp the report is about.
     */
    private int campId;
    /**
     * The report's file extension (eg .txt or .csv).
     */
    private String fileType;
    /**
     * The report's file name.
     */
    private String fileName;
    /**
     * The report's file path. Set to current directory by default.
     */
    private String filePath;

    /**
     * Default constructor for camp report options.
     */
    public CampReportOptions() {
        campId = 0;
        fileType = ".txt"; // extension like txt, csv with the dot
        fileName = "output";
        filePath = "./"; // Set to current directory
    }

    /**
     * Getter for campId
     * 
     * @return The ID of the camp the report is about.
     */
    public int getCampId() {
        return campId;
    }

    /**
     * Setter for campId.
     * 
     * @param campId The campId to be set.
     */
    public void setCampId(int campId) {
        this.campId = campId;
    }

    /**
     * Getter for the report's file type.
     * 
     * @return The report's file type.
     */
    public String getFileType() {
        return fileType;
    }

    /**
     * Setter for the report's file type.
     * 
     * @param fileType The file type to be set.
     */
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    /**
     * Getter for the report's file name.
     * 
     * @return The report's file name.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Setter for the report's file name.
     * 
     * @param fileName The file name to be set.
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Getter for the report's file path.
     * 
     * @return The file path to be set.
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Setter for the report's file path.
     * 
     * @param filePath The report's file path.
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

}
