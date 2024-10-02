package com.itkolleg.bookingsystem.repos.employee;

import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.exceptions.employeeExceptions.EmployeeAlreadyExistsException;
import com.itkolleg.bookingsystem.exceptions.employeeExceptions.EmployeeDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.employeeExceptions.EmployeeNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Component
public class EmployeeDBAccess_JPAH2 implements EmployeeDBAccess {

    private final EmployeeJPARepo employeeJPARepo;

    public EmployeeDBAccess_JPAH2(EmployeeJPARepo employeeJPARepo) {
        this.employeeJPARepo = employeeJPARepo;
    }

    /*@Override
    public employee saveEmployee(employee employee) throws EmployeeAlreadyExistsException {
        try{
            return this.employeeJPARepo.save(employee);
        } catch(Exception e){
            throw new EmployeeAlreadyExistsException("Mitarbeiter existiert bereits!");
        }
    }
*/
    @Override
    public Employee saveEmployee(Employee employee) throws EmployeeAlreadyExistsException {

        return employeeJPARepo.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return this.employeeJPARepo.findAll();
    }

    @Override
    public Employee getEmployeeById(Long id) throws EmployeeNotFoundException {
        Optional<Employee> optEmployee = this.employeeJPARepo.findById(id);
        if (optEmployee.isPresent()) {
            return optEmployee.get();
        } else {
            throw new EmployeeNotFoundException("The employee with the ID: " + employeeJPARepo.findById(id) + " was not found!");
        }
    }

    @Override
    public void deleteEmployeeById(Long id) throws EmployeeDeletionNotPossibleException {
        try {
            this.employeeJPARepo.deleteById(id);
        } catch (Exception e) {
            throw new EmployeeDeletionNotPossibleException("Mitarbeiter mit der ID " + id + " konnte nicht gelöscht werden!");
        }
    }

    @Override
    public List<Employee> findEmployeesByNickLikeIgnoreCase(String nick) throws EmployeeNotFoundException, ExecutionException, InterruptedException {
        return this.employeeJPARepo.findEmployeesByNickLikeIgnoreCase("%" + nick + "%");
    }

    @Override
    public Employee getEmployeeByEmail(String email) {
        return this.employeeJPARepo.getEmployeeByEmail(email);
    }

    @Override
    public Employee getEmployeeByNick(String nick) {
        return this.employeeJPARepo.getEmployeeByNick(nick);
    }

    @Override
    public Page<Employee> getAllEmployeesByPage(Pageable pageable) {
        return this.employeeJPARepo.findAllEmployeesByPage(pageable);
    }

    @Override
    public Employee updateEmployeeById(Long id, Employee updatedEmployee) throws EmployeeNotFoundException {
        Optional<Employee> employeeOptional = this.employeeJPARepo.findById(id);
        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            employee.setFname(updatedEmployee.getFname());
            employee.setLname(updatedEmployee.getLname());
            employee.setNick(updatedEmployee.getNick());
            employee.setEmail(updatedEmployee.getEmail());
            employee.setPassword(updatedEmployee.getPassword());
            employee.setRole(updatedEmployee.getRole());
            return this.employeeJPARepo.save(employee);
        } else {
            throw new EmployeeNotFoundException("The employee with the ID: " + employeeJPARepo.findById(id) + " was not found!");
        }
    }

    @Override
    public Employee updateEmployee(Employee updatedEmployee) throws EmployeeNotFoundException {
        return this.employeeJPARepo.save(updatedEmployee);
    }


}