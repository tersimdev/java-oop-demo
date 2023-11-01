package util.DataStore;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import entity.Faculty;
import entity.Staff;
import entity.Student;
import util.Log;

/**
 * <p>
 * This is a singleton class to handle reading and writing to csv file
 * Database is loaded into memory on init, and saved on cleanup
 * It stores the following tables: students, staff, camps, enquiries, suggestions
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 1-11-2023
 */
public class DataStoreCSVImpl implements DataStoreInterface {

    private Map<String, ArrayList<String>> tables;

    private static final String initialStudentsFile = "data/sample/student_list.csv";
    private static final String initialStaffFile = "data/sample/staff_list.csv";
    private static final String studentsPath = "data/users/students.csv";
    private static final String staffPath = "data/users/staff.csv";

    @Override
    public void init() {
        tables = new HashMap<>();

        // create database from initial data
        if (!dataExists(studentsPath))
            initializeStudentList();
        if (!dataExists(staffPath))
            initializeStaffList();

        // load csvs into memory
        tables.put("students", readCSV(studentsPath));
        tables.put("staff", readCSV(staffPath));
    }

    @Override
    public void cleanup() {
        writeLoadedCSV(studentsPath, tables.get("students"));
        writeLoadedCSV(staffPath, tables.get("staff"));
    }

    @Override
    public boolean dataExists(String path) {
        File f = new File(path);
        return f.exists() && !f.isDirectory();
    }

    @Override
    public String queryRow(String table, int keyIndex, String keyValue) {
        ArrayList<String> rows = tables.get(table);
        for (int i = 0; i < rows.size(); ++i) {
            try {
            String[] split = rows.get(i).split(",");
            if (split[keyIndex].equals(keyValue))
                return rows.get(i);
            } catch (ArrayIndexOutOfBoundsException e) {
                Log.error("Error querying for key");
            }
        }
        return null;
    }

    @Override
    public void updateRow(String table, String oldRow, String newRow) {
        ArrayList<String> rows = tables.get(table);
        for (int i = 0; i < rows.size(); ++i) {
            if (rows.get(i).equals(oldRow))
                rows.set(i, newRow);
        }
    }

    private ArrayList<String> readCSV(String path) {
        Log.info("Reading data from " + path);
        ArrayList<String> ret = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                if (line != null && !line.isEmpty()) {
                    ret.add(line);
                }
            }
        } catch (IOException e) {
            Log.error("Error parsing file " + path);
        }
        return ret;
    }

    // use generic for easy writing
    private <T extends SerializeToCSV> void writeCSV(String path, ArrayList<T> data) {
        Log.info("Writing data to " + path);
        // write to csv file
        try (FileWriter fw = new FileWriter(path)) {
            for (T obj : data)
                fw.write(obj.toCSVLine() + "\n");
        } catch (IOException e) {
            Log.error("Error adding rows to " + path);
        }
    }

    private void writeLoadedCSV(String path, ArrayList<String> data) {
        Log.info("Writing data to " + path);
        // write to csv file
        try (FileWriter fw = new FileWriter(path)) {
            for (String s : data)
                fw.write(s + "\n");
        } catch (IOException e) {
            Log.error("Error adding rows to " + path);
        }
    }

    private void initializeStudentList() {
        // heaaders: Name,Email,Faculty
        // load initialStudentsFile into data/users/student.csv
        // create Student object from initial file
        // then call toCSV and add to data/users/student.csv

        ArrayList<String> initialData = readCSV(initialStudentsFile);
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
        writeCSV(studentsPath, studentList);
    }

    private void initializeStaffList() {

        // heaaders: Name,Email,Faculty
        // load intialStaffFile into data/users/staff.csv
        // create Staff object from initial file
        // then call toCSV and add to data/users/staff.csv

        ArrayList<String> initialData = readCSV(initialStaffFile);
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
        writeCSV(staffPath, staffList);
    }
}
