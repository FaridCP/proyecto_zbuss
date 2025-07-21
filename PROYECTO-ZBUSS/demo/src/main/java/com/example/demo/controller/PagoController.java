package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.model.BoletoComprobante.TipoComprobante;
import com.example.demo.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/pago")
public class PagoController {

    @Autowired
    private MetodoPagoService metodoPagoService;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private BoletoComprobanteService boletoService;

    @Autowired
    private DetalleBoletoComprobanteService detalleBoletoService;

    @Autowired
    private PdfGeneratorService pdfGeneratorService;

    @Autowired
    private EmailService emailService;

    @GetMapping
    public String mostrarFormularioPago(HttpSession session, Model model) {
        Long tiempoInicio = (Long) session.getAttribute("tiempoInicio");
        if (tiempoInicio != null) {
            long tiempoActual = System.currentTimeMillis();
            long diferencia = tiempoActual - tiempoInicio;

            if (diferencia > 5 * 60 * 1000) {
                limpiarSesionCompra(session);
                session.setAttribute("timeoutCompra", true);
                return "redirect:/rutas";
            }

            long restanteSegundos = Math.max((5 * 60 * 1000 - diferencia) / 1000, 0);
            model.addAttribute("segundosRestantes", restanteSegundos);
        } else {
            model.addAttribute("segundosRestantes", 300);
        }

        @SuppressWarnings("unchecked")
        List<Pasajero> pasajeros = (List<Pasajero>) session.getAttribute("pasajerosSeleccionados");
        Ruta ruta = (Ruta) session.getAttribute("rutaSeleccionada");
        Horario horario = (Horario) session.getAttribute("horarioSeleccionado");
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogeado");

        if (pasajeros == null || pasajeros.isEmpty() || ruta == null || horario == null) {
            return "redirect:/rutas";
        }

        Double total = (Double) session.getAttribute("precioTotalCalculado");
        if (total == null) {
            total = ruta.getPrecio() * pasajeros.size();
        }

        List<MetodoPago> metodos = metodoPagoService.listarTodos();

        model.addAttribute("pasajeros", pasajeros);
        model.addAttribute("metodos", metodos);
        model.addAttribute("usuario", usuario);
        model.addAttribute("precioTotal", total);

        return "pago";
    }

