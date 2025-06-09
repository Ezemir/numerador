package br.gov.ce.cgd.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmailWithQRCode(String to, String subject, String text, byte[] qrCodeImage) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);
        helper.addAttachment("QRCode.png", new ByteArrayDataSource(qrCodeImage, "image/png"));
        
        mailSender.send(mimeMessage);
    }
}
