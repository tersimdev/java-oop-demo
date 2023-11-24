package boundary;

import java.time.LocalDate;
import java.util.ArrayList;

import control.CampSystem;
import control.EnquirySystem;
import control.ReportSystem;
import control.SuggestionSystem;
import entity.Camp;
import entity.CampInformation;
import entity.CampReportFilter;
import entity.CampReportOptions;
import entity.Faculty;
import entity.Staff;
import entity.UserGroup;
import util.Log;
import util.helpers.InputHelper;

/**
 * <p>
 * Class defining the staff menu.
 * </p>
 * 
 * @author Team 2
 * @version 1.0
 * @since 1-11-2023
 */
public class StaffMenu extends Menu {

    private final CampSystem campSystem;
    private final EnquirySystem enquirySystem;
    private final SuggestionSystem suggestionSystem;
    private final ReportSystem reportSystem;

    private Staff staff;

    public StaffMenu(ConsoleUI ui, CampSystem campSystem, EnquirySystem enquirySystem,
            SuggestionSystem suggestionSystem, ReportSystem reportSystem) {
        super(ui);
        this.campSystem = campSystem;
        // this.feedbackSystem = feedbackSystem;
        this.enquirySystem = enquirySystem;
        this.suggestionSystem = suggestionSystem;
        this.reportSystem = reportSystem;

        // define which choice triggers which function
        addMenuFunction(1, this::createCamp);
        addMenuFunction(2, this::editCamp);
        addMenuFunction(3, this::deleteCamp);
        addMenuFunction(4, this::viewAllCamps);
        addMenuFunction(5, this::viewCampAttendeeList);
        addMenuFunction(6, this::viewCampCommitteeList);
        addMenuFunction(7, this::viewEnquiries);
        addMenuFunction(8, this::replyEnquiries);
        addMenuFunction(9, this::viewSuggestions);
        addMenuFunction(10, this::acceptRejectSuggestions);
        addMenuFunction(11, this::generateCampReport);
        addMenuFunction(12, this::generatePerformanceReport);
    }

    @Override
    public boolean show() {
        // assume safe, check handled by state machine
        staff = (Staff) ui.getUser();

        Log.println("===Staff Menu===");
        Log.println("(1) Create Camp");
        Log.println("(2) Edit Camp");
        Log.println("(3) Delete Camp");
        Log.println("(4) View All Camps");
        Log.println("(5) View Camp Student List");
        Log.println("(6) View Camp Committee List");
        Log.println("(7) View Enquiries");
        Log.println("(8) Reply Unprocessed Enquiries");
        Log.println("(9) View Suggestions");
        Log.println("(10) Accept/Reject Unprocessed Suggestions");
        Log.println("(11) Generate Camp Report");
        Log.println("(12) Generate Performance Report");
        Log.println("(13) Back to Start");
        int choice = -1;
        while (choice < 0) {
            choice = getChoice(1, 12, 13);
            if (choice == 0) {
                ui.setStateDirty(true);
                return false;
            }
            runMenuFunction(choice);
        }
        return false;
    }

    // menu functions defineed below

