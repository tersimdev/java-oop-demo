package util.DataStore;

import java.util.ArrayList;

/**
 * <p>
 * This is a singleton class to handle reading and writing to file
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 1-11-2023
 */
public class DeviceStorageImpl implements DataStoreInterface {
    public static DeviceStorageImpl instance = null;
    private DeviceStorageImpl() {}
    public static DeviceStorageImpl getInstance() {
        if (instance == null)
            instance = new DeviceStorageImpl();
        return instance;
    }
    @Override
    public ArrayList<DataStoreItem> read(String path) {
        // TODO Auto-generated method stub
        // TODO read each csv line into a datastore item
        throw new UnsupportedOperationException("Unimplemented method 'read'");
    }
    @Override
    public void write(String path, ArrayList<DataStoreItem> data) {
        // TODO Auto-generated method stub
        // TODO
        throw new UnsupportedOperationException("Unimplemented method 'write'");
    }
}
