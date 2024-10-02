package com.itkolleg.bookingsystem.service.employee;

import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.exceptions.employeeExceptions.EmployeeAlreadyExistsException;
import com.itkolleg.bookingsystem.exceptions.employeeExceptions.EmployeeDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.employeeExceptions.EmployeeNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface EmployeeService extends UserDetailsService {
    Employee addEmployee(Employee employee) throws EmployeeAlreadyExistsException, ExecutionException, InterruptedException;

    List<Employee> getAllEmployees() throws ExecutionException, InterruptedException;

    Page<Employee> getAllEmployeesByPage(Pageable pageable);

    Employee getEmployeeById(Long id) throws EmployeeNotFoundException, ExecutionException, InterruptedException;

    Employee updateEmployeeById(Employee employee) throws ExecutionException, InterruptedException, EmployeeNotFoundException, EmployeeAlreadyExistsException;

    void deleteEmployeeById(Long id) throws EmployeeDeletionNotPossibleException;

    List<Employee> getEmployeesWithNickLikeIgnoreCase(String nick) throws ExecutionException, InterruptedException, EmployeeNotFoundException;

    Employee getEmployeeByEmail(String email) throws EmployeeNotFoundException;

    Employee getEmployeeByNick(String nick);

    Employee findByNick(String username);
    //String getPasswordForEmployee(String username);

    UserDetails loadEmployeeByUsername(String username);

}