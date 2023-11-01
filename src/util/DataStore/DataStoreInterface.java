package util.DataStore;

import java.util.ArrayList;

/**
 * <p>
 * An interface to handle storage
 * Uses generics to handle writing function overloading
 * Read returns the csv strings
 * types
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 1-11-2023
 */
public interface DataStoreInterface {
    public ArrayList<String> read(String path);
    public <T extends DataStoreItem> void write(String path, ArrayList<T> data);
    public boolean dataExists(String path);

}
