package com.example.demo.controller;

import com.example.demo.model.Asiento;
import com.example.demo.model.Horario;
import com.example.demo.model.Ruta;
import com.example.demo.service.AsientoService;
import com.example.demo.service.HorarioService;
import com.example.demo.service.ReservaAsientoService;
import com.example.demo.service.RutaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
public class AsientoController {

    @Autowired
    private RutaService rutaService;

    @Autowired
    private AsientoService asientoService;

    @Autowired
    private HorarioService horarioService;

    @Autowired
    private ReservaAsientoService reservaAsientoService;

    @GetMapping("/asientos/{idRuta}")
    public String mostrarAsientosPorRuta(@PathVariable Long idRuta,
                                         @RequestParam(name = "horarioId", required = false) Long horarioId,
                                         Model model,
                                         HttpSession session) {

        Optional<Ruta> rutaOptional = rutaService.getRutaById(idRuta);
        if (rutaOptional.isEmpty()) {
            model.addAttribute("error", "Ruta no encontrada.");
            return "error";
        }

        Ruta ruta = rutaOptional.get();
        List<Horario> horarios = horarioService.getHorariosByRutaId(idRuta);

        if (horarioId == null && !horarios.isEmpty()) {
            horarioId = horarios.get(0).getIdHorario(); // valor por defecto
        }

        List<Asiento> asientos = asientoService.obtenerAsientosPorRuta(idRuta);
        Set<Long> asientosReservados = reservaAsientoService.obtenerReservadosPorHorario(horarioId);

        for (Asiento a : asientos) {
            a.setOcupado(asientosReservados.contains(a.getIdAsiento()));
        }

        if (session.getAttribute("tiempoInicio") == null) {
            session.setAttribute("tiempoInicio", System.currentTimeMillis());
        }

        session.removeAttribute("timeoutCompra");

        model.addAttribute("ruta", ruta);
        model.addAttribute("asientos", asientos);
        model.addAttribute("horarios", horarios);
        model.addAttribute("horarioIdSeleccionado", horarioId);

        return "asientos";
    }

    @PostMapping("/asientos/continuar")
    public String continuarSeleccion(@RequestParam("rutaId") Long rutaId,
                                     @RequestParam("horarioId") Long horarioId,
                                     @RequestParam("asientoIds") String asientoIds,
                                     HttpSession session,
                                     Model model) {

        Object usuario = session.getAttribute("usuarioLogeado");

        if (usuario == null) {
            session.setAttribute("asientosPendientes", asientoIds);
            session.setAttribute("rutaPendiente", rutaId);
            return "redirect:/login";
        }

        List<Long> idsSeleccionados = List.of(asientoIds.split(","))
                                           .stream()
                                           .map(Long::parseLong)
                                           .toList();

        for (Long id : idsSeleccionados) {
            if (asientoService.estaReservado(id, horarioId)) {
                model.addAttribute("error", "Uno o más asientos ya han sido reservados. Por favor vuelve a seleccionar.");
                return "error";
            }
        }

        session.setAttribute("rutaIdSeleccionada", rutaId);
        session.setAttribute("horarioIdSeleccionado", horarioId);
        session.setAttribute("asientosSeleccionados", asientoIds);

        return "redirect:/pasajeros?rutaId=" + rutaId + "&asientos=" + asientoIds;
    }

    @GetMapping("/asientos/seleccionar")
    public String seleccionarAsientosPorHorario(@RequestParam("rutaId") Long rutaId,
                                                @RequestParam("horarioId") Long horarioId,
                                                HttpSession session,
                                                Model model) {

        Ruta ruta = rutaService.getRutaById(rutaId).orElse(null);
        if (ruta == null) {
            model.addAttribute("error", "Ruta no encontrada.");
            return "error";
        }

        List<Asiento> asientos = asientoService.obtenerAsientosPorRuta(rutaId);
        Set<Long> idsOcupados = reservaAsientoService.obtenerReservadosPorHorario(horarioId);

        for (Asiento asiento : asientos) {
            asiento.setOcupado(idsOcupados.contains(asiento.getIdAsiento()));
        }

        List<Horario> horarios = horarioService.getHorariosByRutaId(rutaId);

        model.addAttribute("ruta", ruta);
        model.addAttribute("asientos", asientos);
        model.addAttribute("horarios", horarios);
        model.addAttribute("horarioIdSeleccionado", horarioId);

        session.setAttribute("rutaIdSeleccionada", rutaId);
        session.setAttribute("horarioIdSeleccionado", horarioId);

        return "asientos";
    }
}

