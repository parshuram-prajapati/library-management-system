package com.example.library_management.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/set-session")
    @ResponseBody
    public String setSession(@RequestParam String email, HttpSession session) {
        session.setAttribute("userEmail", email);
        return "Session set";
    }

    @GetMapping("/logout-success")
    public String logoutSuccess() {
        return "redirect:/login";
    }
}
