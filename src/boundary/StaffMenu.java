package boundary;

import java.time.LocalDateTime;
import java.util.ArrayList;

import control.CampSystem;
import control.FeedbackSystem;
import entity.CampEnquiry;
import entity.CampSuggestion;
import entity.Faculty;
import entity.Staff;
import entity.Student;
import entity.UserGroup;
import entity.User;
import util.Input;
import util.Log;

/**
 * <p>
 * Class defining the staff menu functions
 * </p>
 * 
 * @author 
 * @version 1.0
 * @since 1-11-2023
 */
public class StaffMenu extends Menu {

    public StaffMenu(ConsoleUI ui) {
        super(ui);
    }

    @Override
    public boolean show() {
        String selCampName;

        // assume safe, check handled by state machine
        Staff staff = (Staff) ui.getLoginSystem().getCurrentUser();
        Log.println("===Staff Menu===");
        Log.println("(1) Create Camp");
        Log.println("(2) Edit Camp");
        Log.println("(3) View All Camps");
        Log.println("(4) View Camp Student List");
        Log.println("(5) View Camp Committee List");
        Log.println("(6) View Camp Suggestions");
        Log.println("(7) Accept/Reject Suggestion");
        Log.println("(8) Generate Camp Report");
        Log.println("(9) Generate Performance Report");
        Log.println("(10) Generate Enquiry Report");
        Log.println("(11) Back to Start");
        int choice = -1;
        while (choice < 0) {
            choice = getChoice(1, 10, 11);
            if (choice == 0) {
                ui.setStateDirty(true);
            }

            switch (choice) {
                case 1:
                    // Create Camp
                    // get camp info from user
                    String campName = Input.getInstance().getLine("Please enter the camp name: ");
                    String description = Input.getInstance().getLine("Please enter the camp's description: ");
                    String location = Input.getInstance().getLine("Please enter the camp's location: ");
                    int totalSlots = Input.getInstance().getInt("Please enter the camp's total number of slots: ");
                    int committeeSlots = Input.getInstance().getInt("Please enter the camp's number of committee slots: ");
                    int duration = Input.getInstance().getInt("Please enter the number of days the camp will be held: "); 
                    LocalDateTime registrationClosingDate = Input.getInstance().getDate("Please enter the closing date for registration (DD/MM/YYYY): ");
                    LocalDateTime firstDate = Input.getInstance().getDate("Please enter the date of day number 1 (DD/MM/YYYY): ");
                    ArrayList<LocalDateTime> dates = new ArrayList<LocalDateTime>();
                    for (int i = 0; i < duration; i++) {
                        dates.add(firstDate);
                        firstDate = firstDate.plusDays(1);
                    }
                    
                    ArrayList<Student> studentList = null;
                    String staffInChargeId = staff.getUserID();
                    Faculty organisingFaculty= staff.getFaculty();
                    UserGroup userGroup = new UserGroup().setFaculty(organisingFaculty);
                    
                    // create the camp
                    ui.getCampSystem().createCamp(studentList, campName, description, location, totalSlots, committeeSlots,
                    dates, registrationClosingDate, staffInChargeId, userGroup, organisingFaculty);

                    break;
                
                case 2:
                    // Edit Camp
                    String editCampName = Input.getInstance().getLine("Please enter the name of the camp you would like to edit");
                    // query the camp name
                    
                    Log.println("===What would you like to edit?===");
                    Log.println("(1) Camp name");
                    Log.println("(2) Camp description");
                    Log.println("(3) Camp location");
                    Log.println("(4) Camp's total number of slots");
                    Log.println("(5) Camp's number of committee slots");
                    Log.println("(6) Camp dates");
                    Log.println("(7) Camp registration closing date");
                    Log.println("(8) Back to Staff Menu");
                    int EditChoice = -1;
                    while (EditChoice < 0) {
                        EditChoice = getChoice(1, 7, 8);
                        if (EditChoice == 0) {
                            choice = -1;
                            break;
                        }

                        switch (EditChoice) {
                            case 1:
                                String newCampName = Input.getInstance().getLine("Please enter the new camp name");
                                ui.getCampSystem().editCamp(newCampName);
                                break;

                            case 2:
                                
                                break;

                            case 3:
                                
                                break;

                            case 4:
                                
                                break;

                            case 5:
                                
                                break;

                            case 6:
                                
                                break;

                            case 7:
                                
                                break;
                        
                            default:
                                break;
                        }
                    }
                    break;
                
                case 3:
                    // View All Camps
                    ui.getCampSystem().viewAllCamps();
                    break;

                case 4:
                    // View Camp Student List
                    selCampName = Input.getInstance().getLine("Please enter the name of the camp you would like to inspect");
                    ui.getCampSystem().viewCampStudentList(selCampName);
                    break;

                case 5:
                    // View Camp Committee List
                    selCampName = Input.getInstance().getLine("Please enter the name of the camp you would like to inspect");
                    ui.getCampSystem().viewCampCommitteeList(selCampName);
                    break;

                case 6:
                    // View Camp Suggestions
                    selCampName = Input.getInstance().getLine("Please enter the name of the camp you would like to inspect");
                    ArrayList<CampSuggestion> suggestionList = new ArrayList<>();
                    suggestionList = ui.getFeedbackSystem().getCampSuggestions(selCampName);
                    int size = suggestionList.size();
                    for(int i=0;i<size;i++){
                        CampSuggestion temp = suggestionList.get(i);
                        Log.println("Name of Camp Committee Member: " +temp.getOwner());

                        if(temp.hasApproved())
                            Log.println("Approval status: Approved");
                        else if(temp.hasRejected())
                            Log.println("Approval status: Rejected");
                        else
                            Log.println("Approval status: Pending");
                        
                        Log.println("Suggestion: " +temp.getSuggestion());
                    }

                    
                    break;

                case 7:
                    // Accept/Reject Suggestion
                    selCampName = Input.getInstance().getLine("Please enter the name of the camp you would like to inspect");
                    
                    break;

                case 8:
                    // Generate Camp Report
                    selCampName = Input.getInstance().getLine("Please enter the name of the camp you would like to inspect");
                    
                    break;

                case 9:
                    // Generate Performance Report
                    selCampName = Input.getInstance().getLine("Please enter the name of the camp you would like to inspect");
                    
                    break;

                case 10:
                    // Generate Enquiry Report
                    selCampName = Input.getInstance().getLine("Please enter the name of the camp you would like to inspect");
                    

                    break;
            
                default:
                    break;
            }
        }
        return false;
    }

}
