package com.smartbooking.service;

import com.smartbooking.dto.LoginRequest;
import com.smartbooking.entity.User;
import com.smartbooking.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    public AuthService(AuthenticationManager authenticationManager){
        this.authenticationManager=authenticationManager;
    }

//    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManagerBuilder authenticationManagerBuilder){
//        this.userRepository=userRepository;
//        this.passwordEncoder=passwordEncoder;
//    }

//    public User login(LoginRequest request){
//        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(()->new RuntimeException("Invalid email"));
//
//        if(!passwordEncoder.matches(request.getPassword(),user.getPassword())){
//            throw new RuntimeException("Invalid email or password");
//        }
//        return user;
//    }

    public void login(LoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword())
        );
    }
}
