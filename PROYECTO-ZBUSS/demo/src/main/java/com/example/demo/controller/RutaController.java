package com.example.demo.controller;

import com.example.demo.model.Ruta;
import com.example.demo.service.RutaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class RutaController {

    @Autowired
    private RutaService rutaService;

    @GetMapping("/rutas")
    public String mostrarRutas(Model model) {
        List<Ruta> rutas = rutaService.obtenerTodas();
        model.addAttribute("rutas", rutas);
        return "rutas";
    }

    @GetMapping("/seleccionar-ruta")
    public String mostrarSeleccionRuta(Model model) {
        List<Ruta> rutas = rutaService.obtenerTodas();
        model.addAttribute("rutas", rutas);
        return "seleccionRuta";
    }

}
