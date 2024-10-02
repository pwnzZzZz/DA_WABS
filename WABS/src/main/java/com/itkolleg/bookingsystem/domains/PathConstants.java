package com.itkolleg.bookingsystem.domains;

import lombok.extern.slf4j.Slf4j;

/**
 * Utility class that holds constants for URL paths in the system.
 * These constants are used to reference URL endpoints, ensuring that paths are managed in a centralized location.
 * This class is not meant to be instantiated or subclassed.
 *
 * @author Sonja Lechner
 * @version 1.0
 * @since 2023-09-01
 */

@Slf4j
public final class PathConstants {

    // Prevent instantiation
    private PathConstants() {
        throw new AssertionError("PathConstants class should not be instantiated.");
    }

    //DESKBOOKINGS - ADMIN & OPERATOR URL PATHS
    public static final String ADMIN_VIEW_ALL = "/admin";
    public static final String ADMIN_VIEW_ALL_P = "/web/deskbookings/admin";
    public static final String ADMIN_VIEW_BOOKING = "/admin/view/{id}";
    public static final String ADMIN_ADD = "/admin/add";
    public static final String ADMIN_NEW = "/admin/new/{deskId}";
    public static final String ADMIN_NEW_P = "/admin/new";
    public static final String ADMIN_UPDATE = "/admin/update/{id}";
    public static final String ADMIN_UPDATE_P = "/admin/update";
    public static final String ADMIN_CANCEL = "/admin/cancel/{id}";
    public static final String ADMIN_CANCEL_P = "/admin/cancel";

    //DESKBOOKINGS - NORMAL & PRIVILEGED EMPLOYEE URL PATHS
    public static final String EMP_View_All= "/mydeskbookings";
    public static final String EMP_View_All_P= "/web/deskbookings/mydeskbookings";
    public static final String EMP_VIEW_BOOKING = "/view/{id}";
    public static final String EMP_ADD = "/add";
    public static final String EMP_NEW = "/new/{deskId}";
    public static final String EMP_NEW_P = "/new";
    public static final String EMP_UPDATE = "/update/{id}";
    public static final String EMP_UPDATE_P = "/update";
    public static final String EMP_VIEW_HISTORY = "/deskbookinghistory/{id}";
    public static final String EMP_CANCEL = "/cancel/{id}";
    public static final String EMP_CANCEL_P = "/cancel";


    //DESKS - ADMIN & OPERATOR URL PATHS
    public static final String VIEW_ALL = "/admin";
    public static final String VIEW_ALL_P = "/web/desks/admin";
    public static final String VIEW_DESK = "/view/{id}";
    public static final String ADD = "/add";
    public static final String UPDATE = "/update/{id}";
    public static final String UPDATE_P = "/update";
    public static final String DELETE = "/delete/{id}";
    public static final String DELETE_P = "/delete";

    //DESKS - NORMAL & PRIVILEGED EMPLOYEE URL PATHS
    public static final String VIEW_ALL_EMP = "/employees";

    // ERRORVIEW URL PATH
    public static final String ERROR = "/error";

}
