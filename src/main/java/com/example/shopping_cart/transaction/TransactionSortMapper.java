package com.example.shopping_cart.transaction;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class TransactionSortMapper {

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
}
