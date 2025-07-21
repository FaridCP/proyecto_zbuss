package com.example.demo.service;

import com.example.demo.model.Pasajero;
import com.example.demo.model.Usuario;
import com.example.demo.repository.PasajeroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PasajeroService {

    @Autowired
    private PasajeroRepository pasajeroRepository;

    public void guardarPasajeros(List<Pasajero> pasajeros) {
        pasajeroRepository.saveAll(pasajeros);
    }

    public void actualizarPasajero(Pasajero pasajero){
    }

    public List<Pasajero> obtenerPasajerosPorUsuario(Usuario usuario) {
        return pasajeroRepository.findByUsuario(usuario);
    }

    public List<Pasajero> listarTodos() {
        return pasajeroRepository.findAll();
    }

    public long contarTodos() {
        return pasajeroRepository.count();
    }

    public List<Pasajero> buscarConFiltros(String dni, LocalDate fechaNacimiento, String correoUsuario) {
        return pasajeroRepository.buscarConFiltros(
            dni != null ? dni.toLowerCase() : null,
            fechaNacimiento,
            correoUsuario != null ? correoUsuario.toLowerCase() : null
        );
    }

}
