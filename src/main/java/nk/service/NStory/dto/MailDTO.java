package nk.service.NStory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class MailDTO {
    private String to;
    private String subject;
    private String body;
}
