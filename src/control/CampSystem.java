package control;

import entity.User;
import entity.UserGroup;
import util.Input;
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
    private ArrayList<Integer> deletedIdList;

    public CampSystem() {
        camps = new ArrayList<Camp>();
        deletedIdList = new ArrayList<Integer>();
    }

    // Staff functions
    public void createCamp(int campId, CampInformation campInfo, ArrayList<String> studentList) {
        camps.add(0, new Camp(campId, campInfo, studentList));
    }

    public void deleteCamp(String campName) {
        Camp camp = getCampByName(campName);
        deletedIdList.add(0, camp.getCampId());
        camps.remove(camp);
        return;
    }

    public void editCamp (String campName, int updateChoice) {
        Camp camp = getCampByName(campName);
        Input input = Input.getInstance();
        
        switch (updateChoice) {
            case 1:
                String newCampName = input.getLine("Please enter the new camp name: ");
                camp.getCampInformation().setCampName(newCampName);
                break;
            case 2:
                String description = input.getLine("Please enter the new description: ");
                camp.getCampInformation().setDescription(description);
                break;

            case 3:
                String location = input.getLine("Please enter the new location: ");
                camp.getCampInformation().setLocation(location);
                break;

            case 4:
                int totalSlots = input.getInt("Please enter the new total number of slots: ");
                camp.getCampInformation().setTotalSlots(totalSlots);
                break;

            case 5:
                int committeeSlots = input.getInt("Please enter the new number of committee slots: ");
                camp.getCampInformation().setCommitteeSlots(committeeSlots);
                break;

            case 6:
                int duration = input.getInt("Please enter the number of days the camp will be held: ");
                LocalDateTime firstDate = input.getDate("Please enter the date of the first day of the camp: ");
                ArrayList<LocalDateTime> dates = new ArrayList<LocalDateTime>();
                for (int i = 0; i < duration; i++) {
                    dates.add(i, firstDate);
                    firstDate = firstDate.plusDays(1);
                }
                camp.getCampInformation().setDates(dates);
                break;

            case 7:
                LocalDateTime registrationClosingDate = input.getDate("Please enter the new closing date for registration: ");
                camp.getCampInformation().setRegistrationClosingDate(registrationClosingDate);
                break;

            case 8:
                boolean visibility = camp.toggleVisibility();
                if (visibility == true) Log.println("The camp is now visible.");
                else Log.println("The camp is now not visibile.");
                break;

            default:
                break;
        }
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


    // utility functions
    public Camp getCampByName(String campName) {
        for (Camp camp : camps) {
            if (camp.getCampInformation().getCampName().equalsIgnoreCase(campName)) {
                return camp;
            }
        }
        return null;
    }

    public int generateNewCampId() {
        if (deletedIdList.isEmpty()) {
            int lastIndex = camps.size()-1;
            return camps.get(lastIndex).getCampId()+1;
        }
        else {
            return deletedIdList.remove(0);
        }
    }
}
