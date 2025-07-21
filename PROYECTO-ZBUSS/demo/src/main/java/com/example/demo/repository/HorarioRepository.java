package com.example.demo.repository;

import com.example.demo.model.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HorarioRepository extends JpaRepository<Horario, Long> {
    List<Horario> findByRutaIdRuta(Long idRuta);

    Horario findFirstByRutaIdRuta(Long idRuta);
}
