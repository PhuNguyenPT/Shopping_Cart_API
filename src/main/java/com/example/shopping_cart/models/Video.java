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
//@PrimaryKeyJoinColumn(name = "video_id") -> only with join table strategy
//@DiscriminatorValue("V") -> only with single table strategy
public class Video extends Resource {
    private int length;
}
