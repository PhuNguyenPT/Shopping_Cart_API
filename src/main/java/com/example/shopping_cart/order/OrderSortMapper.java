package com.example.shopping_cart.order;

import com.example.shopping_cart.transaction.TransactionSort;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class OrderSortMapper {
    public static OrderSort toOrderSortDefaultCreatedDate(
            @NotNull String sortAttribute
    ) {
        if (sortAttribute.equalsIgnoreCase("created-date")) {
            return OrderSort.CREATED_DATE;
        } else if (sortAttribute.equalsIgnoreCase("last-modified-date")) {
            return OrderSort.LAST_MODIFIED_DATE;
        } else if (sortAttribute.equalsIgnoreCase("amount")) {
            return OrderSort.AMOUNT;
        } else if (sortAttribute.equalsIgnoreCase("status")) {
            return OrderSort.STATUS;
        } else {
            return OrderSort.CREATED_DATE;
        }
    }
}
