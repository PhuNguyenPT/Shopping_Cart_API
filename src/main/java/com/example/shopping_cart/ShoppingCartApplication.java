package com.example.shopping_cart;

import com.example.shopping_cart.role.MyRole;
import com.example.shopping_cart.role.MyRoleRepository;
import com.example.shopping_cart.user.MyUser;
import com.example.shopping_cart.user.MyUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
public class ShoppingCartApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingCartApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(
			MyRoleRepository myRoleRepository,
			MyUserRepository myUserRepository,
			PasswordEncoder passwordEncoder
	) {
		return args -> {
			if (myRoleRepository.findByAuthority(MyRole.Value.USER.name()).isEmpty()) {
				myRoleRepository.save(
						MyRole.builder()
								.authority(
										MyRole.Value.USER.name()
								)
								.build()
				);
			}
			if (myRoleRepository.findByAuthority(MyRole.Value.ADMIN.name()).isEmpty()) {
				myRoleRepository.save(
						MyRole.builder()
								.authority(
										MyRole.Value.ADMIN.name()
								)
								.build()
				);
			}
			MyRole roleAdmin = myRoleRepository.findByAuthority(MyRole.Value.ADMIN.name()).orElseThrow(()-> new EntityNotFoundException("Role ADMIN not found"));
			MyRole roleUser = myRoleRepository.findByAuthority(MyRole.Value.USER.name()).orElseThrow(()-> new EntityNotFoundException("Role USER not found"));
			MyUser admin = MyUser.builder()
					.email("admin@email.com")
					.firstName("first")
					.lastName("last")
					.password(passwordEncoder.encode("password"))
					.isEnabled(true)
					.isAccountNonExpired(true)
					.isAccountNonLocked(true)
					.isCredentialsNonExpired(true)
					.roles(Arrays.asList(roleAdmin, roleUser))
					.createdBy("first" + " " + "last")
					.build();
			if (myUserRepository.findByEmail(admin.getEmail()).isEmpty()) {
				myUserRepository.save(admin);
			} else {
				admin = myUserRepository.findByEmail(admin.getEmail()).orElseThrow(()-> new EntityNotFoundException("Admin not found"));
				if (admin != null) {
					admin.setEmail("admin@email.com");
					admin.setFirstName("first");
					admin.setLastName("last");
					admin.setPassword(passwordEncoder.encode("password"));
					admin.setAccountNonExpired(true);
					admin.setAccountNonLocked(true);
					admin.setCredentialsNonExpired(true);
					admin.setEnabled(true);
					admin.setRoles(Arrays.asList(roleAdmin, roleUser));
					admin.setCreatedBy(admin.getFullName());
					admin.setLastModifyBy(admin.getFullName());
					myUserRepository.save(admin);
				}
			}
		};
	}
}


