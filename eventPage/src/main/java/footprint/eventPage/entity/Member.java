package footprint.eventPage.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Member extends BaseEntity {
    @Id @GeneratedValue
    private Long id;
    private String email;
    private boolean agreed;

    protected Member() {
        super();
    }

    public Member(final String email, boolean agreed) {
        super();

        this.email = email;
        this.agreed = agreed;
    }
}
