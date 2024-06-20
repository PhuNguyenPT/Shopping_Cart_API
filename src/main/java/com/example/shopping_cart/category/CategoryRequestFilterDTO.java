package com.example.shopping_cart.category;

import jakarta.validation.Valid;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Getter
@Setter
@Builder
public class CategoryRequestFilterDTO {
    @NotNull("Page size must not be null") int pageSize;
    @NotNull("Page index must not be null") int pageNumber;
    @Valid
    private List<CategoryRequestDTO> categoryRequestDTOList;
}
