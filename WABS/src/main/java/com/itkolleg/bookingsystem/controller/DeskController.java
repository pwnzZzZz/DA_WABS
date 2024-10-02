package com.itkolleg.bookingsystem.controller;

import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.exceptions.ResourceDeletionFailureException;
import com.itkolleg.bookingsystem.exceptions.ResourceNotFoundException;
import com.itkolleg.bookingsystem.service.desk.DeskService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.itkolleg.bookingsystem.domains.PathConstants.*;
import static com.itkolleg.bookingsystem.domains.PathConstants.ERROR;
import static com.itkolleg.bookingsystem.domains.PathConstants.VIEW_DESK;
import static com.itkolleg.bookingsystem.domains.ViewConstants.*;

@Controller
@Slf4j
@RequestMapping("/web/desks")
public class DeskController {

    private final DeskService deskService;

    /**
     * Constructs a new DeskController with the specified DeskService.
     *
     * @param deskService the DeskService to be used
     */
    public DeskController(DeskService deskService) {
        this.deskService = deskService;
    }

    void handleValidationErrors(BindingResult bindingResult, Model model, Desk desk) {
        log.warn("Validation errors: {}", bindingResult.getAllErrors());

        List<String> errors = bindingResult.getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                .collect(Collectors.toList());

        model.addAttribute("validationErrors", errors);
        model.addAttribute("desk", desk);
    }

    /**
     * Retrieves all desks and adds them to the model.
     *
     * @param model the model to be used
     * @return the view name for displaying all desks
     */
    @GetMapping(VIEW_ALL)
    public String getAllDesks(Model model) {
        model.addAttribute("viewAllDesks", this.deskService.getAllDesks());
        return ALL_DESKS;
    }

    /**
     * Retrieves all desks and adds them to the model.
     *
     * @param model the model to be used
     * @return the view name for displaying all desks
     */
    @GetMapping(VIEW_ALL_EMP)
    public String getAllEDesks(Model model) {
        model.addAttribute("viewAllDesks", this.deskService.getAllDesks());
        return ALL_DESKS_EMP;
    }


    /**
     * Displays the details of a specific desk.
     *
     * @param id     the ID of the desk to be viewed
     * @param model  the model to be used
     * @return the view name for viewing a desk
     */
    @GetMapping(VIEW_DESK)
    public String viewDesk(@PathVariable Long id, Model model) throws ResourceNotFoundException {
        try {
            Desk desk = this.deskService.getDeskById(id);
            if (desk != null) {
                model.addAttribute("myDesk", desk);
                return DESK_VIEW;
            }
        } catch (ResourceNotFoundException e) {
            log.error("Error: Desk with ID {} was not found.", id, e);
            throw e;
        }
        return "redirect:" + VIEW_ALL;
    }


    /**
     * Displays the form for adding a new desk.
     *
     * @param model the model to be used
     * @return the view name for adding a desk
     */
    @GetMapping(ADD)
    public String addDeskForm(Model model, @ModelAttribute ("desk") Desk flashDesk) {
        if (flashDesk!= null && flashDesk.getId() != null) {
            model.addAttribute("desk", flashDesk);
        }else {
            model.addAttribute("desk", new Desk());
        }
        // Check if the model already contains an "errorMessage" attribute
        if (!model.containsAttribute("errorMessage")) {
            // If not, add a null "errorMessage" attribute to the model
            model.addAttribute("errorMessage", null);
        }
        return ADD_DESK;
    }

    /**
     * Adds a new desk based on the submitted form data.
     *
     * @param desk                the desk to be added
     * @param model               the model
     * @return the redirect URL after adding the desk
     */
    @PostMapping(ADD)
    public String addDesk(@ModelAttribute("desk") @Valid Desk desk, Model model) {
        if (desk == null){
                model.addAttribute("errorMessage", "Desk ID is missing.");
                return ADD_DESK;
        } else {
            this.deskService.addDesk(desk);
            model.addAttribute("message", "Desk added successfully!");
            return "redirect:"+ VIEW_ALL_P;
        }
    }

    /**
     * Displays the form for updating a specific desk.
     *
     * @param id     the ID of the desk to be updated
     * @param model  the model to be used
     * @return the view name for updating a desk
     */
    @GetMapping(UPDATE)
    public String updateDeskForm(@PathVariable Long id, Model model){
        try {
            Desk desk = this.deskService.getDeskById(id);
            model.addAttribute("updatedDesk", desk);
            if (!model.containsAttribute("errorMessage")) {
                model.addAttribute("errorMessage", null);
            }
            return UPDATE_DESK;
        } catch (ResourceNotFoundException e) {
            log.error("Error: Desk with ID {} was not found while trying to update.", id, e);
            return "redirect:" + VIEW_ALL;
        }
    }

    /**
     * Updates a specific desk based on the submitted form data.
     *
     * @param desk                the updated desk
     * @param bindingResult       the binding result for validation
     * @param model  the redirect attributes for flash messages
     * @return the redirect URL after updating the desk
     */
    @PostMapping(UPDATE_P)
    public String updateDesk(@Valid Desk desk, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            handleValidationErrors(bindingResult, model, desk);
            return "redirect:" + UPDATE_DESK;
        } else {
            try {
                this.deskService.updateDesk(desk);
                model.addAttribute("message", "desk updated successfully!");
                return "redirect:" + VIEW_ALL_P;
            } catch (ResourceNotFoundException e) {
                log.error("Error: Desk with ID {} was not found while trying to update.", desk.getId(), e);
                if (!model.containsAttribute("errorMessage")) {
                    model.addAttribute("errorMessage", null);
                }
                return "redirect:" + UPDATE_DESK;
            }
        }
    }




    /**
     * Displays the form for deleting a specific desk.
     *
     * @param id     the ID of the desk to be deleted
     * @param model  the model to be used
     * @return the view name for deleting a desk
     * @throws ResourceNotFoundException if the desk with the given ID is not found
     */
    @GetMapping(DELETE)
    public String deleteDeskForm(@PathVariable Long id, Model model) throws ResourceNotFoundException {
        Desk desk = this.deskService.getDeskById(id);
        model.addAttribute("desk", desk);
        return DELETE_DESK;
    }

    /**
     * Deletes a specific desk.
     *
     * @param id                   the ID of the desk to be deleted
     * @param model the redirect attributes for flash messages
     * @return the redirect URL after deleting the desk
     */
    @PostMapping(DELETE)
    public String deleteDesk(@PathVariable Long id, Model model) {
        try {
            this.deskService.deleteDeskById(id);
            return "redirect:" + VIEW_ALL_P;
        }catch (ResourceDeletionFailureException e) {
            log.error("Error: Failed to delete the desk with ID {}.", id, e);
            model.addAttribute("errorMessage", "Failed to delete the desk.");
            return "redirect:" + DELETE_DESK;
        }
    }

    /**
     * Displays the error page.
     *
     * @return the view for the error page
     */
    @GetMapping(ERROR)
    public String getError() {
        return ERRORVIEW;
    }
}
