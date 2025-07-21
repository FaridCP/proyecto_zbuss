package com.example.demo.service;

import com.example.demo.model.Horario;
import com.example.demo.model.Ruta;
import com.example.demo.repository.HorarioRepository;
import com.example.demo.repository.RutaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RutaService {

    @Autowired
    private RutaRepository rutaRepository;

    @Autowired
    private HorarioRepository horarioRepository;

    public List<Ruta> obtenerTodas() {
        return rutaRepository.findAll();
    }

    public List<Ruta> getRutasByOrigen(String origen) {
        return rutaRepository.findByOrigen(origen);
    }

    public List<Ruta> getRutasByDestino(String destino) {
        return rutaRepository.findByDestino(destino);
    }

    public List<Ruta> getRutasByOrigenAndDestino(String origen, String destino) {
        return rutaRepository.findByOrigenAndDestino(origen, destino);
    }

    public Optional<Ruta> getRutaById(Long id) {
        return rutaRepository.findById(id);
    }

    public Ruta saveRuta(Ruta ruta) {
        return rutaRepository.save(ruta);
    }

    public void deleteRuta(Long id) {
        rutaRepository.deleteById(id);
    }

    public List<Horario> obtenerHorariosPorRuta(Long idRuta) {
        return horarioRepository.findByRutaIdRuta(idRuta);
    }

    public Horario obtenerPrimerHorario(Long idRuta) {
        return horarioRepository.findFirstByRutaIdRuta(idRuta);
    }

    
}
