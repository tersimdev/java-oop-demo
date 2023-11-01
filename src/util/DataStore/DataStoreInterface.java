package util.DataStore;

import java.util.ArrayList;

/**
 * <p>
 * An interface to handle storage
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 1-11-2023
 */
public interface DataStoreInterface {
    public ArrayList<DataStoreItem> read(String path);
    public void write(String path, ArrayList<DataStoreItem> data);
}
