package control;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import entity.Camp;
import entity.CampCommitteeMember;
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

    public CampSystem(DataStoreSystem dataStoreSystem) {
        this.dataStoreSystem = dataStoreSystem;
        // load in camps from datastore
        ArrayList<Camp> dataStoreCamps = dataStoreSystem.getAllCamps();
        // dataStore camps would not have deleted entries, so index might be wrong
        // sort to get the biggest id
        int size = dataStoreCamps.get(dataStoreCamps.size() - 1).getCampId() + 1;
        camps = new ArrayList<>(Collections.nCopies(size, null)); //init camp arrays of size to null
        //create deleted list
        for (Camp dsc : dataStoreCamps) {
            int id = dsc.getCampId();
            camps.set(id, dsc); 
        }
        for (Camp c : camps) {
            if (c == null) {
                //add to deleted list
            }
        }
    }

    // Staff functions
    public void createCamp(int campId, CampInformation campInfo) {
        Camp newCamp = new Camp(campId, campInfo);
        camps.add(campId, newCamp);
        dataStoreSystem.addCamp(newCamp);
    }

    public void deleteCamp(int campId) {
        camps.set(campId, null);
        dataStoreSystem.deleteCamp(campId);
        return;
    }

    public void editCamp(int campId, int updateChoice, Input input) {
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
                LocalDate firstDate = input
                        .getDate("Please enter the date of the first day of the camp (DD/MM/YYYY): ");
                ArrayList<LocalDate> dates = new ArrayList<LocalDate>();
                for (int i = 0; i < duration; i++) {
                    dates.add(i, firstDate);
                    firstDate = firstDate.plusDays(1);
                }
                camp.getCampInformation().setDates(dates);
                break;

            case 7:
                LocalDate registrationClosingDate = input
                        .getDate("Please enter the new closing date for registration: ");
                camp.getCampInformation().setRegistrationClosingDate(registrationClosingDate);
                break;

            case 8:
                boolean visibility = camp.toggleVisibility();
                if (visibility == true)
                    Log.println("The camp is now visible.");
                else
                    Log.println("The camp is now not visibile.");
                break;

            default:
                break;
        }
        // TODO figure out what to do for datastore update
        // dataStoreSystem.updateCampDetails(camp);
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
        for (String student : camp.getAttendeeList()) {
            Log.println(student);
        }
    }

    public void viewCampCommitteeList(int campId) {
        Log.println("===List of all the committee members attending this camp===");
        Camp camp = getCampById(campId);
        for (String campCommitteeMember : camp.getCommitteeList()) {
            Log.println(campCommitteeMember);
        }
    }

    // Student functions
    public void viewAvailableCamps(Student student) {
        Log.println("===List of all available camps===");
        for (Camp camp : camps) {
            if (!checkCampFull(camp) && !checkRegistrationClosed(camp) && !checkDateClash(camp, student)) {
                printCamp(camp);
            }
        }
    }

    public void registerAsAttendee(Student student, int campId) {
        String studentId = student.getUserID();
        Camp camp = getCampById(campId);

        if (!checkCampFull(camp) && !checkRegistrationClosed(camp) && !camp.getAttendeeList().contains(studentId)
                && !checkDateClash(camp, student)) {
            camp.addAttendee(student);
            Log.println(studentId + " has been registered for camp " + campId);
        } else if (camp.getAttendeeList().contains(studentId)) {
            Log.println(studentId + " is already registered for camp " + campId);
            Log.error(studentId + " was not registered for camp " + campId);
        } else if (checkDateClash(camp, student)) {
            Log.println("This camp clashes with another camp " + studentId + " is already registered for");
            Log.error(studentId + " was not registered for camp " + campId);
        } else if (checkRegistrationClosed(camp)) {
            Log.println("Registration for this camp has closed");
            Log.error(studentId + " was not registered for camp " + campId);
        } else if (checkCampFull(camp)) {
            Log.println("This camp is full");
            Log.error(studentId + " was not registered for camp " + campId);
        }
    }

    public void registerAsCommittee(Student student, int campId) {
        String studentId = student.getUserID();
        Camp camp = getCampById(campId);
        if (!checkCampFull(camp) && !checkRegistrationClosed(camp) && !camp.getCommitteeList().contains(studentId)
                && !checkDateClash(camp, student)) {
            CampCommitteeMember committeeMember = student.getCampCommitteeMember();
            committeeMember.setCampId(campId);
            committeeMember.setIsMember(true);
            camp.addCampCommitteeMember(committeeMember);
            Log.println(studentId + " has been registered for camp " + campId + " as a camp committee member");
        } else if (camp.getCommitteeList().contains(studentId)) {
            Log.println(studentId + " is already registered for camp " + campId);
            Log.error(studentId + " was not registered for camp " + campId);
        } else if (checkDateClash(camp, student)) {
            Log.println("This camp clashes with another camp " + studentId + " is already registered for");
            Log.error(studentId + " was not registered for camp " + campId);
        } else if (checkRegistrationClosed(camp)) {
            Log.println("Registration for this camp has closed");
            Log.error(studentId + " was not registered for camp " + campId);
        } else if (checkCampFull(camp)) {
            Log.println("This camp is full");
            Log.error(studentId + " was not registered for camp " + campId);
        }
    }

    public void viewRegisteredCamps(Student student) {
        String studentId = student.getUserID();
        Log.println("===List of all the camps you are registered for===");
        for (Camp camp : camps) {
            for (String studentPointer : camp.getAttendeeList()) {
                if (studentId == studentPointer) {
                    printCamp(camp);
                }
            }
            for (String studentPointer : camp.getCommitteeList()) {
                if (studentId == studentPointer) {
                    printCamp(camp);
                }
            }
        }
    }

    public void withdrawFromCamp(Student student, int campId) {
        Camp camp = getCampById(campId);
        String studentId = student.getUserID();
        CampCommitteeMember committeeMember = student.getCampCommitteeMember();
        if (committeeMember.getIsMember()) { // is a committee member
            if (!camp.getCommitteeList().contains(studentId)) {
                Log.println(studentId + " is already not registered for camp " + campId);
                Log.error(studentId + " was not registered for camp " + campId);
            } else {
                committeeMember.setCampId(-1);
                committeeMember.setIsMember(false);
                camp.removeCampCommitteeMember(committeeMember);
            }
        } else { // not a committee member
            if (!camp.getAttendeeList().contains(studentId)) {
                Log.println(studentId + " is already not registered for camp " + campId);
                Log.error(studentId + " was not registered for camp " + campId);
            } else {
                camp.removeAttendee(student);
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
        Log.println("Camp not found");
        return null;
    }

    public Camp getCampById(int campId) {
        if (checkValidCampId(campId))
            return camps.get(campId);
        else {
            Log.println("Camp not found");
            return null;
        }
    }

    public int generateNewCampId() {
        // if there is a null slot in camps, give that index
        for (int i = 0; i < camps.size(); i++) {
            if (camps.get(i) == null) {
                return i;
            }
        }
        // else give camps.size()
        return camps.size();
    }

    public boolean checkValidCampId(int campId) {
        Camp camp = getCampById(campId);
        if (camp != null) {
            return true;
        } else if (camps.isEmpty()) {
            Log.error("There are no camps in the system");
        }
        return false;
    }

    public boolean checkRegistrationClosed(Camp camp) {
        LocalDate today = LocalDate.now();
        LocalDate deadline = camp.getCampInformation().getRegistrationClosingDate();
        if (today.compareTo(deadline) >= 0)
            return true; // registration is closed
        return false;
    }

    public boolean checkCampFull(Camp camp) {
        if (camp.getAttendeeList().size() >= camp.getCampInformation().getTotalSlots())
            return true; // camp is full
        return false;
    }

    public boolean checkDateClash(Camp camp, Student student) { // returns true if camp clashes with other camps student
                                                                // is registered for
        String studentId = student.getUserID();
        // camp dates for new camp we are checking against
        LocalDate campFirstDate;
        LocalDate campLastDate;
        // camp dates for camps student is already registered for
        LocalDate ptrCampFirstDate;
        LocalDate ptrCampLastDate;

        campFirstDate = camp.getCampInformation().getDates().get(0);
        if (camp.getCampInformation().getDates().size() > 1) {
            campLastDate = camp.getCampInformation().getDates().get(camp.getCampInformation().getDates().size());
        } else {
            campLastDate = campFirstDate;
        }
        for (Camp campPointer : camps) {
            if (campPointer.getAttendeeList().contains(studentId)
                    || campPointer.getCommitteeList().contains(studentId)) { // student is registered for this camp

                ptrCampFirstDate = campPointer.getCampInformation().getDates().get(0);
                if (campPointer.getCampInformation().getDates().size() > 1) {
                    ptrCampLastDate = camp.getCampInformation().getDates()
                            .get(camp.getCampInformation().getDates().size());
                } else {
                    ptrCampLastDate = ptrCampFirstDate;
                }

                // check for date clash
                if (campFirstDate.compareTo(ptrCampLastDate) <= 0 && campLastDate.compareTo(ptrCampFirstDate) >= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public void printCamp(Camp camp) {
        Log.println("=======================");
        Log.println("Camp ID: " + camp.getCampId());
        Log.println("Camp Name: " + camp.getCampName());
        Log.println("Location: " + camp.getCampInformation().getLocation());
        Log.println("Start date: " + DateStringHelper.DateToStrConverter(camp.getCampInformation().getDates().get(0)));
        Log.println("End date: " + DateStringHelper.DateToStrConverter(
                camp.getCampInformation().getDates().get(camp.getCampInformation().getDates().size() - 1)));
        Log.println("Registration closing date: "
                + DateStringHelper.DateToStrConverter(camp.getCampInformation().getRegistrationClosingDate()));
        Log.println("----------------------");
        Log.println(camp.getCampInformation().getDescription());
        Log.println("----------------------");
        Log.println(
                "Attendee slots left: " + (camp.getCampInformation().getTotalSlots() - camp.getAttendeeList().size()));
        Log.println("Committee slots left: "
                + (camp.getCampInformation().getCommitteeSlots() - camp.getCommitteeList().size()));
        Log.println("=======================");
    }
}
