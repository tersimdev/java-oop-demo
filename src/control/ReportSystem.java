package control;

/**
 * <p>
 * A singleton class to generate reports for staff and committee memhers 
 * </p>
 * 
 * @author
 * @version 1.0
 * @since 1-11-2023
 */
public class ReportSystem {
    private static ReportSystem instance = null;

    private ReportSystem() {
    }

    public static ReportSystem getInstance() {
        if (instance == null)
            instance = new ReportSystem();
        return instance;
    }
}
