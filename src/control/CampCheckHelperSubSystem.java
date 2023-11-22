package control;

import java.time.LocalDate;
import java.util.ArrayList;

import entity.Camp;
import entity.Student;
import entity.UserGroup;
import util.Log;

public class CampCheckHelperSubSystem {

    private CampSystem campSystem;

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
        UserGroup userGroup = camp.getCampInformation().getUserGroup();

        CheckResult ret = checkRegistrationOpen(camp);
        if (ret.getSuccess() == false)
            return ret;
        ret = ret
                // attendee not registered for this camp
                .and(new CheckResult(!camp.getAttendeeList().contains(studentId), "Already registered as attendee"))
                // not registered as committee for this camp
                .and(new CheckResult(!camp.getCommitteeList().contains(studentId), "Already registered as committee"))
                // camp is not open to this user's faculty
                .and(new CheckResult(student.getFaculty() == userGroup.getFaculty(), "Not available for your faculty"))
                .and(checkDatesNoClash(camp, student, campSystem.getCampsByStudent(studentId)))
                .and(checkCampAttendeeNotFull(camp))
                .and(checkStudentNotWithdrawn(camp, student));

        return ret;
    }

    public CheckResult canRegisterCommittee(Camp camp, Student student) {
        int campId = camp.getCampId();
        String studentId = student.getUserID();
        UserGroup userGroup = camp.getCampInformation().getUserGroup();
        CheckResult checkResult = new CheckResult().setSuccess();

        // TODO
        if (student.getCampCommitteeMember().getIsMember()) {
            Log.println(campId + " is already a committee member for a camp.");
            Log.error(studentId + " was not registered for camp " + campId);
        } else if (camp.getAttendeeList().contains(studentId)) {
            Log.error(studentId + " was not registered for camp " + campId);
            checkResult.setFail(studentId + " is already registered for " + campId + " as an attendee");
        } else if (checkRegistrationClosed(camp)) {
            Log.error(studentId + " was not registered for camp " + campId);
            checkResult.setFail("Registration for this camp has closed");
        } else if (checkDateClash(camp, student, campSystem.getCampsByStudent(studentId))) {
            Log.error(studentId + " was not registered for camp " + campId);
            checkResult.setFail("This camp clashes with another camp " + studentId + " is already registered for");
        } else if (checkCampCommitteeFull(camp)) {
            Log.error(studentId + " was not registered for camp " + campId);
            checkResult.setFail("This camp is full");
        } else if (student.getFaculty() != userGroup.getFaculty()) {
            Log.error(studentId + " was not registered for camp " + campId);
            checkResult.setFail("Camp " + campId + " is only open to " + userGroup.getFaculty());
        } else if (checkStudentWithdrawn(camp, student)) {
            Log.error(studentId + " was not registered for camp " + campId);
            checkResult.setFail(studentId + " previously withdrew from camp " + campId);
        }
        return checkResult;
    }

    // utility
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
