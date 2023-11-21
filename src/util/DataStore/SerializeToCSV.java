package util.DataStore;

/**
 * <p>
 * Interface to convert classes to and from a csv line string
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 19-11-2023
 */
public interface SerializeToCSV {
    /**
     * Serializes this class into a line of CSV.
     * 
     * @return csv line, e.g. <code>"id,camp name,date"</code>
     */
    public String toCSVLine();

    /**
     * Deserializes CSVLine and sets variables in this object.
     * 
     * @param CSVLine csv line, e.g. <code>"hello,world,"</code>
     */
    public void fromCSVLine(String CSVLine);

    /**
     * Returns the length / number of columns in CSVLine to expect.
     * 
     * @return length of CSV Line.
     */
    public int getCSVLineLength();
}
