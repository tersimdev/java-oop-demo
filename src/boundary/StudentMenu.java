package boundary;

import control.FeedbackSystem;
import control.ReportSystem;

import java.util.ArrayList;

import control.CampSystem;
import entity.Camp;
import entity.CampReportFilter;
import entity.CampReportOptions;
import entity.CampEnquiry;
import entity.CampSuggestion;
import entity.Student;
import util.Input;
import util.Log;

/**
 * <p>
 * Class defining the student menu functions
 * </p>
 * 
 * @author 
 * @version 1.0
 * @since 1-11-2023
 */
public class StudentMenu extends Menu {

    private final CampSystem campSystem;
    private final FeedbackSystem feedbackSystem;
    private final ReportSystem reportSystem;

    public StudentMenu(ConsoleUI ui, CampSystem campSystem, FeedbackSystem feedbackSystem, ReportSystem reportSystem) {
        super(ui);
        this.campSystem = campSystem;
        this.feedbackSystem = feedbackSystem;
        this.reportSystem = reportSystem;
    }

    @Override
    public boolean show() {
        String selCampName;

        // assume safe, check handled by state machine
        Student student = (Student) ui.getUser();
        boolean isCommittee = student.getCampCommitteeMember() != null;
        Log.println("===Student Menu===");
        Log.println("(1) View Available Camps");
        Log.println("(2) Register as Attendee");
        Log.println("(3) Register as Committee");
        Log.println("(4) View Registered Camps");
        Log.println("(5) Submit Enquiries");
        Log.println("(6) View/Edit/Delete Enquiries");
        Log.println("(7) View Enquiry Replies");
        Log.println("(8) Withdraw from Camp");
        if (!isCommittee)
            Log.println("(9) Back to Start");
        else {
            Log.println("==Committee Member Menu==");
            Log.println("(10) Submit Suggestions");
            Log.println("(11) View Enquiries");
            Log.println("(12) Reply Enquiries");
            Log.println("(13) View Suggestions");
            Log.println("(14) Edit Suggestions");
            Log.println("(15) Delete Suggestions");
            Log.println("(16) Generate Camp Report");
            Log.println("(17) Generate Enquiry Report");
            Log.println("(18) Back to Start");
        }
        int choice = -1;
        while (choice < 0) {
            if (isCommittee)
                choice = getChoice(1, 17, 18);
            else
                choice = getChoice(1, 8, 9);
            if (choice == 0) {
                ui.setStateDirty(true);
            }
            switch (choice) {
                case 5:
                    selCampName = Input.getInstance().getLine("Please enter the camp name to submit enquiry: ");
                    String enquiryStr = Input.getInstance().getLine("Please enter enquiry: ");
                    CampEnquiry enquiry = new CampEnquiry(student.getUserID(),enquiryStr);
                    feedbackSystem.addCampEnquiry(selCampName, enquiry);
                    Log.println("Enquiry submitted.");
                    break;
                case 6:
                    selCampName = Input.getInstance().getLine("Please enter the camp name to view/edit/delete your enquiry: ");
                    ArrayList<CampEnquiry> studentEnquiryList = new ArrayList<>();
                    studentEnquiryList = feedbackSystem.getCampEnquiries(selCampName);
                    int size = studentEnquiryList.size();
                    int j = 0;
                    for (int i = 0; i < size; i++) {
                        CampEnquiry temp = studentEnquiryList.get(i);
                        if (temp.getOwner()!=student.getUserID()) continue;
                        else {
                            Log.println("EnquiryID: " + j);
                            Log.println("StudentID: " + temp.getOwner());
                            if (temp.getReply()==null)
                                Log.println("Reply: Null");
                            else
                                Log.println("Reply: " +temp.getReply());
                            Log.println("");
                        }
                        j++;
                    }
                    break;

                case 10:
                    selCampName = Input.getInstance().getLine("Please enter the camp name to submit suggestion: ");
                    String suggestionStr = Input.getInstance().getLine("Please enter suggestion: ");
                    CampSuggestion suggestion = new CampSuggestion(student.getUserID(),suggestionStr);
                    feedbackSystem.addCampSuggestion(selCampName, suggestion);
                    Log.println("Suggestion submitted.");
                    break;
                case 11:
                    // View Camp Enquiries
                    selCampName = Input.getInstance()
                            .getLine("Please enter the name of the camp you would like to view enquiries: ");
                    ArrayList<CampEnquiry> enquiryList = new ArrayList<>();
                    enquiryList = feedbackSystem.getCampEnquiries(selCampName);
                    int size2 = enquiryList.size();
                    for (int i = 0; i < size2; i++) {
                        CampEnquiry temp = enquiryList.get(i);
                        Log.println("StudentID: " + temp.getOwner());
                        if (temp.getReply()==null)
                            Log.println("Reply: Null");
                        else
                            Log.println("Reply: " +temp.getReply());
                        Log.println("");
                    }

                case 16:
                    selCampName = Input.getInstance().getLine("Please enter the camp name for report generation: ");
                
                    Camp camp = campSystem.getCampByName(selCampName);
                
                    if (camp != null) {
                        String fileName = Input.getInstance().getLine("Please enter the file name: ");
                
                        int filterChoice = Input.getInstance().getInt("Please enter the filter (1 for ATTENDEE, 2 for CAMP_COMMITTEE, 3 for no filter): ");
                        CampReportFilter filter = null;
                        switch (filterChoice) {
                            case 1:
                                filter = CampReportFilter.ATTENDEE;
                                break;
                            case 2:
                                filter = CampReportFilter.CAMP_COMMITTEE;
                                break;
                            default:
                                break;
                        }

                        CampReportOptions reportOptions = new CampReportOptions();
                        reportOptions.setCampId(camp.getCampId());
                        reportOptions.setFileName(fileName);
                        reportOptions.setFilter(filter);
            
                        reportSystem.generateReport(reportOptions, student, camp);
                    } else {
                        Log.println("Camp not found with the given name: " + selCampName);
                    }
                    break;

            }
        }
        return false;
    }

}
