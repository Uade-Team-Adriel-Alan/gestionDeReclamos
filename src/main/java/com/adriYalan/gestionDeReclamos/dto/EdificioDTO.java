package com.adriYalan.gestionDeReclamos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EdificioDTO {
    private Long codigo;
    private String nombre;
    private String direccion;
    private List<UnidadSimpleDTO> unidades;
    private List<PersonaSimpleDTO> duenios;
    private List<PersonaSimpleDTO> inquilinos;
    private List<PersonaSimpleDTO> habitantes;
}