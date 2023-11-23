package util.DataStore;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import util.Log;

/**
 * <p>
 * This abstract class defines methods common to DataStoreCSVImpl.
 * Specifically, it provides helpers to maintain a hasmap of
 * <code>CSVTables</code>
 * Database is loaded into memory on init, and saved on cleanup.
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 23-11-2023
 */
public abstract class BaseDataStoreCSV {

    /**
     * A map of table names to csv tables
     */
    protected Map<String, CSVTable> tables;

    /**
     * Initializes the tables hashmap.
     */
    public BaseDataStoreCSV() {
        tables = new HashMap<>();
    }

    /**
     * Initialises the csv tables.
     * Creates the file if they dont exist, else reads in data.
     * Override this if you need to init addtional things, but
     * ensure to call super.init().
     */
    public void init() {
        // load csvs into memory
        for (CSVTable t : tables.values()) {
            if (dataExists(t.getPath()))
                t.readFromFile();
            else
                // create the file by writing empty list
                t.writeToFile(new ArrayList<>());
        }
    }

    /**
     * Writes all data in memory back to csv files.
     * Unlikely that you need to override,
     * but if yes ensure to do super.cleanup().
     */
    public void cleanup() {
        Log.info("Saving all data to CSVs");
        for (CSVTable t : tables.values()) {
            // should sort by id just incase
            t.sortRows();
            t.writeToFile();
        }
    }

    /**
     * Checks if csv file exists at path.
     * 
     * @param path filepath to csv file
     * @return true if file exists
     */
    protected boolean dataExists(String path) {
        File f = new File(path);
        return f.exists() && !f.isDirectory();
    }
}
