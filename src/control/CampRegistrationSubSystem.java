package control;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import control.CampCheckHelperSubSystem.CheckResult;
import entity.Camp;
import entity.CampCommitteeMember;
import entity.CampInformation;
import entity.Student;
import entity.UserGroup;
import util.Input;
import util.Log;

public class CampRegistrationSubSystem {
    private CampSystem campSystem;
    private DataStoreSystem dataStoreSystem;
    private CampCheckHelperSubSystem campCheckHelperSubSystem;

    public CampRegistrationSubSystem(CampSystem campSystem, DataStoreSystem dataStoreSystem, CampCheckHelperSubSystem campCheckHelperSubSystem) {
        this.campSystem = campSystem;
        this.dataStoreSystem = dataStoreSystem;
        this.campCheckHelperSubSystem = campCheckHelperSubSystem;
    }

     public void registerAsAttendee(Student student, int campId) {
        Camp camp = campSystem.getCampById(campId);
        CheckResult checkResult = campCheckHelperSubSystem.canRegisterAttendee(camp, student);
        if (checkResult.getSuccess()) {
            camp.addAttendee(student);
            Log.println(student.getUserID() + " has been registered for camp " + campId);
        }
        else {
            Log.println("Registration failed: " + checkResult.getFailReason());
        }
        dataStoreSystem.updateCampDetails(camp);
    }

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
        }
        else {
            Log.println("Registration failed: " + checkResult.getFailReason());
        }
        dataStoreSystem.updateCampDetails(camp);
    }
}
