package com.gtcafe.asimov.consumer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
            .authorizeHttpRequests(requests -> requests
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll() // 允許訪問靜態資源
                // .requestMatchers(HttpMethod.POST, "/api/members").permitAll()
                // .requestMatchers("/api/v1alpha/tokens").permitAll()
                // .requestMatchers("/api/v1alpha/users").permitAll()
                .requestMatchers("/api/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**").permitAll()
                .requestMatchers("/actuator/prometheus").permitAll()

                .requestMatchers(HttpMethod.GET, "/_/**").permitAll()
                
                .anyRequest().authenticated()
            )

            .build();
    }
   
}
