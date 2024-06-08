package com.example.shopping_cart.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Embeddable
public class BaseEntity {
    private ZonedDateTime createAt;
    private ZonedDateTime lastModifiedAt;
    private String createdBy;
    private String lastModifyBy;
}
