package com.rspl.configuration;

import org.springframework.context.annotation.Configuration;




import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.rspl.filters.JwtRequestFilter;
import com.rspl.services.jwt.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;


@Configuration
@EnableMethodSecurity
@EnableWebSecurity

public class WebSecurityConfiguration {
	
	private final JwtRequestFilter jwtRequestFilter;
	
	
	public WebSecurityConfiguration(JwtRequestFilter jwtRequestFilter) {
		super();
		this.jwtRequestFilter = jwtRequestFilter;
	}

	@Autowired
	@Bean
	public UserDetailsService detailsService()
	{
		return new UserDetailsServiceImpl();
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider()
	{
		
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(detailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
		
	}
	
	@Autowired
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
		
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
	
	
	

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {


		return httpSecurity.csrf().disable()
		.cors().disable()
		.authorizeHttpRequests().requestMatchers("/authenticate").permitAll()
		.requestMatchers("/employee/**").hasRole("Employee")
		.requestMatchers("/admin/**").hasRole("Admin")
		.anyRequest().authenticated()
		.and()
		.authenticationProvider(authenticationProvider())
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class).build()
		;
		


		   
		
		

		
		
	}
	
	


	
}
