package com.itkolleg.bookingsystem.controller;

import com.itkolleg.bookingsystem.domains.*;
import com.itkolleg.bookingsystem.exceptions.DeskNotAvailableException;
import com.itkolleg.bookingsystem.exceptions.ResourceDeletionFailureException;
import com.itkolleg.bookingsystem.exceptions.ResourceNotFoundException;
import com.itkolleg.bookingsystem.service.desk.DeskService;
import com.itkolleg.bookingsystem.service.deskbooking.DeskBookingService;
import com.itkolleg.bookingsystem.service.employee.EmployeeService;
import com.itkolleg.bookingsystem.domains.booking.DeskBooking;
import com.itkolleg.bookingsystem.exceptions.employeeExceptions.EmployeeNotFoundException;
import com.itkolleg.bookingsystem.service.timeslot.TimeslotService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.itkolleg.bookingsystem.domains.PathConstants.*;
import static com.itkolleg.bookingsystem.domains.PathConstants.ERROR;
import static com.itkolleg.bookingsystem.domains.ViewConstants.*;

/**
 * This class represents a controller for managing desk bookings within the booking system.
 * It handles operations such as retrieving desk bookings, adding desk bookings, updating desk bookings,
 * and canceling desk bookings.
 *
 * <p>It interacts with the {@link DeskBookingService}, {@link DeskService}, {@link EmployeeService},
 * and {@link TimeslotService} to perform the necessary operations.
 *
 * <p>The controller is responsible for handling all HTTP requests related to desk bookings.
 *
 * <p>Note: This controller is designed to work with the Web interface of the booking system.
 *
 * <p>The endpoints provided by this controller are:
 * <ul>
 *   <li>/web/deskbookings/admin - Retrieves all desk bookings (accessible to admins and operators)</li>
 *   <li>/web/deskbookings/admin/view/{id} - Retrieves the details of a specific desk booking (accessible to admins and operators)</li>
 *   <li>/web/deskbookings/admin/add - Displays the form for adding a desk booking (accessible to admins and operators)</li>
 *   <li>/web/deskbookings/admin/add - Adds a new desk booking based on the submitted form data (accessible to admins and operators)</li>
 *   <li>/web/deskbookings/admin/new/{deskId} - Displays the form for adding a new desk booking for a specific desk (accessible to admins and operators)</li>
 *   <li>/web/deskbookings/admin/new - Adds a new desk booking based on the submitted form data (accessible to admins and operators)</li>
 *   <li>/web/deskbookings/admin/update/{id} - Displays the form for updating a specific desk booking (accessible to admins and operators)</li>
 *   <li>/web/deskbookings/admin/update - Updates a specific desk booking based on the submitted form data (accessible to admins and operators)</li>
 *   <li>/web/deskbookings/admin/cancel/{id} - Displays the form for canceling a specific desk booking ((accessible to admins and operators)</li>
 *   <li>/web/deskbookings/admin/cancel/{id} - Cancels a specific desk booking (accessible to admins and operators)</li>
 *   <li>/web/deskbookings/mydeskbookings - Retrieves the desk bookings for the currently logged-in employee</li>
 *   <li>/web/deskbookings/view/{id} - Retrieves the details of a specific desk booking for the currently logged-in employee</li>
 *   <li>/web/deskbookings/add - Displays the form for adding a desk booking for the currently logged-in employee</li>
 *   <li>/web/deskbookings/add - Adds a new desk booking based on the submitted form data for the currently logged-in employee</li>
 *   <li>/web/deskbookings/update/{id} - Displays the form for updating a specific desk booking for the currently logged-in employee</li>
 *   <li>/web/deskbookings/update - Updates a specific desk booking based on the submitted form data for the currently logged-in employee</li>
 *   <li>/web/deskbookings/deskbookinghistory/{id} - Retrieves the desk booking history for a specific employee</li>
 *   <li>/web/deskbookings/cancel/{id} - Displays the form for cancelling a specific desk booking for the currently logged-in employee</li>
 *   <li>/web/deskbookings/cancel/{id} - Cancels a specific desk booking for the currently logged-in employee</li>
 *   <li>/web/deskbookings/error - Displays the error page</li>
 * </ul>
 *
 * <p>It also includes various model attribute methods to provide data for the views, such as the list of employees,
 * desks, start times, and end times.
 *
 * <p>Note: This controller assumes the use of Spring Framework for building web applications.
 *
 * @author Sonja Lechner
 * @version 1.0
 * @since 2023-05-24
 */
