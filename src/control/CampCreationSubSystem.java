package control;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import entity.Camp;
import entity.CampInformation;
import entity.UserGroup;
import util.Input;
import util.Log;

/**
 * <p>
 * A class that handles logic for creation and editing of camps.
 * </p>
 * 
 * @author Jon Daniel Acu Kang
 * @version 1.0
 * @since 24-11-2023
 */
public class CampCreationSubSystem {
    /**
     * Dependency Injection
     */
    private CampSystem campSystem;
    /**
     * Dependency Injection
     */
    private DataStoreSystem dataStoreSystem;

    /**
     * Enum to provide readability for user edit camp choice
     */
    private enum EditChoice {
        /**
         * Edit NAME
         */
        NAME,
        /**
         * Edit DESCRIPTION
         */
        DESCRIPTION,
        /**
         * Edit LOCATION
         */
        LOCATION,
        /**
         * Edit TOTAL_SLOTS
         */
        TOTAL_SLOTS,
        /**
         * Edit COMMITTEE_SLOTS
         */
        COMMITTEE_SLOTS,
        /**
         * Edit DATES
         */
        DATES,
        /**
         * Edit REGISTRATION_CLOSING_DATE
         */
        REGISTRATION_CLOSING_DATE,
        /**
         * Edit USERGROUP
         */
        USERGROUP,
        /**
         * Edit VISIBILITY
         */
        VISIBILITY
    }

    /**
     * An ArrayList that contains enumerators for different aspects of a camp to be
     * edited.
     */
    private final static ArrayList<EditChoice> editChoiceEnumList = new ArrayList<>(
            Arrays.asList(EditChoice.NAME, EditChoice.DESCRIPTION, EditChoice.LOCATION,
                    EditChoice.TOTAL_SLOTS, EditChoice.COMMITTEE_SLOTS,
                    EditChoice.DATES, EditChoice.REGISTRATION_CLOSING_DATE, EditChoice.USERGROUP,
                    EditChoice.VISIBILITY));

    /**
     * Constructor for the camp creation sub system.
     * 
     * @param campSystem      A class that stores all camps, and controls access to
     *                        them.
     * @param dataStoreSystem A class to handle all datastore operations.
     */
    public CampCreationSubSystem(CampSystem campSystem, DataStoreSystem dataStoreSystem) {
        this.campSystem = campSystem;
        this.dataStoreSystem = dataStoreSystem;
    }

    /**
     * Creates a camp.
     * 
     * @param campInfo ID of a camp.
     */
    public void createCamp(CampInformation campInfo) {
        Camp newCamp = new Camp(campSystem.getNextCampId(), campInfo);
        campSystem.getCamps().add(newCamp);
        dataStoreSystem.getCampDataStoreSubSystem().addCamp(newCamp);
        Log.println("Camp successfully created.");
    }

    /**
     * Deletes a camp.
     * 
     * @param campId ID of a camp.
     */
    public void deleteCamp(int campId) {
        Camp camp = campSystem.getCampById(campId);
        if (camp == null || !camp.getAttendeeList().isEmpty() || !camp.getCommitteeList().isEmpty()) {
            Log.println("Camp could not be deleted.");
            return;
        }
        campSystem.getCamps().remove(camp);
        dataStoreSystem.getCampDataStoreSubSystem().deleteCamp(campId);
        Log.println("Camp successfully deleted.");
    }

    /**
     * Takes in int as user input for what they would like to edit about the camp,
     * then edits the camp.
     * 
     * @param campId       ID of a camp.
     * @param updateChoice Determines which aspect of the camp will be edited.
     * @param input        Input object.
     */
    public void editCamp(int campId, int updateChoice, Input input) {
        Camp camp = campSystem.getCampById(campId);
        EditChoice editChoice = editChoiceEnumList.get(updateChoice - 1);
        switch (editChoice) {
            case NAME:
                String newCampName = input.getLine("Please enter the new camp name: ");
                camp.getCampInformation().setCampName(newCampName);
                break;
            case DESCRIPTION:
                String description = input.getLine("Please enter the new description: ");
                camp.getCampInformation().setDescription(description);
                break;

            case LOCATION:
                String location = input.getLine("Please enter the new location: ");
                camp.getCampInformation().setLocation(location);
                break;

            case TOTAL_SLOTS:
                int totalSlots = input.getInt("Please enter the new total number of slots: ");
                camp.getCampInformation().setTotalSlots(totalSlots);
                break;

            case COMMITTEE_SLOTS:
                int committeeSlots = input.getInt("Please enter the new number of committee slots: ");
                camp.getCampInformation().setCommitteeSlots(committeeSlots);
                break;

            case DATES:
                int duration = input.getInt("Please enter the number of days the camp will be held: ");
                LocalDate firstDate = input
                        .getDate("Please enter the date of the first day of the camp (DD/MM/YYYY): ");
                ArrayList<LocalDate> dates = new ArrayList<LocalDate>();
                for (int i = 0; i < duration; i++) {
                    dates.add(i, firstDate);
                    firstDate = firstDate.plusDays(1);
                }
                camp.getCampInformation().setDates(dates);
                break;

            case REGISTRATION_CLOSING_DATE:
                LocalDate registrationClosingDate = input
                        .getDate("Please enter the new closing date for registration: ");
                camp.getCampInformation().setRegistrationClosingDate(registrationClosingDate);
                break;

            case USERGROUP:
                UserGroup userGroup = camp.getCampInformation().getUserGroup();
                boolean yesno;
                if (userGroup.isWholeNTU()) {
                    yesno = input.getBool("Would you like to only open the camp to " + userGroup.getFaculty() + "?");
                    if (yesno == true)
                        userGroup.setFaculty(camp.getCampInformation().getOrganisingFaculty());
                } else {
                    yesno = input.getBool("Would you like to open the camp to the whole of NTU?");
                    if (yesno == true)
                        userGroup.setWholeNTU();
                }
                break;

            case VISIBILITY:
                boolean visibility = camp.toggleVisibility();
                if (visibility == true)
                    Log.println("The camp is now visible.");
                else
                    Log.println("The camp is now not visibile.");
                break;

            default:
                break;
        }
        dataStoreSystem.getCampDataStoreSubSystem().updateCampDetails(camp);
    }

}
