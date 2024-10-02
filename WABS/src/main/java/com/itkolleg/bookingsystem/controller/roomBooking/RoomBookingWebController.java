package com.itkolleg.bookingsystem.controller.roomBooking;

import com.itkolleg.bookingsystem.domains.*;
import com.itkolleg.bookingsystem.domains.booking.RoomBooking;
import com.itkolleg.bookingsystem.exceptions.employeeExceptions.EmployeeNotFoundException;
import com.itkolleg.bookingsystem.exceptions.roomExceptions.RoomDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.roomExceptions.RoomNotAvailableException;
import com.itkolleg.bookingsystem.exceptions.roomExceptions.RoomNotFoundException;
import com.itkolleg.bookingsystem.service.employee.EmployeeService;
import com.itkolleg.bookingsystem.service.room.RoomService;
import com.itkolleg.bookingsystem.service.roombooking.RoomBookingService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/web/roomBooking")
public class RoomBookingWebController {

    RoomBookingService roomBookingService;
    RoomService roomService;
    EmployeeService employeeService;

    /**
     * Constructor for RoomBookingWebController.
     *
     * @param roomBookingService The RoomBookingService to be used.
     * @param roomService The RoomService to be used.
     * @param employeeService The EmployeeService to be used.
     */
    public RoomBookingWebController(RoomBookingService roomBookingService, RoomService roomService, EmployeeService employeeService) {
        this.roomBookingService = roomBookingService;
        this.roomService = roomService;
        this.employeeService = employeeService;
    }

    /**
     * Get mapping for retrieving all room bookings of an employee.
     *
     * @return ModelAndView object containing the view and the list of room bookings.
     * @throws EmployeeNotFoundException if the employee is not found.
     */
    @GetMapping("/allBookingsEmployee")
    public ModelAndView allBookingsEmployee() throws EmployeeNotFoundException {
        //TODO: Hilfsmethode schreiben um Code-Duplikate zu vermeiden
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            throw new EmployeeNotFoundException("employee not found");
        }

        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        Employee employee = this.employeeService.getEmployeeByNick(username);

