package com.example.petadoption.security;

import com.example.petadoption.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final CustomUserDetailsService userDetailsService;
    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    // Password Encoder (REQUIRED)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // Main Security Configuration
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                //public routes
                .requestMatchers(
                    "/",
                    "/pets/**",
                    "/petAdoption/user/register",
                    "/petAdoption/user/login",
                    "/css/**",
                    "/js/**",
                    "/images/**"
                ).permitAll()
                //Admin routAll
                .requestMatchers(
                    "/admin/**",
                    "/petAdoption/admin/**").hasRole("ADMIN")
                //User and Admin routes
                .requestMatchers(
                    "/petAdoption/user/",
                    "/petAdoption/user/applications",
                    "/petAdoption/user/history",
                    "/user/applications",
                    "/user/history",
                    "/adoptions/**"
                    ).hasAnyRole("USER", "ADMIN")
                //All other routes require login
                .anyRequest()
                .authenticated()
            )

// Hook your custom user service
                .userDetailsService(userDetailsService)
                .formLogin(form -> form
                        .loginPage("/petAdoption/user/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .failureUrl("/petAdoption/user/login?error")
                        //.defaultSuccessUrl("/petAdoption/user/", true)
                        .successHandler((request, response, authentication) -> {
                            boolean isAdmin = authentication.getAuthorities()
                                    .stream()
                                    .anyMatch(a ->
                                            a.getAuthority().equals("ROLE_ADMIN"));

                            if (isAdmin) {
                                response.sendRedirect(
                                        "/petAdoption/admin/");
                            } else {
                                response.sendRedirect(
                                        "/petAdoption/user/");
                            }
                        })
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/petAdoption/user/login?logout")
                        .permitAll()
                );
        return http.build();
    }
    // AuthenticationManager (needed for login logic / APIs)
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}