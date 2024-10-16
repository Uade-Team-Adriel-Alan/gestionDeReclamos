package com.adriYalan.gestionDeReclamos.service;

import com.adriYalan.gestionDeReclamos.entity.*;
import com.adriYalan.gestionDeReclamos.exception.PersonaException;
import com.adriYalan.gestionDeReclamos.exception.UnidadException;
import com.adriYalan.gestionDeReclamos.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UnidadService {

    @Autowired
    private UnidadDAO unidadDAO;

    @Autowired
    private PersonaDAO personaDAO;

    @Autowired
    private DuenioDAO duenioDAO;

    @Autowired
    private InquilinoDAO inquilinoDAO;

    @Autowired
    private HabitanteDAO habitanteDAO;

    @Autowired
    private EdificioDAO edificioDAO;



    public List<Persona> dueniosPorUnidad(int codigo, String piso, String numero) throws UnidadException {
        Optional<Unidad> unidadOpt = unidadDAO.getUnidadByDetalles(codigo, piso, numero);
        if (unidadOpt.isEmpty()) {
            throw new UnidadException("La unidad en el edificio " + codigo + " con piso " + piso + " y número " + numero + " no existe.");
        }
        return unidadOpt.get().getDuenios();
    }

    public List<Persona> inquilinosPorUnidad(int codigo, String piso, String numero) throws UnidadException {
        Optional<Unidad> unidadOpt = unidadDAO.getUnidadByDetalles(codigo, piso, numero);
        if (unidadOpt.isEmpty()) {
            throw new UnidadException("La unidad en el edificio " + codigo + " con piso " + piso + " y número " + numero + " no existe.");
        }
        return unidadOpt.get().getInquilinos();
    }

    public List<Persona> habitantesPorUnidad(int codigo, String piso, String numero) throws UnidadException {
        Optional<Unidad> unidadOpt = unidadDAO.getUnidadByDetalles(codigo, piso, numero);
        if (unidadOpt.isEmpty()) {
            throw new UnidadException("La unidad en el edificio " + codigo + " con piso " + piso + " y número " + numero + " no existe.");
        }
        return unidadOpt.get().getHabitantes();
    }

    public void agregarDuenioUnidad(int codigo, String piso, String numero, String documento) throws UnidadException, PersonaException {
        Unidad unidad = unidadDAO.getUnidadByDetalles(codigo, piso, numero)
                .orElseThrow(() -> new UnidadException("La unidad no existe."));

        Persona persona = personaDAO.getPersonaByDocumento(documento)
                .orElseThrow(() -> new PersonaException("La persona no existe."));

        boolean duenioExiste = unidad.getDuenios().stream()
                .anyMatch(duenio -> duenio.getDocumento().equals(documento));

        if (duenioExiste) {
            throw new UnidadException("La persona ya es dueña de esta unidad.");
        }

        Duenio nuevoDuenio = new Duenio();
        nuevoDuenio.setDocumento(documento);
        nuevoDuenio.setIdentificador(unidad.getIdentificador());

        duenioDAO.guardarDuenio(nuevoDuenio);
        unidad.getDuenios().add(persona);
        unidadDAO.actualizarUnidad(unidad);
    }

    public void transferirUnidad(int codigo, String piso, String numero, String documento) throws UnidadException, PersonaException {
        Unidad unidad = unidadDAO.getUnidadByDetalles(codigo, piso, numero)
                .orElseThrow(() -> new UnidadException("La unidad no existe."));

        Persona persona = personaDAO.getPersonaByDocumento(documento)
                .orElseThrow(() -> new PersonaException("La persona no existe."));

        List<Persona> duenios = new ArrayList<>();
        duenios.add(persona);
        unidad.setDuenios(duenios);

        unidadDAO.actualizarUnidad(unidad);
    }

    public void agregarInquilinoUnidad(int codigo, String piso, String numero, String documento) throws UnidadException, PersonaException {
        Unidad unidad = unidadDAO.getUnidadByDetalles(codigo, piso, numero)
                .orElseThrow(() -> new UnidadException("La unidad no existe."));

        Persona persona = personaDAO.getPersonaByDocumento(documento)
                .orElseThrow(() -> new PersonaException("La persona no existe."));

        boolean inquilinoExiste = unidad.getInquilinos().stream()
                .anyMatch(inquilino -> inquilino.getDocumento().equals(documento));

        if (inquilinoExiste) {
            throw new UnidadException("La persona ya es inquilino en esta unidad.");
        }

        Inquilino nuevoInquilino = new Inquilino();
        nuevoInquilino.setDocumento(documento);
        nuevoInquilino.setIdentificador(unidad.getIdentificador());

        inquilinoDAO.guardarInquilino(nuevoInquilino);
        unidad.getInquilinos().add(persona);
        unidadDAO.actualizarUnidad(unidad);
    }

    public void agregarHabitanteUnidad(int codigo, String piso, String numero, String documento) throws UnidadException, PersonaException {
        Unidad unidad = unidadDAO.getUnidadByDetalles(codigo, piso, numero)
                .orElseThrow(() -> new UnidadException("La unidad no existe."));

        Persona persona = personaDAO.getPersonaByDocumento(documento)
                .orElseThrow(() -> new PersonaException("La persona no existe."));

        boolean habitanteExiste = unidad.getHabitantes().stream()
                .anyMatch(habitante -> habitante.getDocumento().equals(documento));

        if (habitanteExiste) {
            throw new UnidadException("La persona ya es habitante de esta unidad.");
        }

        Habitante nuevoHabitante = new Habitante();
        nuevoHabitante.setDocumento(documento);
        nuevoHabitante.setIdentificador(unidad.getIdentificador());

        habitanteDAO.guardarHabitante(nuevoHabitante);

        if (!unidad.estaHabitado()) {
            unidad.habitar();
        }

        unidad.getHabitantes().add(persona);
        unidadDAO.actualizarUnidad(unidad);
    }

    @Transactional
    public void liberarUnidad(int codigo, String piso, String numero) throws UnidadException {
        Unidad unidad = unidadDAO.getUnidadByDetalles(codigo, piso, numero)
                .orElseThrow(() -> new UnidadException("La unidad no existe."));

        int identificadorUnidad = unidad.getIdentificador();
        habitanteDAO.eliminarHabitantesPorUnidad(identificadorUnidad);

        unidad.liberar();
        unidadDAO.actualizarUnidad(unidad);
    }

    public Unidad agregarUnidad(int codigoEdificio, String piso, String numero) throws UnidadException, PersonaException {
        // Verificamos si el edificio existe

        Edificio edificio = edificioDAO.getEdificioByCodigo((long) codigoEdificio)
                .orElseThrow(() -> new UnidadException("El edificio no existe."));

        // Verificamos si ya existe una unidad con los mismos detalles (codigo, piso, numero)
        Optional<Unidad> unidadExistente = unidadDAO.getUnidadByDetalles(codigoEdificio, piso, numero);
        if (unidadExistente.isPresent()) {
            throw new UnidadException("La unidad ya existe en este edificio.");
        }

        // Creamos una nueva unidad
        Unidad nuevaUnidad = new Unidad();
        nuevaUnidad.setEdificio(edificio);
        nuevaUnidad.setPiso(piso);
        nuevaUnidad.setNumero(numero);

        // Si hay dueños, los agregamos

        // Guardamos la unidad en la base de datos
        return unidadDAO.guardarUnidad(nuevaUnidad);
    }
}
