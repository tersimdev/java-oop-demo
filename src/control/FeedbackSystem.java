package control;

/**
 * <p>
 * A singleton class to store enquiries and suggestions 
 * </p>
 * 
 * @author 
 * @version 1.0
 * @since 1-11-2023
 */
public class FeedbackSystem {
    private static FeedbackSystem instance = null;

    private FeedbackSystem() {
    }

    public static FeedbackSystem getInstance() {
        if (instance == null)
            instance = new FeedbackSystem();
        return instance;
    }
}
