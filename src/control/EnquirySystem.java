package control;

import entity.CampEnquiry;
import entity.CampFeedback;
import util.Log;

public class EnquirySystem extends FeedbackSystem {

    public EnquirySystem(DataStoreSystem dataStoreSystem) {
        super(dataStoreSystem);
        initFeedbackMap(dataStoreSystem.getFeedbackDataStoreSubSystem().getAllEnquiries());
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
}
