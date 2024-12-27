package com.example.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.service.MyUserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig  {
	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtFilter jwtFilter;

	 @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
			/*
			 * http .authorizeRequests() .requestMatchers("/admin/**").hasRole("ADMIN") //
			 * Only accessible to ADMIN .requestMatchers("/user/**").hasRole("USER") // Only
			 * accessible to USER .requestMatchers("/public/**").permitAll() // Public URL
			 * .and() .formLogin() .permitAll() .and() .logout() .permitAll();
			 */
		 System.out.println("insert into security config.");
	       return http.csrf(customizer -> customizer.disable())

	        	.authorizeHttpRequests((request) ->{
					request.requestMatchers("/student/login", "/student/saveStudent").permitAll();
					request.anyRequest().authenticated();
				})
	        	.formLogin(Customizer.withDefaults())//using this we are enabling the form in web.
	         	.httpBasic(Customizer.withDefaults())//using this we run on postman also.
	         	.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) )
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
	         	.build();
	       // return http.build();

		 /*http.csrf().disable()
				 .authorizeHttpRequests((authorize)->{
					 authorize.requestMatchers("api/v1/**").permitAll();

					 authorize.anyRequest().authenticated();
				 }).httpBasic(Customizer.withDefaults());
		 return http.build();*/
	    }

	 @Bean
	 public UserDetailsService userDetailsService() {
		 return new MyUserDetailsService();

	 }

	 @Bean
	 public AuthenticationProvider authenticationProvider() {
		 DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		 provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
		 provider.setUserDetailsService(userDetailsService());
		 return provider;

	 }

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}





}
