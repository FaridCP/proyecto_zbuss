package com.example.demo.service;

import com.example.demo.model.Horario;
import com.example.demo.repository.HorarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HorarioService {

    @Autowired
    private HorarioRepository horarioRepository;

    public List<Horario> getHorariosByRutaId(Long idRuta) {
        return horarioRepository.findByRutaIdRuta(idRuta);
    }

    public Horario obtenerPrimerHorarioPorRuta(Long idRuta) {
        return horarioRepository.findFirstByRutaIdRuta(idRuta);
    }

    public Optional<Horario> getHorarioById(Long id) {
        return horarioRepository.findById(id);
    }

    public Horario obtenerHorarioPorId(Long id) {
        return horarioRepository.findById(id).orElse(null);
    }

        public Horario guardar(Horario horario) {
        return horarioRepository.save(horario);
    }

    public void eliminar(Long id) {
        horarioRepository.deleteById(id);
    }

    public List<Horario> getTodos() {
        return horarioRepository.findAll();
    }

}
