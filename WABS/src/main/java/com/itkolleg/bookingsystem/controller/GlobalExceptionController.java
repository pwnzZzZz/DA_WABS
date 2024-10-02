package com.itkolleg.bookingsystem.controller;

import com.itkolleg.bookingsystem.domains.ErrorCode;
import com.itkolleg.bookingsystem.exceptions.*;
import com.itkolleg.bookingsystem.exceptions.employeeExceptions.EmployeeAlreadyExistsException;
import com.itkolleg.bookingsystem.exceptions.employeeExceptions.EmployeeDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.employeeExceptions.EmployeeNotFoundException;
import com.itkolleg.bookingsystem.exceptions.employeeExceptions.EmployeeValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;
import java.util.stream.Collectors;

import static com.itkolleg.bookingsystem.domains.ViewConstants.*;

/**
 *
 * This class handles global exceptions for the booking system.
 * @author Sonja Lechner, Marcel Schranz
 * @version 1.3
 * @since 2023-09-03
 */

@Slf4j
@Order(1)
@ControllerAdvice
public class GlobalExceptionController {

    /**
     * Handles custom illegal argument exceptions and reirects accordingly.
     *
     * @param redirectAttributes The RedirectAttributes object to add flash attributes.
     * @return A string representing the redirect path.
     */
    @ExceptionHandler(CustomIllegalArgumentException.class)
    public String handleCustomIllegalArgumentException(CustomIllegalArgumentException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return switch (ex.getOrigin()) {
            case "AdminAddDeskBooking" -> "redirect:" + A_ADD_DESKBOOKING;
            case "AdminNewDeskBooking" -> "redirect:" + A_NEW_DESKBOOKING;
            case "AdminUpdateDeskBooking" -> "redirect:" + A_UPDATE_DESKBOOKING;
            case "EmpAddDeskBooking" -> "redirect:" + E_ADD_DESKBOOKING;
            case "EmpNewDeskBooking" -> "redirect:" + E_NEW_DESKBOOKING;
            case "EmpUpdateDeskBooking" -> "redirect:" + E_UPDATE_DESKBOOKING;
            default -> "redirect:/error";
        };
    }

    /**
     * Centralizes the logic for handling exceptions and redirecting to the error page.
     */
    private String handleExceptionAndRedirect(Exception e, ErrorCode errorCode, RedirectAttributes redirectAttributes) {
        return handleExceptionAndRedirect(e, errorCode, redirectAttributes, e.getMessage());
    }

    /**
     * Centralizes the logic for handling exceptions, setting error messages, and redirecting to the error page.
     */
    private String handleExceptionAndRedirect(Exception e, ErrorCode errorCode, RedirectAttributes redirectAttributes, String errorMessage) {
        log.error("{}: {}", e.getClass().getSimpleName(), e.getMessage(), e);
        redirectAttributes.addFlashAttribute("errorCode", errorCode.getCode());
        redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        return "redirect:/error";
    }

    /**
     * Map of exceptions to their error codes.
     */
    private static final Map<Class<? extends Exception>, ErrorCode> ERROR_CODE_MAP = Map.of(
            ResourceNotFoundException.class, ErrorCode.RESOURCE_NOT_FOUND,
            ResourceDeletionFailureException.class, ErrorCode.RESOURCE_DELETION_FAILURE,
            DeskNotAvailableException.class, ErrorCode.DESK_NOT_AVAILABLE,
            EmptyResultDataAccessException.class, ErrorCode.DATA_ACCESS_ERROR,
            MethodArgumentNotValidException.class, ErrorCode.METHOD_ARGUMENT_NOT_VALID
    );

    /**
     * Handles exceptions that require a redirect to the error page.
     */
    @ExceptionHandler({ResourceNotFoundException.class, ResourceDeletionFailureException.class,
            DeskNotAvailableException.class, EmptyResultDataAccessException.class, IllegalArgumentException.class})
    public String handleRedirectExceptions(Exception e, RedirectAttributes redirectAttributes) {
        return handleExceptionAndRedirect(e, ERROR_CODE_MAP.get(e.getClass()), redirectAttributes);
    }

    /**
     * Handles validation exceptions and redirects to the error page.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleMethodArgumentNotValid(MethodArgumentNotValidException e, RedirectAttributes redirectAttributes) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));
        redirectAttributes.addFlashAttribute("errorCode", ErrorCode.METHOD_ARGUMENT_NOT_VALID.getCode());
        redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        return "redirect:/error";
    }

    /**
     * Handles general exceptions and redirects to the error page.
     */
    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception e, RedirectAttributes redirectAttributes) {
        return handleExceptionAndRedirect(e, ErrorCode.GENERAL_ERROR, redirectAttributes, "An unexpected error occurred.");
    }

    /**
     * Handles employee not found exceptions.
     */
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ExceptionDTO> employeeNotFound(EmployeeNotFoundException employeeNotFoundException) {
        return new ResponseEntity<>(new ExceptionDTO("1000", employeeNotFoundException.getMessage()), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles employee validation exceptions.
     */
    @ExceptionHandler(EmployeeValidationException.class)
    public ResponseEntity<FormValidationExceptionDTO> employeeValidationException(EmployeeValidationException employeeValidationException) {
        return new ResponseEntity<>(employeeValidationException.getErrorMap(), HttpStatus.NOT_ACCEPTABLE);
    }

    /**
     * Handles employee deletion not possible exceptions.
     */
    @ExceptionHandler
    public ResponseEntity<ExceptionDTO> employeeDeletionNotPossibleException(EmployeeDeletionNotPossibleException employeeDeletionNotPossibleException) {
        return new ResponseEntity<>(new ExceptionDTO("1000", employeeDeletionNotPossibleException.getMessage()), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles employee already exists exceptions.
     */
    @ExceptionHandler(EmployeeAlreadyExistsException.class)
    public ResponseEntity<ExceptionDTO> employeeAlreadyExists(EmployeeAlreadyExistsException employeeAlreadyExistsException) {
        return new ResponseEntity<>(new ExceptionDTO("1000", employeeAlreadyExistsException.getMessage()), HttpStatus.NOT_FOUND);
    }
}
