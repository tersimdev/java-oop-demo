package control;

import entity.CampEnquiry;
import entity.CampSuggestion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * <p>
 * A singleton class to store enquiries and suggestions 
 * </p>
 * 
 * @author Yen Zhi Wei
 * @version 1.0
 * @since 1-11-2023
 */
public class FeedbackSystem {
    private Map<Integer, List<CampEnquiry>> enquiriesMap;
    private Map<Integer, List<CampSuggestion>> suggestionsMap;
    /*private ArrayList<CampEnquiry> campEnquiries;
    private ArrayList<CampSuggestion> campSuggestions;*/

    private static FeedbackSystem instance = null;

    public FeedbackSystem() {
        this.enquiriesMap = new HashMap<>();
        this.suggestionsMap= new HashMap<>();
    }

    public static FeedbackSystem getInstance() {
        if (instance == null)
            instance = new FeedbackSystem();
        return instance;
    }

    public void addCampEnquiry(int campId, CampEnquiry enquiry) {
        enquiriesMap.computeIfAbsent(campId, k -> new ArrayList<>()).add(enquiry);
    }

    public void addCampSuggestion(int campId, CampSuggestion suggestion) {
        suggestionsMap.computeIfAbsent(campId, k -> new ArrayList<>()).add(suggestion);
    }

    public List<CampEnquiry> getCampEnquiries(int campId){
        return enquiriesMap.getOrDefault(campId, new ArrayList<>());
    }

    public List<CampSuggestion> getCampSuggestions(int campId){
        return suggestionsMap.getOrDefault(campId, new ArrayList<>());
    }

}
