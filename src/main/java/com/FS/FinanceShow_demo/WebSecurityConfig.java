package com.FS.FinanceShow_demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	public WebSecurityConfig() {
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests((requests) -> requests
				.requestMatchers("/", "/home", "/css/**", "/images/**", "/user/login", "/user/registration", "/free-pages/**").permitAll()
				.requestMatchers("/user/list").hasRole("ADMIN")
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
			)
			.exceptionHandling((exceptions) -> exceptions
				.accessDeniedHandler((request, response, accessDeniedException) -> {
					response.sendRedirect("/hello");
				})
			)
			.csrf(AbstractHttpConfigurer::disable);

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12); // Increased strength for added security
	}

}
