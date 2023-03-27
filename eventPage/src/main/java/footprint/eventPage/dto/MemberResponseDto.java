package footprint.eventPage.dto;

import lombok.Getter;

@Getter
public class MemberResponseDto {
    private boolean joined;
    private Long sequence;

    public MemberResponseDto(boolean joined, Long sequence) {
        this.joined = joined;
        this.sequence = sequence;
    }
}
