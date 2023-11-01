package util.DataStore;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
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
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line != null && !line.isEmpty())
                    result.add(line);
            }
        } catch (IOException e) {
            Log.error("Error parsing file " + path);
        }
        return result;
    }

    @Override
    public <T extends DataStoreItem> void write(String path, ArrayList<T> data) {
        String fileData = "";
        for (T item : data) {
            fileData += item.toCSVLine() + "\n";
        }
        Log.info("Writing to " + path);
        if (fileData.isEmpty()) {
            Log.info("Data is empty, skipping write");
            return;
        }
        Log.info("Data:\n" + fileData);
        // write to csv file
        File csvOutputFile = new File(path);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            pw.write(fileData);
        } catch (IOException e) {
            Log.error("Error writing to file " + path);
        }
    }

    @Override
    public boolean dataExists(String path) {
        File f = new File(path);
        return f.exists() && !f.isDirectory();
    }
}
