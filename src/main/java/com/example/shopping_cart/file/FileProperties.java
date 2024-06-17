package com.example.shopping_cart.file;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class FileProperties {
    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;
}
