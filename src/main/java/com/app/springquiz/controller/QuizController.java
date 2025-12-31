package com.app.springquiz.controller;

import com.app.springquiz.model.Quiz;
import com.app.springquiz.service.QuizService;
import com.app.springquiz.service.QuizUserDetailsService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class QuizController {

    // DI

    private final QuizUserDetailsService quizUserDetailsService;
    private final QuizService quizService;
    private final AuthenticationManager authenticationManager;

    public QuizController(QuizUserDetailsService quizUserDetailsService, QuizService quizService, AuthenticationManager authenticationManager) {
        this.quizUserDetailsService = quizUserDetailsService;
        this.quizService = quizService;
        this.authenticationManager = authenticationManager;
    }

    // Home

    @GetMapping({"/", "/home"})
    public String showHomepage(Model model, Authentication authentication) {
        // Get user's role
        String role = authentication.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .findFirst()
                .orElse("ROLE_USER");
        model.addAttribute("username", authentication.getName());

        List<Quiz> quizzes = quizService.loadQuizzes();
        model.addAttribute("quizzes", quizzes);

        if (role.equals("ROLE_ADMIN")){
            return "quiz/QuizList";
        } else {
            return "quizz";
        }
    }


    // Authentication endpoints

    @GetMapping("/login")
    public String loginPage(){
        return "auth/login";
    }

    @GetMapping("/logout")
    public String logoutPage(){
        return "auth/logout";
    }

    @GetMapping("/register")
    public String registerPage(){
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String role,
            @RequestParam String email
    ) {
        // Adding the user to the in-memory database
        try {
            quizUserDetailsService.registerUser(username, password, role, email);
            System.out.println("user: " + username + " Successfully registered");
        } catch (Exception e){
            System.out.println(e.getMessage());
            return "redirect:/register?error";
        }

        // Authenticate user programmatically and adding the authentication in the SecurityContex
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:/login?success";
    }

    // Quiz service endpoints

    @GetMapping("/quiz/add")
    public String ShowAddQuiz(Model model) {
        model.addAttribute("quiz", new Quiz());
        return "quiz/add";
    }

    @PostMapping("/quiz/add")
    public String addQuiz(@ModelAttribute Quiz quiz, Model model, Authentication authentication) {

        // Get user's role
        String role = authentication.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .findFirst()
                .orElse("ROLE_USER");

        if (role.equals("ROLE_ADMIN")) {
            quiz.setId(quizService.getNextId());
            quizService.addQuiz(quiz);

            model.addAttribute("success", "Quiz added successfully!");
            return "redirect:/home";
        } else {
            model.addAttribute("error", "You don't have permission to add a quiz.");
            return "redirect:/quiz/add?error";
        }
    }

    @GetMapping("/quiz/edit/{id}")
    public String ShowEditQuiz(@PathVariable("id") int id, Model model) {
        Quiz quiz = quizService.getQuizById(id);
        model.addAttribute("quiz", quiz);
        return "quiz/edit";
    }

    @PostMapping("/quiz/edit")
    public String editQuiz(@ModelAttribute Quiz quiz, Model model, Authentication authentication) {
        // Get user's role
        String role = authentication.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .findFirst()
                .orElse("ROLE_USER");

        if (role.equals("ROLE_ADMIN")) {
            quizService.editQuiz(quiz);

            model.addAttribute("success", "Quiz edited successfully!");
            return "redirect:/home";
        } else {
            return "redirect:/home";
        }
    }

    @GetMapping("/quiz/delete/{id}")
    public String deleteQuiz(@PathVariable("id") int id, Model model, Authentication authentication) {
        // Get user's role
        String role = authentication.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .findFirst()
                .orElse("ROLE_USER");

        if (role.equals("ROLE_ADMIN")) {
            quizService.deleteQuiz(id);

            model.addAttribute("success", "Quiz deleted successfully!");
            return "redirect:/home";
        } else {
            return "redirect:/home";
        }
    }

    @PostMapping("/submitQuiz")
    public String evaluateQuiz(@RequestParam Map<String, String> allParams, Model model) {
        int correctAnswers = 0;
        List<String> userAnswers = new ArrayList<>();
        List<Quiz> quizzes = quizService.loadQuizzes();

        for (int i = 0; i < quizzes.size(); i++) {
            String userAnswer = allParams.get("answer"+ i);
            userAnswers.add(userAnswer);
            if (userAnswer.equals(quizzes.get(i).getCorrectAnswer())){
                correctAnswers++;
            }
        }

        model.addAttribute("quizzes", quizzes);
        model.addAttribute("userAnswers", userAnswers);
        model.addAttribute("correctAnswers", correctAnswers);
        model.addAttribute("totalQuestions", quizzes.size());

        return "quiz/result";
    }


}
