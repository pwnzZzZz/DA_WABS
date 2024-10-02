package com.itkolleg.bookingsystem.service.employee;

import com.itkolleg.bookingsystem.domains.CustomEmployeeDetails;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.exceptions.employeeExceptions.EmployeeAlreadyExistsException;
import com.itkolleg.bookingsystem.exceptions.employeeExceptions.EmployeeDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.employeeExceptions.EmployeeNotFoundException;
import com.itkolleg.bookingsystem.repos.employee.EmployeeDBAccess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;


@Service
public class EmployeeServiceImplementation implements EmployeeService {

    private final EmployeeDBAccess employeeDBAccess;

    public EmployeeServiceImplementation(EmployeeDBAccess employeeDBAccess) {
        this.employeeDBAccess = employeeDBAccess;
    }


    @Override
    public Employee addEmployee(Employee employee) throws EmployeeAlreadyExistsException, ExecutionException, InterruptedException {
        String email = employee.getEmail();
        String nick = employee.getNick();

        Employee existingEmployeeByEmail = employeeDBAccess.getEmployeeByEmail(email);
        Employee existingEmployeeByNick = employeeDBAccess.getEmployeeByNick(nick);

        if (existingEmployeeByEmail != null) {
            throw new EmployeeAlreadyExistsException("employee with email already exists");
        }

        if (existingEmployeeByNick != null) {
            throw new EmployeeAlreadyExistsException("employee with nick already exists");
        }

        return this.employeeDBAccess.saveEmployee(employee);
    }

    @Override
    public List<Employee> getAllEmployees() throws ExecutionException, InterruptedException {
        return this.employeeDBAccess.getAllEmployees();
    }

    @Override
    public Page<Employee> getAllEmployeesByPage(Pageable pageable) {
        return this.employeeDBAccess.getAllEmployeesByPage(pageable);
    }

    @Override
    public Employee getEmployeeById(Long id) throws EmployeeNotFoundException, ExecutionException, InterruptedException {
        return this.employeeDBAccess.getEmployeeById(id);
    }

    @Override
    public Employee updateEmployeeById(Employee employee) throws ExecutionException, InterruptedException, EmployeeNotFoundException, EmployeeAlreadyExistsException {
        Employee employeeFromDb = this.employeeDBAccess.getEmployeeById(employee.getId());
        if (employeeFromDb == null) {
            throw new EmployeeNotFoundException("The employee with the ID: " + employeeDBAccess.getEmployeeById(employee.getId()) + " was not found!");
        }
        employeeFromDb.setFname(employee.getFname());
        employeeFromDb.setLname(employee.getLname());
        employeeFromDb.setNick(employee.getNick());
        employeeFromDb.setPassword(employee.getPassword());
        employeeFromDb.setEmail(employee.getEmail());
        employeeFromDb.setRole(employee.getRole());

        return this.employeeDBAccess.saveEmployee(employeeFromDb);

    }

    @Override
    public void deleteEmployeeById(Long id) throws EmployeeDeletionNotPossibleException {
        this.employeeDBAccess.deleteEmployeeById(id);
    }

    @Override
    public List<Employee> getEmployeesWithNickLikeIgnoreCase(String nick) throws ExecutionException, InterruptedException, EmployeeNotFoundException {
        return this.employeeDBAccess.findEmployeesByNickLikeIgnoreCase(nick);
    }

    @Override
    public Employee getEmployeeByEmail(String email) throws EmployeeNotFoundException {
        return this.employeeDBAccess.getEmployeeByEmail(email);

    }

    @Override
    public Employee getEmployeeByNick(String nick) {
        Employee employeeDB = this.employeeDBAccess.getEmployeeByNick(nick);
        if (employeeDB == null) {
            // Benutzer nicht gefunden, Fehlerbehandlung durchführen
            throw new IllegalArgumentException("Benutzername nicht gefunden: " + nick);
        }
        return employeeDB;

    }

    @Override
    public Employee findByNick(String username) {
        return this.employeeDBAccess.getEmployeeByNick(username);
    }

    public UserDetails loadEmployeeByUsername(String username) throws UsernameNotFoundException {
        Employee employee = employeeDBAccess.getEmployeeByNick(username);
        if (employee == null) {
            throw new UsernameNotFoundException(username);
        }
        return new CustomEmployeeDetails(employee);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = this.employeeDBAccess.getEmployeeByNick(username);
        if (employee == null) {
            throw new UsernameNotFoundException("Mitarbeiter mit dem nick " + username + "nicht gefunden!");
        }
        return User.builder()
                .username(employee.getNick())
                .password(employee.getPassword())
                .roles(employee.getRole().toString())
                .build();
    }
}