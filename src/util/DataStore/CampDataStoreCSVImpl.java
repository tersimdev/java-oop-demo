package util.DataStore;

import java.util.ArrayList;
import java.util.Map;

import entity.Camp;

/**
 * <p>
 * This class implements datastore using reading and writing to csv file.
 * Database is loaded into memory on init, and saved on cleanup.
 * It stores the following tables: camps
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 19-11-2023
 */
public class CampDataStoreCSVImpl extends BaseDataStoreCSV implements CampDataStoreInterface {
    // file path constants
    private static final String pathCamps = "data/camps/camps.csv";

    // table name constants
    private static final String tableCamps = "camps";

    /**
     * Constructor, calls super()
     * Add mapping of feedback tables.
     */
    public CampDataStoreCSVImpl() {
        super();
        this.tables.put(tableCamps, new CSVTable(tableCamps, pathCamps, 0));
    }

    @Override
    public void addCamp(Camp camp) {
        tables.get(tableCamps).addRow(camp.toCSVLine());
    }

    @Override
    public void deleteCamp(int campId) {
        tables.get(tableCamps).deleteRow(0, Integer.toString(campId));
    }

    @Override
    public void updateCampDetails(Camp camp) {
        String row = tables.get(tableCamps).queryRow(0, Integer.toString(camp.getCampId()));
        tables.get(tableCamps).updateRow(row, camp.toCSVLine());
    }

    @Override
    public ArrayList<Camp> getAllCamps() {
        ArrayList<Camp> ret = new ArrayList<>();
        ArrayList<String> data = tables.get(tableCamps).getRowData();
        for (String s : data) {
            Camp tmp = new Camp();
            tmp.fromCSVLine(s);
            ret.add(tmp);
        }
        return ret;
    }
}
