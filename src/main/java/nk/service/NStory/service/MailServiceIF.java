package nk.service.NStory.service;

import nk.service.NStory.dto.MailDTO;
import org.springframework.messaging.MessagingException;

public interface MailServiceIF {
    void sendEmail(MailDTO mailDTO) throws MessagingException;
}
