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
        Log.println("(6) View/Edit/Delete Pending Enquiries");
        Log.println("(7) View Processed Enquiry Replies");
        Log.println("(8) Withdraw from Camp");
        if (!isCommittee)
            Log.println("(9) Back to Start");
        else {
            Log.println("==Committee Member Menu==");
            Log.println("(10) Submit Suggestions");
            Log.println("(11) View/Edit/Delete Pending Suggestions");
            Log.println("(12) View Processed Suggestions");
            Log.println("(13) View Enquiries");
            Log.println("(14) Reply Unprocessed Enquiries");
            Log.println("(15) Generate Camp Report");
            Log.println("(16) Generate Enquiry Report");
            Log.println("(17) Back to Start");
        }
        int choice = -1;
        while (choice < 0) {
            if (isCommittee)
                choice = getChoice(1, 16, 17);
            else
                choice = getChoice(1, 8, 9);
            if (choice == 0) {
                ui.setStateDirty(true);
            }
            switch (choice) {
                case 5:
                    //Submit Enquiry
                    selCampName = ui.getInput().getLine("Please enter the camp name to submit enquiry: ");
                    String enquiryStr = ui.getInput().getLine("Please enter enquiry: ");
                    CampEnquiry enquiry = new CampEnquiry(student.getUserID(),enquiryStr);
                    feedbackSystem.addCampEnquiry(selCampName, enquiry);
                    Log.println("Enquiry submitted.");
                    break;
                case 6:
                    //View/Edit/Delete Pending Enquiries
                    selCampName = ui.getInput().getLine("Please enter the camp name to view/edit/delete your enquiries: ");
                    ArrayList<CampEnquiry> studentEnquiryList = new ArrayList<>();
                    studentEnquiryList = feedbackSystem.getCampEnquiries(selCampName);
                    int size = studentEnquiryList.size();
                    Log.println("===Pending Enquiries===");
                    for (int i = 0; i < size; i++) {
                        CampEnquiry temp = studentEnquiryList.get(i);
                        if (temp.getOwner()!=student.getUserID() || temp.getReply()!=null) continue;
                        else {
                            Log.println("EnquiryID: " + temp.getEnquiryId());
                            Log.println("StudentID: " + temp.getOwner());
                            Log.println("Enquiry Status: Pending");
                            Log.println("Enquiry: " + temp.getEnquiry());
                            Log.println("");
                        }
                    }
                    Log.println("===Please select the following options=== ");
                    Log.println("(1) Edit Enquiry");
                    Log.println("(2) Delete Enquiry");
                    Log.println("(3) Back to Student Menu");
                    int sChoice = -1;
                    while (sChoice < 0) {
                        sChoice = ui.getInput().getInt("Enter choice: ");
                        if (sChoice==1){
                            int enquiryId = ui.getInput().getInt("Please enter the enquiryId of the enquiry to edit: ");
                            String newEnquiry = ui.getInput().getLine("Please enter new enquiry: ");
                            Boolean result = feedbackSystem.editCampEnquiry(selCampName, enquiryId, newEnquiry);
                            if(result) 
                                Log.println("Edit successful.");
                            else 
                                Log.println("Edit failed.");
                            break;
                        }
                        else if (sChoice==2){
                            int enquiryId = ui.getInput().getInt("Please enter the enquiryId of the enquiry to delete: ");
                            Boolean result = feedbackSystem.removeCampEnquiryById(selCampName, enquiryId);
                            if(result) 
                                Log.println("Deletion successful.");
                            else 
                                Log.println("Deletion failed.");
                            break;
                        }
                        else if (sChoice ==3) break;
                        else{
                            Log.println("Invalid choice! Try again.");
                            sChoice = -1;
                        }
                    }
                    break;
                case 7:
                    //View Processed Enquiry Replies
                    selCampName = ui.getInput().getLine("Please enter the camp name to view processed enquiries: ");
                    ArrayList<CampEnquiry> processedEnquiryList = new ArrayList<>();
                    processedEnquiryList = feedbackSystem.getCampEnquiries(selCampName);
                    int size5 = processedEnquiryList.size();
                    Log.println("===Processed Enquiries===");
                    for (int i = 0; i < size5; i++) {
                        CampEnquiry temp = processedEnquiryList.get(i);
                        if (temp.getOwner()!=student.getUserID() || temp.getReply()==null) continue;
                        else {
                            Log.println("EnquiryID: " + temp.getEnquiryId());
                            Log.println("StudentID: " + temp.getOwner());
                            Log.println("Enquiry Status: Processed");
                            Log.println("Enquiry: " + temp.getEnquiry());
                            Log.println("Reply: " + temp.getReply());
                            Log.println("");
                        }
                    }
                    break;
                case 10:
                    //Submit Suggestions
                    selCampName = ui.getInput().getLine("Please enter the camp name to submit suggestion: ");
                    String suggestionStr = ui.getInput().getLine("Please enter suggestion: ");
                    CampSuggestion suggestion = new CampSuggestion(student.getUserID(),suggestionStr);
                    feedbackSystem.addCampSuggestion(selCampName, suggestion);
                    Log.println("Suggestion submitted.");
                    break;
                case 11:
                    //View/Edit/Delete Pending Suggestions
                    selCampName = ui.getInput().getLine("Please enter the camp name to view/edit/delete your suggestions: ");
                    ArrayList<CampSuggestion> comSuggestionList = new ArrayList<>();
                    comSuggestionList = feedbackSystem.getCampSuggestions(selCampName);
                    int size3 = comSuggestionList.size();
                    Log.println("===Pending Suggestions===");
                    for (int i = 0; i < size3; i++) {
                        CampSuggestion temp = comSuggestionList.get(i);
                        if (temp.getOwner()!=student.getUserID() || !temp.isPending()) continue;
                        else {
                            Log.println("SuggestionID: " + temp.getSuggestionId());
                            Log.println("CampCommitteeMemberID: " + temp.getOwner());
                            Log.println("Suggestion Status: Pending");
                            Log.println("Suggestion: " + temp.getSuggestion());
                            Log.println("");
                        }
                    }
                    Log.println("===Please select the following options=== ");
                    Log.println("(1) Edit Suggestion");
                    Log.println("(2) Delete Suggestion");
                    Log.println("(3) Back to Student Menu");
                    int cChoice = -1;
                    while (cChoice < 0) {
                        cChoice = ui.getInput().getInt("Enter choice: ");
                        if (cChoice==1){
                            int suggestionId = ui.getInput().getInt("Please enter the suggestionId of the suggestion to edit: ");
                            String newSuggestion = ui.getInput().getLine("Please enter new suggestion: ");
                            Boolean result = feedbackSystem.editCampSuggestion(selCampName, suggestionId, newSuggestion);
                            if(result) 
                                Log.println("Edit successful.");
                            else 
                                Log.println("Edit failed.");
                            break;
                        }
                        else if (cChoice==2){
                            int suggestionId = ui.getInput().getInt("Please enter the suggestionId of the suggestion to delete: ");
                            Boolean result = feedbackSystem.removeCampSuggestionById(selCampName, suggestionId);
                            if(result) 
                                Log.println("Deletion successful.");
                            else 
                                Log.println("Deletion failed.");
                            break;
                        }
                        else if (cChoice ==3) break;
                        else{
                            Log.println("Invalid choice! Try again.");
                            sChoice = -1;
                        }
                    }
                    break;
                case 12:
                    //View Processed Suggestions
                    selCampName = ui.getInput().getLine("Please enter the camp name to view processed suggestions: ");
                    ArrayList<CampSuggestion> processedSuggestionList = new ArrayList<>();
                    processedSuggestionList = feedbackSystem.getCampSuggestions(selCampName);
                    int size6 = processedSuggestionList.size();
                    Log.println("===Processed Suggestions===");
                    for (int i = 0; i < size6; i++) {
                        CampSuggestion temp = processedSuggestionList.get(i);
                        if (temp.getOwner()!=student.getUserID() || temp.isPending()) continue;
                        else {
                            Log.println("SuggestionID: " + temp.getSuggestionId());
                            Log.println("CampCommitteeMemberID: " + temp.getOwner());
                            if(temp.hasApproved()) 
                                Log.println("Suggestion Status: Approved");
                            else if(temp.hasRejected()) 
                                Log.println("Suggestion Status: Rejected");
                            Log.println("Suggestion: " + temp.getSuggestion());
                            Log.println("");
                        }
                    }
                    break;
                case 13:
                    // View Camp Enquiries
                    selCampName = ui.getInput()
                            .getLine("Please enter the name of the camp you would like to view enquiries: ");
                    ArrayList<CampEnquiry> enquiryList = new ArrayList<>();
                    enquiryList = feedbackSystem.getCampEnquiries(selCampName);
                    int size2 = enquiryList.size();
                    Log.println("===All Enquiries===");
                    for (int i = 0; i < size2; i++) {
                        CampEnquiry temp = enquiryList.get(i);
                        Log.println("EnquiryID: " + temp.getEnquiryId());
                        Log.println("StudentID: " + temp.getOwner());
                        if (temp.getReply()==null){
                            Log.println("Enquiry Status: Pending");
                            Log.println("Enquiry: " + temp.getEnquiry());
                            Log.println("Reply: Null");
                        }
                        else{
                            Log.println("Enquiry Status: Processed");
                            Log.println("Enquiry: " + temp.getEnquiry());
                            Log.println("Reply: " +temp.getReply());
                        }
                        Log.println("");
                    }
                    break;
                case 14:
                    // Reply Unprocessed Enquiries
                    selCampName = ui.getInput()
                            .getLine("Please enter the name of the camp you would like to reply unprocessed enquiries: ");
                    ArrayList<CampEnquiry> pendingEnquiryList = new ArrayList<>();
                    pendingEnquiryList = feedbackSystem.getCampEnquiries(selCampName);
                    int size4 = pendingEnquiryList.size();
                    Log.println("===Unprocessed Enquiries===");
                    for (int i = 0; i < size4; i++) {
                        CampEnquiry temp = pendingEnquiryList.get(i);
                        if (temp.getReply()!=null) continue;
                        Log.println("EnquiryID: " + temp.getEnquiryId());
                        Log.println("StudentID: " + temp.getOwner());
                        Log.println("Enquiry Status: Pending");
                        Log.println("Enquiry: " + temp.getEnquiry());
                        Log.println("Reply: Null");
                        Log.println("");
                    }
                    int enquiryId = ui.getInput().getInt("Please enter the enquiryId of the enquiry to reply: ");
                    String reply = ui.getInput().getLine("Please enter reply: ");
                    Boolean result = feedbackSystem.replyCampEnquiry(student.getUserID(),selCampName, enquiryId, reply);
                    if(result) 
                            Log.println("Enquiry successfully processed.");
                        else 
                            Log.println("Enquiry processing failed.");
                    break;
                case 15:
                    selCampName = ui.getInput().getLine("Please enter the camp name for report generation: ");
                
                    Camp camp = campSystem.getCampByName(selCampName);
                
                    if (camp != null) {
                        String fileName = ui.getInput().getLine("Please enter the file name: ");
                
                        int filterChoice = ui.getInput().getInt("Please enter the filter (1 for ATTENDEE, 2 for CAMP_COMMITTEE, 3 for no filter): ");
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
            
                        reportSystem.generateCampReport(reportOptions, student, camp);
                    } else {
                        Log.println("Camp not found with the given name: " + selCampName);
                    }
                    break;
            }
        }
        return false;
    }

}
