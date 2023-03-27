package footprint.eventPage.dto;

import lombok.Getter;

@Getter
public class AuthenticResponseDto {
    private boolean sent;

    public AuthenticResponseDto(boolean sent) {
        this.sent = sent;
    }
}
