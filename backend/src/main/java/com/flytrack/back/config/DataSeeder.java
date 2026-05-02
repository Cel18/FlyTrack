package com.flytrack.back.config;

import com.flytrack.back.model.*;
import com.flytrack.back.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Carga datos iniciales al arrancar la aplicación, solo si no existen.
 */
@Configuration
public class DataSeeder {

        @Value("${ADMIN_EMAIL:#{null}}")
        private String adminEmail;

        @Value("${ADMIN_PASSWORD:#{null}}")
        private String adminPassword;

        @Bean
        CommandLineRunner seedData(UsuarioRepository usuarioRepository,
                        PasswordEncoder passwordEncoder) {
                return args -> {
                        if (adminEmail != null && adminPassword != null && !adminEmail.trim().isEmpty()) {
                                if (!usuarioRepository.existsByCorreo(adminEmail)) {
                                        Usuario admin = new Usuario(
                                                        "Administrador FlyTrack",
                                                        adminEmail,
                                                        passwordEncoder.encode(adminPassword),
                                                        Rol.ADMIN);
                                        usuarioRepository.save(admin);
                                        System.out.println("✅ Usuario ADMIN creado: " + adminEmail);
                                }
                        } else {
                                System.out.println(
                                                "⚠️ Variables de entorno ADMIN_EMAIL o ADMIN_PASSWORD no configuradas. No se creará el usuario admin por defecto.");
                        }
                };
        }
}
