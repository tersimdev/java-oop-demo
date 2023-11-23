package control;

import entity.CampFeedback;
import java.util.ArrayList;

import entity.CampEnquiry;
import util.Log;

public class EnquirySystem extends FeedbackSystem {

    public EnquirySystem(DataStoreSystem dataStoreSystem) {
        super(dataStoreSystem);
    }

    @Override
    public ArrayList<CampFeedback> loadFeedbackFromDatastore() {
        return dataStoreSystem.getFeedbackDataStoreSubSystem().getAllEnquiries();
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
}
