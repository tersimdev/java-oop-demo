package util.DataStore;

import java.util.ArrayList;

import entity.CampCommitteeMember;
import entity.Faculty;
import entity.Staff;
import entity.Student;
import entity.User;
import util.Log;

/**
 * <p>
 * This class implements datastore using reading and writing to csv file.
 * Database is loaded into memory on init, and saved on cleanup.
 * It stores the following tables: students, staff
 * </p>
 * 
 * @author Sim Yi Wan Terence
 * @version 1.0
 * @since 19-11-2023
 */
public class UserDataStoreCSVImpl extends BaseDataStoreCSV implements UserDataStoreInterface {

    /**
     * file path constants
     */
    private static final String initStudents = "data/sample/student_list.csv";

    /**
     * file path constants
     */
    private static final String initStaff = "data/sample/staff_list.csv";

    /**
     * file path constants
     */
    private static final String pathStudents = "data/users/students.csv";

    /**
     * file path constants
     */
    private static final String pathStaff = "data/users/staff.csv";

    /**
     * table name constants
     */
    private static final String tableStudents = "students";
    /**
     * table name constants
     */
    private static final String tableStaff = "staff";

    /**
     * Constructor, calls super().
     * Add mapping of user tables.
     */
    public UserDataStoreCSVImpl() {
        super();
        this.tables.put(tableStudents, new CSVTable(tableStudents, pathStudents, 1));
        this.tables.put(tableStaff, new CSVTable(tableStaff, pathStaff, 1));
    }

    /**
     * Students and Staff are loaded from initial sample file
     * if they dont exist in data folder.
     */
    @Override
    public void init() {
        // load in initial data
        if (!dataExists(pathStudents))
            initializeStudentList();
        if (!dataExists(pathStaff))
            initializeStaffList();

        super.init();
    }

    @Override
    public User queryStudent(String userID) {
        String row = tables.get(tableStudents).queryRow(1, userID);
        if (row != null) {
            Student ret = new Student();
            ret.fromCSVLine(row);
            return ret;
        }
        return null;
    }

    @Override
    public User queryStaff(String userID) {
        String row = tables.get(tableStaff).queryRow(1, userID);
        if (row != null) {
            Staff ret = new Staff();
            ret.fromCSVLine(row);
            return ret;
        }
        return null;
    }

    @Override
    public void updateUserPassword(String userID, String newPassword) {
        String row = tables.get(tableStudents).queryRow(1, userID);
        if (row != null) {
            Log.info("updating student password");
            Student s = new Student();
            s.fromCSVLine(row);
            s.setPassword(newPassword);
            tables.get(tableStudents).updateRow(row, s.toCSVLine());
            tables.get(tableStudents).writeToFile(); // for immediate feedback
        }
        row = tables.get(tableStaff).queryRow(1, userID);
        if (row != null) {
            Log.info("updating staff password");
            Staff s = new Staff();
            s.fromCSVLine(row);
            s.setPassword(newPassword);
            tables.get(tableStaff).updateRow(row, s.toCSVLine());
            tables.get(tableStaff).writeToFile(); // for immediate feedback
        }
    }

    @Override
    public ArrayList<CampCommitteeMember> queryCommitteeMembers(ArrayList<String> committeeMemberIDs) {
        ArrayList<CampCommitteeMember> ret = new ArrayList<>();
        for (String id : committeeMemberIDs) {
            String row = tables.get(tableStudents).queryRow(1, id);
            if (row != null) {
                Student tmp = new Student();
                tmp.fromCSVLine(row);
                ret.add(tmp.getCampCommitteeMember());
            }
        }
        return ret;
    }

    @Override
    public void updateCommitteeMemberDetails(CampCommitteeMember campCommitteeMember) {
        String row = tables.get(tableStudents).queryRow(1, (campCommitteeMember.getStudentId()));
        if (row != null) {
            Log.info("updating committe member info");
            Student s = new Student();
            s.fromCSVLine(row);
            s.setCampCommitteeMember(campCommitteeMember);
            tables.get(tableStudents).updateRow(row, s.toCSVLine());
            tables.get(tableStudents).writeToFile(); // for immediate feedback
        }
    }

    /**
     * Creates a student csv table from the sample csv file.
     * Makes use of a temporary CSVTable to parse sample file.
     */
    private void initializeStudentList() {
        // heaaders: Name,Email,Faculty
        // load initialStudentsFile into data/users/student.csv
        // create Student object from initial file
        // then call toCSV and add to data/users/student.csv

        CSVTable initTable = new CSVTable("temp", initStudents, 1);
        ArrayList<String> initialData = initTable.readFromFile();
        ArrayList<Student> studentList = new ArrayList<>();

        // convert initial data format to student objects, skip 1st line (header)
        for (int i = 1; i < initialData.size(); ++i) {
            String[] split = initialData.get(i).split(",");
            if (split.length != 3) {
                Log.error("Error intialising student list");
                continue;
            }
            try {
                String displayName = split[0].trim();
                String userID = split[1].trim().split("@")[0].toUpperCase();
                Faculty faculty = Faculty.valueOf(split[2].trim());
                Student student = new Student(displayName, userID, faculty);
                studentList.add(student);
            } catch (IllegalArgumentException e) {
                Log.error("Error parsing faculty from initial data: " + split[2]);
                continue;
            }
        }

        // then write to proper data store
        tables.get(tableStudents).writeToFile(studentList);
    }

    /**
     * Creates a staff csv table from the sample csv file.
     * Makes use of a temporary CSVTable to parse sample file.
     */
    private void initializeStaffList() {

        // heaaders: Name,Email,Faculty
        // load intialStaffFile into data/users/staff.csv
        // create Staff object from initial file
        // then call toCSV and add to data/users/staff.csv

        CSVTable initTable = new CSVTable("temp", initStaff, 1);
        ArrayList<String> initialData = initTable.readFromFile();
        ArrayList<Staff> staffList = new ArrayList<>();

        // convert initial data format to student objects
        for (int i = 1; i < initialData.size(); ++i) {
            String[] split = initialData.get(i).split(",");
            if (split.length != 3) {
                Log.error("Error intialising staff list");
                continue;
            }
            try {
                String displayName = split[0].trim();
                String userID = split[1].trim().split("@")[0].toUpperCase();
                Faculty faculty = Faculty.valueOf(split[2].trim());
                Staff staff = new Staff(displayName, userID, faculty);
                staffList.add(staff);
            } catch (IllegalArgumentException e) {
                Log.error("Error parsing faculty from initial data: " + split[2]);
                continue;
            }
        }

        // then write to proper data store
        tables.get(tableStaff).writeToFile(staffList);

    }
}
