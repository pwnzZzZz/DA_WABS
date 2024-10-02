package com.itkolleg.bookingsystem.domains.booking;


import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.domains.Ressource;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Die Klasse ressourcebooking repräsentiert eine Buchung für eine ressource in einem Buchungssystem.
 * Sie erweitert die Klasse booking und enthält zusätzliche Informationen spezifisch für Ressourcenbuchungen.
 *
 * @author Manuel Payer
 * @version 1.0
 * @since 25.06.2023
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RessourceBooking extends Booking {
    @ManyToOne
    @JoinColumn(name = "ressource_id")
    private Ressource ressource;

    /**
     * Konstruktor der Klasse ressource booking. Er nimmt folgende Parameter entgegen:
     *
     * @param employee  vom Typ employee
     * @param ressource vom Typ ressource
     * @param date      vom Typ LocalDate
     * @param startTime vom Typ LocalTime
     * @param endTime   vom Typ LocalTime
     */
    public RessourceBooking(Employee employee, Ressource ressource, LocalDate date, LocalTime startTime, LocalTime endTime) {
        super(employee, date, startTime, endTime);
        this.ressource = ressource;

    }
}


