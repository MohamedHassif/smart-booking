package com.smartbooking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration //Here are some objects that I want you to create and manage
//marks a class as a source of bean definitions and configuration that Spring processes.
public class SecurityConfig {

        @Bean //Call this method, take the object it returns, and register that object as a Spring Bean.
        //you explicitly tell Spring how to create the object.
        // tells Spring to register the object returned by the annotated method as a Spring-managed bean.
        //"A Spring Bean is an object created and managed by the Spring IoC container, allowing Spring to handle its lifecycle and inject it wherever it's required."
        public PasswordEncoder passwordEncoder(){
            return new BCryptPasswordEncoder();
        }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST,"/api/users").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/auth/login").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
            return configuration.getAuthenticationManager();
    }
}
