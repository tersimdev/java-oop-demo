package util.DataStore;

import entity.User;

/**
 * <p>
 * An interface to handle storage queries needed by the app
 * Uses strategy design pattern
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

    public User queryUser(String userID);
    public void updateUser(String userID, String newPassword);

    //todo, similar functiosn for camps etc
}
