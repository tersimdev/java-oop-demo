package util.DataStore;

import java.util.ArrayList;

/**
 * <p>
 * An interface to handle storage
 * Uses generics to handle conversion from list of super types to list of sub
 * types
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 1-11-2023
 */
public interface DataStoreInterface {
    public <T extends DataStoreItem<T>> void read(String path, ArrayList<T> result, T example);
    public <T extends DataStoreItem<T>> void write(String path, ArrayList<T> data);

}
