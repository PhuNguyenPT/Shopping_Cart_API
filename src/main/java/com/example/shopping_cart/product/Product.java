package com.example.shopping_cart.product;

import com.example.shopping_cart.cart.ShoppingCart;
import com.example.shopping_cart.common.BaseEntity;
import com.example.shopping_cart.file.File;
import com.example.shopping_cart.order.Order;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
@Tag(name = "Product")
@EntityListeners(AuditingEntityListener.class)
public class Product extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Double price;
    private Long stockQuantity;
    private String description;

    @ManyToMany(mappedBy = "products")
    @JsonIgnore
    private List<Order> orders;

    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL
    )
    @JsonIgnore
    private List<File> files;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "products_categories",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;

    @ManyToMany(mappedBy = "products")
    @JsonIgnore
    private List<ShoppingCart> shoppingCarts;


    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<ProductQuantity> quantities;
}
