package entity;

/**
 * <p>
 * This is an entity class to represent a staff
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 19-11-2023
 */
public class Staff extends User {
    /**
     * Default constructor for a staff object.
     */
    public Staff() {
        super();
    }

    /**
     * Constructor for a staff object.
     * 
     * @param displayName Display name of the staff.
     * @param userID      User ID of the staff.
     * @param faculty     Faculty of the staff.
     */
    public Staff(String displayName, String userID, Faculty faculty) {
        super(displayName, userID, faculty);
    }
}
