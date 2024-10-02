package com.itkolleg.bookingsystem.domains;

import lombok.extern.slf4j.Slf4j;

/**
 * Utility class that holds constants for view paths in the system.
 * These constants are used to reference view templates, ensuring that view paths are managed in a centralized location.
 * This class is not meant to be instantiated or subclassed.
 *
 * @author Sonja Lechner
 * @version 1.0
 * @since 2023-09-01
 */

@Slf4j

public final class ViewConstants {

    // Prevent instantiation
    private ViewConstants() {
        throw new AssertionError("ViewConstants class should not be instantiated.");
    }

    //ADMIN & OPERATOR DESKBOOKING VIEWS
    public static final String A_ALL_DESKBOOKINGS = "deskbookings/admin/allDeskBookings";
    public static final String A_VIEW_DESKBOOKING = "deskbookings/admin/viewDeskBooking";
    public static final String A_ADD_DESKBOOKING = "deskbookings/admin/addDeskBooking";
    public static final String A_NEW_DESKBOOKING = "deskbookings/admin/newDeskBooking";
    public static final String A_UPDATE_DESKBOOKING = "deskbookings/admin/updateDeskBooking";
    public static final String A_CANCEL_DESKBOOKING = "deskbookings/admin/cancelDeskBooking";

    //NORMAL & PRIVILEGED EMPLOYEE DESKBOOKING VIEWS
    public static final String E_MY_DESKBOOKING = "deskbookings/emp/myDeskBookings";
    public static final String E_VIEW_DESKBOOKING = "deskbookings/emp/viewDeskBooking";
    public static final String E_ADD_DESKBOOKING = "deskbookings/emp/addDeskBooking";
    public static final String E_NEW_DESKBOOKING = "deskbookings/emp/newDeskBooking";
    public static final String E_UPDATE_DESKBOOKING = "deskbookings/emp/updateDeskBooking";
    public static final String E_HISTORY_DESKBOOKING = "deskbookings/emp/myDeskBookingHistory";
    public static final String E_CANCEL_DESKBOOKING = "deskbookings/emp/cancelDeskBooking";

    //DESK VIEWS
    public static final String ALL_DESKS = "desks/allDesks";
    public static final String ALL_DESKS_EMP = "desks/allDesksEmp";
    public static final String DESK_VIEW = "desks/viewDesk";
    public static final String ADD_DESK = "desks/addDesk";
    public static final String UPDATE_DESK = "desks/updateDesk";
    public static final String DELETE_DESK = "desks/deleteDesk";

    //HOLIDAY VIEWS
    public static final String ALL_HOLIDAYS = "desks/allHolidays";
    public static final String ADD_HOLIDAY= "desks/addHoliday";
    public static final String VIEW_HOLIDAY= "desks/viewHoliday";
    public static final String UPDATE_HOLIDAY = "desks/updateHoliday";
    public static final String DELETE_HOLIDAY = "desks/deleteHoliday";

    //TIMESLOT VIEWS
    public static final String ALL_TIMESLOTS = "desks/allTimeslots";
    public static final String ADD_TIMESLOT= "desks/addTimeslot";
    public static final String VIEW_TIMESLOT= "desks/viewTimeslot";
    public static final String UPDATE_TIMESLOT= "desks/updateTimeslot";
    public static final String DELETE_TIMESLOT= "desks/deleteTimeslot";

    //ERRORVIEW VIEW
    public static final String ERRORVIEW = "/error";

}
