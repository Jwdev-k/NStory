package nk.service.NStory.service.impl;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import nk.service.NStory.dto.MailDTO;
import nk.service.NStory.service.MailServiceIF;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService implements MailServiceIF {
    private final JavaMailSender javaMailSender;

    @SneakyThrows
    @Override
    public void sendEmail(MailDTO mailDTO) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setTo(mailDTO.getTo());
        mimeMessageHelper.setSubject(mailDTO.getSubject());
        mimeMessageHelper.setText(mailDTO.getBody(), true);
        javaMailSender.send(mimeMessage);
    }
}
