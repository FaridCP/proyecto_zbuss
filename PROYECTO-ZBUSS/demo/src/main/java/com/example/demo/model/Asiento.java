package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ASIENTO")
public class Asiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asiento")
    private Long idAsiento;

    @ManyToOne
    @JoinColumn(name = "id_bus", nullable = false)
    private Bus bus;

    @Column(nullable = false)
    private String numero;

    @Column(nullable = false)
    private Boolean ocupado = false;

    public Asiento() {
    }

    // Constructor con parámetros
    public Asiento(Bus bus, String numero, Boolean ocupado) {
        this.bus = bus;
        this.numero = numero;
        this.ocupado = ocupado;
    }

    // Getters y Setters
    public Long getIdAsiento() {
        return idAsiento;
    }

    public void setIdAsiento(Long idAsiento) {
        this.idAsiento = idAsiento;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Boolean getOcupado() {
        return ocupado;
    }

    public void setOcupado(Boolean ocupado) {
        this.ocupado = ocupado;
    }
}
