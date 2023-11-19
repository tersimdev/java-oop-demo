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
    private Map<String, ArrayList<CampEnquiry>> enquiriesMap;
    private Map<String, ArrayList<CampSuggestion>> suggestionsMap;

    public FeedbackSystem() {
        this.enquiriesMap = new HashMap<>();
        this.suggestionsMap= new HashMap<>();
    }

    public void addCampEnquiry(String campName, CampEnquiry enquiry) {
        enquiriesMap.computeIfAbsent(campName, k -> new ArrayList<>()).add(enquiry);
    }

    public void addCampSuggestion(String campName, CampSuggestion suggestion) {
        suggestionsMap.computeIfAbsent(campName, k -> new ArrayList<>()).add(suggestion);
    }

    public ArrayList<CampEnquiry> getCampEnquiries(String campName){
        return enquiriesMap.getOrDefault(campName, new ArrayList<>());
    }

    public ArrayList<CampSuggestion> getCampSuggestions(String campName){
        return suggestionsMap.getOrDefault(campName, new ArrayList<>());
    }

}
