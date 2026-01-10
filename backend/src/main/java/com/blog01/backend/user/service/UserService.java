package com.blog01.backend.user.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.blog01.backend.user.model.User;
import com.blog01.backend.user.dto.*;
import com.blog01.backend.common.response.*;
import com.blog01.backend.user.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository ur;
    private final PasswordEncoder pe;

    public UserService(UserRepository ur, PasswordEncoder pe) {
        this.ur = ur;
        this.pe = pe;

    }

    public ResponseData<User> Login(UserLogin requestUser) {
        Optional<User> userOpt = ur.findByEmail(requestUser.getEmail());
        if (!userOpt.isPresent()) {
            // case of error user doesn't exists
            return ResponseData.error("This Email doesn't exist !");
        }

        User user = userOpt.get();

        if (!pe.matches(requestUser.getPassword(), user.getPassword())) {
            return ResponseData.error("Incorrect password !");
        }

        // still need to handle JWT
        return ResponseData.success("User logged in successfully !", user);
    }

    public ResponseData<User> Register(UserRegister userRequest) {  
        if (ur.existsByEmail(userRequest.getEmail())) {
            return ResponseData.error("Email already existed, try another one !");
        }
        User userToCreate = User.builder()
            .firstName(userRequest.getFirstName())
            .lastName(userRequest.getLastName())
            .username(userRequest.getUsername())
            .email(userRequest.getEmail())
            .password(pe.encode(userRequest.getPassword()))
            .profileImage(userRequest.getProfileImage())
            .build();

        User saved = ur.save(userToCreate);
        
        return ResponseData.success("User registered successfully !", saved);
    }
}