package com.example.shopping_cart.product;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class ProductSortMapper {

    public static ProductSort toProductionSortDefaultCreatedDate(
            @NotNull String sortAttribute
    ) {
        if (sortAttribute.equalsIgnoreCase("created-date")) {
            return ProductSort.CREATED_DATE;
        } else if (sortAttribute.equalsIgnoreCase("last-modified-date")) {
            return ProductSort.LAST_MODIFIED_DATE;
        } else if (sortAttribute.equalsIgnoreCase("price")) {
            return ProductSort.PRICE;
        } else if (sortAttribute.equalsIgnoreCase("stock-quantity")) {
            return ProductSort.STOCK_QUANTITY;
        }
        else {
            return ProductSort.CREATED_DATE;
        }
    }
}
