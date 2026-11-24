package com.mangaflow.auth.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String remitente;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void enviarAlertaLogin(String destinatario, String rol) {
        String fechaHora = LocalDateTime.now().format(FORMATO_FECHA);
        String cuerpoHtml = construirPlantillaHtml(
                "Nuevo inicio de sesión detectado",
                "¡Hola! Detectamos un acceso reciente a tu cuenta en <strong>MangaFlow</strong>.",
                destinatario,
                rol,
                fechaHora,
                "Si fuiste tú, puedes ignorar este mensaje. Si no reconoces este acceso, te recomendamos cambiar tu contraseña."
        );
        enviarHtml(destinatario, "Nuevo inicio de sesión en MangaFlow", cuerpoHtml);
    }

    @Async
    public void enviarCorreoRegistro(String destinatario, String rol) {
        String fechaHora = LocalDateTime.now().format(FORMATO_FECHA);
        String cuerpoHtml = construirPlantillaHtml(
                "¡Bienvenido a MangaFlow!",
                "Tu cuenta ha sido creada exitosamente. Ya puedes explorar el catálogo, acumular puntos y guardar tus mangas.",
                destinatario,
                rol,
                fechaHora,
                "Gracias por unirte a nuestra comunidad de lectores."
        );
        enviarHtml(destinatario, "Bienvenido a MangaFlow", cuerpoHtml);
    }

    private void enviarHtml(String destinatario, String asunto, String html) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            helper.setFrom(remitente, "MangaFlow - Seguridad");
            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(html, true);

            ClassPathResource logo = new ClassPathResource("static/logoMangaFlow.png");
            if (logo.exists()) {
                helper.addInline("logoMangaFlow", logo);
            }

            mailSender.send(mensaje);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.warn("No se pudo enviar el correo HTML a {}: {}", destinatario, e.getMessage());
        }
    }

    private String construirPlantillaHtml(
            String titulo,
            String introduccion,
            String correo,
            String rol,
            String fechaHora,
            String notaPie
    ) {
        ClassPathResource logo = new ClassPathResource("static/logoMangaFlow.png");
        String bloqueLogo = logo.exists()
                ? """
                  <div style="display:inline-block;padding:4px;border-radius:50%%;background:linear-gradient(135deg,#a100ff,#e100ff);margin-bottom:12px;">
                    <img src="cid:logoMangaFlow" alt="MangaFlow" width="58" height="58" style="display:block;border-radius:50%%;object-fit:cover;" />
                  </div><br/>
                  """
                : "";

        return """
                <!DOCTYPE html>
                <html lang="es">
                <head>
                  <meta charset="UTF-8">
                  <meta name="viewport" content="width=device-width, initial-scale=1.0">
                </head>
                <body style="margin:0;padding:0;background-color:#0b0b12;font-family:system-ui,-apple-system,'Segoe UI',Roboto,Helvetica,Arial,sans-serif;color:#f8f9fa;">
                  <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" style="background-color:#0b0b12;padding:32px 16px;">
                    <tr>
                      <td align="center">
                        <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" style="max-width:540px;background-color:#12131f;border:1px solid #3b1754;border-radius:16px;overflow:hidden;">
                          <tr>
                            <td style="height:5px;background:linear-gradient(135deg,#a100ff,#e100ff);"></td>
                          </tr>
                          <tr>
                            <td align="center" style="padding:28px 28px 18px 28px;border-bottom:1px solid rgba(209,120,255,0.14);">
                              %s
                              <span style="font-size:24px;font-weight:800;letter-spacing:-0.01em;color:#ffffff;">
                                Manga<span style="color:#d178ff;">Flow</span>
                              </span>
                            </td>
                          </tr>
                          <tr>
                            <td style="padding:24px 32px;">
                              <h1 style="margin:0 0 12px 0;font-size:20px;font-weight:700;color:#f8f9fa;text-align:center;">
                                %s
                              </h1>
                              <p style="margin:0 0 22px 0;font-size:14px;line-height:1.65;color:#c3c1ce;text-align:center;">
                                %s
                              </p>
                              <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" style="background-color:#231238;border:1px solid #3b1754;border-radius:12px;padding:12px 16px;">
                                <tr>
                                  <td style="padding:7px 10px;font-size:13px;color:#9695a3;">Cuenta:</td>
                                  <td style="padding:7px 10px;font-size:13px;color:#f8f9fa;font-weight:600;text-align:right;">%s</td>
                                </tr>
                                <tr>
                                  <td style="padding:7px 10px;font-size:13px;color:#9695a3;">Rol asignado:</td>
                                  <td style="padding:7px 10px;text-align:right;">
                                    <span style="display:inline-block;padding:3px 11px;font-size:11px;font-weight:700;color:#ffffff;background-color:#c800ff;border-radius:999px;">
                                      %s
                                    </span>
                                  </td>
                                </tr>
                                <tr>
                                  <td style="padding:7px 10px;font-size:13px;color:#9695a3;">Fecha y hora:</td>
                                  <td style="padding:7px 10px;font-size:13px;color:#d178ff;font-weight:600;text-align:right;">%s</td>
                                </tr>
                              </table>
                              <p style="margin:20px 0 0 0;font-size:12px;line-height:1.55;color:#9695a3;text-align:center;">
                                %s
                              </p>
                            </td>
                          </tr>
                          <tr>
                            <td style="background-color:#0d0e18;border-top:1px solid #3b1754;padding:18px 24px;text-align:center;">
                              <p style="margin:0 0 4px 0;font-size:12px;font-weight:600;color:#c3c1ce;">
                                © 2026 MangaFlow. Todos los derechos reservados.
                              </p>
                              <p style="margin:0;font-size:11px;color:#9695a3;">
                                Este es un correo automático de seguridad, por favor no respondas a este mensaje.
                              </p>
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </table>
                </body>
                </html>
                """.formatted(bloqueLogo, titulo, introduccion, correo, rol, fechaHora, notaPie);
    }
}