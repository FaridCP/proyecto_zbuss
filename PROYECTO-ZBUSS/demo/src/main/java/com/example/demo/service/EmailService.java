package com.example.demo.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarComprobante(String destinatario, byte[] archivoPdf, String nombreArchivo) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            helper.setTo(destinatario);
            helper.setSubject("Tu comprobante electronico - ZBUSS 🚌");
            helper.setText("""
                <p>Hola,</p>
                <p>Adjunto encontrarás tu comprobante de compra en formato PDF.</p>
                <p>Gracias por confiar en <strong>ZBUSS</strong>.</p>
                <br>
                <p>Este correo es automático, no respondas a este mensaje.</p>
                """, true);

            InputStreamSource adjunto = new ByteArrayResource(archivoPdf);
            helper.addAttachment(nombreArchivo, adjunto);

            mailSender.send(mensaje);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo: " + e.getMessage(), e);
        }
    }
}
