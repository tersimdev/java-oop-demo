package control;

import util.Log;
import util.DataStore.DeviceStorageImpl;
import util.DataStore.DataStoreInterface;

import java.util.ArrayList;

import entity.User;
import entity.Faculty;
import entity.Staff;
import entity.Student;

/**
 * <p>
 * A singleton class to handle login logic
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 1-11-2023
 */
public class LoginSystem {
    private static LoginSystem instance = null;

    private LoginSystem() {
        dataStore = DeviceStorageImpl.getInstance(); // can bring this line to a factory if there were more implementations
        init();
    }

    public static LoginSystem getInstance() {
        if (instance == null)
            instance = new LoginSystem();
        return instance;
    }

    private DataStoreInterface dataStore = null;
    private ArrayList<Student> studentList = null;
    private ArrayList<Staff> staffList = null;

    private static final String initialStudentsFile = "data/sample/student_list.csv";
    private static final String initialStaffFile = "data/sample/staff_list.csv";
    private static final String staffPath = "data/users/staff.csv";
    private static final String studentsPath = "data/users/student.csv";

    public void changeUserPassword(User user, String newPassword) {
        String oldPassword = user.getPassword();
        if (oldPassword == newPassword) {
            Log.println("Error! New password same as old password!");
        }
        if (LoginSystem.getInstance().checkValidPassword(newPassword)) {
            user.setPassword(newPassword);
            Log.println("Password changed.");
        }
    }

    /**
     * Initializes data store
     * Loads data from initial sample csv if needed
     */
    public void init() {
        // if data/users/ have no files,
        // call initialize student list and initialize staff list
        // else load from data/users/

        if (!dataStore.dataExists(studentsPath))
            initializeStudentList();
        else
            loadStudents();
        if (!dataStore.dataExists(staffPath))
            initializeStaffList();
        else
            loadStaff();
    }

    private void initializeStudentList() {
        // heaaders: Name,Email,Faculty
        // load initialStudentsFile into data/users/student.csv
        // create Student object from initial file
        // then call toCSV and add to data/users/student.csv

        ArrayList<String> initialData = dataStore.read(initialStudentsFile);
        studentList = new ArrayList<>();

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
        staffList = new ArrayList<>();

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

    private void loadStudents() {
        ArrayList<String> res = dataStore.read(studentsPath);
        studentList = new ArrayList<>();
        for (String s : res) {
            Student student = new Student();
            student.fromCSVLine(s);
            studentList.add(student);
        }
    }

    private void loadStaff() {
        ArrayList<String> res = dataStore.read(staffPath);
        staffList = new ArrayList<>();
        for (String s : res) {
            Staff staff = new Staff();
            staff.fromCSVLine(s);
            staffList.add(staff);
        }
    }

    private boolean checkValidPassword(String password) {
        // todo
        return false;
    }

    public void cleanup() {
        if (staffList != null)
            dataStore.write(staffPath, staffList);
        if (studentList != null)
            dataStore.write(studentsPath, studentList);
    }
}
