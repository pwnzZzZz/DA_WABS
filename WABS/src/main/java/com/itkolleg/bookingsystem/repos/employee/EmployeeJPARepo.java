package com.itkolleg.bookingsystem.repos.employee;

import com.itkolleg.bookingsystem.domains.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeJPARepo extends JpaRepository<Employee, Long> {

    List<Employee> findEmployeesByNickLikeIgnoreCase(String nick);

    Employee getEmployeeByEmail(String email);

    Employee getEmployeeByNick(String nick);

    @Query("select a from Employee a")
    Page<Employee> findAllEmployeesByPage(Pageable pageable);

/*
    List<employee>findEmployeesByNameContainsIgnoreCase(String name);
    employee findByNameIgnoreCase(String name);
    employee findByNickIgnoreCase(String nick);
    @Query("select a from employee a")


 */
}