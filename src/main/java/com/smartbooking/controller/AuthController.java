package com.smartbooking.controller;

import com.smartbooking.dto.LoginRequest;
import com.smartbooking.entity.User;
import com.smartbooking.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService=authService;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public String login(@Valid @RequestBody LoginRequest request){
        authService.login(request);
        return "Login Successful";
    }
}
