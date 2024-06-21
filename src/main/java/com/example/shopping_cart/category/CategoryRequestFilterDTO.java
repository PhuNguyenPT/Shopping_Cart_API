package com.example.shopping_cart.category;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CategoryRequestFilterDTO {
    @NotNull(message = "Page size must not be null")
    @Min(value = 1) @Max(value = 20)
    Integer pageSize;
    @NotNull(message = "Page index must not be null")
    @Min(value = 1)
    Integer pageNumber;
    @Valid
    private List<CategoryRequestDTO> categoryRequestDTOList;
}
