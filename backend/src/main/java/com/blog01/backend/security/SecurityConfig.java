package com.blog01.backend.security;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Disable CSRF (because we use stateless JWTs, not browser sessions)
                .csrf(csrf -> csrf.disable())

                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 2. Define URL Permissions
                .authorizeHttpRequests(auth -> auth
                        // Allow anyone to access Login and Register
                        .requestMatchers("/api/auth/**").permitAll()

                        .requestMatchers("/error").permitAll() // <--- ADD THIS LINE

                        // Example: Restrict Admin endpoints to "ADMIN" role only
                        // Note: "hasRole" automatically adds "ROLE_" prefix, so it checks for
                        // "ROLE_ADMIN"
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // Any other request requires the user to be logged in
                        .anyRequest().authenticated())

                // 3. Stateless Session Management (Don't store session in server memory)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 4. Set our custom Authentication Provider
                .authenticationProvider(authenticationProvider())

                // 5. Add our JWT Filter BEFORE the standard Username/Password filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();    
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Allow your Angular Frontend
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));

        // Allow all HTTP methods (GET, POST, PUT, DELETE, etc.)
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Allow specific headers (Authorization is vital for JWT)
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        // Allow sending credentials (if you ever use cookies/sessions)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply to all endpoints
        return source;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        // Pass the Service and Encoder directly to the constructor
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Use BCrypt for password hashing
    }
}