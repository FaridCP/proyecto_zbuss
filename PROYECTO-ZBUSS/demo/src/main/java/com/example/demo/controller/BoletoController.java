package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.service.BoletoComprobanteService;
import com.example.demo.service.DetalleBoletoComprobanteService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
@RequestMapping("/boletos")
public class BoletoController {

    @Autowired
    private BoletoComprobanteService boletoService;

    @Autowired
    private DetalleBoletoComprobanteService detalleService;

    @GetMapping("/pdf/{id}")
    @ResponseBody
    public void generarPdf(@PathVariable Long id, HttpServletResponse response) {
        try {
            BoletoComprobante boleto = boletoService.obtenerPorId(id);
            List<DetalleBoletoComprobante> detalles = detalleService.listarPorIdBoleto(id);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=boleta_" + id + ".pdf");

            OutputStream outputStream = response.getOutputStream();
            Document document = new Document(PageSize.A6);
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();

            Font normal = new Font(Font.FontFamily.COURIER, 9, Font.NORMAL);
            Font bold = new Font(Font.FontFamily.COURIER, 9, Font.BOLD);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            // Logo
            Image logo = Image.getInstance(new ClassPathResource("static/img/logo-comprobante.png").getURL());
            logo.scaleToFit(120, 50);
            logo.setAlignment(Image.ALIGN_CENTER);
            document.add(logo);

            // Datos empresa centrados
            Paragraph empresa = new Paragraph("ZBUSS S.A.C.", bold);
            empresa.setAlignment(Element.ALIGN_CENTER);
            document.add(empresa);

            Paragraph ruc = new Paragraph("RUC: 20480999991", normal);
            ruc.setAlignment(Element.ALIGN_CENTER);
            document.add(ruc);

            Paragraph direccion = new Paragraph("Av. Javier Prado 123 - Lima", normal);
            direccion.setAlignment(Element.ALIGN_CENTER);
            document.add(direccion);

            document.add(Chunk.NEWLINE);

            Paragraph tipo = new Paragraph("COMPROBANTE ELECTRÓNICO", bold);
            tipo.setAlignment(Element.ALIGN_CENTER);
            document.add(tipo);

            Paragraph serie = new Paragraph(boleto.getNumeroSerie(), bold);
            serie.setAlignment(Element.ALIGN_CENTER);
            document.add(serie);

            document.add(Chunk.NEWLINE);

            // Condicional para factura o boleta
            if (boleto.getTipoComprobante() == BoletoComprobante.TipoComprobante.FACTURA){
                document.add(new Paragraph("Datos de la factura:", bold));
                document.add(new Paragraph("Emisión: " + sdf.format(boleto.getFechaEmision()), normal));

                Usuario u = boleto.getUsuario();
                if (u.getTipoDocumento() == Usuario.TipoDocumento.RUC) {
                    document.add(new Paragraph("RUC: " + u.getDocumento(), normal));
                    document.add(new Paragraph("Razón Social: " + u.getRazonSocial(), normal));
                }
            } else {
                document.add(new Paragraph("Datos de la boleta:", bold));
                document.add(new Paragraph("A nombre de: " + boleto.getUsuario().getNombre(), normal));
                document.add(new Paragraph("DNI: " + boleto.getUsuario().getDocumento(), normal));
            }

            document.add(new Paragraph("Ruta: " + boleto.getRuta().getOrigen() + " - " + boleto.getRuta().getDestino(), normal));
            document.add(new Paragraph("Horario: " + sdf.format(boleto.getHorario().getFechaHoraSalida()), normal));
            document.add(new Paragraph("Método de pago: " + boleto.getMetodoPago().getNombre(), normal));
            document.add(new Paragraph("Total a pagar: S/. " + boleto.getPrecio(), normal));
            document.add(new Paragraph("Estado: " + boleto.getPagado(), normal));

            document.add(Chunk.NEWLINE);
            document.add(new Paragraph("PASAJEROS Y ASIENTOS", bold));

            for (DetalleBoletoComprobante d : detalles) {
                Pasajero p = d.getPasajero();
                String numAsiento = d.getAsiento().getNumero();
                document.add(new Paragraph("- " + p.getNombres() + " " + p.getApellidos()
                        + " | DNI: " + p.getDni() + " | Asiento: " + numAsiento, normal));
            }

            document.add(Chunk.NEWLINE);
            document.add(new Paragraph("CONDICIONES DEL VIAJE", bold));
            document.add(new Paragraph("1. No se responde por pérdida de equipaje.", normal));
            document.add(new Paragraph("2. Prohibido viajar en estado etílico.", normal));
            document.add(new Paragraph("3. Postergaciones con 3 horas de antelación.", normal));
            document.add(Chunk.NEWLINE);

            Paragraph gracias = new Paragraph("Gracias por viajar con ZBUSS 🚌", bold);
            gracias.setAlignment(Element.ALIGN_CENTER);
            document.add(gracias);

            document.add(Chunk.NEWLINE);

            BarcodeQRCode qrCode = new BarcodeQRCode("Boleta #" + boleto.getNumeroSerie(), 100, 100, null);
            Image qrImage = qrCode.getImage();
            qrImage.scaleAbsolute(80, 80);
            qrImage.setAlignment(Image.ALIGN_CENTER);
            document.add(qrImage);

            document.close();
            writer.close();
            outputStream.flush();
            outputStream.close();

        } catch (Exception e) {
            throw new RuntimeException("Error al generar el boleto PDF: " + e.getMessage(), e);
        }
    }

}

