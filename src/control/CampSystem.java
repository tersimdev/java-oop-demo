package control;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import entity.Camp;
import entity.CampCommitteeMember;
import entity.CampInformation;
import entity.Staff;
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

    private enum EditChoice {
        NAME,
        DESCRIPTION,
        LOCATION,
        TOTAL_SLOTS,
        COMMITTEE_SLOTS,
        DATES,
        REGISTRATION_CLOSING_DATE,
        USERGROUP,
        VISIBILITY
    }

    private final static ArrayList<EditChoice> editChoiceEnumList = new ArrayList<>(
            Arrays.asList(EditChoice.NAME, EditChoice.DESCRIPTION, EditChoice.LOCATION,
                    EditChoice.TOTAL_SLOTS, EditChoice.COMMITTEE_SLOTS,
                    EditChoice.DATES, EditChoice.REGISTRATION_CLOSING_DATE, EditChoice.USERGROUP,
                    EditChoice.VISIBILITY));

    private enum PrintCampSortOrder {
        DATES,
        LOCATION,
        ATTENDEE_SLOTS_REMAINING,
        COMMITTEE_SLOTS_REMAINING,
        REGISTRATION_CLOSING_DATE
    }

    private final static ArrayList<PrintCampSortOrder> PrintCampSortOrderEnumList = new ArrayList<>(
            Arrays.asList(PrintCampSortOrder.DATES, PrintCampSortOrder.LOCATION,
                    PrintCampSortOrder.ATTENDEE_SLOTS_REMAINING,
                    PrintCampSortOrder.COMMITTEE_SLOTS_REMAINING, PrintCampSortOrder.REGISTRATION_CLOSING_DATE));

    private int nextCampId;

    public CampSystem(DataStoreSystem dataStoreSystem) {
        this.dataStoreSystem = dataStoreSystem;
        // load in camps from datastore
        camps = dataStoreSystem.getAllCamps();
        nextCampId = 0;
        if (camps.size() > 0)
            nextCampId = camps.get(camps.size() - 1).getCampId() + 1;
    }

    /**
     * Creates a camp.
     * 
     * @param campInfo ID of a camp.
     */
    public void createCamp(CampInformation campInfo) {
        Camp newCamp = new Camp(nextCampId++, campInfo);
        camps.add(newCamp);
        dataStoreSystem.addCamp(newCamp);
    }

    /**
     * Deletes a camp.
     * 
     * @param campId ID of a camp.
     */
    public void deleteCamp(int campId) {
        Camp camp = getCampById(campId);
        if (camp == null || !camp.getAttendeeList().isEmpty() || !camp.getCommitteeList().isEmpty()) {
            return;
        }

        camps.remove(campId);
        dataStoreSystem.deleteCamp(campId);
    }

    /**
     * Takes in int as user input for what they would like to edit about the camp,
     * then edits the camp.
     * 
     * @param campId       ID of a camp.
     * @param updateChoice Determines which aspect of the camp will be edited.
     * @param input        Input object.
     */
    public void editCamp(int campId, int updateChoice, Input input) {
        Camp camp = getCampById(campId);
        EditChoice editChoice = editChoiceEnumList.get(updateChoice - 1);
        switch (editChoice) {
            case NAME:
                String newCampName = input.getLine("Please enter the new camp name: ");
                camp.getCampInformation().setCampName(newCampName);
                break;
            case DESCRIPTION:
                String description = input.getLine("Please enter the new description: ");
                camp.getCampInformation().setDescription(description);
                break;

            case LOCATION:
                String location = input.getLine("Please enter the new location: ");
                camp.getCampInformation().setLocation(location);
                break;

            case TOTAL_SLOTS:
                int totalSlots = input.getInt("Please enter the new total number of slots: ");
                camp.getCampInformation().setTotalSlots(totalSlots);
                break;

            case COMMITTEE_SLOTS:
                int committeeSlots = input.getInt("Please enter the new number of committee slots: ");
                camp.getCampInformation().setCommitteeSlots(committeeSlots);
                break;

            case DATES:
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

            case REGISTRATION_CLOSING_DATE:
                LocalDate registrationClosingDate = input
                        .getDate("Please enter the new closing date for registration: ");
                camp.getCampInformation().setRegistrationClosingDate(registrationClosingDate);
                break;

            case USERGROUP:
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

            case VISIBILITY:
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

    public void viewAllCamps(int CampSortOrderChoice) {
        Log.println("===List of all camps===");
        PrintCampSortOrder printCampSortOrder = PrintCampSortOrderEnumList.get(CampSortOrderChoice - 1);
        ArrayList<Camp> sortedCamps = sortCamps(camps, printCampSortOrder);
        for (Camp camp : sortedCamps) {
            if (camp != null)
                printCamp(camp);
        }
    }

    public void viewCampsOfStaff(Staff staff, int CampSortOrderChoice) {
        Log.println("===List of all camps created by " + staff.getUserID() + "===");
        PrintCampSortOrder printCampSortOrder = PrintCampSortOrderEnumList.get(CampSortOrderChoice - 1);
        ArrayList<Camp> sortedCamps = sortCamps(camps, printCampSortOrder);
        for (Camp camp : sortedCamps) {
            if (camp != null
                    && camp.getCampInformation().getStaffInChargeId() == staff.getUserID()) // camp created by staff
                printCamp(camp);
        }
    }

    public void viewAttendeeList(int campId, Staff staff) {
        Log.println("===List of all the students attending this camp===");
        Camp camp = getCampById(campId);
        for (String student : camp.getAttendeeList()) {
            Log.println(student);
        }
    }
    // function overloading!
    public void viewAttendeeList(int campId, CampCommitteeMember campCommitteeMember) {
        Log.println("===List of all the students attending this camp===");
        Camp camp = getCampById(campId);
        if (!camp.getCommitteeList().contains(campCommitteeMember.getStudentId())) return;
        for (String student : camp.getAttendeeList()) {
            Log.println(student);
        }
    }

    public void viewCampCommitteeList(int campId, Staff staff) {
        Log.println("===List of all the committee members attending this camp===");
        Camp camp = getCampById(campId);
        for (String student : camp.getCommitteeList()) {
            Log.println(student);
        }
    }

    public void viewCampCommitteeList(int campId, CampCommitteeMember campCommitteeMember) {
        Log.println("===List of all the committee members attending this camp===");
        Camp camp = getCampById(campId);
        if (!camp.getCommitteeList().contains(campCommitteeMember.getStudentId())) return;
        for (String student : camp.getCommitteeList()) {
            Log.println(student);
        }
    }

    // Student functions
    public void viewAvailableCamps(Student student, int CampSortOrderChoice) {
        Log.println("===List of all available camps===");
        PrintCampSortOrder printCampSortOrder = PrintCampSortOrderEnumList.get(CampSortOrderChoice - 1);
        ArrayList<Camp> sortedCamps = sortCamps(camps, printCampSortOrder);

        for (Camp camp : sortedCamps) {
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
        } else {
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
        } else {
            CampCommitteeMember committeeMember = student.getCampCommitteeMember();
            committeeMember.setCampId(campId);
            committeeMember.setIsMember(true);
            camp.addCampCommitteeMember(committeeMember);
            Log.println(studentId + " has been registered for camp " + campId + " as a camp committee member");
        }
    }

    public void viewRegisteredCamps(Student student, int CampSortOrderChoice) {
        String studentId = student.getUserID();
        Log.println("===List of all the camps you are registered for===");
        PrintCampSortOrder printCampSortOrder = PrintCampSortOrderEnumList.get(CampSortOrderChoice - 1);
        ArrayList<Camp> sortedCamps = sortCamps(camps, printCampSortOrder);
        for (Camp camp : sortedCamps) {
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

    private boolean checkStudentWithdrawn(Camp camp, Student student) {
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

    private ArrayList<Camp> sortCamps(ArrayList<Camp> unsortedCamps, PrintCampSortOrder printCampSortOrder) {
        ArrayList<Camp> camps = (ArrayList<Camp>) unsortedCamps.clone();

        switch (printCampSortOrder) {
            case DATES:
                camps.sort((o1, o2) -> {
                    LocalDate firstDate1 = o1.getCampInformation().getDates().get(0);
                    LocalDate firstDate2 = o2.getCampInformation().getDates().get(0);
                    return firstDate1.compareTo(firstDate2);
                });
                break;

            case LOCATION:
                camps.sort((o1, o2) -> {
                    String location1 = o1.getCampInformation().getLocation();
                    String location2 = o2.getCampInformation().getLocation();
                    return location1.compareTo(location2);
                });
                break;

            case ATTENDEE_SLOTS_REMAINING:
                camps.sort((o1, o2) -> {
                    int slots1 = o1.getCampInformation().getTotalSlots() - o1.getCampInformation().getCommitteeSlots()
                            - o1.getAttendeeList().size();
                    int slots2 = o2.getCampInformation().getTotalSlots() - o1.getCampInformation().getCommitteeSlots()
                            - o1.getAttendeeList().size();
                    return Integer.compare(slots1, slots2);
                });
                break;

            case COMMITTEE_SLOTS_REMAINING:
                camps.sort((o1, o2) -> {
                    int slots1 = o1.getCampInformation().getCommitteeSlots() - o1.getCommitteeList().size();
                    int slots2 = o1.getCampInformation().getCommitteeSlots() - o1.getCommitteeList().size();
                    return Integer.compare(slots1, slots2);
                });
                break;

            case REGISTRATION_CLOSING_DATE:
                camps.sort((o1, o2) -> {
                    LocalDate firstDate1 = o1.getCampInformation().getDates().get(0);
                    LocalDate firstDate2 = o2.getCampInformation().getDates().get(0);
                    return firstDate1.compareTo(firstDate2);
                });
                break;
        }
        return camps;
    }

    public void printCamp(Camp camp) {
        Log.println("");
        Log.println("Camp ID: " + camp.getCampId() + "=====================================");
        Log.println("Camp Name: " + camp.getCampName());
        Log.println("Location: " + camp.getCampInformation().getLocation());
        Log.println("Start date: " + DateStringHelper.DateToStrConverter(camp.getCampInformation().getDates().get(0)));
        Log.println("End date: " + DateStringHelper.DateToStrConverter(
                camp.getCampInformation().getDates().get(camp.getCampInformation().getDates().size() - 1)));
        Log.println("Registration closing date: "
                + DateStringHelper.DateToStrConverter(camp.getCampInformation().getRegistrationClosingDate()));
        Log.println("----------------------");
        Log.println("Description " + camp.getCampInformation().getDescription());
        Log.println("----------------------");
        if (camp.getCampInformation().getUserGroup().isWholeNTU())
            Log.println("This camp is open to all students from NTU");
        else
            Log.println(
                    "This camp is open only to students from " + camp.getCampInformation().getUserGroup().toString());
        Log.println(
                "Attendee slots left: " + (camp.getCampInformation().getTotalSlots()
                        - camp.getCampInformation().getCommitteeSlots() - camp.getAttendeeList().size()));
        Log.println("Committee slots left: "
                + (camp.getCampInformation().getCommitteeSlots() - camp.getCommitteeList().size()));
        Log.println("Contact: " + camp.getCampInformation().getStaffInChargeId());
        Log.println("=====================================================");
    }
}