    private boolean createCamp(Menu menu) {
        // Create Camp
        // get camp info from user
        String campName = ui.getInput().getLine("Please enter the camp name: ");
        String description = ui.getInput().getLine("Please enter the camp's description: ");
        String location = ui.getInput().getLine("Please enter the camp's location: ");
        int committeeSlots = InputHelper.getBoundedInt(ui.getInput(), 0, 10,
                "Please enter the camp's number of committee slots (MAX 10): ");
        int totalSlots = InputHelper.getBoundedInt(ui.getInput(), committeeSlots + 1, 999,
                "Please enter the camp's total number of slots (including committee members): ");
        int duration = InputHelper.getBoundedInt(ui.getInput(), 1, 9999,
                "Please enter the number of days the camp will be held: ");
        LocalDate firstDate = ui.getInput()
                .getDate("Please enter the date of the first day of the camp (DD/MM/YYYY): ");
        LocalDate registrationClosingDate = ui.getInput()
                .getDate("Please enter the registration deadline (DD/MM/YYYY): ");
        ArrayList<LocalDate> dates = new ArrayList<LocalDate>();
        for (int i = 0; i < duration; i++) {
            dates.add(firstDate);
            firstDate = firstDate.plusDays(1);
        }
        boolean isWholeNTU = ui.getInput()
                .getBool("Will this camp be open to the whole of NTU (Y/N)? ");
        Faculty organisingFaculty = staff.getFaculty();
        UserGroup userGroup = new UserGroup();
        if (isWholeNTU)
            userGroup.setWholeNTU();
        else
            userGroup.setFaculty(organisingFaculty);
        String staffInChargeId = staff.getUserID();

        // create the camp
        CampInformation campInformation = new CampInformation.CampInformationBuilder().setCampName(campName)
                .setDates(dates)
                .setRegistrationClosingDate(registrationClosingDate).setTotalSlots(totalSlots)
                .setCommitteeSlots(committeeSlots)
                .setLocation(location).setDescription(description).setStaffInChargeId(staffInChargeId)
                .setUserGroup(userGroup)
                .setOrganisingFaculty(organisingFaculty).build();

        campSystem.getCampCreationSubSystem().createCamp(campInformation);
        // campSystem.getCampCreationSubSystem.createCamp();
        return false;
    }

    private boolean editCamp(Menu menu) {
        // Edit Camp
        int selCampId;
        boolean campBelongsToStaff = false;
        do {
            selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "edit");
            campBelongsToStaff = (staff.getUserID().equals(campSystem.getCampById(selCampId).getCampInformation().getStaffInChargeId()));
            if (campBelongsToStaff == false) Log.println("You are not in charge of this camp.");
        } 
        while (campBelongsToStaff == false || selCampId < 0);

        Log.println("===What would you like to edit?===");
        Log.println("(1) Camp name");
        Log.println("(2) Camp description");
        Log.println("(3) Camp location");
        Log.println("(4) Camp's total number of slots");
        Log.println("(5) Camp's number of committee slots");
        Log.println("(6) Camp dates");
        Log.println("(7) Camp registration closing date");
        Log.println("(8) Toggle camp visibility");
        Log.println("(9) Back to Staff Menu");

