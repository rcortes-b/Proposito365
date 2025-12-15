package com.proposito365.app.middleware.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.proposito365.app.middleware.auth.AuthService;

/*First of all -> What do i want to achieve?
 - I want to everytime a webuser tries to access a page check if he's authorized 
	- If he is authorized -> Back to the frontend
	- If he is not authorized -> login form in the frontend
		- Check if user exists + register, etc
 */

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	public final static String LOGIN_URL_MATCHER = "/auth/login";
	public final static String LOGOUT_URL_MATCHER = "/auth/logout";
	public final static String REGISTER_URL_MATCHER = "/auth/register";

	private AuthService authService;
	private UserDetailsService userDetailsService;
	private PasswordEncoder passwordEncoder;

	public SecurityConfig(AuthService authService, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
		this.authService = authService;
		this.userDetailsService = userDetailsService;
		this.passwordEncoder = passwordEncoder;
	}


	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		return http
				.formLogin(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(auth -> auth
										.requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
										.anyRequest().permitAll()
										)
				.httpBasic(Customizer.withDefaults())
				.csrf(csrf -> csrf.disable())
				.build();
	}

	@Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        ProviderManager providerManager = new ProviderManager(authenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(true);

        return providerManager;
    }

}
