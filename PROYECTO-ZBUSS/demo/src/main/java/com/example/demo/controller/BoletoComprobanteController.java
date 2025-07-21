package com.example.demo.controller;

import com.example.demo.model.BoletoComprobante;
import com.example.demo.service.BoletoComprobanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boletos-comprobantes")
public class BoletoComprobanteController {

    @Autowired
    private BoletoComprobanteService service;

    @GetMapping
    public List<BoletoComprobante> findAll() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoletoComprobante> findById(@PathVariable Long id) {
        BoletoComprobante result = service.obtenerPorId(id);
        return (result != null)
                ? ResponseEntity.ok(result)
                : ResponseEntity.notFound().build();
    }

    @PostMapping
    public BoletoComprobante save(@RequestBody BoletoComprobante entity) {
        return service.guardar(entity);
    }

    @GetMapping("/pdf/{id}")
    public ResponseEntity<byte[]> descargarComprobantePdf(@PathVariable Long id) {
        byte[] pdfBytes = service.generarComprobantePdf(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=comprobante_zbuss_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
