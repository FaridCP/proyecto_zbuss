package com.example.demo.repository;

import com.example.demo.model.ReservaAsiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservaAsientoRepository extends JpaRepository<ReservaAsiento, Long> {

    List<ReservaAsiento> findByHorarioIdHorario(Long idHorario);

    boolean existsByAsientoIdAsientoAndHorarioIdHorarioAndReservadoTrue(Long idAsiento, Long idHorario);
}

