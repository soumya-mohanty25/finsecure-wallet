package com.finsecure.wallet.utils;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.mail.util.ByteArrayDataSource;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.mail.util.ByteArrayDataSource;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Logger;

@Slf4j
@Component("frameworkMailServiceUtil")
public class MailServiceUtil {

    @Autowired
    private ServletContext servletContext;

    public boolean sendMail(String body, String subject, String mailTo, String mailFrom, String bodyType,byte[] pdfBytes,String fileName) {

        ResourceBundle rb = ResourceBundle.getBundle("application");
        boolean outcome = false;

        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", rb.getString("mail.smtp.auth"));
            props.put("mail.smtp.starttls.enable", rb.getString("mail.smtp.starttls.enable"));
            props.put("mail.smtp.host", rb.getString("mail.smtp.host"));
            props.put("mail.smtp.port", rb.getString("mail.smtp.port"));

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(
                            rb.getString("mail.username"),
                            rb.getString("mail.password")
                    );
                }
            });

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(rb.getString("mail.username"), "Governor Secretariat", "UTF-8"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailTo));
            message.setSubject(subject, "UTF-8");

            // ========== MULTIPART ==========
            MimeMultipart multipart = new MimeMultipart("related");

            // ---------- HTML PART ----------
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(body, "text/html; charset=UTF-8");
            multipart.addBodyPart(htmlPart);

            // ---------- IMAGE PART ----------
            MimeBodyPart imagePart = new MimeBodyPart();

            InputStream imageStream = servletContext.getResourceAsStream("/assets/img/india-logo.png");

            if (imageStream == null) {
                log.error("Image NOT FOUND at /assets/img/Emblem_of_India.png");
                throw new RuntimeException("Image NOT FOUND at /assets/img/Emblem_of_India.png");
            }

            byte[] imageBytes = imageStream.readAllBytes();
            DataSource ds = new ByteArrayDataSource(imageBytes, "image/png");

            imagePart.setDataHandler(new DataHandler(ds));
            imagePart.setHeader("Content-ID", "<govLogo>");
            imagePart.setDisposition(MimeBodyPart.INLINE);


            multipart.addBodyPart(imagePart);

            if (pdfBytes != null) {

                MimeBodyPart attachmentPart = new MimeBodyPart();

                DataSource pdfDs =
                        new ByteArrayDataSource(pdfBytes, "application/pdf");

                attachmentPart.setDataHandler(new DataHandler(pdfDs));
                attachmentPart.setFileName(
                        fileName.endsWith(".pdf")
                                ? fileName.substring(0, fileName.length() - 4)
                                : fileName
                );
                attachmentPart.setDisposition(MimeBodyPart.ATTACHMENT);

                multipart.addBodyPart(attachmentPart);
            }
            message.setContent(multipart);

            Transport.send(message);

            log.info("Email sent successfully to: " + mailTo);
            outcome = true;

        } catch (Exception e) {
            log.error("ERROR while sending email", e);
        }

        return outcome;
    }
}

