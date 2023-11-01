package control;

import util.Log;
import util.DataStore.DeviceStorageImpl;
import util.DataStore.DataStoreItem;

import java.util.ArrayList;

import entity.User;
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
    public static LoginSystem instance = null;
    private LoginSystem() { init(); }
    public static LoginSystem getInstance() {
        if (instance == null)
            instance = new LoginSystem();
        return instance;
    }

    private ArrayList<Student> studentList;
    private ArrayList<Staff> staffList;
    
    private static final String initialStudentsFile = "data/sample/student_list.csv"; 
    private static final String initialStaffFile = "data/sample/staff_list.csv"; 

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

    public void init() {
        //if data/users/ have no files,
        //call initialize student list and initialize staff list
        //else do nothing
    } 
    /**
     * Heaaders: Name,UserID,Faculty,Password
     */
    private void initializeStudentList() {
        //load initialStudentsFile into data/users/student.csv
        //create Student object from initial file
        //then call toCSV and add to data/users/student.csv
    }
    
    /**
     * Heaaders: Name,UserID,Faculty,Password
     */
    private void initializeStaffList() {
        //load intialStaffFile into data/users/staff.csv
        //create Staff object from initial file
        //then call toCSV and add to data/users/staff.csv
    }

    private boolean checkValidPassword(String password) {
        //todo
        return false;
    }
}
