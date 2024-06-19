package com.example.shopping_cart.common;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigInteger;

@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
//@Inheritance(
//        strategy = InheritanceType.TABLE_PER_CLASS
//)
//@DiscriminatorColumn(
//        name = "resource_type"
//) -> only with single table strategy
public class Resource {
    @Column(unique = true)
    private String name;
    private String url;
    private BigInteger size;

}
