package com.example.demo.repository;

import com.example.demo.model.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {
    // Aquí puedes agregar consultas personalizadas si es necesario
}
