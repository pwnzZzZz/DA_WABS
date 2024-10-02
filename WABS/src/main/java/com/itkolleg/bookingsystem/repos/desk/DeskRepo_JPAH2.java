package com.itkolleg.bookingsystem.repos.desk;

import com.itkolleg.bookingsystem.domains.booking.DeskBooking;
import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.domains.Port;
import com.itkolleg.bookingsystem.exceptions.ResourceNotFoundException;
import com.itkolleg.bookingsystem.exceptions.ResourceDeletionFailureException;
import com.itkolleg.bookingsystem.repos.deskbooking.DeskBookingJPARepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * This class implements the DeskRepo interface using JPA and H2 database.
 * It provides methods for CRUD operations and other desk-related operations.
 * It also handles exceptions related to desk operations.
 * @author Sonja Lechner
 * @version 1.0
 * @since 2023-05-24
 */
@Slf4j
@Component
public class DeskRepo_JPAH2 implements DeskRepo {

    /**
     * The DeskJPARepo instance used for desk operations.
     */
    final DeskJPARepo deskJPARepo;

    /**
     * The DeskBookingJPARepo instance used for desk booking operations.
     */
    final DeskBookingJPARepo deskBookingJPARepo;

    /**
     *Constructs a new instance of DeskRepo_JPAH2 with the specified DeskJPARepo and DeskBookingJPARepo.
     *
     * @param deskJPARepo The DeskJPARepo to be used for desk operations.
     * @param deskBookingJPARepo The DeskBookingJPARepo to be used for desk booking operations.
     */
    public DeskRepo_JPAH2(DeskJPARepo deskJPARepo, DeskBookingJPARepo deskBookingJPARepo) {
        this.deskJPARepo = deskJPARepo;
        this.deskBookingJPARepo = deskBookingJPARepo;
    }


    /**
     * Adds a desk to the repository.
     *
     * @param desk The desk to add.
     * @return the added desk
     */
    @Override
    public Desk addDesk(Desk desk) {
        return this.deskJPARepo.save(desk);
    }

    /**
     * Retrieves all desks from the repository.
     *
     * @return A list of all desks.
     */
    @Override
    public List<Desk> getAllDesks() {
        return this.deskJPARepo.findAll();
    }


    /**
     * Retrieves a page of desks from the repository.
     *
     * @param pageable The pageable object.
     * @return A page of desks.
     */
    public Page<Desk> getAllDesksByPage(Pageable pageable) {
        return this.deskJPARepo.findAllDesksByPage(pageable);
    }

    /**
     * If the desk is not found.
     *
     * @param id The ID of the desk.
     * @return The desk with the specified ID.
     * @throws ResourceNotFoundException  If the desk is not found.
     */
    @Override
    public Desk getDeskById(Long id) throws ResourceNotFoundException {
        Optional<Desk> deskOptional = this.deskJPARepo.findById(id);
        if (deskOptional.isPresent()) {
            return deskOptional.get();
        } else {
            throw new ResourceNotFoundException("The desk with the ID: " + id + " was not found!");
        }
    }

    /**
     * Updates a desk by its ID.
     *
     * @param id The ID of the desk.
     * @param updatedDesk The updated desk.
     * @return The updated desk.
     * @throws ResourceNotFoundException If the desk is not found.
     */
    @Override
    public Desk updateDeskById(Long id, Desk updatedDesk) throws ResourceNotFoundException {
        Optional<Desk> deskOptional = this.deskJPARepo.findById(id);
        if (deskOptional.isPresent()) {
            Desk desk = deskOptional.get();
            desk.setDeskNr(updatedDesk.getDeskNr());
            desk.setNrOfMonitors(updatedDesk.getNrOfMonitors());
            desk.setPorts(updatedDesk.getPorts());
            return this.deskJPARepo.save(desk);
        } else {
            throw new ResourceNotFoundException("The desk with the ID: " + id + " was not found!");
        }
    }

