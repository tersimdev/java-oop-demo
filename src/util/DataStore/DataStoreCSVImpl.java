package util.DataStore;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
 * @since 1-11-2023
 */
public class DataStoreCSVImpl implements DataStoreInterface {

    private Map<String, CSVTable> tables;

    // file paths
    private static final String initStudents = "data/sample/student_list.csv";
    private static final String initStaff = "data/sample/staff_list.csv";
    private static final String pathStudents = "data/users/students.csv";
    private static final String pathStaff = "data/users/staff.csv";

    // table names
    private static final String tableStudents = "students";
    private static final String tableStaff = "staff";

    @Override
    public void init() {
        // load in initial data
        if (!dataExists(pathStudents))
            initializeStudentList();
        if (!dataExists(pathStaff))
            initializeStaffList();

        // create mapping
        tables = new HashMap<>();
        tables.put(tableStudents, new CSVTable(tableStudents, pathStudents));
        tables.put(tableStaff, new CSVTable(tableStaff, pathStaff));

        // load csvs into memory
        for (CSVTable t : tables.values())
            t.readFromFile();
    }

    @Override
    public void cleanup() {
        for (CSVTable t : tables.values())
            t.writeToFile();
    }

    @Override
    public boolean dataExists(String path) {
        File f = new File(path);
        return f.exists() && !f.isDirectory();
    }

    @Override
    public User queryUser(String userID) {
        String row = tables.get(tableStudents).queryRow(1, userID);
        if (row != null) {
            Student ret = new Student();
            ret.fromCSVLine(row);
            return ret;
        }
        row = tables.get(tableStaff).queryRow(1, userID);
        if (row != null) {
            Staff ret = new Staff();
            ret.fromCSVLine(row);
            return ret;
        }
        return null;
    }

    @Override
    public void updateUser(String userID, String newPassword) {
        String row = tables.get(tableStudents).queryRow(1, userID);
        if (row != null) {
            Student s = new Student();
            s.fromCSVLine(row);
            s.setPassword(newPassword);
            tables.get(tableStudents).updateRow(row, s.toCSVLine());

        }
        row = tables.get(tableStaff).queryRow(1, userID);
        if (row != null) {
            Staff s = new Staff();
            s.fromCSVLine(row);
            s.setPassword(newPassword);
            tables.get(tableStaff).updateRow(row, s.toCSVLine());
        }
    }

    private void initializeStudentList() {
        // heaaders: Name,Email,Faculty
        // load initialStudentsFile into data/users/student.csv
        // create Student object from initial file
        // then call toCSV and add to data/users/student.csv

        CSVTable initTable = new CSVTable("temp", initStudents);
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

        CSVTable initTable = new CSVTable("temp", initStaff);
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
