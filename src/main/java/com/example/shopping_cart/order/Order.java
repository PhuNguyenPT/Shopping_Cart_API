package com.example.shopping_cart.order;

import com.example.shopping_cart.common.BaseEntity;
import com.example.shopping_cart.product.Product;
import com.example.shopping_cart.product_quantity.ProductQuantity;
import com.example.shopping_cart.transaction.Transaction;
import com.example.shopping_cart.user.MyUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "orders")
@Tag(name = "Order")
public class Order extends BaseEntity {
//    @EmbeddedId
//    private OrderId orderID;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal totalAmount;
    private String status;
    private LocalDateTime deliveryDate;
    private String orderInfo;
    private String anotherField;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "orders_users",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private MyUser user;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductQuantity> quantities = new ArrayList<>();

//    @CreatedDate
//    @Column(nullable = false, updatable = false)
//    private LocalDateTime orderDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "orders_products",
            joinColumns = @JoinColumn(name = "oder_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products = new ArrayList<>();

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Transaction transaction;
}

