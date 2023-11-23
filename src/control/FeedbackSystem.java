package control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import entity.CampFeedback;
import entity.CampEnquiry;
import entity.CampSuggestion;
import util.Log;
import util.Input;

/**
 * <p>
 * A singleton class to store enquiries and suggestions
 * </p>
 * 
 * @author Yen Zhi Wei
 * @version 1.0
 * @since 1-11-2023
 */
public abstract class FeedbackSystem {
    protected DataStoreSystem dataStoreSystem;
    /*
     * private Map<Integer, ArrayList<CampEnquiry>> enquiriesMap;
     * private Map<Integer, ArrayList<CampSuggestion>> suggestionsMap;
     */
    protected Map<Integer, ArrayList<CampFeedback>> feedbacksMap;
    protected int nextFeedbackId;

    /*
     * private int nextEnquiryId;
     * private int nextSuggetionId;
     */

    public FeedbackSystem(DataStoreSystem dataStoreSystem) {
        this.feedbacksMap = new HashMap<>();
        this.dataStoreSystem = dataStoreSystem;

        // ArrayList<CampFeedback> feedbackList = dataStoreSystem.getAllFeedback();
        // ArrayList<CampSuggestion> suggestionList =
        // dataStoreSystem.getAllSuggestions();

        ArrayList<CampFeedback> feedbackList = loadFeedbackFromDatastore();

        // store in system, use these as next id
        nextFeedbackId = 0;
        if (feedbackList.size() > 0)
            nextFeedbackId = feedbackList.get(feedbackList.size() - 1).getFeedbackId() + 1;

        // add to map based on camp id
        for (CampFeedback cf : feedbackList) {
            if (!feedbacksMap.containsKey(cf.getCampId()))
                feedbacksMap.put(cf.getCampId(), new ArrayList<>());
            feedbacksMap.get(cf.getCampId()).add(cf);
        }
    }

    public abstract ArrayList<CampFeedback> loadFeedbackFromDatastore();


    /*
     * public FeedbackSystem(DataStoreSystem dataStoreSystem) {
     * this.enquiriesMap = new HashMap<>();
     * this.suggestionsMap = new HashMap<>();
     * this.dataStoreSystem = dataStoreSystem;
     * 
     * ArrayList<CampEnquiry> enquiryList = dataStoreSystem.getAllEnquiries();
     * ArrayList<CampSuggestion> suggestionList =
     * dataStoreSystem.getAllSuggestions();
     * 
     * // cld use these as next id, store in system?
     * nextEnquiryId = 0;
     * nextSuggetionId = 0;
     * if (enquiryList.size() > 0)
     * nextEnquiryId = enquiryList.get(enquiryList.size() - 1).getEnquiryId() + 1;
     * if (suggestionList.size() > 0)
     * nextSuggetionId = suggestionList.get(enquiryList.size() -
     * 1).getSuggestionId() + 1;
     * 
     * // add to map based on camp id
     * for (CampEnquiry ce : enquiryList) {
     * if (!enquiriesMap.containsKey(ce.getCampId()))
     * enquiriesMap.put(ce.getCampId(), new ArrayList<>());
     * enquiriesMap.get(ce.getCampId()).add(ce);
     * }
     * for (CampSuggestion cs : suggestionList) {
     * if (!suggestionsMap.containsKey(cs.getCampId()))
     * suggestionsMap.put(cs.getCampId(), new ArrayList<>());
     * suggestionsMap.get(cs.getCampId()).add(cs);
     * }
     * }
     */

    /*
     * public void pendingEnquiries(String studentId, int campId, Input input) {
     * int pending = 0;
     * ArrayList<CampEnquiry> studentEnquiryList = new ArrayList<>();
     * studentEnquiryList = getCampEnquiries(campId);
     * for (CampEnquiry temp : studentEnquiryList) {
     * boolean belongsToUser = temp.getOwner().equals(studentId);
     * boolean processed = !temp.isPending();
     * if (temp == null || !belongsToUser || processed)
     * continue;
     * else {
     * pending += 1;
     * Log.println("EnquiryID: " + temp.getEnquiryId());
     * Log.println("StudentID: " + temp.getOwner());
     * Log.println("Enquiry Status: Pending");
     * Log.println("Enquiry: " + temp.getEnquiry());
     * Log.println("");
     * }
     * }
     * if (pending==0) {
     * Log.println("No pending enquiries found. Directing back to menu...");
     * return;
     * }
     * Log.println("===Please select the following options=== ");
     * Log.println("(1) Edit Enquiry");
     * Log.println("(2) Delete Enquiry");
     * Log.println("(3) Back to Student Menu");
     * int sChoice = -1;
     * while (sChoice < 0) {
     * sChoice = input.getInt("Enter choice: ");
     * if (sChoice == 1) {
     * int enquiryId =
     * input.getInt("Please enter the enquiryId of the enquiry to edit: ");
     * String newEnquiry = input.getLine("Please enter new enquiry: ");
     * Boolean result = editCampEnquiry(campId, enquiryId,
     * newEnquiry);
     * if (result)
     * Log.println("Edit successful.");
     * else
     * Log.println("Edit failed.");
     * }
     * else if (sChoice == 2) {
     * int enquiryId =
     * input.getInt("Please enter the enquiryId of the enquiry to delete: ");
     * Boolean result = removeCampEnquiry(campId, enquiryId);
     * if (result)
     * Log.println("Deletion successful.");
     * else
     * Log.println("Deletion failed.");
     * }
     * }
     * return;
     * }
     */
    public void addCampFeedback(int campId, CampFeedback feedback) {
        ArrayList<CampFeedback> feedbacks = feedbacksMap.computeIfAbsent(campId, k -> new ArrayList<>());
        // Index in the ArrayList used as enquiryID
        int feedbackId = nextFeedbackId++;
        feedback.setFeedbackId(feedbackId);
        // Add the new feedback to the ArrayList
        feedbacks.add(feedback);
        // NEED EDIT dataStoreSystem.addFeedback(feedback);
    }

