package com.itkolleg.bookingsystem.service.ressource;

import com.itkolleg.bookingsystem.domains.Ressource;
import com.itkolleg.bookingsystem.exceptions.ressourceExceptions.RessourceDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.ressourceExceptions.RessourceNotFoundException;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface RessourceService {
    Ressource addRessource(Ressource ressource) throws ExecutionException, InterruptedException;

    List<Ressource> getAllRessource() throws ExecutionException, InterruptedException;

    Ressource getRessourceById(Long id) throws RessourceNotFoundException, ExecutionException, InterruptedException;

    Ressource updateRessource(Ressource ressource) throws RessourceNotFoundException, ExecutionException, InterruptedException;

    void deleteRessourceById(Long id) throws RessourceDeletionNotPossibleException;

    Ressource findRessourceBySerialnumber(String Serialnumber) throws RessourceNotFoundException, ExecutionException, InterruptedException; //Must Equal


    //public void deleteRessourceByRessourceType(Ressourcetype ressourcetype) throws RessourceDeletionNotPossibleException;
    //public ressource updateRessourceByRessourceType(Ressourcetype ressourcetype) throws RessourceNotFoundException;
}
