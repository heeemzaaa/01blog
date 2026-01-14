package com.blog01.backend.common;

import com.blog01.backend.user.model.User;
import com.blog01.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Check if the admin already exists
        if (!userRepository.existsByEmail("admin@blog.com")) {
            User admin = User.builder()
                    .firstName("Super")
                    .lastName("Admin")
                    .username("admin")
                    .email("admin@blog.com")
                    .password(passwordEncoder.encode("admin123")) // Default password
                    .role(User.Role.ADMIN) // <--- THIS IS THE KEY
                    .profileImage(null)
                    .build();

            userRepository.save(admin);
            System.out.println("✅ Admin user created: admin@blog.com / admin123");
        }
    }
}