package com.example.demo.service;

import com.example.demo.model.BoletoComprobante;
import com.example.demo.repository.BoletoComprobanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompraAdminService {

    @Autowired
    private BoletoComprobanteRepository boletoRepository;

    public List<BoletoComprobante> obtenerTodasLasCompras() {
        return boletoRepository.findAllByOrderByFechaEmisionDesc();
    }
}
