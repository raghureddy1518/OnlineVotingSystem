package com.example.demo.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VoterSecurityConfig {

    @Bean
    @Order(2)
    public SecurityFilterChain voterFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout") // Default logout for voters
                .logoutSuccessUrl("/")
                .permitAll()
            );
        return http.build();
    }
}
