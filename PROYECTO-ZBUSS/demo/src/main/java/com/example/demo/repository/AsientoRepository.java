package com.example.demo.repository;

import com.example.demo.model.Asiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsientoRepository extends JpaRepository<Asiento, Long> {

    List<Asiento> findByBus_IdBusAndOcupadoFalse(Long busId);

    List<Asiento> findByBus_IdBus(Long idBus);

    // 🔧 AGREGADO: Búsqueda por número + bus
    List<Asiento> findByBus_IdBusAndNumeroIn(Long idBus, List<String> numeros);
}
