package control;

import java.time.LocalDate;
import java.util.ArrayList;

import entity.Camp;
import entity.Student;
import entity.UserGroup;

/**
 * <p>
 * A class that provides helpers for checking various camp related things,
 * such as if an attendee is allowed to register for a camp.
 * </p>
 * 
 * @author Jon Daniel Acu Kang
 * @version 1.0
 * @since 24-11-2023
 */
public class CampCheckHelperSubSystem {

    /**
     * Dependency Injection of camp system
     */
    private CampSystem campSystem;

    /**
     * Constructor for camp check helper system.
     * 
     * @param campSystem A class that stores all camps, and controls access to
     *                   them.
     */
    public CampCheckHelperSubSystem(CampSystem campSystem) {
        this.campSystem = campSystem;
    }

    /**
     * A boolean wrapper that also contains a <code>failReason</code> indicating the
     * reason why the boolean value is false.
     */
    public class CheckResult {
        /**
         * Whether the check returns true.
         */
        private boolean success;

        /**
         * The reason for why the check returned false, if it did.
         */
        private String failReason;

        /**
         * Empty constructor for a CheckResult.
         */
        public CheckResult() {
        }

        /**
         * Constructor for a CheckResult.
         * 
         * @param success    Boolean value.
         * @param failReason Reason that <code>success</code> is set to false if it is.
         */
        public CheckResult(boolean success, String failReason) {
            this.success = success;
            this.failReason = failReason;
        }

        /**
         * Setter to set <code>success</code> to true.
         * Also sets <code>failReason</code> to null.
         * 
         * @return Returns itself.
         */
        public CheckResult setSuccess() {
            this.failReason = null;
            this.success = true;
            return this;
        }

        /**
         * Setter to set <code>success</code> to false.
         * Also sets <code>failReason</code>.
         * 
         * @param failReason Reason to be stored in failReason.
         * @return Returns itself.
         */
        public CheckResult setFail(String failReason) {
            this.failReason = failReason;
            this.success = false;
            return this;
        }

        /**
         * Gets the boolean value.
         * 
         * @return success or fail
         */
        public boolean getSuccess() {
            return this.success;
        }

        /**
         * Gets the fail reason.
         * 
         * @return Returns the fail reason as a String.
         */
        public String getFailReason() {
            return this.failReason;
        }

        /**
         * Logical operator and.
         * Stores the <code>failReason</code> of the CheckResult that is set to false,
         * or both if both are false.
         * 
         * @param checkResult CheckResult argument to be checked against.
         * @return Returns a new CheckResult.
         */
        public CheckResult and(CheckResult checkResult) {
            CheckResult ret = new CheckResult();
            if (this.success && checkResult.success)
                ret.setSuccess();
            else if (this.success == false && checkResult.success == false) // both are false
                ret.setFail(this.failReason + ", " + checkResult.getFailReason());
            else if (this.success == false) // keep original fail reason
                ret.setFail(this.failReason);
            else // keep new fail reason
                ret.setFail(checkResult.getFailReason());

            return ret;
        }
    }

    /**
     * A helper function to check if a student can register for a camp as an
     * attendee.
     * Checks if:
     * <ul>
     * <li>Camp deadline has not been passed</li>
     * <li>The camp is visible</li>
     * <li>Student is not registered as a committee member</li>
     * <li>Student is not registered as an attendee</li>
     * <li>Dates of the camp does not overlap with other camps registered by the
     * student</li>
     * <li>The camp's committee slots are not filled</li>
     * <li>The camp is open to the student's faculty</li>
     * <li>The student has not withdrawn from this camp in the past</li>
     * </ul>
     * 
     * @param camp    The camp being registered for.
     * @param student The student trying to register.
     * @return Returns a <code>CheckResult</code>
     */
    public CheckResult canRegisterAttendee(Camp camp, Student student) {
        String studentId = student.getUserID();

        CheckResult ret = checkRegistrationOpen(camp);
        if (ret.getSuccess() == false)
            return ret;
        ret = ret
                .and(checkVisibile(camp))
                .and(checkAttendeeNotRegistered(camp, student))
                .and(checkCommitteeMemberNotRegistered(camp, student))
                .and(checkCampOpenToFaculty(camp, student))
                .and(checkDatesNoClash(camp, student, campSystem.getCampsByStudent(studentId)))
                .and(checkCampAttendeeNotFull(camp))
                .and(checkStudentNotWithdrawn(camp, student));

        return ret;
    }

