package entity;

/**
 * <p>
 * Class to group together camp opitions for report generation
 * </p>
 * 
 * @author Lim Jun Rong Ryan
 * @version 1.0
 * @since 6-11-2023
 */
public class CampReportOptions {
    private int campId;
    private String fileType = ".txt";
    private String fileName = "output";

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
    
}
