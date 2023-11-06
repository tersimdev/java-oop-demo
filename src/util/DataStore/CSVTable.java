package util.DataStore;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import util.Log;

public class CSVTable {
    private ArrayList<String> rowData;
    private String path;
    private String tableName;

    public CSVTable(String tableName, String path) {
        this.tableName = tableName;
        this.path = path;
        rowData = null;
    }

    public String getTableName() {
        return tableName;
    }

    public boolean isLoaded() {
        return rowData != null;
    }

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

    public void updateRow(String oldRow, String newRow) {
        if (!isLoaded())
            return;
        for (int i = 0; i < rowData.size(); ++i) {
            if (rowData.get(i).equals(oldRow))
                rowData.set(i, newRow);
        }
    }
}