    /**
     * A helper function to check if a student can register for a camp as a
     * committee member.
     * Checks if:
     * <ul>
     * <li>Camp deadline has not been passed</li>
     * <li>The camp is visible</li>
     * <li>Student is not already a committee member</li>
     * <li>Student is not registered as an attendee</li>
     * <li>Dates of the camp does not overlap with other camps registered by the
     * student</li>
     * <li>The camp's committee slots are not filled</li>
     * <li>The camp is open to the student's faculty</li>
     * <li>The student has not withdrawn from this camp in the past</li>
     * </ul>
     * 
     * @param camp    The camp being registered for.
     * @param student The student trying to register.
     * @return Returns a <code>CheckResult</code>
     */
    public CheckResult canRegisterCommittee(Camp camp, Student student) {
        String studentId = student.getUserID();

        CheckResult ret = checkRegistrationOpen(camp);
        if (ret.getSuccess() == false)
            return ret;
        ret = ret
                .and(checkVisibile(camp))
                .and(checkStudentIsNotCommitteeMember(student))
                .and(checkAttendeeNotRegistered(camp, student))
                .and(checkDatesNoClash(camp, student, campSystem.getCampsByStudent(studentId)))
                .and(checkCampCommitteeNotFull(camp))
                .and(checkCampOpenToFaculty(camp, student))
                .and(checkStudentNotWithdrawn(camp, student));

        return ret;
    }

    /**
     * A helper function to check if a camp is available to a student.
     * Checks if:
     * <ul>
     * <li>The camp is visible</li>
     * <li>The camp is not full</li>
     * <li>Camp deadline has not been passed</li>
     * <li>Dates of the camp does not overlap with other camps registered by the
     * student</li>
     * <li>The student has not withdrawn from this camp in the past</li>
     * <li>The camp is open to the student's faculty</li>
     * </ul>
     * 
     * @param camp    The camp being checked
     * @param student The student we are checking for
     * @return Returns a <code>CheckResult</code>
     */
    public CheckResult checkCampAvailableToStudent(Camp camp, Student student) {
        String studentId = student.getUserID();
        CheckResult ret = checkVisibile(camp);
        if (ret.getSuccess() == false)
            return ret;
        ret = ret
                .and(checkVisibile(camp))
                .and(checkCampSlotsNotFull(camp))
                .and(checkRegistrationOpen(camp))
                .and(checkDatesNoClash(camp, student, campSystem.getCampsByStudent(studentId)))
                .and(checkStudentNotWithdrawn(camp, student))
                .and(checkCampOpenToFaculty(camp, student));

        return ret;
    }

    // utility
    /**
     * Checks if an attendee is not registered for a camp.
     * 
     * @param camp    The camp.
     * @param student The attendee.
     * @return Returns a CheckResult with <code>success</code> set to true if the
     *         attendee is not registered.
     */
    private CheckResult checkAttendeeNotRegistered(Camp camp, Student student) {
        return new CheckResult(!camp.getAttendeeList().contains(student.getUserID()), "Already registered as attendee");
    }

    /**
     * Checks if a committee member is not registered for a camp.
     * 
     * @param camp    The camp.
     * @param student The attendee.
     * @return Returns a CheckResult with <code>success</code> set to true if the
     *         committee member is not registered.
     */
    private CheckResult checkCommitteeMemberNotRegistered(Camp camp, Student student) {
        return new CheckResult(!camp.getCommitteeList().contains(student.getUserID()),
                "Already registered as committee");
    }

    /**
     * Checks if a student is not already a committee member.
     * 
     * @param student The student.
     * @return Returns a CheckResult with <code>success</code> set to true if the
     *         student is not a committee member.
     */
    private CheckResult checkStudentIsNotCommitteeMember(Student student) {
        return new CheckResult(!student.getCampCommitteeMember().getIsMember(), "Already a committee member");
    }

    /**
     * Checks if a camp is open to a student's faculty.
     * 
     * @param camp    The camp.
     * @param student The student.
     * @return Returns a CheckResult with <code>success</code> set to true if the
     *         camp is open to whole NTU or is open to the student's faculty.
     */
    private CheckResult checkCampOpenToFaculty(Camp camp, Student student) {
        UserGroup userGroup = camp.getCampInformation().getUserGroup();
        boolean ret = userGroup.isWholeNTU();
        if (!ret)
            ret = (student.getFaculty().equals(userGroup.getFaculty()));
        return new CheckResult(ret,
                "Not available for your faculty");
    }

