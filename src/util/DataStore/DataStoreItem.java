package util.DataStore;

/**
 * <p>
 * Interface to convert classes to csv line
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 1-11-2023
 */
public interface DataStoreItem<T> {
    public String toCSVLine();
    public void fromCSVLine(String CSVLine);
    public T makeCopy(); //used to make new instance of T
}
