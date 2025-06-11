package vn.hcmute.appfood;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import vn.hcmute.appfood.entity.Role;
import vn.hcmute.appfood.repository.RoleRepository;

@SpringBootApplication
@EnableAsync // Bật bất đồng bộ async
public class AppFoodApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppFoodApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(RoleRepository roleRepository) {
		return args -> {
			System.out.println("Checking for existing roles...");
			if(roleRepository.count() == 0) {
				roleRepository.save(new Role(null, "ADMIN", null));
				roleRepository.save(new Role(null, "USER",null));
			}
			else {
				System.out.println("Roles already exist.");
			}
		};
	}
}
