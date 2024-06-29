package com.ccunarro.user_project.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final SimpleUserDetailsService simpleUserDetailsService;

    public SecurityConfig(SimpleUserDetailsService simpleUserDetailsService) {
        this.simpleUserDetailsService = simpleUserDetailsService;
    }

    @Bean
    @Order(2)
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/**")
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .userDetailsService(simpleUserDetailsService)
                .httpBasic(withDefaults())
                .build();
    }

    @Bean
    @Order(1)
    SecurityFilterChain allowUserCreationWithoutAuthentication(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/users/")
                .authorizeHttpRequests( auth -> auth.requestMatchers(AntPathRequestMatcher.antMatcher("/users/"), AntPathRequestMatcher.antMatcher(HttpMethod.POST)).permitAll())
                .csrf((AbstractHttpConfigurer::disable))
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}