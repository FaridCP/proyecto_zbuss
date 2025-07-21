package com.example.demo.repository;

import com.example.demo.model.Ruta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RutaRepository extends JpaRepository<Ruta, Long> {

    // Consulta personalizada para traer rutas con buses y asientos asociados
  
    // Otros métodos de consulta por origen, destino o ambos
    List<Ruta> findByOrigen(String origen);

    List<Ruta> findByDestino(String destino);

    List<Ruta> findByOrigenAndDestino(String origen, String destino);
}
