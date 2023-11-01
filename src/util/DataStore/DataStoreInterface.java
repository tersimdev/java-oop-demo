package util.DataStore;

/**
 * <p>
 * An interface to handle storage
 * Has functions to read, update, query data in storage
 * Implementation of actual storage is left to concrete subclasses
 * Assumes relational SQL-like database
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 1-11-2023
 */
public interface DataStoreInterface {
    public void init();
    public void cleanup();
    public boolean dataExists(String table);
    public String queryRow(String table, int keyIndex, String keyValue);
    public void updateRow(String table, String oldRow, String newRow);
}