@Slf4j
@Controller
//@RequiredArgsConstructor
@RequestMapping("/web/deskbookings")
public class DeskBookingController {
    private final DeskBookingService deskBookingService;
    private final DeskService deskService;
    private final EmployeeService employeeService;
    private final TimeslotService timeslotService;

    public DeskBookingController(DeskBookingService deskBookingService, DeskService deskService, EmployeeService employeeService, TimeslotService timeslotService) {
        this.deskBookingService = deskBookingService;
        this.deskService = deskService;
        this.employeeService = employeeService;
        this.timeslotService = timeslotService;
    }


    /**
     * Retrieves a list of all employees to be used as a model attribute.
     * This method attempts to retrieve all employees from the employee service.
     * If any exception occurs during the fetch operation, it logs the error and returns an empty list as a fallback.
     *
     * @return A list of all employees, or an empty list if an error occurs.
     */

    @ModelAttribute("employees")
    public List<Employee> getEmployees() {
        try {
            return this.employeeService.getAllEmployees();
        } catch (Exception e) { // Catch specific exception
            log.error("Error occurred while getting all employees: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    /**
     * Retrieves a list of all desks to be used as a model attribute.
     * This method attempts to retrieve all desks from the desk service.
     * If any exception occurs during the fetch operation, it logs the error and returns an empty list as a fallback.
     *
     * @return A list of all desks, or an empty list if an error occurs.
     */
    @ModelAttribute("desks")
    public List<Desk> getDesks(){
        try {
            return this.deskService.getAllDesks();
        } catch (Exception e) {
            log.error("Error occurred while getting all desks: {}", e.getMessage(), e);
            return Collections.emptyList(); // Returns an empty list as a default value
        }
    }

    /**
     * Retrieves a list of all start times for time slots to be used as a model attribute.
     * * This method fetches all time slots from the timeslot service, converts them to a list of start times, and removes duplicates.
     * @return A list of all start times.
     */
    @ModelAttribute("startTimes")
    public List<String> getStartTimes() {
        try {
            return this.timeslotService.getAllTimeslots()
                    .stream()
                    .map(Timeslot::getStartTimeAsString)
                    .distinct()
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error occurred while getting all start times: {}", e.getMessage(), e);
            return Collections.emptyList(); // Returns an empty list as a default value
        }
    }

    /**
     * Retrieves a list of all end times for time slots to be used as a model attribute.
     * This method fetches all time slots from the timeslot service, converts them to a list of end times, and removes duplicates.
     * @return A list of all end times.
     */
    @ModelAttribute("endTimes")
    public List<String> getEndTimes() {
        try {
            return this.timeslotService.getAllTimeslots()
                    .stream()
                    .map(Timeslot::getEndTimeAsString)
                    .distinct()
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error occurred while getting all end times: {}", e.getMessage(), e);
            return Collections.emptyList(); // Returns an empty list as a default value
        }
    }

    void handleValidationErrors(BindingResult bindingResult, Model model, DeskBooking booking) {
        log.warn("Validation errors: {}", bindingResult.getAllErrors());

        List<String> errors = bindingResult.getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                .collect(Collectors.toList());

        model.addAttribute("validationErrors", errors);
        model.addAttribute("deskBooking", booking);
    }


    /**
     * Handles the request to retrieve and display all desk bookings in the admin view.
     *
     * @param model The model object to which attributes can be added.
     * @return The view name for the admin view that displays all desk bookings.
     * @throws ResourceNotFoundException If there are no desk bookings available.
     */
    @GetMapping(ADMIN_VIEW_ALL)
    public String getAllDeskBookings(Model model) throws ResourceNotFoundException {
        // Fetch all the desk bookings
        List<DeskBooking> bookings = this.deskBookingService.getAllBookings();

        // Check if there are no desk bookings
        if (bookings.isEmpty()) {
            // Add a message to the model indicating no bookings are available
            model.addAttribute("errorMessage", "No desk bookings are available.");
            // Throw an exception indicating no desk bookings were found
            throw new ResourceNotFoundException("No desk bookings found.");
        } else {
            // Add the list of desk bookings to the model for the view to access
            model.addAttribute("viewAllDeskBookings", bookings);
        }
        // Return the view name for the admin view that displays all desk bookings
        return A_ALL_DESKBOOKINGS;
    }

    /**
     * Handles the request to view a specific desk booking in the admin view.
     *
     * @param id    The ID of the desk booking to retrieve.
     * @param model The model object to which attributes can be added.
     * @return The view name for the admin view of the specific desk booking.
     * @throws ResourceNotFoundException If the desk booking with the given ID is not found.
     */
    @GetMapping(ADMIN_VIEW_BOOKING)
    public String viewDeskBooking(@PathVariable Long id, Model model) throws ResourceNotFoundException {
        Optional<DeskBooking> deskBooking = this.deskBookingService.getBookingById(id);

        // Check if the desk booking is present
        if (deskBooking.isPresent()) {
            // Add the desk booking to the model for the view to access
            model.addAttribute("deskBooking", deskBooking.get());
            // Return the view name for the admin view of the specific desk booking
            return A_VIEW_DESKBOOKING;
        } else {
            // Throw an exception if the desk booking is not found
            throw new ResourceNotFoundException("Desk booking with ID " + id + " was not found.");
        }
    }

    @GetMapping({ADMIN_NEW, ADMIN_ADD})
    public String deskBookingForm(@PathVariable(value = "deskId", required = false) Long deskId, Model model, DeskBooking flashDeskBooking) throws ResourceNotFoundException {
        if (deskId != null) {
            // Find the desk
            Desk desk = deskService.getDeskById(deskId);
            model.addAttribute("desk", desk);
            if (flashDeskBooking != null && flashDeskBooking.getId() != null) {
                model.addAttribute("deskBooking", flashDeskBooking);
            } else {
                model.addAttribute("deskBooking", new DeskBooking());
            }
            if (!model.containsAttribute("errorMessage")) {
                model.addAttribute("errorMessage", null);
            }
            return A_NEW_DESKBOOKING;
        } else {
            if (flashDeskBooking != null && flashDeskBooking.getId() != null) {
                model.addAttribute("deskBooking", flashDeskBooking);
            } else {
                model.addAttribute("deskBooking", new DeskBooking());
            }
            if (!model.containsAttribute("errorMessage")) {
                model.addAttribute("errorMessage", null);
            }
            return A_ADD_DESKBOOKING;
        }
    }


    @PostMapping({ADMIN_NEW, ADMIN_ADD})
    public String adminNewDeskBooking(@ModelAttribute("deskBooking") @Valid DeskBooking booking, BindingResult bindingResult, @RequestParam("employee.id") Long employeeId, @RequestParam(value = "desk.id", required = false) Long deskId, Model model) throws ResourceNotFoundException, EmployeeNotFoundException, DeskNotAvailableException, InterruptedException, ExecutionException {
        if (deskId == null && booking.getDesk().getId() == null) {
            model.addAttribute("errorMessage", "Desk ID is missing.");
            return A_ADD_DESKBOOKING;
        }

        if (deskId != null) {
            Desk desk = deskService.getDeskById(deskId);
            booking.setDesk(desk);
        }

        Employee employee = employeeService.getEmployeeById(employeeId);
        booking.setEmployee(employee);

        if (bindingResult.hasErrors()) {
            if (deskId != null) {
                handleValidationErrors(bindingResult, model, booking);
                return A_NEW_DESKBOOKING;
            } else {
                handleValidationErrors(bindingResult, model, booking);
                return A_ADD_DESKBOOKING;
            }
        }

        try {
            this.deskBookingService.addDeskBooking(booking);
        } catch (DeskNotAvailableException e) {
            model.addAttribute("errorMessage", e.getMessage());
            if (deskId != null) {
                return A_NEW_DESKBOOKING;
            } else {
                return A_ADD_DESKBOOKING;
            }
        }

        return "redirect:" + ADMIN_VIEW_ALL_P;
    }


    /**
     * Displays the form for updating a specific desk booking in the admin view.
     *
     * <p>
     * This method retrieves the desk booking with the specified ID and adds it to the model.
     * It also converts the booking date to a format suitable for the Thymeleaf template and adds it to the model.
     * Additionally, it retrieves all time slots and adds unique start times and end times to the model.
     * If the booking is not found, it logs an error and redirects to the update desk booking view.
     * </p>
     *
     * @param id      The ID of the desk booking to be updated.
     * @param model   The model to which the desk booking, booking date, employees, desks, unique start times, and unique end times are added.
     * @return The view name for the update desk booking form in the admin view.
     * @throws ResourceNotFoundException If the desk booking with the specified ID is not found.
     *
     */
    @GetMapping(ADMIN_UPDATE)
    public String updateDeskBookingForm(@PathVariable Long id, Model model) throws ResourceNotFoundException{
        Optional<DeskBooking> booking = this.deskBookingService.getBookingById(id);

        if (booking.isEmpty()) {
            // Log an error message indicating that the booking was not found
            log.error("Desk booking with ID {} not found.", id);
            return "redirect:"+ A_ALL_DESKBOOKINGS;
        }

        // Convert the LocalDate to java.util.Date for the Thymeleaf template
        Date bookingDate = java.sql.Date.valueOf(booking.get().getDate());
        model.addAttribute("bookingDate", bookingDate);

        // Add booking, bookingDate, employee, desks, unique booking start and end times to the model
        model.addAttribute("booking", booking.get());
        model.addAttribute("bookingDate", bookingDate);
        if (!model.containsAttribute("errorMessage")) {
            model.addAttribute("errorMessage", null);
        }
        return A_UPDATE_DESKBOOKING;
    }


    /**
     * Handles the POST request to update a desk booking.
     *
     * @param booking         The desk booking object to be updated.
     * @param bindingResult   Results of the validation on the booking object.
     * @param id              The ID of the booking to be updated.
     * @param deskId          The ID of the desk associated with the booking.
     * @param employeeId      The ID of the employee associated with the booking.
     * @param date            The date of the booking in "yyyy-MM-dd" format.
     * @param model           The model to contain the Attributes to be passed to the redirected view.
     * @return ADMIN_VIEW_ALL_P The Constant representing the View to see all deskbookings.
     * @throws ResourceNotFoundException   If the desk is not found.
     * @throws EmployeeNotFoundException   If the employee is not found.
     * @throws DeskNotAvailableException   If the desk is not available.
     * @throws ExecutionException          If any execution exception occurs.
     * @throws InterruptedException        If the thread is interrupted.
     */
    @PostMapping(ADMIN_UPDATE_P)
    public String updateDeskBooking(@Valid DeskBooking booking, BindingResult bindingResult,
                                    @RequestParam("id") Long id, @RequestParam("desk.id") Long deskId,
                                    @RequestParam("employee.id") Long employeeId, @RequestParam("date") String date,
                                    Model model)
            throws ResourceNotFoundException, EmployeeNotFoundException, DeskNotAvailableException,
            ExecutionException, InterruptedException {

        // Check if there are validation errors in the booking object.
        if (bindingResult.hasErrors()) {
            handleValidationErrors(bindingResult, model, booking);
            return A_UPDATE_DESKBOOKING;
        } else {
            // Fetch the desk by its ID.
            Desk desk = deskService.getDeskById(deskId);
            if(desk == null) {
                throw new ResourceNotFoundException("Desk not found for id: " + deskId);
            }

            // Fetch the employee by its ID.
            Employee employee = employeeService.getEmployeeById(employeeId);
            if(employee == null) {
                throw new EmployeeNotFoundException("Employee not found for id: " + employeeId);
            }

            // Convert the date string to a LocalDate object.
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(date, formatter);

            // Set the updated attributes to the booking object.
            booking.setDate(localDate);
            booking.setDesk(desk);
            booking.setEmployee(employee);
            booking.setId(id);

            // Update the booking in the database.
            this.deskBookingService.updateBookingById(booking.getId(), booking);

            // Redirect to the view all bookings page.
            return "redirect:" + ADMIN_VIEW_ALL_P;
        }
    }


    /**
     * Displays the cancellation form for a specific desk booking in the admin view.
     *
     * <p>
     * This method retrieves the desk booking based on the provided ID and adds it to the model.
     * If the booking is not found, a ResourceNotFoundException is thrown. Additionally, if there's no error message attribute in the model,
     * it sets the "errorMessage" attribute to null.
     * </p>
     *
     * @param id The ID of the desk booking to be canceled.
     * @param model The model object to which the booking and any error messages are added.
     * @return The view name for the cancellation form in the admin view.
     * @throws ResourceNotFoundException If the desk booking with the specified ID is not found.
     */
    @GetMapping(ADMIN_CANCEL)
    public String cancelDeskBookingForm(@PathVariable Long id, Model model) throws ResourceNotFoundException {
        //Retrieve the desk booking from the database by its ID
        Optional<DeskBooking> booking = this.deskBookingService.getBookingById(id);

        // Check if the desk booking is present
        if (booking.isPresent()) {
            // Add the desk booking to the model for the view to access
            model.addAttribute("booking", booking.get());
            // Return the view name for the admin view of the specific desk booking
            return A_CANCEL_DESKBOOKING;
        } else {
            // Throw an exception if the desk booking is not found
            throw new ResourceNotFoundException("Desk booking with ID " + id + " not found.");
        }
    }

    /**
     * Handles the cancellation of a specific desk booking in the admin view.
     *
     * <p>
     * This method attempts to cancel (delete) a desk booking based on the provided ID. If the booking is successfully canceled,
     * it redirects to the cancellation confirmation view. If there's an error during the cancellation, an error message is added
     * to the redirect attributes and the user is redirected back to the cancellation form.
     * </p>
     *
     * @param id The ID of the desk booking to be canceled.
     * @param model The redirect attributes object to which any error messages are added.
     * @return The view name for the cancellation confirmation or the cancellation form in case of an error.
     * @throws ResourceNotFoundException If the desk booking with the specified ID is not found.
     */
    @PostMapping(ADMIN_CANCEL_P)
    public String cancelDeskBooking(@RequestParam("id") Long id, Model model) throws ResourceNotFoundException {
        try {
            this.deskBookingService.deleteBookingById(id);
            // If successful, redirect to the view all bookings page.
            return "redirect:" + ADMIN_VIEW_ALL_P;
        } catch (ResourceDeletionFailureException e) {
            log.error("Failed to cancel the desk booking with ID {}: {}", id, e.getMessage(), e);
            model.addAttribute("errorMessage", "Failed to cancel the desk booking.");
            return "redirect:" + ADMIN_CANCEL; // Redirect back to the cancellation form for the specific booking
        }
    }

    /**
     * Handles the retrieval of all desk bookings for display in the employee view.
     *
     * <p>
     * This method fetches all desk bookings and adds them to the model for rendering in the view. If no bookings are found,
     * an error message is added to the model and a ResourceNotFoundException is thrown.
     * </p>
     *
     * @param model The model object to which the desk bookings are added.
     * @return The view name for displaying all desk bookings in the employee view.
     * @throws ResourceNotFoundException If no desk bookings are found in the system.
     */
    @GetMapping(EMP_View_All)
    public String getBookings(Model model, Principal principal) throws ResourceNotFoundException {

        // Get the currently logged-in user's username
        String user = principal.getName();

        // Fetch the employee details using the logged-in user's username
        Employee employee = employeeService.findByNick(user);
        Long employeeId = employee.getId();

        // Fetch the bookings specific to the logged-in employee
        List<DeskBooking> myBookings = this.deskBookingService.getBookingsByEmployeeId(employeeId);


        if (myBookings.isEmpty()) {
            model.addAttribute("message", "No desk bookings are available.");
            throw new ResourceNotFoundException("No desk bookings found.");
        } else {
            model.addAttribute("myBookings", myBookings);
        }
        return E_MY_DESKBOOKING;
    }



    /**
     * Retrieves and displays the details of a specific desk booking for an employee.
     *
     * <p>
     * This method fetches the details of a desk booking based on the provided booking ID.
     * If the booking is found, its details are added to the model for rendering in the view.
     * If the booking is not found, a ResourceNotFoundException is thrown.
     * </p>
     *
     * @param id The unique identifier of the desk booking to be retrieved.
     * @param model The model object to which the desk booking details are added.
     * @return The view name for displaying the details of the desk booking in the employee view.
     * @throws ResourceNotFoundException If no desk booking with the specified ID is found in the system.
     */
    @GetMapping(EMP_VIEW_BOOKING)
    public String viewEDeskBooking(@PathVariable Long id, Model model) throws ResourceNotFoundException {
        Optional<DeskBooking> deskBooking = this.deskBookingService.getBookingById(id);
        if (deskBooking.isPresent()) {
            model.addAttribute("myDeskBooking", deskBooking.get());
            return E_VIEW_DESKBOOKING;
        }else{
            throw new ResourceNotFoundException("Desk booking with ID " + id + " was not found.");
        }
    }

    @GetMapping({EMP_NEW, EMP_ADD})
    public String eDeskBookingForm(@PathVariable(value = "deskId", required = false) Long deskId, Model model, DeskBooking flashDeskBooking, Principal principal) throws ResourceNotFoundException {
        // Get the currently logged-in user's username
        String user = principal.getName();

        // Fetch the employee details using the logged-in user's username
        Employee loggedInEmployee = employeeService.getEmployeeByNick(user);

        // Ensure that the employee is only allowed to create a booking for themselves
        if (flashDeskBooking != null && !flashDeskBooking.getEmployee().getId().equals(loggedInEmployee.getId())) {
            model.addAttribute("errorMessage", "You can only create a booking for yourself.");
            return ERROR; // or any other error page you have
        }

        if (deskId != null) {
            // Find the desk
            Desk desk = deskService.getDeskById(deskId);
            model.addAttribute("desk", desk);
            if (flashDeskBooking != null && flashDeskBooking.getId() != null) {
                model.addAttribute("deskBooking", flashDeskBooking);
            } else {
                model.addAttribute("deskBooking", new DeskBooking());
            }
            if (!model.containsAttribute("errorMessage")) {
                model.addAttribute("errorMessage", null);
            }
            return E_NEW_DESKBOOKING;
        } else {
            if (flashDeskBooking != null && flashDeskBooking.getId() != null) {
                model.addAttribute("deskBooking", flashDeskBooking);
            } else {
                model.addAttribute("deskBooking", new DeskBooking());
            }
            if (!model.containsAttribute("errorMessage")) {
                model.addAttribute("errorMessage", null);
            }
            return E_ADD_DESKBOOKING;
        }
    }

    @PostMapping({EMP_NEW, EMP_ADD})
    public String empNewDeskBooking(@ModelAttribute("deskBooking") @Valid DeskBooking booking, Principal principal, BindingResult bindingResult, @RequestParam(value = "desk.id", required = false) Long deskId, Model model) throws ResourceNotFoundException, EmployeeNotFoundException, DeskNotAvailableException, InterruptedException, ExecutionException {
        if (deskId == null && booking.getDesk().getId() == null) {
            model.addAttribute("errorMessage", "Desk ID is missing.");
            return E_ADD_DESKBOOKING;
        }

        if (deskId != null) {
            Desk desk = deskService.getDeskById(deskId);
            booking.setDesk(desk);
        }

        // Get the currently logged-in user's username
        String user = principal.getName();

        // Fetch the employee details using the logged-in user's username
        Employee employee = employeeService.getEmployeeByNick(user);
        booking.setEmployee(employee);

        if (bindingResult.hasErrors()) {
            if (deskId != null) {
                handleValidationErrors(bindingResult, model, booking);
                return E_NEW_DESKBOOKING;
            } else {
                handleValidationErrors(bindingResult, model, booking);
                return E_ADD_DESKBOOKING;
            }
        }

        try {
            this.deskBookingService.addDeskBooking(booking);
        } catch (DeskNotAvailableException e) {
            model.addAttribute("errorMessage", e.getMessage());
            if (deskId != null) {
                return E_NEW_DESKBOOKING;
            } else {
                return E_ADD_DESKBOOKING;
            }
        }

        return "redirect:" + EMP_View_All_P;
    }

    @GetMapping(EMP_UPDATE)
    public String updateEDeskBookingForm(@PathVariable Long id, Model model, Principal principal) throws ResourceNotFoundException {
        Optional<DeskBooking> booking = this.deskBookingService.getBookingById(id);

        if (booking.isEmpty()) {
            log.error("booking with ID {} not found.", id);
            return "redirect:" + EMP_UPDATE;
        }

        // Check if the logged-in user is authorized to update the booking
        String user = principal.getName();
        Employee loggedInEmployee = employeeService.getEmployeeByNick(user);
        if (!booking.get().getEmployee().getId().equals(loggedInEmployee.getId())) {
            // Not authorized
            return "redirect:" + EMP_View_All_P; // Redirect to the list of all bookings
        }

        Date bookingDate = java.sql.Date.valueOf(booking.get().getDate());
        model.addAttribute("bookingDate", bookingDate);
        model.addAttribute("booking", booking.get());

        if (!model.containsAttribute("errorMessage")) {
            model.addAttribute("errorMessage", null);
        }
        return E_UPDATE_DESKBOOKING;
    }

    @PostMapping(PathConstants.EMP_UPDATE_P)
    public String updateEDeskBooking(@Valid DeskBooking booking, BindingResult bindingResult, Principal principal) throws ResourceNotFoundException, ExecutionException, InterruptedException, EmployeeNotFoundException, DeskNotAvailableException {
        if (bindingResult.hasErrors()) {
            System.out.println("Errors: " + bindingResult.getAllErrors());
            return E_UPDATE_DESKBOOKING;
        }

        Long deskId = booking.getDesk().getId();
        Desk desk = deskService.getDeskById(deskId);
        if (desk == null) {
            throw new ResourceNotFoundException("desk not found for id: " + deskId);
        }

        Long employeeId = booking.getEmployee().getId();
        Employee employee = employeeService.getEmployeeById(employeeId);
        if (employee == null) {
            throw new EmployeeNotFoundException("employee not found for id: " + employeeId);
        }

        // Check if the logged-in user is authorized to update the booking
        String user = principal.getName();
        Employee loggedInEmployee = employeeService.getEmployeeByNick(user);
        if (!employeeId.equals(loggedInEmployee.getId())) {
            // Not authorized
            return "redirect:" + EMP_View_All_P; // Redirect to the list of all bookings
        }

        // Since the date is already bound to the booking object, you don't need to parse it again.
        // If you need to format it, you can do so, but it's not necessary for saving to the database.

        booking.setDesk(desk);
        booking.setEmployee(employee);

        this.deskBookingService.updateBooking(booking);
        return "redirect:" + EMP_View_All_P; // Redirect to the list of all bookings
    }

    /**
     * Retrieves and displays the desk booking history for a specific employee.
     *
     * <p>
     * This method fetches the employee details based on the authenticated user's nickname.
     * It then retrieves the desk booking history for the specified employee ID.
     * The employee details and booking history are added to the model for rendering in the view.
     * </p>
     *
     * @param model The model object to which attributes are added for rendering in the view.
     * @param id The unique identifier of the employee whose booking history is to be retrieved.
     * @param principal The authenticated user's principal object containing user details.
     * @return The name of the view that displays the desk booking history for the employee.
     * @throws ResourceNotFoundException If the specified employee or booking history cannot be located.
     */
    @GetMapping(EMP_VIEW_HISTORY)
    public String getMyDeskBookingHistory(Model model, @PathVariable Long id, Principal principal) throws ResourceNotFoundException {
        Employee employee = this.employeeService.getEmployeeByNick(principal.getName());

        // Check if the logged-in employee's ID matches the ID in the URL
        if (!employee.getId().equals(id)) {
            model.addAttribute("errorMessage", "Unauthorized access.");
            return "redirect:"+EMP_View_All;
        }

        List<DeskBooking> myBookingHistory = this.deskBookingService.getBookingHistoryByEmployeeId(id);
        model.addAttribute("employee", employee);
        model.addAttribute("myBookingHistory", myBookingHistory);
        if (!model.containsAttribute("errorMessage")) {
            model.addAttribute("errorMessage", null);
        }
        return E_HISTORY_DESKBOOKING;
    }

    /**
     * Presents the form to the user for canceling a specific desk booking in the employee view.
     *
     * <p>
     * This method fetches the desk booking details based on the provided booking ID.
     * The booking details are then added to the model for rendering in the view.
     * If the model does not already contain an "errorMessage" attribute, a default value of null is set.
     * </p>
     *
     * @param id The unique identifier of the desk booking to be canceled.
     * @param model The model object to which attributes are added for rendering in the view.
     * @return The name of the view that displays the cancellation form for the desk booking.
     * @throws ResourceNotFoundException If the specified desk booking cannot be located.
     */
    @GetMapping(EMP_CANCEL)
    public String cancelEDeskBookingForm(@PathVariable Long id, Model model) throws ResourceNotFoundException {
        Optional<DeskBooking> booking = this.deskBookingService.getBookingById(id);
        if (booking.isPresent()) {
            model.addAttribute("booking", booking.get());
            return E_CANCEL_DESKBOOKING;
        }else{
            model.addAttribute("errorMessage","Desk Booking with id:" + id + " was not found");
            return "redirect:" + E_MY_DESKBOOKING;
        }
    }


    /**
     * Handles the cancellation of a specific desk booking.
     *
     * <p>
     * This method attempts to cancel the desk booking based on the provided booking ID.
     * In case of any issues during the cancellation, appropriate error messages are set
     * in the redirect attributes to notify the user.
     * </p>
     *
     * @param id The unique identifier of the desk booking to be canceled.
     * @param model Attributes used to store flash attributes for the next request.
     * @return The name of the view that displays the result of the cancellation action.
     */
    @PostMapping(EMP_CANCEL)
    public String cancelEDeskBooking(@PathVariable Long id, Model model) throws ResourceNotFoundException {
        try {
            this.deskBookingService.deleteBookingById(id);
            return "redirect:"+ EMP_View_All_P ;
        } catch (ResourceDeletionFailureException e) {
            log.error("Failed to cancel the desk booking with ID {}: {}", id, e.getMessage(), e);
            model.addAttribute("errorMessage", "Failed to cancel the desk booking.");
            return "redirect:"+EMP_CANCEL;
        }
    }


    /**
     Displays the error page.
     @return The view for the error page.
     */
    @GetMapping(ERROR)
    public String getError() {
        return ERRORVIEW;
    }
}