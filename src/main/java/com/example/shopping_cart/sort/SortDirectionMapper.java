package com.example.shopping_cart.sort;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class SortDirectionMapper {
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
