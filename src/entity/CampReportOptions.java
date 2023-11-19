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
public class CampReportOptions {
    private int campId;
    private String fileType; //extension like txt, csv without the dot
    private String fileName;
    private CampReportFilter filter;
    private String filePath; // Set to current directory

    public CampReportOptions() {
        campId = 0;
        fileType = "txt"; //extension like txt, csv without the dot
        fileName = "output";
        filter = CampReportFilter.NONE;
        filePath = "./"; // Set to current directory
    }

    public int getCampId(){
        return campId;
    }

    public void setCampId(int campId){
        this.campId = campId;
    }

    public String getFileType(){
        return fileType;
    }

    public void setFileType(String fileType){
        this.fileType = fileType;
    }

    public String getFileName(){
        return fileName;
    }

    public void setFileName(String fileName){
        this.fileName = fileName;
    }

    public CampReportFilter getFilter(){
        return filter;
    }

    public void setFilter(CampReportFilter filter){
        this.filter = filter;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

}