    @PostMapping("/confirmar")
    public String procesarPago(HttpSession session,
                               @RequestParam("metodoPago") Long idMetodoPago,
                               @RequestParam("tipoComprobante") String tipoComprobante,
                               @RequestParam(value = "paypalTransactionId", required = false) String paypalTransactionId,
                               @RequestParam(value = "rucManual", required = false) String rucManual,
                               @RequestParam(value = "razonSocialManual", required = false) String razonSocialManual,
                               @RequestParam(value = "codigoOperacion", required = false) String codigoOperacion,
                               Model model) {

        Long tiempoInicio = (Long) session.getAttribute("tiempoInicio");
        if (tiempoInicio != null) {
            long tiempoActual = System.currentTimeMillis();
            if (tiempoActual - tiempoInicio > 5 * 60 * 1000) {
                limpiarSesionCompra(session);
                session.setAttribute("timeoutCompra", true);
                return "redirect:/rutas";
            }
        }

        @SuppressWarnings("unchecked")
        List<Pasajero> pasajeros = (List<Pasajero>) session.getAttribute("pasajerosSeleccionados");
        Ruta ruta = (Ruta) session.getAttribute("rutaSeleccionada");
        Horario horario = (Horario) session.getAttribute("horarioSeleccionado");
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogeado");

        if (pasajeros == null || pasajeros.isEmpty() || ruta == null || horario == null || usuario == null) {
            return "redirect:/rutas";
        }

        double total = 0.0;
        final double precioBase = ruta.getPrecio();

        for (Pasajero pasajero : pasajeros) {
            int edad = LocalDateTime.now().getYear() - pasajero.getFechaNacimiento().toLocalDate().getYear();
            if (LocalDateTime.now().toLocalDate().isBefore(pasajero.getFechaNacimiento().toLocalDate().withYear(LocalDateTime.now().getYear()))) {
                edad--;
            }
            double precioFinal = (edad < 18) ? precioBase * 0.5 : precioBase;
            total += precioFinal;
        }

        BoletoComprobante comprobante = new BoletoComprobante();
        comprobante.setUsuario(usuario);
        comprobante.setRuta(ruta);
        comprobante.setHorario(horario);
        comprobante.setPrecio(total);

        MetodoPago metodo = new MetodoPago();
        metodo.setIdMetodoPago(idMetodoPago);
        comprobante.setMetodoPago(metodo);

        String metodoNombre = metodoPagoService.buscarPorId(idMetodoPago).getNombre().toLowerCase();
        if (metodoNombre.contains("efectivo")) {
            comprobante.setPagado(BoletoComprobante.EstadoPago.PENDIENTE);
            comprobante.setAprobado(false);
        } else {
            comprobante.setPagado(BoletoComprobante.EstadoPago.PAGADO);
            comprobante.setAprobado(true);
        }

        comprobante.setFechaEmision(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        comprobante.setTipoComprobante(TipoComprobante.valueOf(tipoComprobante));
        comprobante.setNumeroSerie(boletoService.generarNumeroSerie(tipoComprobante));

        if ("RUC".equalsIgnoreCase(usuario.getTipoDocumento().name())) {
            Empresa empresa = new Empresa();
            empresa.setIdEmpresa(1L);
            comprobante.setEmpresa(empresa);
        } else if ("FACTURA".equalsIgnoreCase(tipoComprobante)) {
            Empresa empresa = new Empresa();
            empresa.setRuc(rucManual);
            empresa.setRazonSocial(razonSocialManual);
            empresaService.guardar(empresa);
            comprobante.setEmpresa(empresa);
        }

        if ((metodoNombre.contains("yape") || metodoNombre.contains("plin")) && codigoOperacion != null && !codigoOperacion.isBlank()) {
            comprobante.setCodigoOperacionYapePlin(codigoOperacion);
        }

        if (paypalTransactionId != null && !paypalTransactionId.isBlank()) {
            comprobante.setPaypalTransactionId(paypalTransactionId);
        }

        BoletoComprobante guardado = boletoService.guardar(comprobante);
        Long idUltimoBoleto = guardado.getIdBoleto();

        for (Pasajero pasajero : pasajeros) {
            pasajero.setRuta(ruta);
            pasajero.setHorario(horario);

            int edad = LocalDateTime.now().getYear() - pasajero.getFechaNacimiento().toLocalDate().getYear();
            if (LocalDateTime.now().toLocalDate().isBefore(pasajero.getFechaNacimiento().toLocalDate().withYear(LocalDateTime.now().getYear()))) {
                edad--;
            }

            double precioFinal = (edad < 18) ? precioBase * 0.5 : precioBase;

            DetalleBoletoComprobante detalle = new DetalleBoletoComprobante();
            detalle.setIdBoleto(idUltimoBoleto);
            detalle.setIdPasajero(pasajero.getIdPasajero());
            detalle.setIdAsiento(pasajero.getAsiento().getIdAsiento());
            detalle.setIdHorario(horario.getIdHorario());
            detalle.setIdRuta(ruta.getIdRuta());
            detalle.setPrecio(precioFinal);

            detalle.setPasajero(pasajero);
            detalle.setAsiento(pasajero.getAsiento());

            detalleBoletoService.guardar(detalle);
        }

        try {
            byte[] pdf = pdfGeneratorService.generarPdfComoBytes(idUltimoBoleto);
            emailService.enviarComprobante(usuario.getCorreo(), pdf, "comprobante_" + guardado.getNumeroSerie() + ".pdf");
        } catch (Exception e) {
            e.printStackTrace();
        }

        limpiarSesionCompra(session);
        model.addAttribute("idUltimoBoleto", idUltimoBoleto);
        return "compra-exitosa";
    }

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
    }
}
