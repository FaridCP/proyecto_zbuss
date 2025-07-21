package com.example.demo.controller;

import com.example.demo.model.BoletoComprobante;
import com.example.demo.model.DetalleBoletoComprobante;
import com.example.demo.model.Usuario;
import com.example.demo.service.BoletoComprobanteService;
import com.example.demo.service.DetalleBoletoComprobanteService;
import com.example.demo.service.EmailService;
import com.example.demo.service.PdfGeneratorService;
import com.example.demo.service.UsuarioService;
import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/perfil")
public class PerfilController {

    @Autowired
    private BoletoComprobanteService boletoService;

    @Autowired
    private DetalleBoletoComprobanteService detalleService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PdfGeneratorService pdfGeneratorService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/compras")
    public String verMisCompras(HttpSession session,
                                Model model,
                                @RequestParam(defaultValue = "0") int pagina,
                                @RequestParam(defaultValue = "15") int tamano) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogeado");
        if (usuario == null) {
            return "redirect:/login";
        }

        Page<BoletoComprobante> paginaBoletos = boletoService.obtenerBoletosPaginados(usuario.getIdUsuario(), pagina, tamano);

        model.addAttribute("boletosPage", paginaBoletos);
        return "mis-compras";
    }

    @GetMapping("/compras/{id}")
    public String verDetalleCompra(@PathVariable Long id, HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogeado");
        if (usuario == null) {
            return "redirect:/login";
        }

        BoletoComprobante boleto = boletoService.obtenerPorId(id);

        if (boleto == null || !boleto.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
            return "redirect:/perfil/compras";
        }

        List<DetalleBoletoComprobante> detalles = detalleService.listarPorIdBoleto(id);

        model.addAttribute("boleto", boleto);
        model.addAttribute("detalles", detalles);
        return "detalle-compra";
    }

    @PostMapping("/compras/enviar-correo/{idBoleto}")
    public String enviarCorreoComprobante(@PathVariable Long idBoleto,
                                        @RequestParam("correoDestino") String correoDestino,
                                        HttpSession session,
                                        Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogeado");
        if (usuario == null) {
            return "redirect:/login";
        }

        BoletoComprobante boleto = boletoService.obtenerPorId(idBoleto);

        if (boleto == null || !boleto.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
            return "redirect:/perfil/compras";
        }

        try {
            byte[] pdf = pdfGeneratorService.generarPdfComoBytes(idBoleto);
            emailService.enviarComprobante(correoDestino, pdf, "comprobante_" + boleto.getNumeroSerie() + ".pdf");
            model.addAttribute("correoEnviado", true);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorEnvioCorreo", "❌ No se pudo enviar el correo.");
        }

        return "redirect:/perfil/compras/" + idBoleto;
    }

    @GetMapping("/cambiar-contrasena")
    public String mostrarFormularioCambio(HttpSession session) {
        if (session.getAttribute("usuarioLogeado") == null) {
            return "redirect:/login";
        }
        return "cambiar-contrasena";
    }

    @PostMapping("/cambiar-contrasena")
    public String cambiarContrasena(HttpSession session,
                                    @RequestParam("claveActual") String claveActual,
                                    @RequestParam("nuevaClave") String nuevaClave,
                                    @RequestParam("confirmarClave") String confirmarClave,
                                    Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogeado");

        if (usuario == null) {
            return "redirect:/login";
        }

        if (!nuevaClave.equals(confirmarClave)) {
            model.addAttribute("error", "❌ Las nuevas contraseñas no coinciden.");
            return "cambiar-contrasena";
        }

        Usuario usuarioDb = usuarioService.loginUser(usuario.getCorreo(), claveActual);

        if (usuarioDb == null) {
            model.addAttribute("error", "❌ La contraseña actual es incorrecta.");
            return "cambiar-contrasena";
        }

        usuarioService.cambiarClave(usuario.getIdUsuario(), nuevaClave);
        model.addAttribute("mensaje", "✅ Contraseña actualizada correctamente.");
        return "cambiar-contrasena";
    }

}
