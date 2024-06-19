package com.example.shopping_cart.product_quantity;

import com.example.shopping_cart.cart.ShoppingCart;
import com.example.shopping_cart.order.Order;
import com.example.shopping_cart.product.Product;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "quantities")
public class ProductQuantity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinTable(name = "quantities_products",
            joinColumns = @JoinColumn(name = "quantity_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Product product;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private ShoppingCart shoppingCart;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private Long quantity;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    public void calculateTotalAmount(Double pricePerUnit) {
        BigDecimal price = BigDecimal.valueOf(pricePerUnit);
        BigDecimal quantityAsBigDecimal = BigDecimal.valueOf(quantity);
        this.totalAmount = price.multiply(quantityAsBigDecimal);
    }
}
