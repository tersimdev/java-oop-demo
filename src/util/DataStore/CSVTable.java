package util.DataStore;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import util.Log;

/**
 * <p>
 * This is a class to represent a csv file as
 * an array of strings, rowData
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 19-11-2023
 */
public class CSVTable {
    /**
     * Defines the column index of the table key
     */
    private int idIndex;
    /**
     * Stores the CSV table as arrays of CSV lines
     */
    private ArrayList<String> rowData;
    /**
     * File path where this table is stored.
     */
    private String path;
    /**
     * Name of table for referencing.
     */
    private String tableName;

    /**
     * Creates a bare CSVTable, initializes rowData to null.
     * 
     * @param tableName name of table, arbitary
     * @param path      filepath to write/read
     * @param idIndex   column index of key
     */
    public CSVTable(String tableName, String path, int idIndex) {
        this.tableName = tableName;
        this.path = path;
        this.idIndex = idIndex;
        rowData = null;
    }

    /**
     * Gets table name
     * 
     * @return table name
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Gets file path
     * 
     * @return file path
     */
    public String getPath() {
        return path;
    }

    /**
     * Gets row data
     * 
     * @return row data
     */
    public ArrayList<String> getRowData() {
        return rowData;
    }

    /**
     * Gets key index
     * 
     * @return key index
     */
    public int getIdIndex() {
        return idIndex;
    }

    /**
     * Checks if row data is null
     * 
     * @return loaded if rowdata is not null
     */
    public boolean isLoaded() {
        return rowData != null;
    }

    /**
     * Writes rowData to file located at path.
     */
    public void writeToFile() {
        if (!isLoaded())
            return;
        // write to csv file
        try (FileWriter fw = new FileWriter(path)) {
            for (String s : rowData)
                fw.write(s + "\n");
        } catch (IOException e) {
            Log.error("Error adding rows to " + path);
        }
    }

    /**
     * Writes given list of <code>SerializeToCSV</code> interfaces to file.
     * 
     * @param <T>     A concrete class implementing <code>SerializeToCSV</code>
     * @param objData list of T objects
     */
    public <T extends SerializeToCSV> void writeToFile(ArrayList<T> objData) {
        Log.info("Writing data to " + path);
        // write to csv file
        try (FileWriter fw = new FileWriter(path)) {
            for (T obj : objData)
                fw.write(obj.toCSVLine() + "\n");
        } catch (IOException e) {
            Log.error("Error adding rows to " + path);
        }
    }

    /**
     * Reads a file from path.
     * Stores data as list of CSV lines.
     * 
     * @return array list of CSV line stringss
     */
    public ArrayList<String> readFromFile() {
        Log.info("Reading data from " + path);
        rowData = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                if (line != null && !line.isEmpty()) {
                    rowData.add(line);
                }
            }
        } catch (IOException e) {
            Log.error("Error parsing file " + path);
        }
        return rowData;
    }

    /**
     * Looks for a specific CSV Line based on a given column index and matching value
     * @param keyIndex index to check
     * @param keyValue value to match
     * @return CSV Line if found, else null
     */

    public String queryRow(int keyIndex, String keyValue) {
        if (!isLoaded())
            return null;
        for (int i = 0; i < rowData.size(); ++i) {
            try {
                String[] split = rowData.get(i).split(",");
                if (split[keyIndex].equals(keyValue))
                    return rowData.get(i);
            } catch (ArrayIndexOutOfBoundsException e) {
                Log.error("Error querying for key");
            }
        }
        return null;
    }

    /**
     * Looks for oldRow, and replaces it with newRow.
     * @param oldRow old csv line
     * @param newRow new csv line
     */
    public void updateRow(String oldRow, String newRow) {
        if (!isLoaded())
            return;
        Log.info("updating row: " + newRow);
        for (int i = 0; i < rowData.size(); ++i) {
            if (rowData.get(i).equals(oldRow))
                rowData.set(i, newRow);
        }
    }

    /**
     * Appends a row to rowData. 
     * Assumes row given is valid.
     * @param row csv line to add
     */
    public void addRow(String row) {
        if (!isLoaded())
            return;
        Log.info("adding row: " + row);
        rowData.add(row);
    }

    /**
     * Searches for row to delete based on given column index and value.
     * @param keyIndex index to search
     * @param keyValue value to match
     */
    public void deleteRow(int keyIndex, String keyValue) {
        if (!isLoaded())
            return;
        Log.info("deleting row of key: " + keyValue);
        for (int i = 0; i < rowData.size(); ++i) {
            try {
                String[] split = rowData.get(i).split(",");
                if (split[keyIndex].equals(keyValue)) {
                    // delete this row
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                Log.error("Error querying for key");
            }
        }
    }

    /**
     * Sorts rowData based on <code>idIndex</code>. 
     */
    public void sortRows() {
        rowData.sort((o1, o2) -> {
            String id1, id2;
            id1 = o1.split(",")[idIndex];
            id2 = o2.split(",")[idIndex];
            return id1.compareTo(id2);
        });
    }
}
