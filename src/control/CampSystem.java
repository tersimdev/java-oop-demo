package control;

import entity.User;
import entity.UserGroup;
import util.DateStringHelper;
import util.Input;
import util.Log;
import entity.Student;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import entity.Camp;
import entity.CampCommitteeMember;
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
    public void createCamp(int campId, CampInformation campInfo) {
        camps.add(0, new Camp(campId, campInfo));
    }

    public void deleteCamp(int campId) {
        Camp camp = getCampById(campId);
        deletedIdList.add(0, campId);
        camps.remove(camp);
        return;
    }

    public void editCamp (int campId, int updateChoice) {
        Camp camp = getCampById(campId);
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
                LocalDate firstDate = input.getDate("Please enter the date of the first day of the camp (DD/MM/YYYY): ");
                ArrayList<LocalDate> dates = new ArrayList<LocalDate>();
                for (int i = 0; i < duration; i++) {
                    dates.add(i, firstDate);
                    firstDate = firstDate.plusDays(1);
                }
                camp.getCampInformation().setDates(dates);
                break;

            case 7:
                LocalDate registrationClosingDate = input.getDate("Please enter the new closing date for registration: ");
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
        Log.println("===List of all camps===");
        for (Camp camp : camps) {
            String campName = camp.getCampInformation().getCampName();
            int campId = camp.getCampId();
            String location = camp.getCampInformation().getLocation();
            Log.println("ID, Camp Name, Camp Location");
            Log.println(campId + ", " + campName + ", " + location);
            Log.println("=======================");
            Log.println(camp.getCampInformation().getDescription());
            Log.println("=======================");
            Log.println("Start date: " + camp.getCampInformation().getDates().get(0));
            Log.println("End date: " + camp.getCampInformation().getDates().get(camp.getCampInformation().getDates().size()-1));
            Log.println("Registration closing date: " + camp.getCampInformation().getRegistrationClosingDate());
        }
    }

    public void viewCampStudentList(int campId) {
        Log.println("===List of all the students attending this camp===");
        Camp camp = getCampById(campId);
        for (Student student : camp.getAttendees()) {
            Log.println(student.getDisplayName());
        }
    }

    public void viewCampCommitteeList(int campId) {
        Log.println("===List of all the committee members attending this camp===");
        Camp camp = getCampById(campId);
        for (CampCommitteeMember campCommitteeMember : camp.getCampCommitteeMembers()) {
            Log.println(campCommitteeMember.getStudentId());
        }
        
        
    }

    // Student functions
    public void viewAvailableCamps(Student student) {
        Log.println("===List of all available camps===");
        Log.println("ID, Camp Name");
        for (Camp camp : camps) {
            if (!camp.checkCampFull() && !camp.checkRegistrationClosed()) {
                String campName = camp.getCampInformation().getCampName();
                int campId = camp.getCampId();
                String location = camp.getCampInformation().getLocation();
                Log.println("ID, Camp Name, Camp Location");
                Log.println(campId + ", " + campName + ", " + location);
                Log.println("=======================");
                Log.println(camp.getCampInformation().getDescription());
                Log.println("=======================");
                Log.println("Start date: " + camp.getCampInformation().getDates().get(0));
                Log.println("End date: " + camp.getCampInformation().getDates().get(camp.getCampInformation().getDates().size()-1));
                Log.println("Registration closing date: " + camp.getCampInformation().getRegistrationClosingDate());
            }
        }
    }

    public void registerAsAttendee(Student student, int campId) {
        Camp camp = getCampById(campId);
        camp.registerStudent(student);
        if (student.getCampCommitteeMember() == null) { // student is not a committee member
            Log.println(student.getUserID() + " has been registered for camp " + campId);
        }
        else {
            Log.println(student.getUserID() + " has been registered for camp " + campId + " as a camp committee member");

        }
    }

    public void registerAsCommittee(CampCommitteeMember campCommitteeMember, int campId) {
        Camp camp = getCampById(campId);
        camp.registerCampCommitteeMember(campCommitteeMember);
    }

    public void viewRegisteredCamps(Student student) {
        Log.println("===List of all the camps you are registered for===");
        for (Camp camp : camps) {
            for (Student studentPointer : camp.getAttendees()) {
                if (student == studentPointer) {
                    String campName = camp.getCampInformation().getCampName();
                    int campId = camp.getCampId();
                    String location = camp.getCampInformation().getLocation();
                    Log.println("ID, Camp Name, Camp Location");
                    Log.println(campId + ", " + campName + ", " + location);
                    Log.println("=======================");
                    Log.println(camp.getCampInformation().getDescription());
                    Log.println("=======================");
                    Log.println("Start date: " + camp.getCampInformation().getDates().get(0));
                    Log.println("End date: " + camp.getCampInformation().getDates().get(camp.getCampInformation().getDates().size()-1));
                    Log.println("Registration closing date: " + camp.getCampInformation().getRegistrationClosingDate());
                }
            }
        }
    }

    public void withdrawFromCamp(Student student, int campId) {
        Camp camp = getCampById(campId);
        camp.withdrawStudent(student);
        Log.println(student.getUserID() + " has been withdrawn from camp " + campId);
    } 

    // utility functions
    public Camp getCampByName(String campName) {
        for (Camp camp : camps) {
            if (camp.getCampInformation().getCampName().equalsIgnoreCase(campName)) {
                return camp;
            }
        }
        Log.error("Camp not found");
        return null;
    }

    public Camp getCampById(int campId) {
        for (Camp camp : camps) {
            if (camp.getCampId() == campId) return camp;
        }
        Log.error("Camp not found");
        return null;
    }

    public int generateNewCampId() {
        if (deletedIdList.isEmpty()) {
            return camps.size();
        }
        else {
            return deletedIdList.remove(0);
        }
    }
}
