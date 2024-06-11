package com.example.shopping_cart.models;

import com.example.shopping_cart.common.Resource;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

//@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "texts")
//@PrimaryKeyJoinColumn(name = "text_id") -> only with join table strategy
//@DiscriminatorValue("T") -> only with single table strategy
public class Text extends Resource {
    @Id
    @GeneratedValue
    private Long id;
    private String content;
}
