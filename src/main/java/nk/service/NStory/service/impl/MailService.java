package nk.service.NStory.service.impl;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import nk.service.NStory.dto.MailDTO;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service @Slf4j
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;

    @Async
    @SneakyThrows
    public void sendEmail(MailDTO mailDTO) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setTo(mailDTO.getTo());
        mimeMessageHelper.setSubject(mailDTO.getSubject());
        mimeMessageHelper.setText(mailDTO.getBody(), true);
        javaMailSender.send(mimeMessage);
    }
}
