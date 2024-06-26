package com.example.shopping_cart.transaction;

import com.example.shopping_cart.order.Order;
import com.example.shopping_cart.user.MyUser;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

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
                .createdBy(myUser.getFullName())
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
                .createdDate(
                        ZonedDateTime.of(
                                transaction.getCreatedDate(),
                                transaction.getCreatedTimeZone()
                        )
                )
                .lastModifiedDate(
                        Optional.ofNullable(transaction.getLastModifiedDate())
                                .map(lastModifiedDate -> {
                                            if (transaction.getModifiedTimeZone() != null) {
                                                return ZonedDateTime.of(
                                                        lastModifiedDate,
                                                        ZoneId.of(String.valueOf(transaction.getModifiedTimeZone()))
                                                );
                                            }
                                            return null;
                                        }
                                )
                                .orElse(null)
                )
                .createdBy(transaction.getCreatedBy())
                .lastModifiedBy(transaction.getLastModifyBy())
                .build();
    }

    public static TransactionSort toTransactionSortDefaultCreatedDate(
            @NotNull String sortAttribute
    ) {
        if (sortAttribute.equalsIgnoreCase("created-date")) {
            return TransactionSort.CREATED_DATE;
        } else if (sortAttribute.equalsIgnoreCase("last-modified-date")) {
            return TransactionSort.LAST_MODIFIED_DATE;
        } else if (sortAttribute.equalsIgnoreCase("amount")) {
            return TransactionSort.AMOUNT;
        } else {
            return TransactionSort.CREATED_DATE;
        }
    }

    public static Sort.Direction toSortDirectionDefaultDesc(
            @NotNull String direction
    ) {
        if (direction.equalsIgnoreCase("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equalsIgnoreCase("desc")) {
            return Sort.Direction.DESC;
        } else {
            return Sort.Direction.DESC;
        }
    }
}
