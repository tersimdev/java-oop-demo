package control;

import java.util.ArrayList;

import entity.Faculty;
import entity.Staff;
import entity.Student;
import entity.User;
import util.DataStore.DataStoreInterface;
import util.DataStore.DeviceStorageImpl;
import util.Log;

/**
 * <p>
 * A singleton class to handle all datastore operations
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 1-11-2023
 */
public class DataStoreSystem {
    private static DataStoreSystem instance = null;

    private DataStoreSystem() {
        dataStore = DeviceStorageImpl.getInstance(); // can bring this line to a factory if there were more //
                                                     // implementations
        init();
    }

    public static DataStoreSystem getInstance() {
        if (instance == null)
            instance = new DataStoreSystem();
        return instance;
    }

    private DataStoreInterface dataStore = null;
    private ArrayList<User> userList = null;

    private static final String initialStudentsFile = "data/sample/student_list.csv";
    private static final String initialStaffFile = "data/sample/staff_list.csv";
    private static final String staffPath = "data/users/staff.csv";
    private static final String studentsPath = "data/users/student.csv";

    /**
     * Initializes data store
     * Loads data from initial sample csv if needed
     */
    public void init() {
        // if data/users/ have no files,
        // call initialize student list and initialize staff list
        // else load from data/users/

        userList = new ArrayList<>();

        if (!dataStore.dataExists(studentsPath))
            initializeStudentList();
        if (!dataStore.dataExists(staffPath))
            initializeStaffList();

        loadUsers();
    }

    public void cleanup() {

    }

    // query for students with username
    public User queryUsers(String userID) {
        for (User u : userList) {
            if (u.getUserID().equals(userID)) {
                return u;
            }
        }
        return null;
    }

    private void initializeStudentList() {
        // heaaders: Name,Email,Faculty
        // load initialStudentsFile into data/users/student.csv
        // create Student object from initial file
        // then call toCSV and add to data/users/student.csv

        ArrayList<String> initialData = dataStore.read(initialStudentsFile);
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
        dataStore.write(studentsPath, studentList);
    }

    private void initializeStaffList() {

        // heaaders: Name,Email,Faculty
        // load intialStaffFile into data/users/staff.csv
        // create Staff object from initial file
        // then call toCSV and add to data/users/staff.csv

        ArrayList<String> initialData = dataStore.read(initialStaffFile);
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
        dataStore.write(staffPath, staffList);
    }

    private void loadUsers() {
        ArrayList<String> resStudent = dataStore.read(studentsPath);
        for (String s : resStudent) {
            Student student = new Student();
            student.fromCSVLine(s);
            userList.add(student);
        }
        ArrayList<String> resStaff = dataStore.read(staffPath);
        for (String s : resStaff) {
            Staff staff = new Staff();
            staff.fromCSVLine(s);
            userList.add(staff);
        }
    }

}
