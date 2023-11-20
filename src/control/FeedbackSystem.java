package control;

import entity.Camp;
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

    private int nextEnquiryId;
    private int nextSuggetionId;

    public FeedbackSystem(DataStoreSystem dataStoreSystem) {
        this.enquiriesMap = new HashMap<>();
        this.suggestionsMap = new HashMap<>();
        this.dataStoreSystem = dataStoreSystem;

        ArrayList<CampEnquiry> enquiryList = dataStoreSystem.getAllEnquiries();
        ArrayList<CampSuggestion> suggestionList = dataStoreSystem.getAllSuggestions();

        // cld use these as next id, store in system?
        nextEnquiryId = 0;
        nextSuggetionId = 0;
        if (enquiryList.size() > 0)
            nextEnquiryId = enquiryList.get(enquiryList.size() - 1).getEnquiryId() + 1;
        if (suggestionList.size() > 0)
            nextSuggetionId = suggestionList.get(enquiryList.size() - 1).getSuggestionId() + 1;

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
        int enquiryId = nextEnquiryId++;
        enquiry.setEnquiryId(enquiryId);
        // Add the new enquiry to the ArrayList
        enquiries.add(enquiry);
        dataStoreSystem.addEnquiry(enquiry);
    }

    public void addCampSuggestion(int campId, CampSuggestion suggestion) {
        ArrayList<CampSuggestion> suggestions = suggestionsMap.computeIfAbsent(campId, k -> new ArrayList<>());
        // Index in the ArrayList used as the suggestionID
        int suggestionId = nextSuggetionId++;
        suggestion.setSuggestionId(suggestionId);
        // Add the new suggestion to the ArrayList
        suggestions.add(suggestion);
        dataStoreSystem.addSuggestion(suggestion);
    }

    public boolean editCampEnquiry(int campId, int enquiryId, String newEnquiry) {
        CampEnquiry campEnquiry = findEnquiryById(enquiryId, campId);
        if (campEnquiry != null) {
            campEnquiry.setEnquiry(newEnquiry);
            return true;
        }
        return false;
    }

    public boolean editCampSuggestion(int campId, int suggestionId, String newSuggestion) {
        CampSuggestion campSuggestion = findSuggestionById(suggestionId, campId);
        if (campSuggestion != null) {
            campSuggestion.setSuggestion(newSuggestion);
            return true;
        }
        return false;
    }

    public boolean removeCampEnquiry(int campId, int enquiryId) {
        CampEnquiry campEnquiry = findEnquiryById(enquiryId, campId);
        if (campEnquiry != null) {
            getCampEnquiries(campId).remove(campEnquiry);
            return true;
        }
        return false;
    }

    public boolean removeCampSuggestion(int campId, int suggestionId) {
        CampSuggestion campSuggestion = findSuggestionById(suggestionId, campId);
        if (campSuggestion != null) {
            getCampSuggestions(campId).remove(campSuggestion);
            return true;
        }
        return false;
    }

    public boolean processCampEnquiry(String commMemberId, int campId, int enquiryId, String reply) {
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
    }

    public ArrayList<CampEnquiry> getCampEnquiries(int campId) {
        return enquiriesMap.getOrDefault(campId, new ArrayList<>());
    }

    public ArrayList<CampSuggestion> getCampSuggestions(int campId) {
        return suggestionsMap.getOrDefault(campId, new ArrayList<>());
    }

    public CampEnquiry findEnquiryById(int enquiryId, int campId) {
        for (CampEnquiry ce : getCampEnquiries(campId)) {
            if (ce.getEnquiryId() == enquiryId)
                return ce;
        }
        return null;
    }

    public boolean checkValidEnquiryId(int enquiryId, int campId) {
        return findEnquiryById(enquiryId, campId) != null;
    }

    public CampSuggestion findSuggestionById(int enquiryId, int campId) {
        for (CampSuggestion cs : getCampSuggestions(campId)) {
            if (cs.getSuggestionId() == enquiryId)
                return cs;
        }
        return null;
    }

    public boolean checkValidSuggestionId(int suggestionId, int campId) {
        return findSuggestionById(suggestionId, campId) != null;
    }

}
