package entity;

import control.LoginSystem;
import util.Log;

/**
 * <p>
 * This is an entity class to represent a camp committee member
 * </p>
 * 
 * @author Jon Daniel Acu Kang
 * @version 1.0
 * @since 1-11-2023
 */
public class CampCommitteeMember {
    private int campId;
    private int points;
    private Student student;

    //inject dependency
    public CampCommitteeMember(Student student) {
        this.student = student;
    } 

    public String getStudentId() { return student.getUserID(); }

    public void viewCampDetails (int campId) {

    }

    public void makeSuggestion () {

    }

    public void replyEnquiries () {

    }

    public void viewSuggestions () {

    }

    public void editSuggestion () {

    }
    
    public void deleteSuggestion () {

    }

    // public void generateCampReport (ReportOptions reportOptions, ReportFilter reportFilter) {
        
    // }
}
