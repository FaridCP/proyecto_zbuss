package com.example.demo.service;

import com.example.demo.model.BoletoComprobante;
import com.example.demo.repository.BoletoComprobanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BoletoComprobanteService {

    @Autowired
    private BoletoComprobanteRepository boletoComprobanteRepository;

    @Autowired
    private PdfGeneratorService pdfGeneratorService;

    public BoletoComprobante guardar(BoletoComprobante boleto) {
        return boletoComprobanteRepository.save(boleto);
    }

    public String generarNumeroSerie(String tipo) {
        Long count = boletoComprobanteRepository.count();
        String prefix = tipo.equalsIgnoreCase("BOLETA") ? "B001" : "F001";
        String numero = String.format("%06d", count + 1);
        return prefix + "-" + numero;
    }

    public List<BoletoComprobante> listarTodos() {
        return boletoComprobanteRepository.findAll();
    }

    public BoletoComprobante obtenerPorId(Long id) {
        return boletoComprobanteRepository.findById(id).orElse(null);
    }


    public byte[] generarComprobantePdf(Long id) {
        return pdfGeneratorService.generarPdfComoBytes(id); // ✅ usa el generador real
    }

    public void eliminarPorId(Long id) {
        boletoComprobanteRepository.deleteById(id);
    }

    public Page<BoletoComprobante> obtenerBoletosPaginados(Long idUsuario, int pagina, int tamano) {
        Pageable pageable = PageRequest.of(pagina, tamano);
        return boletoComprobanteRepository.findByUsuarioIdUsuario(idUsuario, pageable);
    }

    public List<BoletoComprobante> obtenerTodosOrdenadosPorFecha() {
        return boletoComprobanteRepository.findAllByOrderByFechaEmisionDesc();
    }

    public BigDecimal totalRecaudado() {
        return boletoComprobanteRepository.totalRecaudado()
            .orElse(BigDecimal.ZERO);
    }

    @Autowired
    private EmailService emailService;

    public void enviarComprobantePorCorreo(Long idBoleto) {
        BoletoComprobante boleto = obtenerPorId(idBoleto);
        if (boleto != null && boleto.getUsuario() != null) {
            byte[] pdf = generarComprobantePdf(idBoleto);
            String nombreArchivo = "comprobante_zbuss_" + idBoleto + ".pdf";

            String destinatario = boleto.getUsuario().getCorreo();
            if (destinatario != null && !destinatario.isBlank()) {
                emailService.enviarComprobante(destinatario, pdf, nombreArchivo);
            }
        }
    }
 
}
