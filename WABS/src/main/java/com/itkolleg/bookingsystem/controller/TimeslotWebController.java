package com.itkolleg.bookingsystem.controller;

import com.itkolleg.bookingsystem.domains.Timeslot;
import com.itkolleg.bookingsystem.exceptions.ResourceDeletionFailureException;
import com.itkolleg.bookingsystem.exceptions.ResourceNotFoundException;
import com.itkolleg.bookingsystem.service.timeslot.TimeslotService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalTime;
import java.util.Optional;

/**

 This class represents a controller for managing timeslot within the booking system.
 It handles operations such as retrieving timeslot, adding timeslot, updating timeslot,
 and deleting timeslot.
 <p>It interacts with the {@link TimeslotService} to perform the necessary operations.
 <p>The controller is responsible for handling all HTTP requests related to timeslot.
 <p>Note: This controller is designed to work with the Web interface of the booking system.
 <p>The endpoints provided by this controller are:
 <ul>
 <li>/web/timeslot - Retrieves all timeslot</li>
 <li>/web/timeslot/view/{id} - Retrieves the details of a specific timeslot</li>
 <li>/web/timeslot/add - Displays the form for adding a timeslot</li>
 <li>/web/timeslot/add - Adds a new timeslot based on the submitted form data</li>
 <li>/web/timeslot/update/{id} - Displays the form for updating a specific timeslot</li>
 <li>/web/timeslot/update - Updates a specific timeslot based on the submitted form data</li>
 <li>/web/timeslot/delete/{id} - Displays the form for deleting a specific timeslot</li>
 <li>/web/timeslot/delete/{id} - Deletes a specific timeslot</li>
 <li>/web/timeslot/error - Displays the error page</li>
 </ul>
 <p>It also includes various model attribute methods to provide data for the views, such as the list of timeslot.
 <p>Note: This controller assumes the use of Spring Framework for building web applications.
 @author Sonja Lechner
 @version 1.0
 @since 2023-08-07 */

@Controller
@RequestMapping("/web/timeslots")
public class TimeslotWebController {

    private static final Logger logger = LoggerFactory.getLogger(TimeslotWebController.class);

    private final TimeslotService timeslotService;

    /**
     * Constructs a new time slotWebController with the specified time slotService.
     *
     * @param timeslotService the time slotService to be used
     */
    public TimeslotWebController(TimeslotService timeslotService) {
        this.timeslotService = timeslotService;
    }

    /**
     * Retrieves all time slots and adds them to the model.
     *
     * @param model the model to be used
     * @return the view name for displaying all time slots
     */
    @GetMapping
    public String getAllTimeslots(Model model) {
        model.addAttribute("viewAllTimeslots", this.timeslotService.getAllTimeslots());
        return "timeslots/allTimeslots";
    }

    /**
     * Displays the form for adding a new time slot.
     *
     * @param model the model to be used
     * @return the view name for adding a time slot
     */
    @GetMapping("/add")
    public String addTimeslotsForm(Model model) {
        model.addAttribute("timeslot", new Timeslot(LocalTime.of(8, 0), LocalTime.of(12, 30), "AM"));
        return "timeslot/addTimeslot";
    }

