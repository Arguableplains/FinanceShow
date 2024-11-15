package com.FS.FinanceShow_demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.FS.FinanceShow_demo.services.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	private final CustomUserDetailsService customUserDetailsService;

	public WebSecurityConfig(CustomUserDetailsService customUserDetailsService) {
		this.customUserDetailsService = customUserDetailsService;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests((requests) -> requests
				.requestMatchers("/", "/home", "/css/**", "/images/**", "/user/login", "/user/registration", "/free-pages/**").permitAll()
				.anyRequest().authenticated()
			)
			.formLogin((form) -> form
				.loginPage("/user/login")
				.defaultSuccessUrl("/")
				.permitAll()
			)
			.logout((logout) -> logout
				.logoutSuccessUrl("/user/login?logout")
				.invalidateHttpSession(true)
				.deleteCookies("JSESSIONID")
				.permitAll()
			)
			.sessionManagement((session) -> session
				.sessionFixation().migrateSession()
				.maximumSessions(1).expiredUrl("/user/login?expired")
			);

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12); // Increased strength for added security
	}

}
