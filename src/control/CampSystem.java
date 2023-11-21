package control;

import java.time.LocalDate;
import java.util.ArrayList;

import entity.Camp;
import entity.CampCommitteeMember;
import entity.CampInformation;
import entity.Student;
import entity.UserGroup;
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

    private int nextCampId;

    public CampSystem(DataStoreSystem dataStoreSystem) {
        this.dataStoreSystem = dataStoreSystem;
        // load in camps from datastore
        camps = dataStoreSystem.getAllCamps();
        nextCampId = 0;
        if (camps.size() > 0)
            nextCampId = camps.get(camps.size() - 1).getCampId() + 1;
    }

    // Staff functions
    public void createCamp(CampInformation campInfo) {
        Camp newCamp = new Camp(nextCampId++, campInfo);
        camps.add(newCamp);
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
                UserGroup userGroup = camp.getCampInformation().getUserGroup();
                boolean yesno;
                if (userGroup.isWholeNTU()) {
                    yesno = input.getBool("Would you like to only open the camp to " + userGroup.getFaculty() + "?");
                    if (yesno == true)
                        userGroup.setFaculty(camp.getCampInformation().getOrganisingFaculty());
                } else {
                    yesno = input.getBool("Would you like to open the camp to the whole of NTU?");
                    if (yesno == true)
                        userGroup.setWholeNTU();
                }
                break;

            case 9:
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
            if (camp != null)
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
            UserGroup userGroup = camp.getCampInformation().getUserGroup();
            boolean campAvailable = camp.checkVisibility()
                    && (!checkCampFull(camp) || !checkCampCommitteeFull(camp)) // not full
                    && !checkRegistrationClosed(camp) // can register
                    && !checkDateClash(camp, student) // date clashes with registered camps
                    && (userGroup.isWholeNTU() || student.getFaculty().equals(userGroup.getFaculty())); // matches
                                                                                                        // faculty

            if (campAvailable) {
                printCamp(camp);
            }
        }
    }

    public void registerAsAttendee(Student student, int campId) {
        String studentId = student.getUserID();
        Camp camp = getCampById(campId);
        UserGroup userGroup = camp.getCampInformation().getUserGroup();

        if (camp.getAttendeeList().contains(studentId)) {
            Log.println(studentId + " is already registered for camp " + campId);
            Log.error(studentId + " was not registered for camp " + campId);
        } else if (camp.getCommitteeList().contains(studentId)) {
            Log.println(studentId + " is already registered for " + campId + " as a committee member");
            Log.error(studentId + " was not registered for camp " + campId);
        } else if (checkRegistrationClosed(camp)) {
            Log.println("Registration for this camp has closed");
            Log.error(studentId + " was not registered for camp " + campId);
        } else if (checkDateClash(camp, student)) {
            Log.println("This camp clashes with another camp " + studentId + " is already registered for");
            Log.error(studentId + " was not registered for camp " + campId); 
        } else if (checkCampFull(camp)) {
            Log.println("This camp is full");
            Log.error(studentId + " was not registered for camp " + campId);
        } else if (student.getFaculty() != userGroup.getFaculty()) {
            Log.println("Camp " + campId + " is only open to " + userGroup.getFaculty());
            Log.error(studentId + " was not registered for camp " + campId);
        } else if (checkStudentWithdrawn(camp, student)) {
            Log.println(studentId + " previously withdrew from camp " + campId);
            Log.error(studentId + " was not registered for camp " + campId);
        }
        else {
            camp.addAttendee(student);
            Log.println(studentId + " has been registered for camp " + campId);
        }
    }

    public void registerAsCommittee(Student student, int campId) {
        String studentId = student.getUserID();
        Camp camp = getCampById(campId);
        UserGroup userGroup = camp.getCampInformation().getUserGroup();
        if (student.getCampCommitteeMember().getIsMember()) {
            Log.println(campId + " is already a committee member for a camp.");
            Log.error(studentId + " was not registered for camp " + campId);
        } else if (camp.getAttendeeList().contains(studentId)) {
            Log.println(studentId + " is already registered for " + campId + " as an attendee");
            Log.error(studentId + " was not registered for camp " + campId);
        } else if (checkRegistrationClosed(camp)) {
            Log.println("Registration for this camp has closed");
            Log.error(studentId + " was not registered for camp " + campId);
        } else if (checkDateClash(camp, student)) {
            Log.println("This camp clashes with another camp " + studentId + " is already registered for");
            Log.error(studentId + " was not registered for camp " + campId);
        } else if (checkCampCommitteeFull(camp)) {
            Log.println("There are no more camp committee slots for this camp");
            Log.error(studentId + " was not registered for camp " + campId);
        } else if (student.getFaculty() != userGroup.getFaculty()) {
            Log.println("Camp " + campId + " is only open to " + userGroup.getFaculty());
            Log.error(studentId + " was not registered for camp " + campId);
        }
        else {
            CampCommitteeMember committeeMember = student.getCampCommitteeMember();
            committeeMember.setCampId(campId);
            committeeMember.setIsMember(true);
            camp.addCampCommitteeMember(committeeMember);
            Log.println(studentId + " has been registered for camp " + campId + " as a camp committee member");
        }
    }

    public void viewRegisteredCamps(Student student) {
        String studentId = student.getUserID();
        Log.println("===List of all the camps you are registered for===");
        for (Camp camp : camps) {
            if (camp != null) {
                if (camp.getAttendeeList().contains(studentId)) {
                    Log.println("=======================");
                    Log.println("Your role for camp " + camp.getCampId() + ": Attendee");
                    Log.println("-----------------------");
                    printCamp(camp);
                }
                if (camp.getCommitteeList().contains(studentId)) {
                    Log.println("=======================");
                    Log.println("Your role for camp " + camp.getCampId() + ": Committee Member");
                    Log.println("-----------------------");
                    printCamp(camp);
                }
            }
        }
    }

    public void withdrawFromCamp(Student student, int campId) {
        Camp camp = getCampById(campId);
        String studentId = student.getUserID();

        if (camp.getAttendeeList().contains(studentId)) { // is an attendee for this camp
            camp.removeAttendee(student);
        } else {
            Log.println(studentId + " is already not registered for camp " + campId);
            Log.error(studentId + " was not withdrawn for camp " + campId);
        }
    }

    // utility functions
    public Camp getCampById(int campId) {
        for (Camp c : camps) {
            if (c.getCampId() == campId)
                return c;
        }
        return null;
    }

    public ArrayList<Camp> getCampsByStaff(String staffId) {
        ArrayList<Camp> camps = new ArrayList<Camp>();
        for (Camp camp : camps) {
            if (camp.getCampInformation().getStaffInChargeId() == staffId) {
                camps.add(camp);
            }
        }
        return camps;
    }

    public ArrayList<Camp> getCampsByCommittee(String committeeMemberId) {
        ArrayList<Camp> camps = new ArrayList<Camp>();
        for (Camp camp : camps) {
            if (camp.getCommitteeList().contains(committeeMemberId)) {
                camps.add(camp);
            }
        }
        return camps;
    }

    public boolean checkValidCampId(int campId) {
        return getCampById(campId) != null;
    }

    private boolean checkStudentWithdrawn (Camp camp, Student student) {
        if (camp.getWithdrawnList().contains(student.getUserID())) {
            return true;
        }
        return false;
    }

    public boolean checkRegistrationClosed(Camp camp) {
        LocalDate today = LocalDate.now();
        LocalDate deadline = camp.getCampInformation().getRegistrationClosingDate();
        return (today.compareTo(deadline) >= 0); // true if registration is closed
    }

    public boolean checkCampFull(Camp camp) {
        return (camp.getAttendeeList()
                .size() >= (camp.getCampInformation().getTotalSlots() - camp.getCampInformation().getCommitteeSlots()));
    }

    public boolean checkCampCommitteeFull(Camp camp) {
        return (camp.getCommitteeList().size() >= camp.getCampInformation().getCommitteeSlots());
    }

    public boolean checkDateClash(Camp camp, Student student) {
        // returns true if camp clashes with other camps student
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
            campLastDate = camp.getCampInformation().getDates().get(camp.getCampInformation().getDates().size() - 1);
        } else {
            campLastDate = campFirstDate;
        }
        for (Camp campPointer : camps) {
            if (campPointer != null) {
                if (campPointer.getAttendeeList().contains(studentId)
                        || campPointer.getCommitteeList().contains(studentId)) { // student is registered for this camp

                    ptrCampFirstDate = campPointer.getCampInformation().getDates().get(0);
                    if (campPointer.getCampInformation().getDates().size() > 1) {
                        ptrCampLastDate = campPointer.getCampInformation().getDates()
                                .get(campPointer.getCampInformation().getDates().size() - 1);
                    } else {
                        ptrCampLastDate = ptrCampFirstDate;
                    }

                    // check for date clash
                    if (campFirstDate.compareTo(ptrCampLastDate) <= 0
                            && campLastDate.compareTo(ptrCampFirstDate) >= 0) {
                        return true;
                    }
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
        if (camp.getCampInformation().getUserGroup().isWholeNTU())
            Log.println("This camp is open to all students from NTU");
        else
            Log.println("This camp is open only to students from " + camp.getCampInformation().getUserGroup());
        Log.println(
                "Attendee slots left: " + (camp.getCampInformation().getTotalSlots()
                        - camp.getCampInformation().getCommitteeSlots() - camp.getAttendeeList().size()));
        Log.println("Committee slots left: "
                + (camp.getCampInformation().getCommitteeSlots() - camp.getCommitteeList().size()));
        Log.println("=======================");
    }
}
