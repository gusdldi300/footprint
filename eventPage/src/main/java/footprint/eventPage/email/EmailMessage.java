package footprint.eventPage.email;

import lombok.Getter;

@Getter
public class EmailMessage {
    private String to;
    private String subject; // 제목
    private String message;

    public EmailMessage(String to, String subject, String message) {
        this.to = to;
        this.subject = subject;
        this.message = message;
    }
}
