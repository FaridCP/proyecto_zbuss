package com.example.demo.controller.admin;

import com.example.demo.model.BoletoComprobante;
import com.example.demo.model.DetalleBoletoComprobante;
import com.example.demo.service.BoletoComprobanteService;
import com.example.demo.service.DetalleBoletoComprobanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/compras")
public class CompraAdminController {

    @Autowired
    private BoletoComprobanteService boletoService;

    @Autowired
    private DetalleBoletoComprobanteService detalleService;

    @GetMapping
    public String verCompras(Model model) {
        List<BoletoComprobante> compras = boletoService.obtenerTodosOrdenadosPorFecha();
        model.addAttribute("compras", compras);
        return "admin/compras/lista";
    }

    @GetMapping("/{id}")
    public String verDetalleCompra(@PathVariable Long id, Model model) {
        BoletoComprobante boleto = boletoService.obtenerPorId(id);
        if (boleto == null) {
            return "redirect:/admin/compras";
        }

        List<DetalleBoletoComprobante> detalles = detalleService.listarPorIdBoleto(id);

        model.addAttribute("boleto", boleto);
        model.addAttribute("detalles", detalles);
        return "admin/compras/detalle";
    }
}
