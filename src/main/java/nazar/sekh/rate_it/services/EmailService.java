package nazar.sekh.rate_it.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;

@PropertySource("classpath:application.properties")
@Service
public class EmailService {
    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    Environment env;

    public void sendRegMail(String username, String email, String token/*, MultipartFile file*/) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true); // true - для мультипарт об'єктів
        try {
            mimeMessage.setFrom(new InternetAddress(env.getProperty("spring.mail.username"))); // від кого надіслано
            helper.setTo(email); // до кого відправити
            helper.setText("<h1>" + username + ", please, confirm your registration</h1> \n" +
                    "http://localhost:8080/confirm-" + token, true); // текст листа
//                FileSystemResource pathToFile = new FileSystemResource(new File());
//            helper.addAttachment(file.getOriginalFilename(), new File(System.getProperty("user.home") + File.separator + "images" + File.separator + file.getOriginalFilename()));
            helper.setSubject("Registration"); // тема листа
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        javaMailSender.send(mimeMessage);
    }

    public void sendForgPassMail(String username, String email, String token/*, MultipartFile file*/) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true); // true - для мультипарт об'єктів
        try {
            mimeMessage.setFrom(new InternetAddress(env.getProperty("spring.mail.username"))); // від кого надіслано
            helper.setTo(email); // до кого відправити
            helper.setText("<h1>" + username + ", please, reset your password</h1> \n" +
                    "http://localhost:8080/password-reset-" + token, true); // текст листа
            helper.setSubject("Password reset"); // тема листа
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        javaMailSender.send(mimeMessage);
    }

    public void answerTicket(String username, String email, String subject, String text, String answer/*, MultipartFile file*/) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true); // true - для мультипарт об'єктів
        try {
            mimeMessage.setFrom(new InternetAddress(env.getProperty("spring.mail.username"))); // від кого надіслано
            helper.setTo(email); // до кого відправити
            helper.setText("<h1>Hello, " + username + ", here is an answer to your question:</h1> \n" +
                    "<h3>Q:</h3> " + text + " \n" +
                    "<h3>A:</h3> " +  answer, true); // текст листа
            helper.setSubject(subject); // тема листа
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        javaMailSender.send(mimeMessage);
    }
}