    /**
     * Adds a new time slot based on the submitted form data.
     *
     * @param timeslot            the time slot to be added
     * @param bindingResult       the binding result for validation
     * @param redirectAttributes  the redirect attributes for flash messages
     * @return the redirect URL after adding the time slot
     */
    @PostMapping("/add")
    public String addTimeslot(@Valid Timeslot timeslot, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            logger.warn("Attempted to add a new timeslot but validation failed.");
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create new time slot!");
            return "timeslot/addTimeslot";
        } else {
            this.timeslotService.addTimeslot(timeslot);
            logger.info("Successfully added new timeslot with ID: {}", timeslot.getId());
            redirectAttributes.addFlashAttribute("message", "New time slot added successfully!");
            return "redirect:/web/timeslots";
        }
    }

    /**
     * Displays the details of a specific time slot.
     *
     * @param id     the ID of the time slot to be viewed
     * @param model  the model to be used
     * @throws ResourceNotFoundException in case the time slot isn't found
     * @return the view for displaying the details of a time slot
     */
    @GetMapping("/view/{id}")
    public String viewTimeslot(@PathVariable Long id, Model model) throws ResourceNotFoundException{
        Optional<Timeslot> timeslot = this.timeslotService.getTimeslotById(id);
        if (timeslot.isPresent()) {
            model.addAttribute("myTimeslot", timeslot);
            return "timeslot/viewTimeslot";
        } else {
            // If Time slot is not present, throw exception
            throw new ResourceNotFoundException("Time slot with id: " + id + " not found");
        }
    }

    /**
     * Displays the form for updating a specific time slot.
     *
     * @param id     the ID of the time slot to be updated
     * @param model  the model to be used
     * @throws ResourceNotFoundException in case the time slot isn't found
     * @return the view name for updating a time slot
     */
    @GetMapping("/update/{id}")
    public String updateTimeslotForm(@PathVariable Long id, Model model) throws ResourceNotFoundException {
        Optional<Timeslot> timeslot = this.timeslotService.getTimeslotById(id);
        if (timeslot.isPresent()) {
            model.addAttribute("updatedTimeslot", timeslot.get());
            return "timeslot/updateTimeslot";
        } else {
            logger.warn("Attempted to update a non-existent time slot with id: {}", id);
            throw new ResourceNotFoundException("The time slot with id: " + id + " could not be updated because it does not exist.");
        }
    }

    /**
     * Updates a specific time slot based on the submitted form data.
     *
     * @param timeslot            the updated time slot
     * @param bindingResult       the binding result for validation
     * @param redirectAttributes  the redirect attributes for flash messages
     * @return the redirect URL after updating the time slot
     */
    @PostMapping("/update")
    public String updateTimeslot(@Valid Timeslot timeslot, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            logger.warn("Attempted to update timeslot with ID: {} but validation failed.", timeslot.getId());
            return "timeslot/updateTimeslot";
        } else {
            this.timeslotService.updateTimeslot(timeslot);
            logger.info("Successfully updated timeslot with ID: {}", timeslot.getId());
            redirectAttributes.addFlashAttribute("message", "Time slot updated successfully!");
            return "redirect:/web/timeslots";
        }
    }

    /**
     * Displays the form for deleting a specific time slot.
     *
     * @param id     the ID of the time slot to be deleted
     * @param model  the model to be used
     * @throws ResourceNotFoundException in case the time slot is not found
     * @throws ResourceDeletionFailureException in case the time slot deletion fails
     * @return the view name for deleting a time slot
     */

    //TODO
    //resolve exception handling issue so that the correct exception is return is the Timeslot is a part of an existing booking
    @GetMapping("/delete/{id}")
    public String deleteTimeslotForm(@PathVariable Long id, Model model) throws ResourceNotFoundException, ResourceDeletionFailureException {
        Optional<Timeslot> timeslot = this.timeslotService.getTimeslotById(id);
        if (timeslot.isPresent()) {
            model.addAttribute("timeslot", timeslot.get());
            return "timeslot/deleteTimeslot";
        } else {
            if (timeslot.isEmpty()) {
                logger.warn("Attempted to delete a non-existent time slot with id: {}", id);
                throw new ResourceNotFoundException("The time slot with id: " + id + " you tried to delete does not exist.");
            }else{
                logger.warn("Attempt to delete time slot with id: {} failed!", id);
                throw new ResourceDeletionFailureException("The time slot with id: " + id + " could not be deleted. It might be a part of a booking");
            }
        }
    }

    /**
     * Deletes a specific time slot.
     *
     * @param id                   the ID of the time slot to be deleted
     * @param redirectAttributes  the redirect attributes for flash messages
     * @return the redirect URL after deleting the time slot
     */
    @PostMapping("/delete/{id}")
    public String deleteTimeslot(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (this.timeslotService.getTimeslotById(id).isPresent()) {
            this.timeslotService.deleteTimeslotById(id);
            logger.info("Successfully deleted time slot with ID: {}", id);
            redirectAttributes.addFlashAttribute("message", "Time slot deleted successfully!");
        } else {
            logger.warn("Attempted to delete time slot with ID: {} but it doesn't exist.", id);
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete the time slot.");
        }
        return "redirect:/web/timeslots";
    }

    /**
     * Displays the error page.
     *
     * @return the view for the error page
     */
    @GetMapping("/error")
    public String getError(@ModelAttribute("errorCode") String errorCode, @ModelAttribute("errorMessage") String errorMessage, Model model) {
        model.addAttribute("errorCode", errorCode);
        model.addAttribute("errorMessage", errorMessage);
        return "error";
    }

}
