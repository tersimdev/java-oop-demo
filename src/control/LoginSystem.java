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
    private LoginSystem() {}
    public static LoginSystem getInstance() {
        if (instance == null)
            instance = new LoginSystem();
        return instance;
    }

    private ArrayList<Student> studentList;
    private ArrayList<Staff> staffList;

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

    public void initializeStudenList(String filename) {
        ArrayList<DataStoreItem> data = DeviceStorageImpl.getInstance().read(filename);
        studentList = new ArrayList<>();
        for (DataStoreItem item : data)
            studentList.add(new Student(item.getName(), item.getEmail(), item.getFaculty()));
    }
    
    public void initializeStaffList(String filename) {
        ArrayList<DataStoreItem> data = DeviceStorageImpl.getInstance().read(filename);
        staffList = new ArrayList<>();
        for (DataStoreItem item : data)
            staffList.add(new Staff(item.getName(), item.getEmail(), item.getFaculty()));
    }

    private boolean checkValidPassword(String password) {
        //todo
        return false;
    }
}
