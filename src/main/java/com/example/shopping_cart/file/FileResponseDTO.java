package com.example.shopping_cart.file;

import java.math.BigInteger;

public record FileResponseDTO(
        String name,
        String fileType,
        BigInteger size,
        byte[] fileByte
) {
}
