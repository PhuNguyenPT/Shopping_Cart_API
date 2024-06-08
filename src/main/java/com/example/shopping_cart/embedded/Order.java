package com.example.shopping_cart.embedded;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_order")
public class Order {
    @EmbeddedId
    private OrderID orderID;
    @Embedded
    private Address address;
    private String orderInfo;
    private String anotherField;
}
