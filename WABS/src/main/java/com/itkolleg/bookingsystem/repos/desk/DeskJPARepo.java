package com.itkolleg.bookingsystem.repos.desk;

import com.itkolleg.bookingsystem.domains.Desk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * This interface represents the repository for desks in the booking system.
 * @author Sonja Lechner
 * @version 1.0
 * since 2023-05-24
 */
@Repository
public interface DeskJPARepo extends JpaRepository<Desk, Long> {

    /**
     *
     * @param pageable The pageable object
     * @return A page of desks
     */
    @Query("select a from Desk a")
    Page<Desk> findAllDesksByPage(Pageable pageable);

    /**
     * Retrieves a desk by its ID.
     * @param id id of the desk to be found
     * @return desk with the given id
     */
    Desk findDeskById(Long id);
}