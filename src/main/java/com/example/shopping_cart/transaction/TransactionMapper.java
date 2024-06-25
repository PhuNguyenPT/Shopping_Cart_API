package com.example.shopping_cart.transaction;

import com.example.shopping_cart.order.Order;
import com.example.shopping_cart.user.MyUser;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class TransactionMapper {

    public static Transaction toTransactionSave(
            @NotNull TransactionRequestDTO transactionRequestDTO,
            MyUser myUser,
            @NotNull Order order
    ) {
        return Transaction.builder()
                .amount(order.getTotalAmount())
                .currency(transactionRequestDTO.getCurrency())
                .type(transactionRequestDTO.getTransactionType())
                .order(order)
                .user(myUser)
                .build();
    }

    public static TransactionResponseDTO toTransactionResponseDTO(
            @NotNull Transaction transaction
    ) {
        return TransactionResponseDTO.builder()
                .orderId(transaction.getOrder().getId())
                .transactionId(transaction.getId())
                .amount(transaction.getAmount())
                .transactionType(transaction.getType())
                .currency(transaction.getCurrency())
                .build();
    }
}
