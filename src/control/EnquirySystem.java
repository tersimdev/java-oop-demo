package control;

import java.util.ArrayList;

import entity.CampCommitteeMember;
import entity.CampEnquiry;
import entity.CampFeedback;
import util.Input;
import util.Log;

/**
 * <p>
 * A class to handle enquiry operations.
 * </p>
 * 
 * @author Yen Zhi Wei
 * @version 1.0
 * @since 23-11-2023
 */
public class EnquirySystem extends FeedbackSystem {

    /**
     * Constructor for the enquiry system.
     * @param dataStoreSystem A class to handle all datastore operations.
     */
    public EnquirySystem(DataStoreSystem dataStoreSystem) {
        super(dataStoreSystem);
        initFeedbackMap(dataStoreSystem.getFeedbackDataStoreSubSystem().getAllEnquiries());
    }

    /**
     * Displays all enquiries in the hashmap linked to the relevant camp.
     * @param campId campId of the camp to display enquiries.
     * @param input Input object.
     */
    public void viewAllEnquiries(String userId, int campId, Input input) {
        ArrayList<CampFeedback> enquiryList = new ArrayList<>();
        enquiryList = getCampFeedbacks(campId);
        int enquiries = 0;
        Log.println("===All Enquiries===");
        for (CampFeedback campFeedback : enquiryList) {
            if (campFeedback == null || !(campFeedback instanceof CampEnquiry))
                continue;
            CampEnquiry campEnquiry = (CampEnquiry) campFeedback;
            enquiries += 1;
            printEnquiry(campEnquiry);
        }
        if (enquiries == 0) {
            Log.println("No enquiries found. Directing back to menu...");
            return;
        }
    }

    /**
     * Displays unprocessed enquiriesin the hashmap linked to the relevant camp.
     * @param campId campId of the camp to display enquiries.
     * @param input Input object.
     */
    public void viewUnprocessedEnquiries(String userId, int campId, Input input) {
        ArrayList<CampFeedback> processedEnquiryList = new ArrayList<>();
        processedEnquiryList = getCampFeedbacks(campId);
        int enquiries = 0;
        Log.println("===Unprocessed Enquiries===");
        for (CampFeedback campFeedback : processedEnquiryList) {
            if (campFeedback == null || !(campFeedback instanceof CampEnquiry))
                continue;
            CampEnquiry campEnquiry = (CampEnquiry) campFeedback;
            boolean processed = !campEnquiry.isPending();
            if (processed)
                continue;
            else {
                enquiries += 1;
                printEnquiry(campEnquiry);
            }
        }
        if (enquiries == 0) {
            Log.println("No unprocessed enquiries found. Directing back to menu...");
            return;
        }
    }

    /**
     * Displays processed enquiries in the hashmap linked to the relevant camp.
     * @param studentId ID of student viewing processed enquiries.
     * @param campId campId of the camp to display enquiries.
     * @param input Input object.
     */
    public void viewProcessedEnquiries(String studentId, int campId, Input input) {
        ArrayList<CampFeedback> processedEnquiryList = new ArrayList<>();
        processedEnquiryList = getCampFeedbacks(campId);
        int enquiries = 0;
        Log.println("===Processed Enquiries===");
        for (CampFeedback campFeedback : processedEnquiryList) {
            if (campFeedback == null || !(campFeedback instanceof CampEnquiry))
                continue;
            CampEnquiry campEnquiry = (CampEnquiry) campFeedback;
            boolean belongsToUser = campEnquiry.getOwnerId().equals(studentId);
            boolean processed = !campEnquiry.isPending();
            if (!belongsToUser || !processed)
                continue;
            else {
                enquiries += 1;
                printEnquiry(campEnquiry);
            }
        }
        if (enquiries == 0) {
            Log.println("No processed enquiries found. Directing back to menu...");
            return;
        }
    }

