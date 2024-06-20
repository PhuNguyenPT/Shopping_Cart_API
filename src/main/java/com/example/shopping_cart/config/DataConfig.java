package com.example.shopping_cart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class DataConfig {

    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer customizePageable() {
        return pageableResolver -> pageableResolver.setOneIndexedParameters(true);
    }
}
