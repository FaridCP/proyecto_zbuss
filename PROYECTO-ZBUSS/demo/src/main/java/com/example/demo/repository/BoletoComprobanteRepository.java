package com.example.demo.repository;

import com.example.demo.model.BoletoComprobante;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoletoComprobanteRepository extends JpaRepository<BoletoComprobante, Long> {
    
    Page<BoletoComprobante> findByUsuarioIdUsuario(Long idUsuario, Pageable pageable);

    List<BoletoComprobante> findAllByOrderByFechaEmisionDesc();

    @Query("SELECT SUM(b.precio) FROM BoletoComprobante b")
    Optional<BigDecimal> totalRecaudado();
    
}