package com.example.library_management.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity in this stage
                .authorizeHttpRequests(auth -> auth
                        // 1. Allow Static Resources (JS, CSS, Images)
                        .requestMatchers("/js/**", "/css/**", "/images/**", "/webjars/**").permitAll()

                        // 2. Allow Public Pages (Login, Signup)
                        .requestMatchers("/", "/signup", "/error").permitAll()

                        // 3. Allow API Endpoints (So your JS can fetch data)
                        .requestMatchers("/api/**").permitAll()

                        // 4. Any other request requires authentication (Optional: change to permitAll()
                        // if you want no blocks)
                        .anyRequest().permitAll())
                // Disable default Spring Boot Login page so it uses YOUR index.html
                .formLogin(login -> login.disable())
                .logout(logout -> logout.disable());

        return http.build();
    }
}