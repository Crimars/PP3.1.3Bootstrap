package ru.kata.spring.boot_security.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.Set;

@SpringBootApplication
public class SpringBootSecurityDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootSecurityDemoApplication.class, args);
	}


	@Bean
	public CommandLineRunner initUsers(
			UserRepository userRepo,
			RoleRepository roleRepo,
			BCryptPasswordEncoder passwordEncoder
	) {
		return args -> {

			if (userRepo.findByUsername("admin") == null) {


				Role adminRole = new Role(1L, "ROLE_ADMIN");
				Role userRole = new Role(2L, "ROLE_USER");
				roleRepo.save(adminRole);
				roleRepo.save(userRole);


				User admin = new User();
				admin.setUsername("admin");
				admin.setPassword(passwordEncoder.encode("admin")); // Пароль "admin"
				admin.setRoles(Set.of(adminRole));
				admin.setFirstName("Ivan");
				admin.setLastName("Ivanov");
				admin.setAge(35);
				admin.setEmail("admin@mail.ru");
				admin.setRoles(Set.of(adminRole, userRole));
				userRepo.save(admin);


				User user = new User();
				user.setUsername("user");
				user.setPassword(passwordEncoder.encode("user")); // Пароль "user"
				user.setFirstName("Petr");
				user.setLastName("Petrov");
				user.setAge(30);
				user.setEmail("user@mail.ru");
				user.setRoles(Set.of(userRole));
				userRepo.save(user);

				System.out.println("Созданы тестовые пользователи: admin/admin, user/user");
			}
		};
	}
}