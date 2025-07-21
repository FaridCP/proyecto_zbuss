package com.example.demo.service;

import com.example.demo.model.DetalleBoletoComprobante;
import com.example.demo.repository.DetalleBoletoComprobanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetalleBoletoComprobanteService {

    @Autowired
    private DetalleBoletoComprobanteRepository repository;

    public DetalleBoletoComprobante guardar(DetalleBoletoComprobante detalle) {
        return repository.save(detalle);
    }

    public List<DetalleBoletoComprobante> listarPorIdBoleto(Long idBoleto) {
        return repository.findByIdBoletoConRelaciones(idBoleto); // ✅ Usamos la nueva query
    }
}
