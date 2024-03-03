package in.ashokit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import in.ashokit.dto.FileEntityDTO;
import in.ashokit.dto.PaymentStatus;
import in.ashokit.dto.UserDTO;
import in.ashokit.service.FileService;

@Configuration
public class AppConfig {

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDTO userDTO() {
		return new UserDTO();
	}
	
	@Bean
	public FileEntityDTO fileEntiDTO() {
		return new FileEntityDTO();
	}

//	@Bean
//	public FileService fileService() {
//		return new FileService();
//	}

	@Bean
	public PaymentStatus paymentStatus() {
		return new PaymentStatus();
	}

}