package com.example.demo.controller.admin;

import com.example.demo.model.Ruta;
import com.example.demo.service.RutaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/rutas")
public class AdminRutaController {

    @Autowired
    private RutaService rutaService;

    @GetMapping
    public String listarRutas(Model model) {
        model.addAttribute("rutas", rutaService.obtenerTodas());
        return "admin/rutas/lista-rutas";  // tabla con lista de rutas
    }

    @GetMapping("/nueva")
    public String mostrarFormularioNuevaRuta(Model model) {
        model.addAttribute("ruta", new Ruta());
        return "admin/rutas/form"; // formulario para crear
    }

    @PostMapping("/guardar")
    public String guardarRuta(@ModelAttribute Ruta ruta, RedirectAttributes redirect) {
        boolean esNueva = (ruta.getIdRuta() == null);
        rutaService.saveRuta(ruta);
        if (esNueva) {
            redirect.addAttribute("exito", true);
        } else {
            redirect.addAttribute("actualizado", true);
        }
        return "redirect:/admin/rutas";
    }

    @GetMapping("/editar/{id}")
    public String editarRuta(@PathVariable Long id, Model model) {
        Ruta ruta = rutaService.getRutaById(id).orElse(null);
        if (ruta == null) {
            return "redirect:/admin/rutas?noEncontrado";
        }
        model.addAttribute("ruta", ruta);
        return "admin/rutas/formulario-ruta"; // formulario para editar
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarRuta(@PathVariable Long id, RedirectAttributes redirect) {
        rutaService.deleteRuta(id);
        redirect.addAttribute("eliminado", true);
        return "redirect:/admin/rutas";
    }
}
