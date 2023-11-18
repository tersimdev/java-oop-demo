package control;

import entity.User;
import entity.UserGroup;
import util.Log;
import entity.Student;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import entity.Camp;
import entity.CampInformation;
import entity.Faculty;

/**
 * <p>
 * A singleton class that stores all camps, and controls access to them 
 * </p>
 * 
 * @author 
 * @version 2.0
 * @since 18-11-2023
 */
public class CampSystem {
    private static CampSystem instance = null;
    private ArrayList<Camp> camps;

    private CampSystem() {
        camps = new ArrayList<Camp>();
    }

    public static CampSystem getInstance() {
        if (instance == null)
            instance = new CampSystem();
        return instance;
    }

    // Staff functions
    public void createCamp(ArrayList<Student> studentList, String campName, String description, String location, int totalSlots, int committeeSlots,
     ArrayList<LocalDateTime> dates, LocalDateTime registrationClosingDate, String staffInChargeId, UserGroup userGroup, Faculty organisingFaculty) {

        int campId = Camp.getTotalNumberOfCamps();

        CampInformation campInfo = new CampInformation(campName, description, location, totalSlots, committeeSlots, 
         dates, registrationClosingDate, staffInChargeId, userGroup, organisingFaculty);

        camps.add(new Camp(campId, campInfo, studentList));
    }

    public void editCamp(String campName) {
        
    }

    public void deleteCamp(String campName) {

    }

    public void viewAllCamps() {
        Log.println("===List of all the camps===");
        for (Camp camp : camps) {
            String campName = camp.getCampInformation().getCampName();
            Log.println(campName);
        }
    }

    public void viewCampStudentList(String campName) {
        Log.println("===List of all the students attending this camp===");
        int index = campNameToIndex(campName);
        ArrayList<Student> students = camps.get(index).getStudentList();
        for (Student student : students) {
            Log.println(student.getDisplayName());
        }
    }

    public void viewCampCommitteeList(String campName) {
        Log.println("===List of all the committee members attending this camp===");
        int index = campNameToIndex(campName);
        ArrayList<Student> students = camps.get(index).getStudentList();
        for (Student student : students) {
            if (student.getCampCommitteeMember() != null) {
                Log.println(student.getDisplayName());
            }
        }
            
    }

    private int campNameToIndex(String campName) {
        for (Camp camp : camps) {
            if (camp.getCampInformation().getCampName() == campName) return camp.getCampId()-1;
            break;
        }
        Log.debug("camp name not found");
        return -1;
    }

}
