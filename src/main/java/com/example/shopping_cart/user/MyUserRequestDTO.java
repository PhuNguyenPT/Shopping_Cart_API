package com.example.shopping_cart.user;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MyUserRequestDTO {
    @NotNull(message = "Page size must not be null")
    @Min(value = 1) @Max(value = 20)
    Integer pageSize;
    @NotNull(message = "Page index must not be null")
    @Min(value = 1)
    Integer pageNumber;
}
