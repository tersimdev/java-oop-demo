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
 * @since 24-11-2023
 */
public class CampSystem {
    private CampCheckHelperSubSystem campCheckHelperSubSystem;
    private CampCreationSubSystem campCreationSubSystem;
    private CampRegistrationSubSystem campRegistrationSubSystem;
    private CampViewerSubSystem campViewerSubSystem;
    private ArrayList<Camp> camps;

    private int currCampId;

    /**
     * A constructor for a camp system.
     * 
     * @param dataStoreSystem A class to handle all datastore operations.
     */
    public CampSystem(DataStoreSystem dataStoreSystem) {
        // load in camps from datastore
        camps = dataStoreSystem.getCampDataStoreSubSystem().getAllCamps();
        currCampId = 0;
        if (camps.size() > 0)
            currCampId = camps.get(camps.size() - 1).getCampId() + 1;
        this.campCheckHelperSubSystem = new CampCheckHelperSubSystem(this);
        this.campCreationSubSystem = new CampCreationSubSystem(this, dataStoreSystem);
        this.campRegistrationSubSystem = new CampRegistrationSubSystem(this, dataStoreSystem, campCheckHelperSubSystem);
        this.campViewerSubSystem = new CampViewerSubSystem(this, campCheckHelperSubSystem);
    }

    /**
     * Gets the camp creation sub system.
     * 
     * @return Returns a CampCreationSubSystem object.
     */
    public CampCreationSubSystem getCampCreationSubSystem() {
        return this.campCreationSubSystem;
    }

    /**
     * Gets the camp check helper sub system.
     * 
     * @return Returns a CampCheckHelperSubSystem object.
     */
    public CampCheckHelperSubSystem getcaCampCheckHelperSubSystem() {
        return this.campCheckHelperSubSystem;
    }

    /**
     * Gets the camp registration sub system.
     * 
     * @return Returns a CampRegistrationSubSystem object.
     */
    public CampRegistrationSubSystem getCampRegistrationSubSystem() {
        return this.campRegistrationSubSystem;
    }

    /**
     * Gets the camp viewer sub system.
     * 
     * @return Returns a CampViewerSubSystem object.
     */
    public CampViewerSubSystem getCampViewerSubSystem() {
        return this.campViewerSubSystem;
    }

    /**
     * Gets the correct campId value for a new camp.
     * 
     * @return Returns the largest CampId in the system +1.
     */
    public int getNextCampId() {
        return currCampId++;
    }

    /**
     * Gets the list of all the camps.
     * 
     * @return Returns the list of all the camps.
     */
    public ArrayList<Camp> getCamps() {
        return camps;
    }

    // utility functions
    /**
     * Gets a camp object from its ID.
     * 
     * @param campId The ID of the camp.
     * @return Returns the camp object corresponding to the campId.
     */
    public Camp getCampById(int campId) {
        for (Camp c : camps) {
            if (c.getCampId() == campId)
                return c;
        }
        return null;
    }

    /**
     * Gets the list of all camps created by a staff.
     * 
     * @param staffId The ID of the staff.
     * @return Returns a list of camp objects.
     */
    public ArrayList<Camp> getCampsByStaff(String staffId) {
        ArrayList<Camp> ret = new ArrayList<Camp>();
        for (Camp camp : ret) {
            if (camp.getCampInformation().getStaffInChargeId() == staffId) {
                ret.add(camp);
            }
        }
        return ret;
    }

    /**
     * Gets the list of all camps that contain a camp committee member
     * 
     * @param committeeMemberId The ID of the camp committee member.
     * @return Returns a list of camp objects.
     */
    public ArrayList<Camp> getCampsByCommittee(String committeeMemberId) {
        ArrayList<Camp> ret = new ArrayList<Camp>();
        for (Camp camp : ret) {
            if (camp.getCommitteeList().contains(committeeMemberId)) {
                ret.add(camp);
            }
        }
        return ret;
    }

    /**
     * Gets the list of all camps that contain an attendee.
     * 
     * @param studentId The ID of the attendee.
     * @return Returns a list of camp objects.
     */
    public ArrayList<Camp> getCampsByAttendee(String studentId) {
        ArrayList<Camp> ret = new ArrayList<Camp>();
        for (Camp camp : ret) {
            if (camp.getAttendeeList().contains(studentId)) {
                ret.add(camp);
            }
        }
        return ret;
    }

    /**
     * Gets the list of all camps containing a student, regardless of his role in
     * the camp.
     * 
     * @param studentId The ID of the student.
     * @return Returns a list of camp objects.
     */
    public ArrayList<Camp> getCampsByStudent(String studentId) {
        ArrayList<Camp> ret = getCampsByAttendee(studentId);
        ret.addAll(getCampsByCommittee(studentId));
        return ret;
    }

    /**
     * Ensures that the camp corresponding to the campId exists.
     * 
     * @param campId The ID of the camp.
     * @return Returns true if the camp is valid.
     */
    public boolean checkValidCampId(int campId) {
        return getCampById(campId) != null;
    }

}
