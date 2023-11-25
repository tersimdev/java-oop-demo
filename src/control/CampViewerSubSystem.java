package control;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import entity.Camp;
import entity.CampCommitteeMember;
import entity.Staff;
import entity.Student;
import util.Log;
import util.helpers.DateStringHelper;

/**
 * <p>
 * A class that handles logic for printing camps and their information for
 * viewing.
 * </p>
 * 
 * @author Jon Daniel Acu Kang
 * @version 1.0
 * @since 24-11-2023
 */
public class CampViewerSubSystem {
    /**
     * Dependency Injection
     */
    private CampSystem campSystem;
    /**
     * Dependency Injection
     */
    private CampCheckHelperSubSystem campCheckHelperSubSystem;

    /**
     * Enum to define order to print camps
     */
    private enum PrintCampSortOrder {
        /**
         * Sort by ID
         */
        ID,
        /**
         * Sort by DATES
         */
        DATES,
        /**
         * Sort by LOCATION
         */
        LOCATION,
        /**
         * Sort by ATTENDEE_SLOTS_REMAINING
         */
        ATTENDEE_SLOTS_REMAINING,
        /**
         * Sort by COMMITTEE_SLOTS_REMAINING
         */
        COMMITTEE_SLOTS_REMAINING,
        /**
         * Sort by REGISTRATION_CLOSING_DATE
         */
        REGISTRATION_CLOSING_DATE
    }

    /**
     * An ArrayList that contains enumerators for different filters for viewing
     * camps.
     */
    private final static ArrayList<PrintCampSortOrder> PrintCampSortOrderEnumList = new ArrayList<>(
            Arrays.asList(PrintCampSortOrder.ID, PrintCampSortOrder.DATES, PrintCampSortOrder.LOCATION,
                    PrintCampSortOrder.ATTENDEE_SLOTS_REMAINING,
                    PrintCampSortOrder.COMMITTEE_SLOTS_REMAINING, PrintCampSortOrder.REGISTRATION_CLOSING_DATE));

    /**
     * Constructor for the camp viewer sub system.
     * 
     * @param campSystem               A class that stores all camps, and controls
     *                                 access to
     *                                 them.
     * @param campCheckHelperSubSystem A class to handle camp related checks.
     */
    public CampViewerSubSystem(CampSystem campSystem,
            CampCheckHelperSubSystem campCheckHelperSubSystem) {
        this.campSystem = campSystem;
        this.campCheckHelperSubSystem = campCheckHelperSubSystem;
    }

    // STAFF FUNCTIONS

    /**
     * Prints a list of all the camps in the system.
     * Only available to staff.
     * 
     * @param campSortOrderChoice The user's choice of filter.
     */
    public void viewAllCamps(int campSortOrderChoice) {
        Log.println("===List of all camps===");
        PrintCampSortOrder printCampSortOrder = PrintCampSortOrderEnumList.get(campSortOrderChoice - 1);
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
     * @param campSortOrderChoice The user's choice of filter.
     */
    public void viewCampsOfStaff(Staff staff, int campSortOrderChoice) {
        Log.println("===List of all camps created by " + staff.getUserID() + "===");
        PrintCampSortOrder printCampSortOrder = PrintCampSortOrderEnumList.get(campSortOrderChoice - 1);
        ArrayList<Camp> sortedCamps = sortCamps(campSystem.getCamps(), printCampSortOrder);
        for (Camp camp : sortedCamps) {
            if (camp != null
                    && camp.getCampInformation().getStaffInChargeId().equals(staff.getUserID())) // camp created by
                                                                                                 // staff
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
     * @param student             The student.
     * @param campSortOrderChoice User's choice for sort filter.
     */
    public void viewAvailableCamps(Student student, int campSortOrderChoice) {
        Log.println("===List of all available camps===");
        PrintCampSortOrder printCampSortOrder = PrintCampSortOrderEnumList.get(campSortOrderChoice - 1);
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
     * @param campSortOrderChoice The user's choice of filter.
     */
    public void viewRegisteredCamps(Student student, int campSortOrderChoice) {
        String studentId = student.getUserID();
        Log.println("===List of all the camps you are registered for===");
        PrintCampSortOrder printCampSortOrder = PrintCampSortOrderEnumList.get(campSortOrderChoice - 1);
        ArrayList<Camp> sortedCamps = sortCamps(campSystem.getCamps(), printCampSortOrder);
        for (Camp camp : sortedCamps) {
            if (camp != null) {
                if (camp.getAttendeeList().contains(studentId)) {
                    Log.println("===================================");
                    Log.println("Your role for camp " + camp.getCampId() + ": Attendee");
                    printCamp(camp);
                }
                if (camp.getCommitteeList().contains(studentId)) {
                    Log.println("===================================");
                    Log.println("Your role for camp " + camp.getCampId() + ": Committee Member");
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
        if (unsortedCamps.isEmpty()) {
            Log.error("Passed empty camp into sorter");
            return unsortedCamps;
        }

        ArrayList<Camp> camps = new ArrayList<>();
        camps.addAll(unsortedCamps);

        switch (printCampSortOrder) {
            case ID:
                camps.sort((o1, o2) -> {
                    int id1 = o1.getCampId();
                    int id2 = o2.getCampId();
                    return Integer.compare(id1, id2);
                });
                break;
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
        Log.println("Camp ID: " + camp.getCampId() + " =====================================");
        Log.println("Camp Name: " + camp.getCampName());
        Log.println("Location: " + camp.getCampInformation().getLocation());
        Log.println("Start date: " + DateStringHelper.DateToStrConverter(camp.getCampInformation().getDates().get(0)));
        Log.println("End date: " + DateStringHelper.DateToStrConverter(
                camp.getCampInformation().getDates().get(camp.getCampInformation().getDates().size() - 1)));
        Log.println("Registration deadline: "
                + DateStringHelper.DateToStrConverter(camp.getCampInformation().getRegistrationClosingDate()));
        Log.println("----------------------");
        Log.println("Description: " + camp.getCampInformation().getDescription());
        Log.println("----------------------");
        if (camp.getCampInformation().getUserGroup().isWholeNTU())
            Log.println("This camp is open to all students from NTU");
        else
            Log.println(
                    "This camp is open only to students from "
                            + camp.getCampInformation().getUserGroup().getFaculty().toString());
        Log.println(
                "Attendee slots left: " + (camp.getCampInformation().getTotalSlots()
                        - camp.getCampInformation().getCommitteeSlots() - camp.getAttendeeList().size()));
        Log.println("Committee slots left: "
                + (camp.getCampInformation().getCommitteeSlots() - camp.getCommitteeList().size()));
        Log.println("Contact: " + camp.getCampInformation().getStaffInChargeId());
        Log.println("=================================================");
        Log.println("");
    }

}
