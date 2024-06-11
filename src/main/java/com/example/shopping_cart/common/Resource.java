package com.example.shopping_cart.common;

import com.example.shopping_cart.models.Lecture;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigInteger;

@Getter
@Setter
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public BigInteger getSize() {
        return size;
    }

    public void setSize(BigInteger size) {
        this.size = size;
    }
}
