package com.example.demo.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "BUS")
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bus")
    private Long idBus;

    @Column(nullable = false, unique = true)
    private String placa;

    private String modelo;

    @Column(nullable = false)
    private Integer capacidad;

    private String estado;

    @ManyToOne
    @JoinColumn(name = "id_conductor", nullable = false)
    private Usuario conductor;

    @OneToMany(mappedBy = "bus")
    private List<Asiento> asientos; // Relación con la entidad Asiento

    // Constructor vacío
    public Bus() {
    }

    // Constructor con parámetros
    public Bus(String placa, String modelo, Integer capacidad, String estado, Usuario conductor, List<Asiento> asientos) {
        this.placa = placa;
        this.modelo = modelo;
        this.capacidad = capacidad;
        this.estado = estado;
        this.conductor = conductor;
        this.asientos = asientos;
    }

    // Getters y Setters
    public Long getIdBus() {
        return idBus;
    }

    public void setIdBus(Long idBus) {
        this.idBus = idBus;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Usuario getConductor() {
        return conductor;
    }

    public void setConductor(Usuario conductor) {
        this.conductor = conductor;
    }

    public List<Asiento> getAsientos() {
        return asientos;
    }

    public void setAsientos(List<Asiento> asientos) {
        this.asientos = asientos;
    }
}