        int editChoice = -1;
        while (editChoice < 0) {
            editChoice = getChoice(1, 8, 9);
            if (editChoice == 0) {
                return false;
            }
            campSystem.getCampCreationSubSystem().editCamp(selCampId, editChoice, ui.getInput());
        }
        return false;
    }

    private boolean deleteCamp(Menu menu) {
        // Delete Camp
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "delete");
        campSystem.getCampCreationSubSystem().deleteCamp(selCampId);
        return false;
    }

    private boolean viewAllCamps(Menu menu) {
        boolean yesno = ui.getInput()
                .getBool("Would you like to view only the camps you created?(Y/N) ");
        int sortChoice = menu.printCampSortOrderChoices();
        if (yesno == true)
            campSystem.getCampViewerSubSystem().viewCampsOfStaff(staff, sortChoice);
        else
            campSystem.getCampViewerSubSystem().viewAllCamps(sortChoice);
        return false;
    }

    private boolean viewCampAttendeeList(Menu menu) {
        // View Camp Attendee List
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "inspect");
        campSystem.getCampViewerSubSystem().viewAttendeeList(staff, selCampId);
        return false;
    }

    private boolean viewCampCommitteeList(Menu menu) {
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "inspect");
        campSystem.getCampViewerSubSystem().viewCampCommitteeList(staff, selCampId);
        return false;
    }

    private boolean viewEnquiries(Menu menu) {
        // View Camp Enquiries
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "view enquiries");
        enquirySystem.viewAllEnquiries(staff.getUserID(), selCampId, ui.getInput());
        return false;
    }

    private boolean replyEnquiries(Menu menu) {
        // Reply Unprocessed Enquiries
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "reply unprocessed enquiries");
        enquirySystem.viewUnprocessedEnquiries(staff.getUserID(), selCampId, ui.getInput());

        int enquiryId = ui.getInput().getInt("Please enter the enquiryId of the enquiry to reply: ");
        String reply = ui.getInput().getLine("Please enter reply: ");
        Boolean result = enquirySystem.processCampEnquiry(staff.getUserID(), selCampId, enquiryId, reply);
        if (result) {
            Log.println("Enquiry successfully processed.");
        } else
            Log.println("Enquiry processing failed.");
        return false;
    }

    private boolean viewSuggestions(Menu menu) {
        // View Suggestions
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "view suggestions");
        suggestionSystem.viewAllSuggestions(selCampId, ui.getInput());
        return false;
    }

    private boolean acceptRejectSuggestions(Menu menu) {
        // Accept/Reject Unprocessed Suggestions
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem,
                "approve/reject unprocessed suggestions");
        suggestionSystem.viewUnprocessedSuggestions(selCampId, ui.getInput());
        int suggestionId = ui.getInput()
                .getInt("Please enter the suggestionId of the suggestion to approve/reject: ");
        Log.println("===Please select the following options===");
        Log.println("(1) Accept suggestion");
        Log.println("(2) Reject suggestion");
        int decision = -1;
        Boolean result1 = false;
        while (decision < 0) {
            decision = ui.getInput().getInt("Enter choice: ");
            if (decision == 1)
                result1 = suggestionSystem.processCampSuggestion(staff.getUserID(), selCampId, suggestionId,
                        true);
            else if (decision == 2)
                result1 = suggestionSystem.processCampSuggestion(staff.getUserID(), selCampId, suggestionId,
                        false);
            else {
                Log.println("Invalid choice! Try again.");
                decision = -1;
            }
        }
        if (result1)
            Log.println("Suggestion successfully processed.");
        else
            Log.println("Suggestion processing failed.");
        return false;
    }

    private boolean generateCampReport(Menu menu) {
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "Generate camp report");
        Camp camp = campSystem.getCampById(selCampId);

        CampReportFilter[] filterChoicesOptions = {
                CampReportFilter.ATTENDEE,
                CampReportFilter.CAMP_COMMITTEE,
                CampReportFilter.NONE,
        };

        String[] fileTypeOptions = {
                ".txt",
                ".csv",
        };

        if (camp != null) {
            String fileName = ui.getInput().getLine("Please enter the file name: ");

            // CampReportOptions reportOptions = ReportInputHelper.getOptionsFromUser();

            int filterChoice = ui.getInput()
                    .getInt("Please enter the filter (1 for ATTENDEE, 2 for CAMP_COMMITTEE, 3 for no filter): ");
            CampReportFilter filter = filterChoicesOptions[filterChoice - 1];

            int fileTypeChoice = ui.getInput().getInt("Choose your filetype((1 for TXT, 2 for CSV): ");
            String fileType = fileTypeOptions[fileTypeChoice - 1];

            CampReportOptions reportOptions = new CampReportOptions();
            reportOptions.setCampId(camp.getCampId());
            reportOptions.setFileName(fileName);
            reportOptions.setFileType(fileType);

            reportSystem.writeCampReport(reportOptions, filter, staff, camp);
        } else {
            Log.println("Camp not found " + selCampId);
        }
        return false;
    }

    private boolean generatePerformanceReport(Menu menu) {
        int selCampId = InputHelper.getCampIdFromUser(ui.getInput(), campSystem, "Generate performance report");

        Camp camp = campSystem.getCampById(selCampId);

        String[] fileTypeOptions = {
                ".txt",
                ".csv",
        };

        if (camp != null) {
            String fileName = ui.getInput().getLine("Please enter the file name: ");

            int fileTypeChoice = ui.getInput().getInt("Choose your filetype((1 for TXT, 2 for CSV): ");
            String fileType = fileTypeOptions[fileTypeChoice - 1];

            CampReportOptions reportOptions = new CampReportOptions();
            reportOptions.setCampId(camp.getCampId());
            reportOptions.setFileName(fileName);
            reportOptions.setFileType(fileType);

            reportSystem.writePerformanceReport(reportOptions, staff, camp);
            Log.println("Performance report generated successfully.");
        } else {
            Log.println("Camp not found " + selCampId);
        }

        return false;
    }
}
