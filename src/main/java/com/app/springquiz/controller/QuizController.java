package com.app.springquiz.controller;

import com.app.springquiz.service.QuizUserDetailsService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class QuizController {

    // DI
    private final QuizUserDetailsService quizUserDetailsService;
    private final AuthenticationManager authenticationManager;

    public QuizController(QuizUserDetailsService quizUserDetailsService, AuthenticationManager authenticationManager) {
        this.quizUserDetailsService = quizUserDetailsService;
        this.authenticationManager = authenticationManager;
    }

    // Authentication endpoints

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/logout")
    public String logoutPage(){
        return "logout";
    }

    @GetMapping("/register")
    public String registerPage(){
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String roles,
            @RequestParam String email
    ) {
        // Adding the user to the in-memory database
        try {
            quizUserDetailsService.registerUser(username, password, roles, email);
        } catch (Exception e){
            return "redirect:/register?error";
        }

        // Authenticate user programmatically and adding the authentication in the SecurityContex
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:/login?success";
    }

}
