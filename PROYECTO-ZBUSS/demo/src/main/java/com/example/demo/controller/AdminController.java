package com.example.demo.controller;

import com.example.demo.model.Asiento;
import com.example.demo.model.Pasajero;
import com.example.demo.model.Usuario;
import com.example.demo.service.AsientoService;
import com.example.demo.service.BoletoComprobanteService;
import com.example.demo.service.EmpresaService;
import com.example.demo.service.HorarioService;
import com.example.demo.service.PasajeroService;
import com.example.demo.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

import com.example.demo.service.RutaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private HorarioService horarioService;

    @Autowired
    private BoletoComprobanteService boletoComprobanteService;

    @Autowired
    private EmpresaService empresaService;


    @GetMapping
    public String panelPrincipal() {
        return "admin/panel";
    }

    @GetMapping("/usuarios")
    public String verUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.listarTodos();
        model.addAttribute("usuarios", usuarios);
        return "admin/usuarios/lista";
    }

    @Autowired
    private PasajeroService pasajeroService;

    @Autowired
    private AsientoService asientoService;

    @Autowired
    private RutaService rutaService;

    @GetMapping("/asientos")
    public String verAsientos(@RequestParam(required = false) Long rutaId,
                            @RequestParam(required = false) Long horarioId,
                            Model model) {

        if (rutaId != null && horarioId != null) {
            List<Asiento> asientos = asientoService.obtenerAsientosConReservas(rutaId, horarioId);
            model.addAttribute("asientos", asientos);
        }

        model.addAttribute("rutas", rutaService.obtenerTodas());
        model.addAttribute("rutaIdSeleccionada", rutaId);
        model.addAttribute("horarioIdSeleccionado", horarioId);

        if (rutaId != null) {
            model.addAttribute("horarios", rutaService.obtenerHorariosPorRuta(rutaId));
        }

        return "admin/asientos/lista";
    }

    @GetMapping("/dashboard")
    public String verDashboard(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogeado");

        if (usuario == null || usuario.getRol() == null || !"ADMIN".equalsIgnoreCase(usuario.getRol().getNombre())) {
            return "redirect:/login";
        }

        model.addAttribute("totalRutas", rutaService.obtenerTodas().size());
        model.addAttribute("totalHorarios", horarioService.getTodos().size());
        model.addAttribute("totalBoletos", boletoComprobanteService.listarTodos().size());
        model.addAttribute("totalPasajeros", pasajeroService.contarTodos());
        model.addAttribute("totalUsuarios", usuarioService.contarTodos());
        model.addAttribute("totalEmpresas", empresaService.contarTodos());
        model.addAttribute("totalRecaudado", boletoComprobanteService.totalRecaudado());

        return "admin-dashboard";
    }

    @GetMapping("/pasajeros")
    public String verPasajeros(
            @RequestParam(value = "dni", required = false) String dni,
            @RequestParam(value = "fechaNacimiento", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaNacimiento,
            @RequestParam(value = "correo", required = false) String correo,
            Model model) {

        List<Pasajero> pasajeros = pasajeroService.buscarConFiltros(dni, fechaNacimiento, correo);

        model.addAttribute("pasajeros", pasajeros);
        model.addAttribute("dni", dni);
        model.addAttribute("fechaNacimiento", fechaNacimiento != null ? fechaNacimiento.toString() : "");
        model.addAttribute("correo", correo);
        return "admin/pasajeros/lista";
    }

}
