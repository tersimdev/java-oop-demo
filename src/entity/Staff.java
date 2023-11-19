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
    public Staff() {
        super();
    }

    public Staff(String displayName, String userID, Faculty faculty) {
        super(displayName, userID, faculty);
    }
}
