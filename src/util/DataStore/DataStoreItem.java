package util.DataStore;

/**
 * <p>
 * A data store item stored in our data store
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 1-11-2023
 */
public class DataStoreItem {
    private String name;
    private String email;
    private String faculty;

    public void DataStoreInterface(String name, String email, String faculty) {
        this.name = name;
        this.email = email;
        this.faculty = faculty;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getFaculty() { return faculty; }
}
