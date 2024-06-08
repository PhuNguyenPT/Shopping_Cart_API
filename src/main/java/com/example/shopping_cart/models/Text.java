package com.example.shopping_cart.models;

import com.example.shopping_cart.file.Resource;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@Entity
//@PrimaryKeyJoinColumn(name = "text_id") -> only with join table strategy
//@DiscriminatorValue("T") -> only with single table strategy
public class Text extends Resource {
    private String content;
}