    /**
     * Checks if a student has previously withdrawn from a camp.
     * 
     * @param camp    The camp.
     * @param student The student.
     * @return Returns a CheckResult with <code>success</code> set to true if the
     *         student has not previously withdrawn from the camp.
     */
    private CheckResult checkStudentNotWithdrawn(Camp camp, Student student) {
        return new CheckResult(!camp.getWithdrawnList().contains(student.getUserID()), "Student previously withdrew");
    }

    /**
     * Checks if a camp's registration deadline has passed.
     * 
     * @param camp The camp.
     * @return Returns a CheckResult with <code>success</code> set to true if the
     *         camp's registration deadline has not passed yet.
     */
    public CheckResult checkRegistrationOpen(Camp camp) {
        LocalDate today = LocalDate.now();
        LocalDate deadline = camp.getCampInformation().getRegistrationClosingDate();
        return new CheckResult((today.compareTo(deadline) < 0), "Deadline passed"); // true if registration is closed
    }

    /**
     * Checks if a camp still has available attendee slots.
     * 
     * @param camp The camp.
     * @return Returns a CheckResult with <code>success</code> set to true if the
     *         camp still has available attendee slots.
     */
    public CheckResult checkCampAttendeeNotFull(Camp camp) {
        int totalSlots = camp.getCampInformation().getTotalSlots();
        int numberOfAttendees = camp.getAttendeeList().size();
        int committeeSlots = camp.getCampInformation().getCommitteeSlots();
        return new CheckResult(numberOfAttendees < (totalSlots - committeeSlots), "No attendee slots");
    }

    /**
     * Checks if a camp still has available committee slots.
     * 
     * @param camp The camp.
     * @return Returns a CheckResult with <code>success</code> set to true if the
     *         camp still has available committee slots.
     */
    public CheckResult checkCampCommitteeNotFull(Camp camp) {
        return new CheckResult(camp.getCommitteeList().size() < camp.getCampInformation().getCommitteeSlots(),
                "No committee slots");
    }

    /**
     * Checks if a camp has any available slots, regardless if its attendee or
     * committee member slots.
     * 
     * @param camp The camp.
     * @return Returns a CheckResult with <code>success</code> set to true if the
     *         camp has any available slots.
     */
    public CheckResult checkCampSlotsNotFull(Camp camp) {
        int totalSlots = camp.getCampInformation().getTotalSlots();
        int numberOfAttendees = camp.getAttendeeList().size();
        int numberOfCommittee = camp.getCommitteeList().size();
        return new CheckResult((numberOfAttendees + numberOfCommittee < totalSlots), "Camp is full");
    }

    /**
     * Checks if the camp visibility is set to visible.
     * 
     * @param camp The camp.
     * @return Returns a CheckResult with <code>success</code> set to true if the
     *         camp is visibile.
     */
    public CheckResult checkVisibile(Camp camp) {
        return new CheckResult(camp.checkVisibility(), "Camp visibility is off");
    }

    /**
     * Checks if dates of <code>camp</code> does not overlap with
     * <code>campsByStudent</code>
     * 
     * @param camp           camp object to check
     * @param student        student object
     * @param campsByStudent list of camp objects that student is registered for
     * @return returns success if camp does not clash with other camps the student
     *         is
     *         registered for
     */
    public CheckResult checkDatesNoClash(Camp camp, Student student, ArrayList<Camp> campsByStudent) {
        // returns true if camp clashes with other camps student
        // is registered for
        // camp dates for new camp we are checking against
        LocalDate campFirstDate;
        LocalDate campLastDate;
        // camp dates for camps student is already registered for
        LocalDate ptrCampFirstDate;
        LocalDate ptrCampLastDate;
        CheckResult ret = new CheckResult();

        campFirstDate = camp.getCampInformation().getDates().get(0);
        if (camp.getCampInformation().getDates().size() > 1) {
            campLastDate = camp.getCampInformation().getDates().get(camp.getCampInformation().getDates().size() - 1);
        } else {
            campLastDate = campFirstDate;
        }
        for (Camp campPointer : campsByStudent) {
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
                return ret.setFail("Clashes with registered camps");
            }
        }
        return ret.setSuccess();
    }

}
