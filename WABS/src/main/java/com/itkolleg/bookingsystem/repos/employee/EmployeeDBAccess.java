package com.itkolleg.bookingsystem.repos.employee;

import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.exceptions.employeeExceptions.EmployeeAlreadyExistsException;
import com.itkolleg.bookingsystem.exceptions.employeeExceptions.EmployeeDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.employeeExceptions.EmployeeNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface EmployeeDBAccess {
    Employee saveEmployee(Employee employee) throws ExecutionException, InterruptedException, EmployeeAlreadyExistsException;

    List<Employee> getAllEmployees() throws ExecutionException, InterruptedException;

    Employee getEmployeeById(Long id) throws EmployeeNotFoundException, ExecutionException, InterruptedException;

    void deleteEmployeeById(Long id) throws EmployeeDeletionNotPossibleException;

    List<Employee> findEmployeesByNickLikeIgnoreCase(String nick) throws EmployeeNotFoundException, ExecutionException, InterruptedException;

    Employee getEmployeeByEmail(String email);

    Employee getEmployeeByNick(String nick);

    Page<Employee> getAllEmployeesByPage(Pageable pageable);

    Employee updateEmployeeById(Long id, Employee employee) throws EmployeeNotFoundException;

    Employee updateEmployee(Employee employee) throws EmployeeNotFoundException;


}