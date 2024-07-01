package com.example.shopping_cart;

import com.example.shopping_cart.role.MyRole;
import com.example.shopping_cart.role.MyRoleMapper;
import com.example.shopping_cart.role.MyRoleService;
import com.example.shopping_cart.user.MyUser;
import com.example.shopping_cart.user.MyUserMapper;
import com.example.shopping_cart.user.MyUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
public class ShoppingCartApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingCartApplication.class, args);
	}

}


