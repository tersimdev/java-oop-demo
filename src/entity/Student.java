package entity;

/**
 * <p>
 * This is an entity class to represent a student
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 1-11-2023
 */
public class Student extends User {
    public Student(String name, String email, String faculty) {
        super(name, email, faculty);
    }

    //private UserGroup userGroup
    private CampCommitteeMember campCommitteeMember;

    public void viewAvailableCamps() {}
    public void registerAsAttendee() {}
    public void registerAsCommittee() {}
    public void submitEnquiry(int campID) {}
    public void viewEnquiry() {}
    public void editEnquiry() {}
    public void deleteEnquiry() {}
    public void viewRegisteredCamps() {}
    public void withdrawFromCamp(int campID) {}
}
