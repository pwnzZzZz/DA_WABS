package com.itkolleg.bookingsystem.domains;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


/**
 * Die Klasse ressource repräsentiert eine ressource in einem Buchungssystem.
 * Sie enthält Informationen über die ressource, wie ihren Namen, ihre Beschreibung, zusätzliche Informationen und Seriennummer.
 *
 * @author Manuel Payer
 * @version 1.0
 * @since 25.06.2023
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Ressource {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Ressourcetype ressourcetype;
    private String name;
    private String description;
    private String info;
    private String serialnumber;

    /**
     * Konstruktor der Klasse ressource. Er nimmt folgende Parameter entgegen:
     *
     * @param id            vom Typ Long
     * @param ressourcetype vom Typ Ressourcetype
     * @param name          vom Typ String
     * @param description   vom Typ String
     * @param info          vom Typ String
     * @param serialnumber  von Typ String
     */
    public Ressource(Long id, Ressourcetype ressourcetype, String name, String description, String info, String serialnumber) {
        this.id = id;
        this.ressourcetype = ressourcetype;
        this.description = description;
        this.info = info;
        this.name = name;
        this.serialnumber = serialnumber;
    }


}

