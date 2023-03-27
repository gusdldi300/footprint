package footprint.eventPage.entity;

import jakarta.persistence.MappedSuperclass;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseEntity {
    private String createdBy;
    private LocalDateTime createdDate;
    //private String lastModifiedBy;
    //private LocalDateTime lastModifiedData;

    protected BaseEntity() {
        this.createdBy = "Admin";
        this.createdDate = LocalDateTime.now();

        //this.lastModifiedBy = "Admin";
        //this.lastModifiedData = createdDate;
    }
}
