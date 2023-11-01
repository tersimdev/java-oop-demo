package util.DataStore;

import java.util.ArrayList;

import util.Log;

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
    private static DeviceStorageImpl instance = null;

    private DeviceStorageImpl() {
    }

    public static DeviceStorageImpl getInstance() {
        if (instance == null)
            instance = new DeviceStorageImpl();
        return instance;
    }

    @Override
    public ArrayList<String> read(String path) {
        ArrayList<String> result = new ArrayList<>();
        // OPEN csv in path
        // for each line
        {
            String line = "some csv line here,second item";
            result.add(line); // add to result array
        }
        return result;
    }

    @Override
    public <T extends DataStoreItem> void write(String path, ArrayList<T> data) {
        // open csv in path
        String fileData = "";
        for (T item : data) {
            fileData += item.toCSVLine();
        }
        Log.info("Writing to " + path);
        Log.info("Data:\n" + fileData);
        // write to csv file
    }
}
