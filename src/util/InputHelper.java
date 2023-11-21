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
     * @return valid campId
     */
    public static int getCampIdFromUser(Input input, CampSystem campSystem, String action) {
        int ret = -1;
        boolean valid = false;
        while (!valid) {
            ret = input.getInt("Please enter the ID of the camp you would like to " + action + ": ");
            valid = campSystem.checkValidCampId(ret);
            if (!valid)
                Log.println("Camp ID not found.");
        }
        return ret;
    }

    /**
     * Get enquiry id from user input.
     * User will be asked to enter ID to <code>action</code>.
     * 
     * @param input          input object
     * @param feedbackSystem feedbackSystem object
     * @param action         string describing what user would do with id
     * @param campId         camp that enquiry belongs to
     * @return valid enquiryId
     */
    public static int getEnquiryIdFromUser(Input input, FeedbackSystem feedbackSystem, String action, int campId) {
        int ret = -1;
        boolean valid = false;
        while (!valid) {
            ret = input.getInt("Please enter the ID of the enquiry you would like to " + action + ": ");
            valid = feedbackSystem.checkValidEnquiryId(ret, campId);
            if (!valid)
                Log.println("Enquiry ID not found.");
        }
        return ret;
    }

    /**
     * Get suggestion id from user input.
     * User will be asked to enter ID to <code>action</code>.
     * 
     * @param input          input object
     * @param feedbackSystem feedbackSystem object
     * @param action         string describing what user would do with id
     * @param campId         camp that suggestion belongs to
     * @return valid suggestionId
     */
    public static int getSuggestionIdFromUser(Input input, FeedbackSystem feedbackSystem, String action, int campId) {
        int ret = -1;
        boolean valid = false;
        while (!valid) {
            ret = input.getInt("Please enter the ID of the suggestion you would like to " + action + ": ");
            valid = feedbackSystem.checkValidSuggestionId(ret, campId);
            if (!valid)
                Log.println("Suggestion ID not found.");
        }
        return ret;
    }

    /**
     * Gets an int from user.
     * Throws an error message if the input is not within the given min and max.
     * @param input
     * @param min
     * @param max
     * @param msg
     * @return
     */
    public static int getBoundedInt(Input input, int min, int max, String msg) {
        int ret = -1;
        boolean valid = false;
        while (!valid) {
            ret = input.getInt(msg);
            valid = (ret >= min && ret <= max);
            if (!valid)
                Log.println("Invalid input. Input exceeds bounds.");
        }
        return ret;
    }

}
