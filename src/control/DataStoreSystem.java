package control;

import entity.Staff;
import entity.Student;
import entity.User;
import util.DataStore.DataStoreInterface;
import util.DataStore.DataStoreCSVImpl;

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
        dataStore = new DataStoreCSVImpl();
        init();
    }

    public static DataStoreSystem getInstance() {
        if (instance == null)
            instance = new DataStoreSystem();
        return instance;
    }

    private DataStoreInterface dataStore = null;

    /**
     * Initializes data store
     * Loads data from initial sample csv if needed
     */
    public void init() {
        dataStore.init();
    }

    public void cleanup() {
        dataStore.cleanup();
    }

    // query for users with username
    public User queryUsers(String userID) {
        String row = dataStore.queryRow("students", 1, userID);
        if (row != null) {
            Student ret = new Student();
            ret.fromCSVLine(row);
            return ret;
        }
        row = dataStore.queryRow("staff", 1, userID);
        if (row != null) {
            Staff ret = new Staff();
            ret.fromCSVLine(row);
            return ret;
        }
        return null;
    }

    public void updateUser(String userID, String newPassword) {
        String row = dataStore.queryRow("students", 1, userID);
        if (row != null) {
            Student s = new Student();
            s.fromCSVLine(row);
            s.setPassword(newPassword);
            dataStore.updateRow("students", row, s.toCSVLine());
        }
        row = dataStore.queryRow("staff", 1, userID);
        if (row != null) {
            Staff s = new Staff();
            s.fromCSVLine(row);
            s.setPassword(newPassword);
            dataStore.updateRow("staff", row, s.toCSVLine());
        }
    }


    //todo add functions to update camps, enquiries, feedback
}
