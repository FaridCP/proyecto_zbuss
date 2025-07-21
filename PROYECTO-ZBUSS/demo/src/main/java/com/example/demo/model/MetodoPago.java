package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "METODO_PAGO")
public class MetodoPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_metodo_pago")
    private Long idMetodoPago;

    @Column(nullable = false, unique = true)
    private String nombre;

    // Constructor vacío
    public MetodoPago() {
    }

    // Constructor con parámetros
    public MetodoPago(String nombre) {
        this.nombre = nombre;
    }

    // Getters y Setters
    public Long getIdMetodoPago() {
        return idMetodoPago;
    }

    public void setIdMetodoPago(Long idMetodoPago) {
        this.idMetodoPago = idMetodoPago;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
