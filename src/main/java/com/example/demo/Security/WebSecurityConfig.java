package com.example.demo.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class WebSecurityConfig {

        private final JwtAuthFilter jwtAuthFilter;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
                httpSecurity
                                // 1. Enable CORS using the Bean defined below
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                // 2. Disable CSRF for stateless APIs
                                .csrf(csrf -> csrf.disable())
                                // 3. Set session management to stateless
                                .sessionManagement(
                                                sessionConfig -> sessionConfig
                                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                // 4. Secure the routes
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/public/**", "/auth/**", "/error").permitAll()
                                                .anyRequest().authenticated())
                                // 5. Add JWT filter
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

                return httpSecurity.build();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
                        throws Exception {
                return authenticationConfiguration.getAuthenticationManager();
        }

        // 6. Define the exact CORS rules
        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();

                // Allows any frontend URL to connect (Netlify, localhost, etc.)
                configuration.setAllowedOriginPatterns(Arrays.asList("*"));

                // Explicitly allow all standard methods, including OPTIONS (Pre-flight)
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

                // Explicitly allow the Authorization header so React can send the JWT
                configuration.setAllowedHeaders(
                                Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin"));

                // Expose the Authorization header so the browser can read it
                configuration.setExposedHeaders(Arrays.asList("Authorization"));

                // Allow credentials (required for Authorization headers)
                configuration.setAllowCredentials(true);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                // Apply these rules to ALL endpoints
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }
}