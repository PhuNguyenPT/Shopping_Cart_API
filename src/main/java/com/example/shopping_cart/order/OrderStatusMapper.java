package com.example.shopping_cart.order;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
public class OrderStatusMapper {
    public static Status toOrderStatus(
            @NotNull String statusAttribute
    ) {
        if (statusAttribute.equalsIgnoreCase(Status.PROCESSING.getValue())) {
            return Status.PROCESSING;
        } else if (statusAttribute.equalsIgnoreCase(Status.PAID.getValue())) {
            return Status.PAID;
        } else if (statusAttribute.equalsIgnoreCase(Status.DELIVERING.getValue())) {
            return Status.DELIVERING;
        } else if (statusAttribute.equalsIgnoreCase(Status.COMPLETE.getValue())) {
            return Status.COMPLETE;
        } else {
            return Status.NONE;
        }
    }
}
