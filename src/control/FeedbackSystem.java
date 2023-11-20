package control;

import entity.CampEnquiry;
import entity.CampSuggestion;

import java.util.ArrayList;
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
    private DataStoreSystem dataStoreSystem;
    private Map<Integer, ArrayList<CampEnquiry>> enquiriesMap;
    private Map<Integer, ArrayList<CampSuggestion>> suggestionsMap;

    public FeedbackSystem(DataStoreSystem dataStoreSystem) {
        this.enquiriesMap = new HashMap<>();
        this.suggestionsMap = new HashMap<>();
        this.dataStoreSystem = dataStoreSystem;

        ArrayList<CampEnquiry> enquiryList = dataStoreSystem.getAllEnquiries();
        ArrayList<CampSuggestion> suggestionList = dataStoreSystem.getAllSuggestions();
        int numEnquiries = enquiryList.get(enquiryList.size() - 1).getEnquiryId() + 1;
        int numSuggestions = suggestionList.get(enquiryList.size() - 1).getSuggestionId() + 1;

        // add to map based on camp id
        for (CampEnquiry ce : enquiryList) {
            if (enquiriesMap.containsKey(ce.getCampId()))
                enquiriesMap.put(ce.getCampId(), new ArrayList<>());
            enquiriesMap.get(ce.getCampId()).add(ce);
        }
        for (CampSuggestion cs : suggestionList) {
            if (suggestionsMap.containsKey(cs.getCampId()))
                suggestionsMap.put(cs.getCampId(), new ArrayList<>());
            suggestionsMap.get(cs.getCampId()).add(cs);
        }
    }

    public void addCampEnquiry(int campId, CampEnquiry enquiry) {
        ArrayList<CampEnquiry> enquiries = enquiriesMap.computeIfAbsent(campId, k -> new ArrayList<>());
        // Index in the ArrayList used as enquiryID
        int enquiryId = enquiries.size();
        enquiry.setEnquiryId(enquiryId);
        // Add the new enquiry to the ArrayList
        enquiries.add(enquiry);
        dataStoreSystem.addEnquiry(enquiry);
    }

    public void addCampSuggestion(int campId, CampSuggestion suggestion) {
        ArrayList<CampSuggestion> suggestions = suggestionsMap.computeIfAbsent(campId, k -> new ArrayList<>());
        // Index in the ArrayList used as the suggestionID
        int suggestionId = suggestions.size();
        suggestion.setSuggestionId(suggestionId);
        // Add the new suggestion to the ArrayList
        suggestions.add(suggestion);
        dataStoreSystem.addSuggestion(suggestion);
    }

    public boolean editCampEnquiry(int campId, int enquiryId, String newEnquiry) {
        ArrayList<CampEnquiry> enquiries = enquiriesMap.get(campId);
        if (enquiries != null && enquiryId >= 0 && enquiryId < enquiries.size()) {
            CampEnquiry campEnquiry = enquiries.get(enquiryId);
            campEnquiry.setEnquiry(newEnquiry);
            return true;
        }
        return false;
    }

    public boolean editCampSuggestion(int campId, int suggestionId, String newSuggestion) {
        ArrayList<CampSuggestion> suggestions = suggestionsMap.get(campId);
        if (suggestions != null && suggestionId >= 0 && suggestionId < suggestions.size()) {
            CampSuggestion campSuggestion = suggestions.get(suggestionId);
            campSuggestion.setSuggestion(newSuggestion);
            return true;
        }
        return false;
    }

    public boolean removeCampEnquiry(int campId, int enquiryId) {
        ArrayList<CampEnquiry> enquiries = enquiriesMap.get(campId);
        if (enquiries != null && enquiryId >= 0 && enquiryId < enquiries.size()) {
            enquiries.remove(enquiryId);
            // Update enquiry IDs after removal to maintain continuous sequence
            updateEnquiryIds(enquiries);
            return true;
        }
        return false;
    }

    public boolean removeCampSuggestion(int campId, int suggestionId) {
        ArrayList<CampSuggestion> suggestions = suggestionsMap.get(campId);
        if (suggestions != null && suggestionId >= 0 && suggestionId < suggestions.size()) {
            suggestions.remove(suggestionId);
            // Update suggestion IDs after removal to maintain continuous sequence
            updateSuggestionIds(suggestions);
            return true;
        }
        return false;
    }

    public boolean processCampEnquiry(String commMemberId, int campId, int enquiryId, String reply) {
        ArrayList<CampEnquiry> enquiries = enquiriesMap.get(campId);
        if (enquiries != null && enquiryId >= 0 && enquiryId < enquiries.size()) {
            CampEnquiry campEnquiry = enquiries.get(enquiryId);
            campEnquiry.reply(commMemberId, reply);
            return true;
        }
        return false;
    }

    public boolean processCampSuggestion(String staffId, int campId, int suggestionId, boolean decision) {
        ArrayList<CampSuggestion> suggestions = suggestionsMap.get(campId);
        if (suggestions != null && suggestionId >= 0 && suggestionId < suggestions.size()) {
            CampSuggestion campSuggestion = suggestions.get(suggestionId);
            campSuggestion.setApproval(staffId, decision);
            return true;
        }
        return false;
    }

    public ArrayList<CampEnquiry> getCampEnquiries(int campId) {
        return enquiriesMap.getOrDefault(campId, new ArrayList<>());
    }

    public ArrayList<CampSuggestion> getCampSuggestions(int campId) {
        return suggestionsMap.getOrDefault(campId, new ArrayList<>());
    }

    private void updateEnquiryIds(ArrayList<CampEnquiry> enquiries) {
        for (int i = 0; i < enquiries.size(); i++) {
            enquiries.get(i).setEnquiryId(i);
        }
    }

    private void updateSuggestionIds(ArrayList<CampSuggestion> suggestions) {
        for (int i = 0; i < suggestions.size(); i++) {
            suggestions.get(i).setSuggestionId(i);
        }
    }

}
