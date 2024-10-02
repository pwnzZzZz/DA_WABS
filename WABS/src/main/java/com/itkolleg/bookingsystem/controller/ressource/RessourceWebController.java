package com.itkolleg.bookingsystem.controller.ressource;

import com.itkolleg.bookingsystem.domains.Ressource;
import com.itkolleg.bookingsystem.exceptions.ressourceExceptions.RessourceDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.ressourceExceptions.RessourceNotFoundException;
import com.itkolleg.bookingsystem.service.ressource.RessourceService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * Diese Klasse repräsentiert einen Web Controller für das Modul Ressourcen. Der Controller ermöglicht die Interaktion mit Ressourcen über HTTP-Anfragen und -Antworten
 *
 * @author Manuel Payer
 * @version 1.0
 * @since 25.06.2023
 */
@Controller
@RequestMapping("/web/ressource")
public class RessourceWebController {

    RessourceService ressourceService;

    /**
     * Konstruktor der Klsse RessourceWebController. Sie nimmt eine Variable (ressourceService) vom Typ RessourceService entgegen und weißt den Wert
     * dem globalen Datenfeld hinzu
     *
     * @param ressourceService vom Typ RessourceService
     */
    public RessourceWebController(RessourceService ressourceService) {
        this.ressourceService = ressourceService;
    }


    /**
     * Dient dazu eine Übersicht aller Ressourcen für den/die admin zu liefern.
     *
     * @return modelAndView
     * @throws ExecutionException   Aufsührungsfehler
     * @throws InterruptedException Unterbrechungsfehler
     */
    @GetMapping("/allRessources")
    public ModelAndView allressources() throws ExecutionException, InterruptedException {
        List<Ressource> allRessources = ressourceService.getAllRessource();
        return new ModelAndView("ressource/allressources", "ressources", allRessources);
    }

    /**
     * Dient dazu eine Übersicht aller Ressourcen für den/die Mitarbeiter:inn zu liefern.
     *
     * @return modelAndView
     * @throws ExecutionException   Aufsührungsfehler
     * @throws InterruptedException Unterbrechungsfehler
     */
    @GetMapping("/allRessourcesEmployee")
    public ModelAndView allRessourcesEmployee() throws ExecutionException, InterruptedException {
        List<Ressource> allRessources = ressourceService.getAllRessource();
        return new ModelAndView("ressource/allressourcesEmployee", "ressourcesEmployee", allRessources);
    }

    /**
     * Diese Methode ermöglicht dem/der admin das hinzufügen einer ressource in die Datenbank. Es ist mit @GetMapping annotiert, da es die HTTP-Anfrage verarbeiten und darstellen muss
     *
     * @param model vom Typ Model
     * @return modelAndView
     */
    @GetMapping("/addRessource")
    public ModelAndView addRessource(Model model) {

        model.addAttribute("newRessource", new Ressource());
        return new ModelAndView("ressource/addRessource", "Ressource", model);
    }

    /**
     * Diese Methode ermöglicht dem/der admin das hinzufügen einer ressource in die Datenbank. Es ist mit @PostMapping annotiert, da es die HTTP-Anfrage übergeben muss.
     * Beim Durchführen wird der/die Benutzer:inn wieder an die HTML-Seite allRessources weitergeleitet.
     * Wird ein Fehler geworfen, dann bleibt der/die Benutzer:inn auf der selben Seite mit einer entsprechenden Fehlermeldung
     *
     * @param ressource     vom Typ ressource
     * @param bindingResult vom Typ BindingResults
     * @return Redirection
     * @throws ExecutionException   Aufsührungsfehler
     * @throws InterruptedException Unterbrechungsfehler
     */
    @PostMapping("/addRessource")
    public String addRessource(@Valid @ModelAttribute("newRessource") Ressource ressource, BindingResult bindingResult, Model model) throws ExecutionException, InterruptedException {
        if (bindingResult.hasErrors()) {
            return "/ressource/addRessource";
        } else {
            this.ressourceService.addRessource(ressource);
            return "redirect:/web/ressource/allRessources";
        }
    }

    /**
     * Diese Methode updated eine ressource, welche aus seinem Listenelement (nicht in dieser Methode) mit der dazugehörigen id geholt wird.
     * Die Methode ist mit @GetMapping annotiert, da sie eine HTTP Anfrage verarbeiten und auf das entsprechende HTML Dokument verweisen muss.
     *
     * @param id    vom Typ Long
     * @param model vom Typ Model
     * @return ModelAndView
     * @throws RessourceNotFoundException ressource nicht gefunden
     * @throws ExecutionException         Aufsührungsfehler
     * @throws InterruptedException       Unterbrechungsfehler
     */
    @GetMapping("/updateRessource/{id}")
    public ModelAndView updateRessource(@PathVariable Long id, Model model) throws RessourceNotFoundException, ExecutionException, InterruptedException {

        Ressource ressource = this.ressourceService.getRessourceById(id);
        model.addAttribute("updateRessource", ressource);
        return new ModelAndView("ressource/editRessource", "Ressource", model);
    }

    /**
     * Diese Methode updated eine ressource, welche aus seinem Listenelement (nicht in dieser Methode) mit der dazugehörigen id geholt wird.
     * Die Methode ist mit @PostMapping annotiert, da sie eine HTTP Anfrage verarbeiten und auf das entsprechende HTML Dokument verweisen muss.
     *
     * @param ressource     vom Typ ressource
     * @param bindingResult vom Typ BindingResult
     * @return Redirection
     * @throws ExecutionException         Aufsührungsfehler
     * @throws InterruptedException       Unterbrechungsfehler
     * @throws RessourceNotFoundException Resource nicht gefunden
     */
    @PostMapping("/updateRessource")
    public String updateRessource(@Valid Ressource ressource, BindingResult bindingResult) throws ExecutionException, InterruptedException, RessourceNotFoundException {
        if (bindingResult.hasErrors()) {
            return "/ressource/editRessource";
        } else {
            this.ressourceService.updateRessource(ressource);
            return "redirect:/web/ressource/allRessources";
        }
    }

    /**
     * Diese Methode löscht eine bestimmte ressource aus einer Liste, und nimmt die ID der ressource entgegen.
     * Die Methode ist NUR mit @GetMapping annotiert, da die Aktion auf der selben Seite durchgeführt werden soll.
     * Es ist anzumerken, dass eine ressource nicht gelöscht werden kann, wenn sie von einer aktiven Buchung verwendet wird.
     *
     * @param id vom Typ Long
     * @return Redirection
     */
    @GetMapping("/deleteRessource/{id}")
    public String deleteRessource(@PathVariable Long id) {
        try {
            this.ressourceService.deleteRessourceById(id);
            return "redirect:/web/ressource/allRessources";
        } catch (RessourceDeletionNotPossibleException e) {
            return "redirect:/web/ressource/allRessources";
        }
    }

}