        List<RoomBooking> bookings = roomBookingService.getBookingsByEmployee(employee);
        return new ModelAndView("roomBooking/viewRoomBookingsEmployee", "bookings", bookings);
    }

    /**
     * Get mapping for retrieving all room bookings.
     *
     * @return ModelAndView object containing the view and the list of room bookings.
     */
    @GetMapping("/allBookings")
    public ModelAndView allBookings(){
        List<RoomBooking> bookings = roomBookingService.getAllBookings();
        return new ModelAndView("roomBooking/viewRoomBookings", "bookings", bookings);
    }

    /**
     * Get mapping for creating a room booking for an employee.
     *
     * @param id The ID of the room to be booked.
     * @param model The Model object for the view.
     * @return ModelAndView object containing the view and the new booking.
     * @throws RoomNotFoundException if the room is not found.
     * @throws ExecutionException if an execution error occurs.
     * @throws InterruptedException if the current thread is interrupted while waiting.
     * @throws EmployeeNotFoundException if the employee is not found.
     */
    @GetMapping("/createBookingEmployee/{id}")
    public ModelAndView createBookingEmployee(@PathVariable Long id, Model model) throws RoomNotFoundException, ExecutionException, InterruptedException, EmployeeNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            throw new EmployeeNotFoundException("employee not found");
        }

        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        Employee employee = this.employeeService.getEmployeeByNick(username);

        RoomBooking booking = new RoomBooking();
        Room room = this.roomService.getRoomById(id);
        booking.setRoom(room);
        booking.setEmployee(employee);

        model.addAttribute("newBooking", booking);
        return new ModelAndView("roomBooking/createRoomBookingEmployee", "Booking", model);
    }

    /**
     * Post mapping for creating a room booking for an employee.
     *
     * @param booking The roombooking object to be created.
     * @param bindingResult The BindingResult object for data binding and validation.
     * @return Redirect URL after creating the booking.
     * @throws RoomNotAvailableException if the room is not available.
     * @throws RoomNotFoundException if the room is not found.
     */
    @PostMapping("/createBookingEmployee")
    public String createBookingEmployee(@Valid RoomBooking booking, BindingResult bindingResult) throws RoomNotAvailableException, RoomNotFoundException {
        if (bindingResult.hasErrors()) {
            return "redirect:/web/roomBooking/createBookingEmployee/" + booking.getRoom().getId();
        } else {
            this.roomBookingService.addRoomBooking(booking);
            return "redirect:/web/rooms/allRoomsEmployee";
        }
    }

    /**
     * Get mapping for creating a room booking for an admin.
     *
     * @param id The ID of the room to be booked.
     * @param model The Model object for the view.
     * @return ModelAndView object containing the view and the new booking.
     * @throws RoomNotFoundException if the room is not found.
     * @throws ExecutionException if an execution error occurs.
     * @throws InterruptedException if the current thread is interrupted while waiting.
     * @throws EmployeeNotFoundException if the employee is not found.
     */
    @GetMapping("/createBooking/{id}")
    public ModelAndView createBookingAdmin(@PathVariable Long id, Model model) throws RoomNotFoundException, ExecutionException, InterruptedException, EmployeeNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            throw new EmployeeNotFoundException("employee not found");
        }

        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        Employee employee = this.employeeService.getEmployeeByNick(username);

        RoomBooking booking = new RoomBooking();
        Room room = this.roomService.getRoomById(id);
        booking.setRoom(room);
        booking.setEmployee(employee);
        model.addAttribute("newBooking", booking);
        return new ModelAndView("roomBooking/createRoomBooking", "Booking", model);
    }

    /**
     * Post mapping for creating a room booking for an admin.
     *
     * @param booking The roombooking object to be created.
     * @param bindingResult The BindingResult object for data binding and validation.
     * @return Redirect URL after creating the booking.
     * @throws RoomNotAvailableException if the room is not available.
     * @throws RoomNotFoundException if the room is not found.
     */
    @PostMapping("/createBooking")
    public String createBookingAdmin(@Valid RoomBooking booking, BindingResult bindingResult) throws RoomNotAvailableException, RoomNotFoundException {
        if (bindingResult.hasErrors()) {
            return "redirect:/web/roomBooking/createBooking/" + booking.getRoom().getId();
        } else {
            this.roomBookingService.addRoomBooking(booking);
            return "redirect:/web/rooms/allRooms";
        }
    }

    /**
     * Get mapping for updating a room booking.
     *
     * @param id The ID of the booking to be updated.
     * @param model The Model object for the view.
     * @return ModelAndView object containing the view and the booking to be updated.
     * @throws RoomNotFoundException if the room is not found.
     * @throws ExecutionException if an execution error occurs.
     * @throws InterruptedException if the current thread is interrupted while waiting.
     * @throws RoomNotFoundException if the room is not found.
     * @throws EmployeeNotFoundException if the employee is not found.
     */
    @GetMapping("/updateBooking/{id}")
    public ModelAndView updateBooking(@PathVariable Long id, Model model) throws ExecutionException, InterruptedException, RoomNotFoundException, EmployeeNotFoundException {
        RoomBooking booking = this.roomBookingService.getBookingById(id);
        model.addAttribute("updateBooking", booking);
        return new ModelAndView("roomBooking/editRoomBooking", "Booking", model);
    }

    /**
     * Post mapping for updating a room booking.
     *
     * @param booking The updated roombooking object.
     * @param bindingResult The BindingResult object for data binding and validation.
     * @return Redirect URL after updating the booking.
     * @throws ExecutionException if an execution error occurs.
     * @throws InterruptedException if the current thread is interrupted while waiting.
     * @throws RoomNotFoundException if the room is not found.
     * @throws RoomNotAvailableException if the room is not available.
     * @throws RoomNotFoundException if the room is not found.
     * @throws EmployeeNotFoundException if the employee is not found.
     */
    @PostMapping("/updateBooking")
    public String updateBooking(@Valid RoomBooking booking, BindingResult bindingResult) throws ExecutionException, InterruptedException, RoomNotAvailableException, RoomNotFoundException, EmployeeNotFoundException {
        if (bindingResult.hasErrors()) {
            return "/roomBooking/editRoomBooking";
        } else {
            this.roomBookingService.updateBooking(booking);
            return "redirect:/web/rooms/allRooms";
        }
    }

    /**
     * Get mapping for deleting a room booking of an employee.
     *
     * @param id The ID of the booking to be deleted.
     * @return Redirect URL after deleting the booking.
     */
    @GetMapping("/deleteBookingEmployee/{id}")
    public String deleteBookingEmployee(@PathVariable Long id) {
        try {
            this.roomBookingService.deleteBookingById(id);
            System.out.println("BUSENBUSENBUSENBUSEN");
            return "redirect:/web/roomBooking/allBookingsEmployee";
        } catch (RoomNotFoundException | RoomDeletionNotPossibleException e) {
            return "redirect:/web/roomBooking/allBookingsEmployee";
        }
    }

    /**
     * Get mapping for deleting a room booking of an admin.
     *
     * @param id The ID of the booking to be deleted.
     * @return Redirect URL after deleting the booking.
     * @throws RoomNotFoundException if the room is not found.
     * @throws RoomDeletionNotPossibleException if the room deletion is not possible.
     */
    @GetMapping("/deleteBooking/{id}")
    public String deleteBookingAdmin(@PathVariable Long id) throws RoomNotFoundException, RoomDeletionNotPossibleException {
        this.roomBookingService.deleteBookingById(id);
        return "redirect:/web/roomBooking/allBookings";
    }
}
