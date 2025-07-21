package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/pasajeros")
public class PasajeroController {

    @Autowired
    private PasajeroService pasajeroService;

    @Autowired
    private RutaService rutaService;

    @Autowired
    private HorarioService horarioService;

    @Autowired
    private AsientoService asientoService;

    @Autowired
    private ReservaAsientoService reservaAsientoService;

    @GetMapping
    public String mostrarFormularioPasajeros(@RequestParam("rutaId") Long rutaId,
                                             @RequestParam("asientos") String asientoIdsStr,
                                             Model model,
                                             HttpSession session) {

        Long tiempoInicio = (Long) session.getAttribute("tiempoInicio");
        long restanteSegundos = 300;

        if (tiempoInicio != null) {
            long tiempoActual = System.currentTimeMillis();
            long diferencia = tiempoActual - tiempoInicio;

            if (diferencia > 5 * 60 * 1000) {
                limpiarSesionCompra(session);
                session.setAttribute("timeoutCompra", true);
                return "redirect:/rutas";
            }

            restanteSegundos = Math.max(0, (5 * 60 * 1000 - diferencia) / 1000);
        }

        model.addAttribute("segundosRestantes", restanteSegundos);

        Ruta ruta = rutaService.getRutaById(rutaId).orElse(null);
        if (ruta == null) {
            model.addAttribute("error", "Ruta no encontrada.");
            return "error";
        }

        Long horarioId = (Long) session.getAttribute("horarioIdSeleccionado");
        if (horarioId == null) {
            return "redirect:/rutas";
        }

        Horario horario = horarioService.obtenerHorarioPorId(horarioId);
        List<Long> asientoIds = List.of(asientoIdsStr.split(",")).stream().map(Long::parseLong).toList();
        List<Asiento> asientos = asientoService.obtenerPorIds(asientoIds);
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogeado");

        session.setAttribute("rutaIdSeleccionada", rutaId);
        session.setAttribute("asientoIdsSeleccionados", asientoIds);

        model.addAttribute("ruta", ruta);
        model.addAttribute("horario", horario);
        model.addAttribute("asientosSeleccionados", asientos);
        model.addAttribute("cantidadPasajeros", asientos.size());
        model.addAttribute("usuario", usuario);

        return "pasajeros";
    }

