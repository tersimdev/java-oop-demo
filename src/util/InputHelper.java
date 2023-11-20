package util;

import control.CampSystem;
import control.FeedbackSystem;

/**
 * A class that provides static functions to help get certain inputs from users
 * that are dependent on validity provided by systems.
 * An example is getting a valid campId from user.
 * Depends on Input class to get the actual input,
 * and runs validity check using the system provided.
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 21-11-2023
 */
public class InputHelper {

    /**
     * Get camp id from user input.
     * User will be asked to enter ID to <code>action</code>.
     * 
     * @param input      input object
     * @param campSystem campsystem object
     * @param action     string describing what user would do with id
     * @return
     */
    public static int getCampIdFromUser(Input input, CampSystem campSystem, String action) {
        int ret = -1;
        boolean valid = false;
        while (!valid) {
            ret = input.getInt("Please enter the ID of the camp you would like to " + action + ": ");
            valid = campSystem.checkValidCampId(ret);
        }
        return ret;
    }

    public static int getEnquiryIdFromUser(Input input, FeedbackSystem feedbackSystem, String action) {
        int ret = -1;
        boolean valid = false;
        while (!valid) {
            ret = input.getInt("Please enter the ID of the enquiry you would like to " + action + ": ");
            // TODO
            // valid = feedbackSystem.checkValidEnquiryId(ret);
        }
        return ret;
    }

    public static int getSuggestionIdFromUser(Input input, FeedbackSystem feedbackSystem, String action) {
        int ret = -1;
        boolean valid = false;
        while (!valid) {
            ret = input.getInt("Please enter the ID of the suggestion you would like to " + action + ": ");
            // TODO
            // valid = feedbackSystem.checkValidSuggestionId(ret);
        }
        return ret;
    }

}
