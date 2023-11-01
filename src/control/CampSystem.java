package control;

/**
 * <p>
 * A singleton class that stores all camps, and controls access to them 
 * </p>
 * 
 * @author 
 * @version 1.0
 * @since 1-11-2023
 */
public class CampSystem {
    private static CampSystem instance = null;

    private CampSystem() {
    }

    public static CampSystem getInstance() {
        if (instance == null)
            instance = new CampSystem();
        return instance;
    }
}
