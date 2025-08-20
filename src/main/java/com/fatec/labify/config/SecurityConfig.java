package com.fatec.labify.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final TokenFilter tokenFilter;

    public SecurityConfig(TokenFilter tokenFilter) {
        this.tokenFilter = tokenFilter;
    }

    @Bean
    public SecurityFilterChain securityFilters(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(
                        request -> {
                            request.requestMatchers(
                                    "/swagger-ui/**", "/v3/api-docs/**",
                                    "/login/**",
                                    "/refresh-token",
                                    "/users/verify-account",
                                    "/patients/create/**",
                                    "/error").permitAll();
                            request.requestMatchers(HttpMethod.POST, "/users").permitAll();
                            request.requestMatchers(HttpMethod.GET, "/users").hasRole("SYSTEM");

                            request.requestMatchers(HttpMethod.GET, "/labs").hasRole("SYSTEM");
                            request.requestMatchers(HttpMethod.POST, "/labs").hasRole("SYSTEM");
                            request.requestMatchers(HttpMethod.PUT, "/labs/**").hasAnyRole("SYSTEM", "SUPER_ADMIN");
                            request.requestMatchers(HttpMethod.DELETE, "/labs").hasRole("SYSTEM");
                            request.requestMatchers(HttpMethod.PUT, "/labs/{id}/status").hasRole("SYSTEM");

                            request.requestMatchers(HttpMethod.GET, "/branches").hasAnyRole("SYSTEM", "SUPER_ADMIN");
                            request.requestMatchers(HttpMethod.POST, "/branches").hasAnyRole("SYSTEM", "SUPER_ADMIN");

                            request.requestMatchers(HttpMethod.POST, "/tests").hasRole("SYSTEM");
                            request.requestMatchers(HttpMethod.PUT, "/tests/{id}").hasRole("SYSTEM");
                            request.requestMatchers(HttpMethod.DELETE, "/tests/{id}").hasRole("SYSTEM");

                            request.requestMatchers(HttpMethod.GET, "schedule/{id}").hasAnyRole("SYSTEM", "SUPER_ADMIN", "ADMIN");
                            request.requestMatchers(HttpMethod.GET, "/me").hasAnyRole("PATIENT");

                            request.requestMatchers(HttpMethod.GET, "/roles/").hasAnyRole("SYSTEM");
                            request.requestMatchers("/user-roles/**").hasAnyRole("SYSTEM", "SUPER_ADMIN", "ADMIN");
                            request.anyRequest().authenticated();
                        }
                )
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder encryptor() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public RoleHierarchy roleHierarchy(){
        String hierarchy =
            """
            ROLE_SYSTEM > ROLE_SUPER_ADMIN
            ROLE_SUPER_ADMIN > ROLE_ADMIN
            ROLE_ADMIN > ROLE_PATIENT
            """;
        return RoleHierarchyImpl.fromHierarchy(hierarchy);
    }

}
