package com.adriYalan.gestionDeReclamos.controller;

import com.adriYalan.gestionDeReclamos.entity.Persona;
import com.adriYalan.gestionDeReclamos.repository.PersonaDAO;
import com.adriYalan.gestionDeReclamos.exception.PersonaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class PersonaController {

    @Autowired
    private PersonaDAO personaDAO;

    public void agregarPersona(String documento, String nombre) {
        Persona persona = new Persona(documento, nombre);
        personaDAO.guardarPersona(persona);
    }

    public void eliminarPersona(String documento) throws PersonaException {
        if (personaDAO.getPersonaByDocumento(documento).isPresent()) {
            personaDAO.eliminarPersona(documento);
        } else {
            throw new PersonaException("La persona con documento " + documento + " no existe.");
        }
    }
}
