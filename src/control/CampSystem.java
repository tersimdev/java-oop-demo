package control;

import entity.User;
import entity.UserGroup;
import util.Log;
import entity.Student;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private ArrayList<Camp> camps;

    public CampSystem() {
        camps = new ArrayList<Camp>();
    }

    // Staff functions
    public void createCamp(int campId, CampInformation campInfo, ArrayList<String> studentList) {
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
        Camp camp = getCampByName(campName);
        for (int i = 0; i < camp.getStudentList().size(); i++) {
            Log.println(camp.getStudentList().get(i));
        }
    }

    public void viewCampCommitteeList(String campName) {
        Log.println("===List of all the committee members attending this camp===");
        
        Camp camp = getCampByName(campName);
        for (int i = 0; i < camp.getStudentList().size(); i++) {
            // query students, if student is a committee member then print
            String studentId = camp.getStudentList().get(i);
            Student student = (Student) DataStoreSystem.getInstance().queryUser(studentId);
            if (student.getCampCommitteeMember() != null) {
                Log.println(studentId);
            }
        }
    }

    public Camp getCampByName(String campName) {
        for (Camp camp : camps) {
            if (camp.getCampInformation().getCampName().equalsIgnoreCase(campName)) {
                return camp;
            }
        }
        return null;
    }
}
