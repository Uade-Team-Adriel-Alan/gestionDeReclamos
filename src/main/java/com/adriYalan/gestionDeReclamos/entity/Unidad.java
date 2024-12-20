package com.adriYalan.gestionDeReclamos.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "unidades")
public class Unidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "identificador")
    private int identificador;

    @Column(nullable = false)
    private String piso;

    @Column(nullable = false)
    private String numero;

    @Column(nullable = false, length = 1)
    private String habitado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "codigoedificio")
    @JsonIgnore
    private Edificio edificio;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "duenios",
            joinColumns = @JoinColumn(name = "identificador"),  // Columna de la tabla unidades
            inverseJoinColumns = @JoinColumn(name = "documento")  // Columna de la tabla personas
    )
    private List<Persona> duenios;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "inquilinos",
            joinColumns = @JoinColumn(name = "identificador"),  // Columna de la tabla unidades
            inverseJoinColumns = @JoinColumn(name = "documento")  // Columna de la tabla personas
    )
    private List<Persona> inquilinos;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "habitantes",
            joinColumns = @JoinColumn(name = "identificador"),  // Columna de la tabla unidades
            inverseJoinColumns = @JoinColumn(name = "documento")  // Columna de la tabla personas
    )
    private List<Persona> habitantes;

    @OneToMany(mappedBy = "unidad", fetch = FetchType.EAGER)
    private List<Reclamo> reclamos = new ArrayList<>();

    public Unidad() {
    }

    public Unidad(String piso, String numero, Edificio edificio) {
        this.piso = piso;
        this.numero = numero;
        this.edificio = edificio;
        this.habitado = "N";  // Por defecto no habitado
        this.duenios = new ArrayList<Persona>();
        this.inquilinos = new ArrayList<Persona>();
        this.habitantes = new ArrayList<>();
        this.reclamos = new ArrayList<>();
    }

    public int getIdentificador() {
        return identificador;
    }

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    public String getPiso() {
        return piso;
    }

    public void setPiso(String piso) {
        this.piso = piso;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getHabitado() {
        return habitado;
    }

    public void setHabitado(String habitado) {
        this.habitado = habitado;
    }

    public Edificio getEdificio() {
        return edificio;
    }

    public void setEdificio(Edificio edificio) {
        this.edificio = edificio;
    }

    public boolean estaHabitado() {
        return "S".equalsIgnoreCase(this.habitado);
    }

    public void actualizarHabitado(){
        if(this.habitantes.isEmpty() && this.inquilinos.isEmpty()){
            this.habitado = "N";
        }else {
            this.habitado = "S";
        }
    }

    public void habitar() {
        this.habitado = "S";
    }

    public void liberar() {
        this.habitado = "N";
    }

    public List<Persona> getDuenios() {
        return duenios;
    }

    public void setDuenios(List<Persona> duenios) {
        this.duenios = duenios;
    }

    public List<Persona> getInquilinos() {
        return inquilinos;
    }

    public void setInquilinos(List<Persona> inquilinos) {
        this.inquilinos = inquilinos;
    }

    public List<Persona> getHabitantes() {
        return habitantes;
    }

    public void setHabitantes(List<Persona> habitantes) {
        this.habitantes = habitantes;
    }

    public List<Reclamo> getReclamos() {
        return reclamos;
    }

    public void setReclamos(List<Reclamo> reclamos) {
        this.reclamos = reclamos;
    }


    @Override
    public String toString() {
        String dueniosInfo = "";
        for (Persona duenio : duenios) {
            dueniosInfo += "Dueño Nombre: " + duenio.getNombre() +
                    ", Documento: " + duenio.getDocumento() + "; ";
        }

        String inquilinosInfo = "";
        for (Persona inquilino : inquilinos) {
            inquilinosInfo += "Inquilino Nombre: " + inquilino.getNombre() +
                    ", Documento: " + inquilino.getDocumento() + "; ";
        }

        String habitantesInfo = "";
        for (Persona habitante : habitantes) {
            habitantesInfo += "Habitante Nombre: " + habitante.getNombre() +
                    ", Documento: " + habitante.getDocumento() + "; ";
        }

        String reclamosInfo = "";
        for (Reclamo reclamo : reclamos) {
            reclamosInfo += "Reclamo ID: " + reclamo.getIdReclamo() +
                    ", Ubicación: " + reclamo.getUbicacion() +
                    ", Descripción: " + reclamo.getDescripcion() + "; ";
        }

        return "Unidad{" +
                "identificador=" + identificador +
                ", piso='" + piso + '\'' +
                ", numero='" + numero + '\'' +
                ", habitado='" + habitado + '\'' +
                ", edificio=" + (edificio != null ? edificio.getNombre() : "null") +
                ", dueños=[" + dueniosInfo + "]" +
                ", inquilinos=[" + inquilinosInfo + "]" +
                ", habitantes=[" + habitantesInfo + "]" +
                ", reclamos=[" + reclamosInfo + "]" +
                '}';
    }


}
