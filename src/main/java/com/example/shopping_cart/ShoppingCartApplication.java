package com.example.shopping_cart;

import com.example.shopping_cart.role.MyRole;
import com.example.shopping_cart.role.MyRoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
public class ShoppingCartApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingCartApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(
			MyRoleRepository roleRepository
	) {
		return args -> {
			if (roleRepository.findByAuthority(MyRole.Value.USER.name()).isEmpty()) {
				roleRepository.save(
						MyRole.builder()
								.authority(
										MyRole.Value.USER.name()
								)
								.build()
				);
			}
			if (roleRepository.findByAuthority(MyRole.Value.ADMIN.name()).isEmpty()) {
				roleRepository.save(
						MyRole.builder()
								.authority(
										MyRole.Value.ADMIN.name()
								)
								.build()
				);
			}
		};
	}
}