    @PostMapping("/confirmar")
    public String registrarPasajeros(@RequestParam("asientoIds") List<Long> asientoIds,
                                     @RequestParam("nombres") List<String> nombres,
                                     @RequestParam("apellidos") List<String> apellidos,
                                     @RequestParam("dni") List<String> dnis,
                                     @RequestParam("telefono") List<String> telefonos,
                                     @RequestParam("fechaNacimiento") List<String> fechasNacimiento,
                                     HttpSession session,
                                     Model model) {

        if (nombres.size() != asientoIds.size()) {
            model.addAttribute("error", "La cantidad de nombres no coincide con la de asientos seleccionados.");
            return "error";
        }

        Long idUsuario = (Long) session.getAttribute("idUsuario");
        Long rutaId = (Long) session.getAttribute("rutaIdSeleccionada");
        Long horarioId = (Long) session.getAttribute("horarioIdSeleccionado");

        if (idUsuario == null || rutaId == null || horarioId == null) {
            return "redirect:/rutas";
        }

        Ruta ruta = rutaService.getRutaById(rutaId).orElse(null);
        Horario horario = horarioService.obtenerHorarioPorId(horarioId);

        if (ruta == null || horario == null) {
            model.addAttribute("error", "Datos inválidos para ruta u horario.");
            return "error";
        }

        List<Asiento> asientos = asientoService.obtenerPorIds(asientoIds);
        if (asientos == null || asientos.size() != asientoIds.size()) {
            model.addAttribute("error", "No se pudieron obtener todos los asientos seleccionados.");
            return "error";
        }

        List<Pasajero> pasajeros = new ArrayList<>();
        List<Double> preciosIndividuales = new ArrayList<>();

        for (int i = 0; i < nombres.size(); i++) {
            Pasajero pasajero = new Pasajero();
            pasajero.setNombres(nombres.get(i));
            pasajero.setApellidos(apellidos.get(i));
            pasajero.setDni(dnis.get(i));
            pasajero.setTelefono(telefonos.get(i));

            Date fechaNac = Date.valueOf(fechasNacimiento.get(i));
            pasajero.setFechaNacimiento(fechaNac);

            Usuario u = new Usuario();
            u.setIdUsuario(idUsuario);
            pasajero.setUsuario(u);

            pasajero.setRuta(ruta);
            pasajero.setHorario(horario);
            pasajero.setAsiento(asientos.get(i));

            pasajeros.add(pasajero);

            reservaAsientoService.marcarComoReservado(asientos.get(i), horario);

            double precioBase = ruta.getPrecio();
            int edad = Period.between(fechaNac.toLocalDate(), LocalDate.now()).getYears();
            double precioFinal = edad < 18 ? precioBase * 0.5 : precioBase;

            preciosIndividuales.add(precioFinal);
        }

        pasajeroService.guardarPasajeros(pasajeros);

        double total = preciosIndividuales.stream().mapToDouble(Double::doubleValue).sum();
        List<String> numeros = asientos.stream().map(Asiento::getNumero).toList();

        session.setAttribute("pasajerosSeleccionados", pasajeros);
        session.setAttribute("rutaSeleccionada", ruta);
        session.setAttribute("horarioSeleccionado", horario);
        session.setAttribute("resumenHecho", "ok");
        session.setAttribute("preciosIndividuales", preciosIndividuales);
        session.setAttribute("precioTotalCalculado", total);
        session.setAttribute("asientosSeleccionados", numeros);

        return "redirect:/pasajeros/resumen-compra";
    }

    @GetMapping("/resumen-compra")
    public String verResumenCompra(HttpSession session, Model model) {
        Long tiempoInicio = (Long) session.getAttribute("tiempoInicio");
        long restanteSegundos = 300;

        if (tiempoInicio != null) {
            long ahora = System.currentTimeMillis();
            long diferencia = ahora - tiempoInicio;

            if (diferencia > 5 * 60 * 1000) {
                limpiarSesionCompra(session);
                session.setAttribute("timeoutCompra", true);
                return "redirect:/rutas";
            }

            restanteSegundos = Math.max(0, (5 * 60 * 1000 - diferencia) / 1000);
        }

        model.addAttribute("segundosRestantes", restanteSegundos);
        model.addAttribute("pasajeros", session.getAttribute("pasajerosSeleccionados"));
        model.addAttribute("ruta", session.getAttribute("rutaSeleccionada"));
        model.addAttribute("horario", session.getAttribute("horarioSeleccionado"));
        model.addAttribute("preciosIndividuales", session.getAttribute("preciosIndividuales"));
        model.addAttribute("precioTotal", session.getAttribute("precioTotalCalculado"));
        model.addAttribute("asientos", session.getAttribute("asientosSeleccionados"));

        return "resumen-compra";
    }

    // 🧹 Limpieza de sesión para cuando se acaba el tiempo
    private void limpiarSesionCompra(HttpSession session) {
        session.removeAttribute("tiempoInicio");
        session.removeAttribute("pasajerosSeleccionados");
        session.removeAttribute("rutaSeleccionada");
        session.removeAttribute("horarioSeleccionado");
        session.removeAttribute("resumenHecho");
        session.removeAttribute("preciosIndividuales");
        session.removeAttribute("precioTotalCalculado");
        session.removeAttribute("asientoIdsSeleccionados");
        session.removeAttribute("rutaIdSeleccionada");
        session.removeAttribute("horarioIdSeleccionado");
        session.removeAttribute("asientosSeleccionados");
    }
}
