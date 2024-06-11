package com.example.shopping_cart.product;

import com.example.shopping_cart.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "quantities")
public class ProductQuantity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(
            name = "products_quantities",
            joinColumns = @JoinColumn(name = "quantity_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Product product;

    private Long quantity;
}