    /**
     * Updates a desk.
     *
     * @param updatedDesk The updated desk.
     * @return The updated desk.
     * @throws ResourceNotFoundException if the desk is not found.
     */
    public Desk updateDesk(Desk updatedDesk) throws ResourceNotFoundException {
        try {
            Optional<Desk> desk = this.deskJPARepo.findById(updatedDesk.getId());
            if (desk.isPresent()) {
                Desk deskToUpdate = desk.get();
                deskToUpdate.setDeskNr(updatedDesk.getDeskNr());
                deskToUpdate.setNrOfMonitors(updatedDesk.getNrOfMonitors());
                deskToUpdate.deleteAllPorts();
                for (Port p : updatedDesk.getPorts()) {
                    deskToUpdate.addPort(new Port(p.getName()));
                }
                return this.deskJPARepo.save(deskToUpdate);

            } else {
                throw new ResourceNotFoundException("desk with ID " + updatedDesk.getId() + " not found!");
            }

        } catch (Exception e) {
            throw new ResourceNotFoundException("desk not found");
        }
    }

    /**
     * Deletes a desk by its ID.
     *
     * @param id The ID of the desk.
     * @throws ResourceDeletionFailureException If the desk deletion fails.
     */
    @Override
    public void deleteDeskById(Long id) throws ResourceDeletionFailureException {
        List<DeskBooking> bookings = this.deskBookingJPARepo.findBookingsByDeskId(id);
        if (!bookings.isEmpty()) {
            throw new ResourceDeletionFailureException("desk with the ID: " + id + " cannot be deleted as it is part of a booking.");
        }

        Optional<Desk> deskOptional = this.deskJPARepo.findById(id);
        if (deskOptional.isPresent()) {
            this.deskJPARepo.deleteById(id);
        } else {
            throw new ResourceDeletionFailureException("Deletion of the desk with the ID: " + id + " was not possible!");
        }
    }

    /**
     * Creates a port for a desk.
     *
     * @param deskId The ID of the desk.
     * @param newPort The new port to create.
     * @return The updated desk with the new port.
     */
    @Override
    public Desk createPort(Long deskId, Port newPort) {
        Desk desk = deskJPARepo.findById(deskId).orElse(null);
        if (desk != null) {
            List<Port> ports = desk.getPorts();
            ports.add(newPort);
            desk.setPorts(ports);
            return deskJPARepo.save(desk);
        }
        return null;

    }

    /**
     * Updates a port for a desk.
     *
     * @param deskId The ID of the desk.
     * @param portName The name of the port to update.
     * @param updatedPort The updated port.
     * @return The updated desk with the updated port.
     */
    @Override
    public Desk updatePort(Long deskId, String portName, Port updatedPort) {
        Desk desk = deskJPARepo.findById(deskId).orElse(null);
        if (desk != null) {
            List<Port> ports = desk.getPorts();
            for (Port port : ports) {
                if (port.getName().equals(portName)) {
                    port.setName(updatedPort.getName());
                    break;
                }
            }
            desk.setPorts(ports);
            return deskJPARepo.save(desk);
        }
        return null;
    }

    /**
     * Deletes a port from a desk.
     *
     * @param deskId The ID of the desk.
     * @param portName The name of the port to delete.
     * @return The updated desk without the deleted port.
     */
    @Override
    public Desk deletePort(Long deskId, String portName) {
        Desk desk = deskJPARepo.findById(deskId).orElse(null);
        if (desk != null) {
            List<Port> ports = desk.getPorts();
            ports.removeIf(port -> port.getName().equals(portName));
            desk.setPorts(ports);
            return deskJPARepo.save(desk);
        }
        return null;
    }

    /**
     * Retrieves the ports of a desk.
     *
     * @param deskId The ID of the desk.
     * @return A list of ports of the desk.
     */
    @Override
    public List<Port> getPorts(Long deskId) {
        Desk desk = deskJPARepo.findById(deskId).orElse(null);
        if (desk != null) {
            return desk.getPorts();
        }
        return null;
    }

}
