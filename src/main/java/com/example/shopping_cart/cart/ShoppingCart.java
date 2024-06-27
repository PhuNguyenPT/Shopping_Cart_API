package com.example.shopping_cart.cart;

import com.example.shopping_cart.common.BaseEntity;
import com.example.shopping_cart.product.Product;
import com.example.shopping_cart.product_quantity.ProductQuantity;
import com.example.shopping_cart.user.MyUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "carts")
public class ShoppingCart extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @OneToOne(mappedBy = "shoppingCart")
    private MyUser user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "carts_products",
            joinColumns = @JoinColumn(name = "cart_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "shoppingCart", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductQuantity> quantities = new ArrayList<>();

    public void addQuantity(ProductQuantity productQuantity) {
        if (quantities == null) {
            quantities = new ArrayList<>();
        }
        quantities.add(productQuantity);
    }
}
