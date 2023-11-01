package control;

/**
 * <p>
 * A singleton class to handle login logic 
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 1-11-2023
 */
public class LoginSystem {
    public static LoginSystem instance = null;
    private LoginSystem() {}
    public static LoginSystem getInstance() {
        if (instance == null)
            instance = new LoginSystem();
        return instance;
    }

    public boolean checkValidPassword(String password) {
        //todo
        return false;
    }

    public void initializeStudenList(String filename) {
        //todo
    }
    
    public void initializeStaffList(String filename) {
        //todo
    }
}
