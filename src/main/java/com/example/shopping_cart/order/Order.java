package com.example.shopping_cart.order;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @EmbeddedId
    private OrderID orderID;
    @Embedded
    private Address address;
    private String orderInfo;
    private String anotherField;
}
