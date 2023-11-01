package control;

import util.Log;
import util.DataStore.DeviceStorageImpl;
import util.DataStore.DataStoreItem;

import java.util.ArrayList;

import entity.User;

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
    }
    
    public void initializeStaffList(String filename) {
        ArrayList<DataStoreItem> data = DeviceStorageImpl.getInstance().read(filename);
    }

    private boolean checkValidPassword(String password) {
        //todo
        return false;
    }
}
