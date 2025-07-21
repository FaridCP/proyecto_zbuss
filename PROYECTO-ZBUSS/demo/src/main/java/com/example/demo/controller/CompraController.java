package com.example.demo.controller;

import com.example.demo.model.Usuario;
import com.example.demo.model.Pasajero;
import com.example.demo.service.RutaService;
import com.example.demo.service.PasajeroService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/compra")
public class CompraController {

    @Autowired
    private RutaService rutaService;

    @Autowired
    private PasajeroService pasajeroService;

    @PostMapping("/resumen")
    public String mostrarResumenCompra(@RequestParam("rutaId") Long rutaId,
                                       @RequestParam("asientoIds") String asientoIds,
                                       HttpSession session,
                                       Model model) {

        Object usuarioObj = session.getAttribute("usuarioLogeado");

        if (usuarioObj == null) {
            session.setAttribute("rutaPendiente", rutaId);
            session.setAttribute("asientosPendientes", asientoIds);
            return "redirect:/login";
        }

        Usuario usuario = (Usuario) usuarioObj;
        var ruta = rutaService.getRutaById(rutaId).orElse(null);
        if (ruta == null) {
            model.addAttribute("error", "La ruta no fue encontrada.");
            return "error";
        }

        List<String> listaAsientos = Arrays.asList(asientoIds.split(","));
        List<Pasajero> pasajeros = pasajeroService.obtenerPasajerosPorUsuario(usuario);

        double precioUnitario = ruta.getPrecio();
        double total = precioUnitario * listaAsientos.size();

        model.addAttribute("usuario", usuario);
        model.addAttribute("ruta", ruta);
        model.addAttribute("asientos", listaAsientos);
        model.addAttribute("precioUnitario", precioUnitario);
        model.addAttribute("total", total);
        model.addAttribute("pasajeros", pasajeros);  // <-- Esta línea es clave

        return "resumen-compra";
    }

    @PostMapping("/confirmar")
    public String procesarPago(HttpSession session, Model model) {
        return "redirect:/confirmacion-exitosa";
    }
}

