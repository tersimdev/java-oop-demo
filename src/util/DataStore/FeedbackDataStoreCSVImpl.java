package util.DataStore;

import java.util.ArrayList;
import java.util.Map;

import entity.CampEnquiry;
import entity.CampSuggestion;

/**
 * <p>
 * This class implements datastore using reading and writing to csv file.
 * Database is loaded into memory on init, and saved on cleanup.
 * It stores the following tables: enquiries, suggestions.
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 19-11-2023
 */
public class FeedbackDataStoreCSVImpl extends BaseDataStoreCSV implements FeedbackDataStoreInterface {

    /**
     * A map of table names to csv tables
     */
    private Map<String, CSVTable> tables;

    // file path constants
    private static final String pathSuggestions = "data/camps/suggestions.csv";
    private static final String pathEnquiries = "data/camps/enquiries.csv";

    // table name constants
    private static final String tableSuggestions = "suggestions";
    private static final String tableEnquiries = "enquiries";

    
    /**
     * Function to add mapping of feedback tables.
     */
    @Override
    protected void addToTable(Map<String, CSVTable> tables) {
        tables.put(tableSuggestions, new CSVTable(tableSuggestions,
                pathSuggestions, 0));
        tables.put(tableEnquiries, new CSVTable(tableEnquiries, pathEnquiries, 0));
    }

    @Override
    public void addSuggestion(CampSuggestion suggestion) {
        tables.get(tableSuggestions).addRow(suggestion.toCSVLine());
    }

    @Override
    public void deleteSuggestion(int suggestionId) {
        tables.get(tableSuggestions).deleteRow(0, Integer.toString(suggestionId));

    }

    @Override
    public void updateSuggestion(CampSuggestion suggestion) {
        String row = tables.get(tableSuggestions).queryRow(0, Integer.toString(suggestion.getSuggestionId()));
        tables.get(tableSuggestions).updateRow(row, suggestion.toCSVLine());
    }

    @Override
    public ArrayList<CampSuggestion> getAllSuggestions() {
        ArrayList<CampSuggestion> ret = new ArrayList<>();
        ArrayList<String> data = tables.get(tableSuggestions).getRowData();
        for (String s : data) {
            CampSuggestion tmp = new CampSuggestion();
            tmp.fromCSVLine(s);
            ret.add(tmp);
        }
        return ret;
    }

    @Override
    public void addEnquiry(CampEnquiry enquiry) {
        tables.get(tableEnquiries).addRow(enquiry.toCSVLine());
    }

    @Override
    public void deleteEnquiry(int enquiryId) {
        tables.get(tableEnquiries).deleteRow(0, Integer.toString(enquiryId));
    }

    @Override
    public void updateEnquiry(CampEnquiry enquiry) {
        String row = tables.get(tableEnquiries).queryRow(0, Integer.toString(enquiry.getEnquiryId()));
        tables.get(tableEnquiries).updateRow(row, enquiry.toCSVLine());
    }

    @Override
    public ArrayList<CampEnquiry> getAllEnquiries() {
        ArrayList<CampEnquiry> ret = new ArrayList<>();
        ArrayList<String> data = tables.get(tableEnquiries).getRowData();
        for (String s : data) {
            CampEnquiry tmp = new CampEnquiry();
            tmp.fromCSVLine(s);
            ret.add(tmp);
        }
        return ret;
    }
}
