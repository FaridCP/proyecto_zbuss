package com.example.demo.controller.admin;

import com.example.demo.model.Horario;
import com.example.demo.model.Ruta;
import com.example.demo.service.HorarioService;
import com.example.demo.service.RutaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/horarios")
public class AdminHorarioController {

    @Autowired
    private HorarioService horarioService;

    @Autowired
    private RutaService rutaService;

    @GetMapping
    public String listarHorarios(Model model) {
        List<Ruta> rutas = rutaService.obtenerTodas();
        model.addAttribute("rutas", rutas);
        return "admin/horarios/lista";
    }

    @GetMapping("/ruta/{idRuta}")
    public String listarHorariosPorRuta(@PathVariable Long idRuta, Model model) {
        List<Horario> horarios = horarioService.getHorariosByRutaId(idRuta);
        Ruta ruta = rutaService.getRutaById(idRuta).orElse(null);

        model.addAttribute("horarios", horarios);
        model.addAttribute("ruta", ruta);
        return "admin/horarios/lista-ruta";
    }

    @GetMapping("/nuevo/{idRuta}")
    public String mostrarFormularioNuevo(@PathVariable Long idRuta, Model model) {
        Horario horario = new Horario();
        Ruta ruta = rutaService.getRutaById(idRuta).orElse(null);
        horario.setRuta(ruta);

        model.addAttribute("horario", horario);
        return "admin/horarios/formulario";
    }

    @PostMapping("/guardar")
    public String guardarHorario(@ModelAttribute("horario") Horario horario) {
        horarioService.guardar(horario);
        return "redirect:/admin/horarios/ruta/" + horario.getRuta().getIdRuta();
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Horario horario = horarioService.obtenerHorarioPorId(id);
        model.addAttribute("horario", horario);
        return "admin/horarios/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        Horario horario = horarioService.obtenerHorarioPorId(id);
        if (horario != null && horario.getRuta() != null) {
            Long idRuta = horario.getRuta().getIdRuta();
            horarioService.eliminar(id);
            return "redirect:/admin/horarios/ruta/" + idRuta;
        }
        return "redirect:/admin/horarios";
    }
}
