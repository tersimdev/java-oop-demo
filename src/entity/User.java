package entity;

import control.LoginSystem;
import util.Log;
import util.DataStore.DataStoreItem;

/**
 * <p>
 * This is an entity class to represent a user
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 1-11-2023
 */
public class User implements DataStoreItem{
    private String displayName; //not mentioned in docs but makes sense to have
    private String userID;
    private String password;
    private Faculty faculty;

    public User(String displayName, String userID, Faculty faculty) {
        this.displayName = displayName;
        this.userID = userID;
        this.faculty = faculty;
        this.password = "password";
    }

    public String getDisplayName() { return displayName; }
    public String getUserID() { return userID; }
    public String getPassword() { return password; }
    public Faculty geFaculty() { return faculty; }

    public void setPassword(String password) {this.password = password;}

    @Override
    public String toCSVLine() {
        
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toCSVLine'");
    }

    @Override
    public void fromCSVLine(String CSVLine) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromCSVLine'");
    }
}
