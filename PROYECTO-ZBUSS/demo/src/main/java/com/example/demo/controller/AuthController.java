package com.example.demo.controller;

import com.example.demo.model.Usuario;
import com.example.demo.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("usuario") Usuario usuario) {
        usuarioService.registerUser(usuario);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "rutaId", required = false) Long rutaId,
                                 @RequestParam(value = "horarioId", required = false) Long horarioId,
                                 @RequestParam(value = "asientos", required = false) String asientos,
                                 HttpSession session) {

        if (rutaId != null && horarioId != null && asientos != null) {
            session.setAttribute("rutaPendiente", rutaId);
            session.setAttribute("horarioPendiente", horarioId);
            session.setAttribute("asientosPendientes", asientos);
        }

        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam("correo") String correo,
                            @RequestParam("contrasena") String clave,
                            Model model,
                            HttpSession session) {

        Usuario usuario = usuarioService.loginUser(correo, clave);

        if (usuario != null) {
            session.setAttribute("usuarioLogeado", usuario);
            session.setAttribute("idUsuario", usuario.getIdUsuario());

            // Si hay asientos pendientes, redirigir a completar compra
            String asientosPendientes = (String) session.getAttribute("asientosPendientes");
            Long rutaPendiente = (Long) session.getAttribute("rutaPendiente");
            Long horarioPendiente = (Long) session.getAttribute("horarioPendiente");

            if (asientosPendientes != null && rutaPendiente != null && horarioPendiente != null) {
                session.removeAttribute("asientosPendientes");
                session.removeAttribute("rutaPendiente");
                session.removeAttribute("horarioPendiente");

                session.setAttribute("rutaIdSeleccionada", rutaPendiente);
                session.setAttribute("horarioIdSeleccionado", horarioPendiente);
                session.setAttribute("asientosSeleccionados", asientosPendientes);

                return "redirect:/pasajeros?rutaId=" + rutaPendiente + "&asientos=" + asientosPendientes;
            }

            if ("ADMIN".equalsIgnoreCase(usuario.getRol().getNombre())) {
                return "redirect:/admin/dashboard";
            }

            return "redirect:/home";
        }

        model.addAttribute("error", "Credenciales incorrectas. Inténtalo de nuevo.");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/home";
    }

    @GetMapping("/cambiar-contrasena")
    public String mostrarFormularioCambio(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogeado");
        if (usuario == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuario", usuario);
        return "cambiar-contrasena";
    }

    @PostMapping("/cambiar-contrasena")
    public String cambiarContrasena(HttpSession session,
                                    @RequestParam("actual") String actual,
                                    @RequestParam("nueva") String nueva,
                                    @RequestParam("confirmar") String confirmar,
                                    Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogeado");
        if (usuario == null) {
            return "redirect:/login";
        }

        if (!usuario.getClave().equals(actual)) {
            model.addAttribute("error", "La contraseña actual es incorrecta.");
            return "cambiar-contrasena";
        }

        if (!nueva.equals(confirmar)) {
            model.addAttribute("error", "Las contraseñas nuevas no coinciden.");
            return "cambiar-contrasena";
        }

        usuario.setClave(nueva);
        usuarioService.actualizarUsuario(usuario);

        model.addAttribute("mensaje", "Contraseña actualizada correctamente.");
        return "cambiar-contrasena";
    }
}
