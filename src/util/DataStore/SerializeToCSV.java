package util.DataStore;

/**
 * <p>
 * Interface to convert classes to and from a csv line string
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 1-11-2023
 */
public interface SerializeToCSV {
    public String toCSVLine();
    public void fromCSVLine(String CSVLine);
    public int getCSVLineLength();
}
