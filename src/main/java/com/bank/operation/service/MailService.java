package com.bank.operation.service;

import com.bank.operation.config.AppProperties;
import com.bank.operation.dto.request.MailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender emailSender;
    private final AppProperties appProperties;

    public void sendMail(MailRequest mailRequest) {
        try {
            MimeMessage message = emailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(appProperties.getFrom());
            helper.setTo(mailRequest.getMail());
            helper.setSubject(mailRequest.getSubject());
            helper.setText(mailRequest.getData(), true);
            message.setFrom(new InternetAddress(appProperties.getFrom(), appProperties.getTitle()));
            emailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException messagingException) {
            // throw new MessagingExceptionHandler(ExceptionCodes.MESSAGING_EXCEPTION_HANDLER, messagingException.getMessage());
            messagingException.printStackTrace();
        }
    }
}
