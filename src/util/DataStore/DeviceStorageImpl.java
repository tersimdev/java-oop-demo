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
    public <T extends DataStoreItem<T>> void read(String path, ArrayList<T> result, T example) {
        result = new ArrayList<>();
        // OPEN csv in path
        // for each line
        String line = "some csv line here,second item";
        T obj = example.makeCopy(); //make instance by cloning an example
        obj.fromCSVLine(line); //read data from csv line
        result.add(obj); //add to result array
    }

    @Override
    public <T extends DataStoreItem<T>> void write(String path, ArrayList<T> data) {
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
