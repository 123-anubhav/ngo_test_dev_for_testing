package in.ashokit.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Bean
	public AuthenticationManager authenticationManager(
			AuthenticationConfiguration configuration)
						throws Exception
	{
		return configuration.getAuthenticationManager();
	}
	
	//Authentication
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder);
		return provider;
	}
	
	//Authorization
	@Bean
	public SecurityFilterChain configAuth(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests(
				
				request -> request.antMatchers("/","/home"
						,"/about","transferedByDeadPerson","viewDonars","fillFormAfterPaymentPost","paymentFormPlanValidityExtend"
						,"processPaymentPlanValidityExtend",
						"/fillFormAfterPayment","otherDonationList","enquiry","loginSuccess","logout",//"logout?",
						"/register","allDoantionList","donation","faq"//,"fillFormAfterPaymentPost"
						).permitAll()
				.antMatchers("/register","/save","/enquiryPost","payVyasthaSulk").permitAll()
				//.antMatchers("/customer","/user","/donateNow","/searchById","/myProfile","/myDonationList").hasAnyAuthority("CUSTOMER","ADMIN")
				.antMatchers("/customer","/user","/donateNow","/searchById","/myProfile","/myDonationList","/vyasthaSulk").hasAnyRole("CUSTOMER","ADMIN")
				
				.antMatchers("/admin","/deleteRecord").hasAuthority("ADMIN")
				//.antMatchers("/user","/donateNow","/searchById").hasAnyRole("ADMIN","CUSTOMER")
				//.antMatchers("/deleteRecord").hasRole("ADMIN")
//				.anyRequest().authenticated()
				)
		.formLogin().and()
		//.formLogin( form -> form.loginPage("/login").permitAll() ) //-> custom login
		//.logout( logout -> logout.permitAll())
		.logout()
        .permitAll(); // Allow anyone to logout
		;
		return http.build();
	}
	
}