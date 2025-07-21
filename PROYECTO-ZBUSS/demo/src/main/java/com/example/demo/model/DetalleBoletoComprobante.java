package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "detalle_boleto_comprobante")
public class DetalleBoletoComprobante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalle;

    @Column(name = "id_boleto", nullable = false)
    private Long idBoleto;

    @Column(name = "id_pasajero", nullable = false)
    private Long idPasajero;

    @Column(name = "id_asiento", nullable = false)
    private Long idAsiento;

    @Column(name = "id_horario", nullable = false)
    private Long idHorario;

    @Column(name = "id_ruta", nullable = false)
    private Long idRuta;

    @Column(nullable = false)
    private Double precio;

    // Relación con Pasajero (para mostrar datos personales)
    @ManyToOne
    @JoinColumn(name = "id_pasajero", insertable = false, updatable = false)
    private Pasajero pasajero;

    // Relación con Asiento (para mostrar número de asiento)
    @ManyToOne
    @JoinColumn(name = "id_asiento", insertable = false, updatable = false)
    private Asiento asiento;

    // --- Getters y Setters ---
    public Long getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(Long idDetalle) {
        this.idDetalle = idDetalle;
    }

    public Long getIdBoleto() {
        return idBoleto;
    }

    public void setIdBoleto(Long idBoleto) {
        this.idBoleto = idBoleto;
    }

    public Long getIdPasajero() {
        return idPasajero;
    }

    public void setIdPasajero(Long idPasajero) {
        this.idPasajero = idPasajero;
    }

    public Long getIdAsiento() {
        return idAsiento;
    }

    public void setIdAsiento(Long idAsiento) {
        this.idAsiento = idAsiento;
    }

    public Long getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(Long idHorario) {
        this.idHorario = idHorario;
    }

    public Long getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(Long idRuta) {
        this.idRuta = idRuta;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Pasajero getPasajero() {
        return pasajero;
    }

    public void setPasajero(Pasajero pasajero) {
        this.pasajero = pasajero;
    }

    public Asiento getAsiento() {
        return asiento;
    }

    public void setAsiento(Asiento asiento) {
        this.asiento = asiento;
    }
}

