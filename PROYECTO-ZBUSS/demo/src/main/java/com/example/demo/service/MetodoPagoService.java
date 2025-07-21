package com.example.demo.service;

import com.example.demo.model.MetodoPago;
import com.example.demo.repository.MetodoPagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetodoPagoService {

    @Autowired
    private MetodoPagoRepository metodoPagoRepository;

    public List<MetodoPago> listarTodos() {
        return metodoPagoRepository.findAll();
    }

    public MetodoPago obtenerPorId(Long id) {
        return metodoPagoRepository.findById(id).orElse(null);
    }

    public MetodoPago buscarPorId(Long id) {
        return metodoPagoRepository.findById(id).orElse(null);
    }

}
