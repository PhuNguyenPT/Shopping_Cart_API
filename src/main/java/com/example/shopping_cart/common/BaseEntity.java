package com.example.shopping_cart.common;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@Embeddable
public class BaseEntity {
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;
    @Column(nullable = false, updatable = false)
    private ZoneId createdTimeZone;
    @Column(insertable = false)
    private ZoneId modifiedTimeZone;
    private String createdBy;
    private String lastModifyBy;

    @PrePersist
    public void prePersist() {
        createdTimeZone = ZoneId.systemDefault();
    }
    @PreUpdate
    public void preUpdate() {
        modifiedTimeZone = ZoneId.systemDefault();
    }
}
