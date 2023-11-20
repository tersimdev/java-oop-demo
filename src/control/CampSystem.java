package control;

import java.time.LocalDate;
import java.util.ArrayList;

import entity.Camp;
import entity.CampInformation;
import entity.Student;
import util.DateStringHelper;
import util.Input;
import util.Log;

/**
 * <p>
 * A singleton class that stores all camps, and controls access to them 
 * </p>
 * 
 * @author Jon Kang
 * @version 2.0
 * @since 20-11-2023
 */
public class CampSystem {
    private DataStoreSystem dataStoreSystem;
    private ArrayList<Camp> camps;
    private ArrayList<Integer> deletedIdList;

    public CampSystem(DataStoreSystem dataStoreSystem) {
        camps = new ArrayList<Camp>();
        deletedIdList = new ArrayList<Integer>();
        this.dataStoreSystem = dataStoreSystem;
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

    public void editCamp (int campId, int updateChoice, Input input) {
        Camp camp = getCampById(campId);
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
            printCamp(camp);
        }
    }

    public void viewCampStudentList(int campId) {
        Log.println("===List of all the students attending this camp===");
        Camp camp = getCampById(campId);
        for (String student : camp.getStudentList()) {
            Log.println(student);
        }
    }

    public void viewCampCommitteeList(int campId) {
        Log.println("===List of all the committee members attending this camp===");
        Camp camp = getCampById(campId);
        for (String campCommitteeMember : camp.getComitteeList()) {
            Log.println(campCommitteeMember);
        }
    }

    // Student functions
    public void viewAvailableCamps(Student student) {
        String studentId = student.getUserID();
        // add dates clash checking
        Log.println("===List of all available camps===");
        for (Camp camp : camps) {
            if (!camp.checkCampFull() && !camp.checkRegistrationClosed()) {
                printCamp(camp);
            }
        }
    }

    public void registerAsAttendee(Student student, int campId) {
        String studentId = student.getUserID();
        Camp camp = getCampById(campId);
        if (camp.registerStudent(student)) {
            Log.println(studentId + " has been registered for camp " + campId);
        }
        else {
            Log.println(studentId + " was not registered for camp " + campId);
        }
    }

    public void registerAsCommittee(Student student, int campId) {
        String studentId = student.getUserID();
        Camp camp = getCampById(campId);
        if (camp.registerCampCommitteeMember(student)) {
            Log.println(studentId + " has been registered for camp " + campId + " as a camp committee member");
        }
        else {
            Log.println(studentId + " was not registered for camp " + campId);
        }
    }

    public void viewRegisteredCamps(Student student) {
        String studentId = student.getUserID();
        Log.println("===List of all the camps you are registered for===");
        for (Camp camp : camps) {
            for (String studentPointer : camp.getStudentList()) {
                if (studentId == studentPointer) {
                    printCamp(camp);
                }
            }
            for (String studentPointer : camp.getComitteeList()) {
                if (studentId == studentPointer) {
                    printCamp(camp);
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
        Log.println("Camp not found");
        return null;
    }

    public Camp getCampById(int campId) {
        for (Camp camp : camps) {
            if (camp.getCampId() == campId) return camp;
        }
        Log.error("Camp not found");
        Log.println("Camp not found");
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

    public boolean checkValidCampId(int campId) { 
        Camp camp = getCampById(campId);
        if (camps.contains(camp)) {
            return true;
        }
        else if (camps.isEmpty()) {
            Log.error("There are no camps in the system");
            return false;
        }
        else return false;
    }

    public void printCamp(Camp camp) {
        Log.println("=======================");
        Log.println("Camp ID: " + camp.getCampId());
        Log.println("Camp Name: " + camp.getCampName());
        Log.println("Location: " + camp.getCampInformation().getLocation());
        Log.println("Start date: " + DateStringHelper.DateToStrConverter(camp.getCampInformation().getDates().get(0)));
        Log.println("End date: " + DateStringHelper.DateToStrConverter(camp.getCampInformation().getDates().get(camp.getCampInformation().getDates().size()-1)));
        Log.println("Registration closing date: " + DateStringHelper.DateToStrConverter(camp.getCampInformation().getRegistrationClosingDate()));
        Log.println("----------------------");
        Log.println(camp.getCampInformation().getDescription());
        Log.println("----------------------");
        Log.println("Attendee slots left: " + (camp.getCampInformation().getTotalSlots() - camp.getStudentList().size()));
        Log.println("Committee slots left: " + (camp.getCampInformation().getCommitteeSlots() - camp.getComitteeList().size()));
        Log.println("=======================");
    }
}
