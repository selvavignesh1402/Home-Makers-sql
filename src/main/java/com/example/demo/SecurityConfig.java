package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/api/userregister/**").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/products/**").permitAll()
                .requestMatchers("/api/orders/**").permitAll()
                .requestMatchers("/api/otp/**").permitAll()
                .requestMatchers("/api/otp/validate").permitAll()
                .anyRequest().authenticated()
            )
            .httpBasic();
        return http.build();
    }
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
