package com.adriYalan.gestionDeReclamos.service;

import com.adriYalan.gestionDeReclamos.entity.Edificio;
import com.adriYalan.gestionDeReclamos.entity.Persona;
import com.adriYalan.gestionDeReclamos.entity.Unidad;
import com.adriYalan.gestionDeReclamos.exception.EdificioException;
import com.adriYalan.gestionDeReclamos.repository.EdificioDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EdificioService {

    @Autowired
    private EdificioDAO edificioDAO;

    public List<Edificio> getEdificios() {
        return edificioDAO.getAllEdificios();
    }

    public Edificio getEdificiosByCodigo(int codigo) throws EdificioException {
        Optional<Edificio> edificioOpt = edificioDAO.getEdificioByCodigo((long) codigo);
        if (edificioOpt.isEmpty()) {
            throw new EdificioException("El edificio con código " + codigo + " no existe.");
        }
        return edificioOpt.get();
    }

    public List<Persona> habilitadosPorEdificio(int codigo) throws EdificioException {
        Edificio edificio = this.getEdificiosByCodigo(codigo);
        return edificio.getHabitantes();
    }

    public List<Persona> dueniosPorEdificio(int codigo) throws EdificioException {
        Edificio edificio = this.getEdificiosByCodigo(codigo);
        return edificio.getDuenios();
    }

    public List<Persona> inquilinosPorEdificio(int codigo) throws EdificioException {
        Edificio edificio = this.getEdificiosByCodigo(codigo);
        return edificio.getInquilinos();
    }

    public List<Unidad> getUnidadesPorEdificio(int codigo) throws EdificioException {
        Edificio edificio = this.getEdificiosByCodigo(codigo);
        return edificio.getUnidades();
    }

    public Edificio agregarEdificio(int codigo, String direccion, String nombre) throws EdificioException {
        // Verificar si ya existe un edificio con el mismo código
        if (edificioDAO.getEdificioByCodigo((long) codigo).isPresent()) {
            throw new EdificioException("El edificio con código " + codigo + " ya existe.");
        }

        // Crear y guardar el nuevo edificio
        Edificio nuevoEdificio = new Edificio();
        nuevoEdificio.setCodigo((long) codigo);
        nuevoEdificio.setDireccion(direccion);
        nuevoEdificio.setNombre(nombre);

        return edificioDAO.guardarEdificio(nuevoEdificio);
    }
}
