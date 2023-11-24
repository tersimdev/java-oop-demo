package control;

import java.util.ArrayList;

import entity.CampEnquiry;
import entity.CampFeedback;
import util.Input;
import util.Log;

public class EnquirySystem extends FeedbackSystem {

    public EnquirySystem(DataStoreSystem dataStoreSystem) {
        super(dataStoreSystem);
        initFeedbackMap(dataStoreSystem.getFeedbackDataStoreSubSystem().getAllEnquiries());
    }

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

    public boolean processCampEnquiry(String commMemberId, int campId, int enquiryId, String reply) {
        CampFeedback campFeedback = findFeedbackById(enquiryId, campId);
        if (campFeedback instanceof CampEnquiry) {
            CampEnquiry campEnquiry = (CampEnquiry) campFeedback;
            campEnquiry.reply(commMemberId, reply);
            return true;
        } else {
            Log.error("Feedback not enquiry for some reason");
        }
        return false;
    }

    @Override
    public void addToDataStore(CampFeedback feedback) {
        if (feedback instanceof CampEnquiry)
            dataStoreSystem.getFeedbackDataStoreSubSystem().addEnquiry((CampEnquiry) feedback);
        else
            Log.error("Tried to add a non enquiry");
    }

    @Override
    public void updateToDataStore(CampFeedback feedback) {
        if (feedback instanceof CampEnquiry)
            dataStoreSystem.getFeedbackDataStoreSubSystem().updateEnquiry((CampEnquiry) feedback);
        else
            Log.error("Tried to update a non enquiry");
    }

    @Override
    public void removeFromDataStore(int feedbackId) {
        dataStoreSystem.getFeedbackDataStoreSubSystem().deleteEnquiry(feedbackId);
    }
    
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
