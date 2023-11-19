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
        ArrayList<CampEnquiry> enquiries = enquiriesMap.computeIfAbsent(campName, k -> new ArrayList<>());
        // Index in the ArrayList used as enquiryID
        int enquiryId = enquiries.size();
        enquiry.setEnquiryId(enquiryId);
        // Add the new enquiry to the ArrayList
        enquiries.add(enquiry);
    }

    public void addCampSuggestion(String campName, CampSuggestion suggestion) {
        ArrayList<CampSuggestion> suggestions = suggestionsMap.computeIfAbsent(campName, k -> new ArrayList<>());
        //Index in the ArrayList used as the suggestionID
        int suggestionId = suggestions.size();
        suggestion.setSuggestionId(suggestionId);
        // Add the new suggestion to the ArrayList
        suggestions.add(suggestion);
    }

    public boolean removeCampEnquiryByID(String campName, int enquiryId) {
        ArrayList<CampEnquiry> enquiries = enquiriesMap.get(campName);
        if (enquiries != null && enquiryId >= 0 && enquiryId < enquiries.size()) {
            enquiries.remove(enquiryId);
            // Update enquiry IDs after removal to maintain continuous sequence
            updateEnquiryIds(enquiries);
            return true;
        }
        return false;
    }

    public ArrayList<CampEnquiry> getCampEnquiries(String campName){
        return enquiriesMap.getOrDefault(campName, new ArrayList<>());
    }

    public ArrayList<CampSuggestion> getCampSuggestions(String campName){
        return suggestionsMap.getOrDefault(campName, new ArrayList<>());
    }

    private void updateEnquiryIds(ArrayList<CampEnquiry> enquiries) {
        for (int i = 0; i < enquiries.size(); i++) {
            enquiries.get(i).setEnquiryId(i);
        }
    }

}
