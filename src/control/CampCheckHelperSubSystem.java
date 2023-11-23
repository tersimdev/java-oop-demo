package control;

import java.time.LocalDate;
import java.util.ArrayList;

import entity.Camp;
import entity.Student;
import entity.UserGroup;
import util.Log;

/**
 * A class that provides helpers for checking various camp related things,
 * such as if an attendee is allowed to register for a camp.
 */
public class CampCheckHelperSubSystem {

    private CampSystem campSystem;

    /**
     * Constructor for camp check helper system
     * 
     * @param campSystem A class that stores all camps, and controls access to
     *                   them.
     */
    public CampCheckHelperSubSystem(CampSystem campSystem) {
        this.campSystem = campSystem;
    }

    public class CheckResult {
        private boolean success;
        private String failReason;

        public CheckResult() {
        }

        public CheckResult(boolean success, String failReason) {
            this.success = success;
            this.failReason = failReason;
        }

        public CheckResult setSuccess() {
            this.failReason = null;
            this.success = true;
            return this;
        }

        public CheckResult setFail(String failReason) {
            this.failReason = failReason;
            this.success = false;
            return this;
        }

        public boolean getSuccess() {
            return this.success;
        }

        public String getFailReason() {
            return this.failReason;
        }

        public CheckResult and(CheckResult checkResult) {
            CheckResult ret = new CheckResult();
            if (this.success && checkResult.success)
                ret.setSuccess();
            else {
                ret.setFail(this.failReason + ", " + checkResult.getFailReason());
            }
            return ret;
        }
    }

    public CheckResult canRegisterAttendee(Camp camp, Student student) {
        String studentId = student.getUserID();

        CheckResult ret = checkRegistrationOpen(camp);
        if (ret.getSuccess() == false)
            return ret;
        ret = ret
                // attendee not registered for this camp
                .and(checkAttendeeNotRegistered(camp, student))
                // not registered as committee for this camp
                .and(checkCommitteeMemberNotRegistered(camp, student))
                // camp is not open to this user's faculty
                .and(checkCampOpenToFaculty(camp, student))
                .and(checkDatesNoClash(camp, student, campSystem.getCampsByStudent(studentId)))
                .and(checkCampAttendeeNotFull(camp))
                .and(checkStudentNotWithdrawn(camp, student));

        return ret;
    }

    public CheckResult canRegisterCommittee(Camp camp, Student student) {
        String studentId = student.getUserID();

        CheckResult ret = checkRegistrationOpen(camp);
        if (ret.getSuccess() == false)
            return ret;
        ret = ret
                .and(checkCommitteeMemberNotRegistered(camp, student))
                .and(checkAttendeeNotRegistered(camp, student))
                .and(checkDatesNoClash(camp, student, campSystem.getCampsByStudent(studentId)))
                .and(checkCampCommitteeNotFull(camp))
                .and(checkCampOpenToFaculty(camp, student))
                .and(checkStudentNotWithdrawn(camp, student));

        return ret;
    }

    /**
     * A helper function to check if a camp is available to a student
     * 
     * @param camp    The camp being checked
     * @param student The student we are checking for
     * @return returns a <code>checkResult</code>
     */
    public CheckResult checkCampAvailableToStudent(Camp camp, Student student) {
        String studentId = student.getUserID();
        CheckResult ret = checkVisibile(camp);
        if (ret.getSuccess() == false)
            return ret;
        ret = ret
                .and(checkCampSlotsNotFull(camp))
                .and(checkRegistrationOpen(camp))
                .and(checkDatesNoClash(camp, student, campSystem.getCampsByStudent(studentId)))
                .and(checkStudentNotWithdrawn(camp, student))
                .and(checkCampOpenToFaculty(camp, student));

        return ret;
    }

    // utility
    private CheckResult checkAttendeeNotRegistered(Camp camp, Student student) {
        return new CheckResult(!camp.getAttendeeList().contains(student.getUserID()), "Already registered as attendee");
    }

    private CheckResult checkCommitteeMemberNotRegistered(Camp camp, Student student) {
        return new CheckResult(!camp.getCommitteeList().contains(student.getUserID()),
                "Already registered as committee");
    }

    private CheckResult checkCampOpenToFaculty(Camp camp, Student student) {
        UserGroup userGroup = camp.getCampInformation().getUserGroup();
        boolean ret = userGroup.isWholeNTU();
        if (!ret)
            ret = (student.getFaculty().equals(userGroup.getFaculty()));
        return new CheckResult(ret,
                "Not available for your faculty");
    }

    private CheckResult checkStudentNotWithdrawn(Camp camp, Student student) {
        return new CheckResult(!camp.getWithdrawnList().contains(student.getUserID()), "Student previously withdrew");
    }

    public CheckResult checkRegistrationOpen(Camp camp) {
        LocalDate today = LocalDate.now();
        LocalDate deadline = camp.getCampInformation().getRegistrationClosingDate();
        return new CheckResult((today.compareTo(deadline) < 0), "Deadline passed"); // true if registration is closed
    }

    public CheckResult checkCampAttendeeNotFull(Camp camp) {
        int totalSlots = camp.getCampInformation().getTotalSlots();
        int numberOfAttendees = camp.getAttendeeList().size();
        int committeeSlots = camp.getCampInformation().getCommitteeSlots();
        return new CheckResult(numberOfAttendees < (totalSlots - committeeSlots), "No attendee slots");
    }

    public CheckResult checkCampCommitteeNotFull(Camp camp) {
        return new CheckResult(camp.getCommitteeList().size() < camp.getCampInformation().getCommitteeSlots(),
                "No committee slots");
    }

    public CheckResult checkCampSlotsNotFull(Camp camp) {
        int totalSlots = camp.getCampInformation().getTotalSlots();
        int numberOfAttendees = camp.getAttendeeList().size();
        int numberOfCommittee = camp.getCommitteeList().size();
        return new CheckResult((numberOfAttendees + numberOfCommittee < totalSlots), "Camp is full");
    }

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
