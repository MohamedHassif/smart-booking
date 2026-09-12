package com.smartbooking.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "bearerAuth")
@RestController
public class HomeController {
    @GetMapping("/")
    public String home(){
        return "Welcome to Smart Booking";
    }
}