    /**
     * View, edit and delete unprocessed enquiries in the hashmap linked to the relevant camp.
     * @param studentId ID of student viewing, editing and deleting unprocessed enquiries.
     * @param campId campId of the camp to view, edit and delete enquiries.
     * @param input Input object.
     */
    public void viewEditDelEnquiries(String studentId, int campId, Input input) {
        ArrayList<CampFeedback> studentEnquiryList = new ArrayList<>();
        studentEnquiryList = getCampFeedbacks(campId);
        int pending = 0;
        Log.println("===Pending Enquiries===");
        for (CampFeedback campFeedback : studentEnquiryList) {
            if (campFeedback == null || !(campFeedback instanceof CampEnquiry))
                continue;
            CampEnquiry campEnquiry = (CampEnquiry) campFeedback;
            boolean belongsToUser = campEnquiry.getOwnerId().equals(studentId);
            boolean processed = !campEnquiry.isPending();
            if (!belongsToUser || processed)
                continue;
            else {
                pending += 1;
                printEnquiry(campEnquiry);
            }
        }
        if (pending == 0) {
            Log.println("No pending enquiries found. Directing back to menu...");
            return;
        }
        Log.println("===Please select the following options=== ");
        Log.println("(1) Edit Enquiry");
        Log.println("(2) Delete Enquiry");
        Log.println("(3) Back to Student Menu");
        int sChoice = -1;
        while (sChoice < 0) {
            sChoice = input.getInt("Enter choice: ");
            if (sChoice == 1) {
                int enquiryId = input.getInt("Please enter the enquiryId of the enquiry to edit: ");
                String newEnquiry = input.getLine("Please enter new enquiry: ");
                CampFeedback campFeedback = editCampFeedback(campId, enquiryId,
                        newEnquiry);
                if (campFeedback != null)
                    Log.println("Edit successful.");
                else
                    Log.println("Edit failed.");
            } else if (sChoice == 2) {
                int enquiryId = input.getInt("Please enter the enquiryId of the enquiry to delete: ");
                Boolean result = removeCampFeedback(campId, enquiryId);
                if (result)
                    Log.println("Deletion successful.");
                else
                    Log.println("Deletion failed.");
            } else if (sChoice == 3)
                    break;
            else {
            Log.println("Invalid choice! Try again.");
            sChoice = -1;
            }
        }
    }

    /**
     * Process enquiries in the hashmap linked to the relevant camp.
     * @param userId ID of User processing the enquiries
     * @param campId campId of the camp to process enquiries.
     * @param enquiryId ID of enquiry to be processed.
     * @param reply Reply to the enquiry.
     */
    public boolean processCampEnquiry(CampCommitteeMember campCommitteeMember, String userId, int campId, int enquiryId, String reply) {
        CampFeedback campFeedback = findFeedbackById(enquiryId, campId);
        if (campFeedback instanceof CampEnquiry) {
            CampEnquiry campEnquiry = (CampEnquiry) campFeedback;
            campEnquiry.reply(userId, reply);
            if(campCommitteeMember != null) {
            campCommitteeMember.addPoints(1);
            dataStoreSystem.getUserDataStoreSubSystem().updateCommitteeMemberDetails(campCommitteeMember);
            }
            updateToDataStore(campEnquiry);
            return true;
        } else {
            Log.error("Feedback not enquiry for some reason");
        }
        return false;
    }

    /**
     * Adds CampEnquiry object to the system.
     * @param feedback CampFeedback object to be added to the system.
     */
    @Override
    protected void addToDataStore(CampFeedback feedback) {
        if (feedback instanceof CampEnquiry)
            dataStoreSystem.getFeedbackDataStoreSubSystem().addEnquiry((CampEnquiry) feedback);
        else
            Log.error("Tried to add a non enquiry");
    }

    /**
     * Updates existing CampEnquiry object in the system.
     * @param feedback CampFeedback object to be updated to the system.
     */
    @Override
    protected void updateToDataStore(CampFeedback feedback) {
        if (feedback instanceof CampEnquiry)
            dataStoreSystem.getFeedbackDataStoreSubSystem().updateEnquiry((CampEnquiry) feedback);
        else
            Log.error("Tried to update a non enquiry");
    }

    /**
     * Deletes existing CampEnquiry object from the system.
     * @param feedback CampFeedback object to be deleted from the system.
     */  
    @Override
    protected void removeFromDataStore(int feedbackId) {
        dataStoreSystem.getFeedbackDataStoreSubSystem().deleteEnquiry(feedbackId);
    }
    
    /**
     * Prints details of CampEnquiry
     * @param feedback CampEnquiry object to be printed.
     */ 
    public void printEnquiry(CampEnquiry campEnquiry) {
        Log.println("EnquiryID: " + campEnquiry.getId());
        Log.println("StudentID: " + campEnquiry.getOwnerId());
        Log.println("Enquiry Status: Pending");            
        Log.println("Enquiry: " + campEnquiry.getFeedback());
        if(campEnquiry.isPending())
            Log.println("Reply: null");
        else
            Log.println("Reply: " + campEnquiry.getReply());
        Log.println("");
    }
}
