package control;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import entity.Camp;
import entity.CampCommitteeMember;
import entity.Staff;
import entity.Student;
import util.DateStringHelper;
import util.Log;

public class CampViewerSubSystem {
    private CampSystem campSystem;
    private CampCheckHelperSubSystem campCheckHelperSubSystem;

    private enum PrintCampSortOrder {
        DATES,
        LOCATION,
        ATTENDEE_SLOTS_REMAINING,
        COMMITTEE_SLOTS_REMAINING,
        REGISTRATION_CLOSING_DATE
    }

    /**
     * An ArrayList that contains enumerators for different filters for viewing
     * camps.
     */
    private final static ArrayList<PrintCampSortOrder> PrintCampSortOrderEnumList = new ArrayList<>(
            Arrays.asList(PrintCampSortOrder.DATES, PrintCampSortOrder.LOCATION,
                    PrintCampSortOrder.ATTENDEE_SLOTS_REMAINING,
                    PrintCampSortOrder.COMMITTEE_SLOTS_REMAINING, PrintCampSortOrder.REGISTRATION_CLOSING_DATE));

    /**
     * Constructor for the camp viewer sub system.
     * 
     * @param campSystem      A class that stores all camps, and controls access to
     *                        them.
     * @param dataStoreSystem A class to handle all datastore operations.
     */
    public CampViewerSubSystem(CampSystem campSystem, DataStoreSystem dataStoreSystem,
            CampCheckHelperSubSystem campCheckHelperSubSystem) {
        this.campSystem = campSystem;
        this.campCheckHelperSubSystem = campCheckHelperSubSystem;
    }

    // STAFF FUNCTIONS

    /**
     * Prints a list of all the camps in the system.
     * Only available to staff.
     * 
     * @param CampSortOrderChoice The user's choice of filter.
     */
    public void viewAllCamps(int CampSortOrderChoice) {
        Log.println("===List of all camps===");
        PrintCampSortOrder printCampSortOrder = PrintCampSortOrderEnumList.get(CampSortOrderChoice - 1);
        ArrayList<Camp> sortedCamps = sortCamps(campSystem.getCamps(), printCampSortOrder);
        for (Camp camp : sortedCamps) {
            if (camp != null)
                printCamp(camp);
        }
    }

    /**
     * Print a list of all the camps created by a staff.
     * Only available to staff.
     * 
     * @param staff               The staff.
     * @param CampSortOrderChoice The user's choice of filter.
     */
    public void viewCampsOfStaff(Staff staff, int CampSortOrderChoice) {
        Log.println("===List of all camps created by " + staff.getUserID() + "===");
        PrintCampSortOrder printCampSortOrder = PrintCampSortOrderEnumList.get(CampSortOrderChoice - 1);
        ArrayList<Camp> sortedCamps = sortCamps(campSystem.getCamps(), printCampSortOrder);
        for (Camp camp : sortedCamps) {
            if (camp != null
                    && camp.getCampInformation().getStaffInChargeId() == staff.getUserID()) // camp created by staff
                printCamp(camp);
        }
    }

    /**
     * A function for staff to view the attendee list for a camp.
     * Only available to staff.
     * 
     * @param staff  Staff object taken in to symbolise that this is a staff
     *               function.
     * @param campId Camp being checked.
     */
    public void viewAttendeeList(Staff staff, int campId) {
        Log.println("===List of all the students attending this camp===");
        Camp camp = campSystem.getCampById(campId);
        for (String student : camp.getAttendeeList()) {
            Log.println(student);
        }
    }

    /**
     * A function for staff to view the committee list for a camp.
     * Only available to staff.
     * 
     * @param staff  Staff object taken in to symbolise that this is a staff
     *               function.
     * @param campId Camp being checked.
     */
    public void viewCampCommitteeList(Staff staff, int campId) {
        Log.println("===List of all the committee members attending this camp===");
        Camp camp = campSystem.getCampById(campId);
        for (String student : camp.getCommitteeList()) {
            Log.println(student);
        }
    }

    // STUDENT FUNCTIONS

    /**
     * A function for camp committee members to view the attendee list for a camp.
     * Only available to camp committee members.
     * 
     * @param campCommitteeMember CampCommitteeMember object taken in to symbolise
     *                            that this is a
     *                            camp committee member function.
     * @param campId              Camp being checked.
     */
    public void viewAttendeeList(CampCommitteeMember campCommitteeMember, int campId) {
        Log.println("===List of all the students attending this camp===");
        Camp camp = campSystem.getCampById(campId);
        if (!camp.getCommitteeList().contains(campCommitteeMember.getStudentId()))
            return;
        for (String student : camp.getAttendeeList()) {
            Log.println(student);
        }
    }

    /**
     * A function for camp committee members to view the committee list for a camp.
     * Only available to camp committee members.
     * 
     * @param campCommitteeMember CampCommitteeMember object taken in to symbolise
     *                            that this is a
     *                            camp committee member function.
     * @param campId              Camp being checked.
     */
    public void viewCampCommitteeList(CampCommitteeMember campCommitteeMember, int campId) {
        Log.println("===List of all the committee members attending this camp===");
        Camp camp = campSystem.getCampById(campId);
        if (!camp.getCommitteeList().contains(campCommitteeMember.getStudentId()))
            return;
        for (String student : camp.getCommitteeList()) {
            Log.println(student);
        }
    }

    /**
     * A function to print all the camps that are available to a student.
     * 
     * @param student             The student being checked.
     * @param CampSortOrderChoice User's choice for sort filter.
     */
    public void viewAvailableCamps(Student student, int CampSortOrderChoice) {
        Log.println("===List of all available camps===");
        PrintCampSortOrder printCampSortOrder = PrintCampSortOrderEnumList.get(CampSortOrderChoice - 1);
        ArrayList<Camp> sortedCamps = sortCamps(campSystem.getCamps(), printCampSortOrder);

        for (Camp camp : sortedCamps) {
            if (campCheckHelperSubSystem.checkCampAvailableToStudent(camp, student).getSuccess()) {
                printCamp(camp);
            }
        }
    }

    /**
     * A function to view all the camps a student has registered for.
     * 
     * @param student             The student.
     * @param CampSortOrderChoice The user's choice of filter.
     */
    public void viewRegisteredCamps(Student student, int CampSortOrderChoice) {
        String studentId = student.getUserID();
        Log.println("===List of all the camps you are registered for===");
        PrintCampSortOrder printCampSortOrder = PrintCampSortOrderEnumList.get(CampSortOrderChoice - 1);
        ArrayList<Camp> sortedCamps = sortCamps(campSystem.getCamps(), printCampSortOrder);
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

    // utility
    /**
     * A private method to sort camps according to an order chosen by the user.
     * 
     * @param unsortedCamps      Unsorted list of camps.
     * @param printCampSortOrder User's choice for sort filter.
     * @return Returns a clone of the original list of camps.
     */
    private ArrayList<Camp> sortCamps(ArrayList<Camp> unsortedCamps, PrintCampSortOrder printCampSortOrder) {
       
        ArrayList<Camp> camps = new ArrayList<>();
        camps.addAll(unsortedCamps);

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

    /**
     * Helper function to print a camp and its details.
     * 
     * @param camp The camp being printed.
     */
    private void printCamp(Camp camp) {
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
        Log.println("=================================================");
    }

}