    /*
     * public void addCampEnquiry(int campId, CampEnquiry enquiry) {
     * ArrayList<CampEnquiry> enquiries = enquiriesMap.computeIfAbsent(campId, k ->
     * new ArrayList<>());
     * // Index in the ArrayList used as enquiryID
     * int enquiryId = nextEnquiryId++;
     * enquiry.setEnquiryId(enquiryId);
     * // Add the new enquiry to the ArrayList
     * enquiries.add(enquiry);
     * dataStoreSystem.addEnquiry(enquiry);
     * }
     * 
     * public void addCampSuggestion(int campId, CampSuggestion suggestion) {
     * ArrayList<CampSuggestion> suggestions =
     * suggestionsMap.computeIfAbsent(campId, k -> new ArrayList<>());
     * // Index in the ArrayList used as the suggestionID
     * int suggestionId = nextSuggetionId++;
     * suggestion.setSuggestionId(suggestionId);
     * // Add the new suggestion to the ArrayList
     * suggestions.add(suggestion);
     * dataStoreSystem.addSuggestion(suggestion);
     * }
     */

    public boolean editCampFeedback(int campId, int feedbackId, String newFeedback) {
        CampFeedback campFeedback = findFeedbackById(feedbackId, campId);
        if (campFeedback != null) {
            campFeedback.setFeedback(newFeedback);
            return true;
        }
        return false;
    }

    /*
     * public boolean editCampEnquiry(int campId, int enquiryId, String newEnquiry)
     * {
     * CampEnquiry campEnquiry = findEnquiryById(enquiryId, campId);
     * if (campEnquiry != null) {
     * campEnquiry.setEnquiry(newEnquiry);
     * return true;
     * }
     * return false;
     * }
     * 
     * public boolean editCampSuggestion(int campId, int suggestionId, String
     * newSuggestion) {
     * CampSuggestion campSuggestion = findSuggestionById(suggestionId, campId);
     * if (campSuggestion != null) {
     * campSuggestion.setSuggestion(newSuggestion);
     * return true;
     * }
     * return false;
     * }
     */

    public boolean removeCampFeedback(int campId, int feedbackId) {
        CampFeedback campFeedback = findFeedbackById(feedbackId, campId);
        if (campFeedback != null) {
            getCampFeedbacks(campId).remove(campFeedback);
            return true;
        }
        return false;
    }

    /*
     * public boolean removeCampEnquiry(int campId, int enquiryId) {
     * CampEnquiry campEnquiry = findEnquiryById(enquiryId, campId);
     * if (campEnquiry != null) {
     * getCampEnquiries(campId).remove(campEnquiry);
     * return true;
     * }
     * return false;
     * }
     * 
     * public boolean removeCampSuggestion(int campId, int suggestionId) {
     * CampSuggestion campSuggestion = findSuggestionById(suggestionId, campId);
     * if (campSuggestion != null) {
     * getCampSuggestions(campId).remove(campSuggestion);
     * return true;
     * }
     * return false;
     * }
     */

    /*public boolean processCampEnquiry(String commMemberId, int campId, int enquiryId, String reply) {
        CampEnquiry campEnquiry = findEnquiryById(enquiryId, campId);
        if (campEnquiry != null) {
            campEnquiry.reply(commMemberId, reply);
            return true;
        }
        return false;
    }

    public boolean processCampSuggestion(String staffId, int campId, int suggestionId, boolean decision) {
        CampSuggestion campSuggestion = findSuggestionById(suggestionId, campId);
        if (campSuggestion != null) {
            campSuggestion.setApproval(staffId, decision);
            return true;
        }
        return false;
    }*/

    public ArrayList<CampFeedback> getCampFeedbacks(int campId) {
        return feedbacksMap.getOrDefault(campId, new ArrayList<>());
    }

    /*
     * public ArrayList<CampEnquiry> getCampEnquiries(int campId) {
     * return enquiriesMap.getOrDefault(campId, new ArrayList<>());
     * }
     * 
     * public ArrayList<CampSuggestion> getCampSuggestions(int campId) {
     * return suggestionsMap.getOrDefault(campId, new ArrayList<>());
     * }
     */

    public CampFeedback findFeedbackById(int feedbackId, int campId) {
        for (CampFeedback cf : getCampFeedbacks(campId)) {
            if (cf.getFeedbackId() == feedbackId)
                return cf;
        }
        return null;
    }

    /*
     * public CampEnquiry findEnquiryById(int enquiryId, int campId) {
     * for (CampEnquiry ce : getCampEnquiries(campId)) {
     * if (ce.getEnquiryId() == enquiryId)
     * return ce;
     * }
     * return null;
     * }
     * 
     * public CampSuggestion findSuggestionById(int enquiryId, int campId) {
     * for (CampSuggestion cs : getCampSuggestions(campId)) {
     * if (cs.getSuggestionId() == enquiryId)
     * return cs;
     * }
     * return null;
     * }
     */

    public boolean checkValidFeedbackId(int feedbackId, int campId) {
        return findFeedbackById(feedbackId, campId) != null;
    }

    /*
     * public boolean checkValidEnquiryId(int enquiryId, int campId) {
     * return findEnquiryById(enquiryId, campId) != null;
     * }
     * 
     * public boolean checkValidSuggestionId(int suggestionId, int campId) {
     * return findSuggestionById(suggestionId, campId) != null;
     * }
     */

}
