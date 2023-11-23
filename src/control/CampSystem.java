package control;

import java.util.ArrayList;

import entity.Camp;

/**
 * <p>
 * A class that stores all camps, and controls access to them
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
    private CampViewerSubSystem campViewerSubSystem;
    private ArrayList<Camp> camps;

    private int currCampId;

    public CampSystem(DataStoreSystem dataStoreSystem) {
        // load in camps from datastore
        camps = dataStoreSystem.getCampDataStoreSubSystem().getAllCamps();
        currCampId = 0;
        if (camps.size() > 0)
            currCampId = camps.get(camps.size() - 1).getCampId() + 1;
        this.campCreationSubSystem = new CampCreationSubSystem(this, dataStoreSystem);
        this.campRegistrationSubSystem = new CampRegistrationSubSystem(this, dataStoreSystem, campCheckHelperSubSystem);
        this.campViewerSubSystem = new CampViewerSubSystem(this, dataStoreSystem, campCheckHelperSubSystem);
        this.campCheckHelperSubSystem = new CampCheckHelperSubSystem(this);
    }

    public CampCreationSubSystem getCampCreationSubSystem() {
        return this.campCreationSubSystem;
    }

    public CampRegistrationSubSystem getCampRegistrationSubSystem() {
        return this.campRegistrationSubSystem;
    }

    public CampViewerSubSystem getCampViewerSubSystem() {
        return this.campViewerSubSystem;
    }

    public int getNextCampId() {
        return currCampId++;
    }

    public ArrayList<Camp> getCamps() {
        return camps;
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

}
