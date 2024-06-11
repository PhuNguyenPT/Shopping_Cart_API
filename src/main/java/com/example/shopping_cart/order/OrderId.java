package com.example.shopping_cart.order;

import com.example.shopping_cart.user.MyUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class OrderId
        implements Serializable
{
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private MyUser user;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime orderDate;
}
