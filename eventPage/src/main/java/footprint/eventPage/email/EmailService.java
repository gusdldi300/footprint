package footprint.eventPage.email;

import footprint.eventPage.email.EmailMessage;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    public void sendMail(EmailMessage emailMessage) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo());
            mimeMessageHelper.setSubject(emailMessage.getSubject());
            mimeMessageHelper.setText(emailMessage.getMessage(), true); // 메시지 본문, HTML 여부

            javaMailSender.send(mimeMessage);
            log.info("Mail send Success");
        } catch (MessagingException e) {
            log.info("Mail send fail");

            throw new RuntimeException(e);
        }
    }

    public void sendTemplateMail(EmailMessage emailMessage) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo());
            mimeMessageHelper.setSubject(emailMessage.getSubject());

            Context context = new Context();
            context.setVariable("authenticCode", emailMessage.getMessage());
            String templateMessage = this.templateEngine.process("email/emailForm", context);

            mimeMessageHelper.setText(templateMessage, true); // 메시지 본문, HTML 여부
            mimeMessageHelper.addInline("logo", new ClassPathResource("static/images/logo.png"));
            mimeMessageHelper.addInline("facebook-dark-gray", new ClassPathResource("static/images/facebook-dark-gray.png"));
            mimeMessageHelper.addInline("instagram-dark-gray", new ClassPathResource("static/images/instagram-dark-gray.png"));

            javaMailSender.send(mimeMessage);
            log.info("Mail send Success");
        } catch (MessagingException e) {
            log.info("Mail send fail");

            throw new RuntimeException(e);
        }
    }
}
