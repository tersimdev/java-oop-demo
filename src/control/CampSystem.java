package control;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import boundary.StaffMenu;
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
    private CampCheckHelperSubSystem campCheckHelperSubSystem;
    private CampCreationSubSystem campCreationSubSystem;
    private CampRegistrationSubSystem campRegistrationSubSystem;
    private ArrayList<Camp> camps;

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

    private int currCampId;

    public CampSystem(DataStoreSystem dataStoreSystem) {
        // load in camps from datastore
        camps = dataStoreSystem.getAllCamps();
        currCampId = 0;
        if (camps.size() > 0)
        currCampId = camps.get(camps.size() - 1).getCampId() + 1;
        this.campCreationSubSystem = new CampCreationSubSystem(this, dataStoreSystem);
        this.campCheckHelperSubSystem = new CampCheckHelperSubSystem(this); 
    }

    public CampCreationSubSystem getCampCreationSubSystem() {
        return this.campCreationSubSystem;
    }

    public int getNextCampId() {
        return currCampId++;
    }

    public ArrayList<Camp> getCamps() {
        return camps;
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
        ArrayList<Camp> ret = new ArrayList<Camp>();
        for (Camp camp : ret) {
            if (camp.getCampInformation().getStaffInChargeId() == staffId) {
                ret.add(camp);
            }
        }
        return ret;
    }

    public ArrayList<Camp> getCampsByCommittee(String committeeMemberId) {
        ArrayList<Camp> ret = new ArrayList<Camp>();
        for (Camp camp : ret) {
            if (camp.getCommitteeList().contains(committeeMemberId)) {
                ret.add(camp);
            }
        }
        return ret;
    }

    public ArrayList<Camp> getCampsByAttendee(String studentId) {
        ArrayList<Camp> ret = new ArrayList<Camp>();
        for (Camp camp : ret) {
            if (camp.getAttendeeList().contains(studentId)) {
                ret.add(camp);
            }
        }
        return ret;
    }

    public ArrayList<Camp> getCampsByStudent(String studentId) {
        ArrayList<Camp> ret = getCampsByAttendee(studentId);
        ret.addAll(getCampsByCommittee(studentId));
        return ret;
    }

    public boolean checkValidCampId(int campId) {
        return getCampById(campId) != null;
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
