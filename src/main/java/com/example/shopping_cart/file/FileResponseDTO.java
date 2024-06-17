package com.example.shopping_cart.file;

import java.math.BigInteger;

public record FileResponseDTO(
        Long id,
        String name,
        String fileType,
        BigInteger size,
        byte[] fileByte
) {
}
