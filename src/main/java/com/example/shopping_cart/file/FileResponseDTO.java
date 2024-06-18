package com.example.shopping_cart.file;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@Builder
public class FileResponseDTO {
    private String message;
    private final Long id;
    private final String name;
    private final String fileType;
    private final BigInteger size;
    private final byte[] fileByte;
}
