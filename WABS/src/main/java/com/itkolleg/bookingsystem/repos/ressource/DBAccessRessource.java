package com.itkolleg.bookingsystem.repos.ressource;


import com.itkolleg.bookingsystem.domains.Ressource;
import com.itkolleg.bookingsystem.exceptions.ressourceExceptions.RessourceDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.ressourceExceptions.RessourceNotFoundException;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface DBAccessRessource {
    Ressource addRessource(Ressource ressource) throws ExecutionException, InterruptedException;

    List<Ressource> getAllRessource() throws ExecutionException, InterruptedException;

    Ressource getRessourceById(Long id) throws RessourceNotFoundException, ExecutionException, InterruptedException;

    Ressource updateRessource(Ressource ressource) throws RessourceNotFoundException, ExecutionException, InterruptedException;

    void deleteRessourceById(Long id) throws RessourceDeletionNotPossibleException;

    Ressource getRessourceBySerialnumber(String serialnumber);
}
