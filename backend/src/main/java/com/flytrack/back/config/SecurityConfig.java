package com.flytrack.back.config;

import com.flytrack.back.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    @SuppressWarnings("java:S4502")
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disabling CSRF as we use stateless JWT authentication
                // sonar-ignore-security-hotspot: CSRF is not required for stateless APIs using JWT
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Autenticación pública
                        .requestMatchers("/api/auth/**").permitAll()
                        // Endpoints públicos (consulta sin sesión)
                        .requestMatchers(HttpMethod.GET, "/api/vuelos/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/puertas/**").permitAll()
                        // Documentación Swagger/OpenAPI
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        // Recursos estáticos del frontend (React / Vite)
                        .requestMatchers("/", "/index.html", "/*.svg", "/*.ico", "/assets/**").permitAll()
                        // SPA fallback: rutas que no son /api/**
                        .requestMatchers(req -> !req.getRequestURI().startsWith("/api/")).permitAll()
                        // El resto requiere autenticación
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // Restringimos orígenes para mejorar la seguridad (Security Hotspot en Sonar)
        config.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "http://localhost:8080",
                "http://3.93.200.242",
                "http://3.93.200.242:8080"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
