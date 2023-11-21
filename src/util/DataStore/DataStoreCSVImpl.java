package util.DataStore;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import entity.Camp;
import entity.CampEnquiry;
import entity.CampSuggestion;
import entity.Faculty;
import entity.Staff;
import entity.Student;
import entity.User;
import util.Log;

/**
 * <p>
 * This is a singleton class to handle reading and writing to csv file
 * Database is loaded into memory on init, and saved on cleanup
 * It stores the following tables: students, staff, camps, enquiries,
 * suggestions
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 19-11-2023
 */
public class DataStoreCSVImpl implements DataStoreInterface {

    private Map<String, CSVTable> tables;

    // file paths
    private static final String initStudents = "data/sample/student_list.csv";
    private static final String initStaff = "data/sample/staff_list.csv";
    private static final String pathStudents = "data/users/students.csv";
    private static final String pathStaff = "data/users/staff.csv";
    private static final String pathCamps = "data/camps/camps.csv";
    private static final String pathSuggestions = "data/camps/suggestions.csv";
    private static final String pathEnquiries = "data/camps/enquiries.csv";

    // table names
    private static final String tableStudents = "students";
    private static final String tableStaff = "staff";
    private static final String tableCamps = "camps";
    private static final String tableSuggestions = "suggestions";
    private static final String tableEnquiries = "enquiries";

    @Override
    public void init() {
        // create mapping
        tables = new HashMap<>();
        tables.put(tableStudents, new CSVTable(tableStudents, pathStudents, 1));
        tables.put(tableStaff, new CSVTable(tableStaff, pathStaff, 1));
        tables.put(tableCamps, new CSVTable(tableCamps, pathCamps, 0));
        tables.put(tableSuggestions, new CSVTable(tableSuggestions,
                pathSuggestions, 0));
        tables.put(tableEnquiries, new CSVTable(tableEnquiries, pathEnquiries, 0));

        // load in initial data
        if (!dataExists(pathStudents))
            initializeStudentList();
        if (!dataExists(pathStaff))
            initializeStaffList();

        // load csvs into memory
        for (CSVTable t : tables.values()) {
            if (dataExists(t.getPath()))
                t.readFromFile();
            else
                // create the file by writing empty list
                t.writeToFile(new ArrayList<>());
        }
    }

    @Override
    public void cleanup() {
        Log.info("Saving all data to CSVs");
        for (CSVTable t : tables.values()) {
            // should sort by id just incase
            t.sortRows();
            t.writeToFile();
        }
    }

    @Override
    public boolean dataExists(String path) {
        File f = new File(path);
        return f.exists() && !f.isDirectory();
    }

    @Override
    public User queryStudent(String userID) {
        String row = tables.get(tableStudents).queryRow(1, userID);
        if (row != null) {
            Student ret = new Student();
            ret.fromCSVLine(row);
            return ret;
        }
        return null;
    }

    @Override
    public User queryStaff(String userID) {
        String row = tables.get(tableStaff).queryRow(1, userID);
        if (row != null) {
            Staff ret = new Staff();
            ret.fromCSVLine(row);
            return ret;
        }
        return null;
    }

    @Override
    public void updateUserPassword(String userID, String newPassword) {
        String row = tables.get(tableStudents).queryRow(1, userID);
        if (row != null) {
            Student s = new Student();
            s.fromCSVLine(row);
            s.setPassword(newPassword);
            tables.get(tableStudents).updateRow(row, s.toCSVLine());
            tables.get(tableStudents).writeToFile(); // for immediate feedback
        }
        row = tables.get(tableStaff).queryRow(1, userID);
        if (row != null) {
            Staff s = new Staff();
            s.fromCSVLine(row);
            s.setPassword(newPassword);
            tables.get(tableStaff).updateRow(row, s.toCSVLine());
            tables.get(tableStaff).writeToFile(); // for immediate feedback
        }
    }

    @Override
    public void addCamp(Camp camp) {
        tables.get(tableCamps).addRow(camp.toCSVLine());
    }

    @Override
    public void deleteCamp(int campId) {
        tables.get(tableCamps).deleteRow(0, Integer.toString(campId));
    }

    @Override
    public void updateCampDetails(Camp camp) {
        String row = tables.get(tableCamps).queryRow(0, Integer.toString(camp.getCampId()));
        tables.get(tableCamps).updateRow(row, camp.toCSVLine());
    }

    @Override
    public ArrayList<Camp> getAllCamps() {
        ArrayList<Camp> ret = new ArrayList<>();
        ArrayList<String> data = tables.get(tableCamps).getRowData();
        for (String s : data) {
            Camp tmp = new Camp();
            tmp.fromCSVLine(s);
            ret.add(tmp);
        }
        return ret;
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

    private void initializeStudentList() {
        // heaaders: Name,Email,Faculty
        // load initialStudentsFile into data/users/student.csv
        // create Student object from initial file
        // then call toCSV and add to data/users/student.csv

        CSVTable initTable = new CSVTable("temp", initStudents, 1);
        ArrayList<String> initialData = initTable.readFromFile();
        ArrayList<Student> studentList = new ArrayList<>();

        // convert initial data format to student objects, skip 1st line (header)
        for (int i = 1; i < initialData.size(); ++i) {
            String[] split = initialData.get(i).split(",");
            if (split.length != 3) {
                Log.error("Error intialising student list");
                continue;
            }
            try {
                String displayName = split[0].trim();
                String userID = split[1].trim().split("@")[0].toUpperCase();
                Faculty faculty = Faculty.valueOf(split[2].trim());
                Student student = new Student(displayName, userID, faculty);
                studentList.add(student);
            } catch (IllegalArgumentException e) {
                Log.error("Error parsing faculty from initial data: " + split[2]);
                continue;
            }
        }

        // then write to proper data store
        tables.get(tableStudents).writeToFile(studentList);
    }

    private void initializeStaffList() {

        // heaaders: Name,Email,Faculty
        // load intialStaffFile into data/users/staff.csv
        // create Staff object from initial file
        // then call toCSV and add to data/users/staff.csv

        CSVTable initTable = new CSVTable("temp", initStaff, 1);
        ArrayList<String> initialData = initTable.readFromFile();
        ArrayList<Staff> staffList = new ArrayList<>();

        // convert initial data format to student objects
        for (int i = 1; i < initialData.size(); ++i) {
            String[] split = initialData.get(i).split(",");
            if (split.length != 3) {
                Log.error("Error intialising staff list");
                continue;
            }
            try {
                String displayName = split[0].trim();
                String userID = split[1].trim().split("@")[0].toUpperCase();
                Faculty faculty = Faculty.valueOf(split[2].trim());
                Staff staff = new Staff(displayName, userID, faculty);
                staffList.add(staff);
            } catch (IllegalArgumentException e) {
                Log.error("Error parsing faculty from initial data: " + split[2]);
                continue;
            }
        }

        // then write to proper data store
        tables.get(tableStaff).writeToFile(staffList);

    }

}
