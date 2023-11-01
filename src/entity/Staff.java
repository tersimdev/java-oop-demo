package entity;

/**
 * <p>
 * This is an entity class to represent a staff
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 1-11-2023
 */
public class Staff extends User {
    public Staff() {super();}
    public Staff(String displayName, String userID, Faculty faculty) {
        super(displayName, userID, faculty);
    }

    //empty functions for now, return type not confirmed
    public void createCamp() {}
    public void editCamp() {}
    public void deleteCamp() {}
    public void toggleCampVisibility() {}
    public void getCreatedCamps() {}
    public void viewCampEnquiries() {}
    public void replyCampEnquiry() {}
    public void viewSuggestions() {}
    public void approveSuggestions() {}
    public void generateCampReport() {}
    public void generatePerformanceReport() {}
}
