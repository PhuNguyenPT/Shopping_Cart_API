package com.example.shopping_cart.category;

import com.example.shopping_cart.common.BaseEntity;
import com.example.shopping_cart.product.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToMany(mappedBy = "categories")
    @JsonIgnore
    private List<Product> products = new ArrayList<>();

    public boolean addProduct(Product product) {
        if (products == null) {
            products = new ArrayList<>();
        }
        return products.add(product);
    }
}
