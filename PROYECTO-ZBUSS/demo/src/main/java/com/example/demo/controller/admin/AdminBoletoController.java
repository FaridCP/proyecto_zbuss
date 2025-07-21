package com.example.demo.controller.admin;

import com.example.demo.model.BoletoComprobante;
import com.example.demo.service.BoletoComprobanteService;
import com.example.demo.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/admin/boletos")
public class AdminBoletoController {

    @Autowired
    private BoletoComprobanteService boletoService;

    @Autowired
    private EmailService emailService;

    @GetMapping
    public String listarBoletos(Model model) {
        List<BoletoComprobante> boletos = boletoService.obtenerTodosOrdenadosPorFecha();
        model.addAttribute("boletos", boletos);
        return "admin/boletos/lista-boletos";
    }

    @GetMapping("/detalle/{id}")
    public String verDetalle(@PathVariable Long id, Model model) {
        BoletoComprobante boleto = boletoService.obtenerPorId(id);
        if (boleto == null) return "redirect:/admin/boletos";
        model.addAttribute("boleto", boleto);
        return "admin/boletos/detalle-boleto";
    }

    @GetMapping("/enviar-correo/{id}")
    public String enviarComprobantePorCorreo(@PathVariable Long id) {
        boletoService.enviarComprobantePorCorreo(id);
        return "redirect:/admin/boletos/detalle/" + id + "?correoEnviado=true";
    }

    @PostMapping("/enviar-correo-manual")
    public String enviarCorreoManual(@RequestParam("idBoleto") Long idBoleto,
                                    @RequestParam("correo") String correo) {
        try {
            byte[] pdf = boletoService.generarComprobantePdf(idBoleto);
            String nombreArchivo = "comprobante_zbuss_" + idBoleto + ".pdf";
            if (correo != null && !correo.isBlank()) {
                emailService.enviarComprobante(correo, pdf, nombreArchivo);
            }
            return "redirect:/admin/boletos/detalle/" + idBoleto + "?correoEnviado=true";
        } catch (Exception e) {
            return "redirect:/admin/boletos/detalle/" + idBoleto + "?correoError=true";
        }
    }


}

