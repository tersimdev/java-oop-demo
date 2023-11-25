package control;

import control.CampCheckHelperSubSystem.CheckResult;
import entity.Camp;
import entity.CampCommitteeMember;
import entity.Student;
import util.Log;

/**
 * <p>
 * A class that handles logic for registration and withdrawal of users for
 * camps.
 * </p>
 * 
 * @author Jon Daniel Acu Kang
 * @version 1.0
 * @since 24-11-2023
 */
public class CampRegistrationSubSystem {
    /**
     * Dependency Injection
     */
    private CampSystem campSystem;
    /**
     * Dependency Injection
     */
    private DataStoreSystem dataStoreSystem;
    /**
     * Dependency Injection
     */
    private CampCheckHelperSubSystem campCheckHelperSubSystem;

    /**
     * Constructor for the camp registration sub system.
     * 
     * @param campSystem               A class that stores all camps, and controls
     *                                 access to them.
     * @param dataStoreSystem          A class to handle all datastore operations.
     * @param campCheckHelperSubSystem A class to handle checking of camp related
     *                                 logic
     */
    public CampRegistrationSubSystem(CampSystem campSystem, DataStoreSystem dataStoreSystem,
            CampCheckHelperSubSystem campCheckHelperSubSystem) {
        this.campSystem = campSystem;
        this.dataStoreSystem = dataStoreSystem;
        this.campCheckHelperSubSystem = campCheckHelperSubSystem;
    }

    /**
     * Registers the student as an attendee for the camp.
     * Prints an error message if registration fails.
     * 
     * @param student Student object of the student being registered.
     * @param campId  campId of the camp being checked.
     */
    public void registerAsAttendee(Student student, int campId) {
        Camp camp = campSystem.getCampById(campId);
        CheckResult checkResult = campCheckHelperSubSystem.canRegisterAttendee(camp, student);
        if (checkResult.getSuccess()) {
            camp.addAttendee(student);
            Log.println(student.getUserID() + " has been registered for camp " + campId);
        } else {
            Log.println("Registration failed: " + checkResult.getFailReason());
        }
        dataStoreSystem.getCampDataStoreSubSystem().updateCampDetails(camp);
    }

    /**
     * Registers the student as an committee member for the camp.
     * Prints an error message if registration fails.
     * 
     * @param student Student object of the student being registered.
     * @param campId  campId of the camp being checked.
     */
    public void registerAsCommittee(Student student, int campId) {
        String studentId = student.getUserID();
        Camp camp = campSystem.getCampById(campId);
        CheckResult checkResult = campCheckHelperSubSystem.canRegisterCommittee(camp, student);
        if (checkResult.getSuccess()) {
            CampCommitteeMember committeeMember = student.getCampCommitteeMember();
            committeeMember.setCampId(campId);
            committeeMember.setIsMember(true);
            camp.addCampCommitteeMember(committeeMember);
            Log.println(studentId + " has been registered for camp " + campId + " as a camp committee member");
        } else {
            Log.println("Registration failed: " + checkResult.getFailReason());
        }
        dataStoreSystem.getCampDataStoreSubSystem().updateCampDetails(camp);
        dataStoreSystem.getUserDataStoreSubSystem().updateCommitteeMemberDetails(student.getCampCommitteeMember());
    }

    /**
     * Withdraws the attendee from the camp.
     * Prints an error message if withdrawal fails.
     * 
     * @param student Student object of the student being registered.
     * @param campId  campId of the camp being checked.
     */
    public void withdrawFromCamp(Student student, int campId) {
        Camp camp = campSystem.getCampById(campId);
        String studentId = student.getUserID();

        if (camp.getAttendeeList().contains(studentId)) {
            camp.removeAttendee(student);
        } else if (camp.getCommitteeList().contains(studentId)) {
            Log.println("Camp committee members cannot withdraw");
        } else {
            Log.println("Withdrawal failed");
        }
        dataStoreSystem.getCampDataStoreSubSystem().updateCampDetails(camp);
    }
}
