package com.example.demo.repository;

import com.example.demo.model.DetalleBoletoComprobante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface DetalleBoletoComprobanteRepository extends JpaRepository<DetalleBoletoComprobante, Long> {

    @Query("SELECT d FROM DetalleBoletoComprobante d " +
           "JOIN FETCH d.pasajero " +
           "JOIN FETCH d.asiento " +
           "WHERE d.idBoleto = :idBoleto")
    List<DetalleBoletoComprobante> findByIdBoletoConRelaciones(@Param("idBoleto") Long idBoleto);
}
