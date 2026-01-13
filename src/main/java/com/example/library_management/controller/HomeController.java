package com.example.library_management.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // ROOT → always redirect to login
    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    // DASHBOARD
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (session.getAttribute("userEmail") == null) {
            return "redirect:/login";
        }
        return "dashboard";
    }

    @GetMapping("/students")
    public String students(HttpSession session) {
        if (session.getAttribute("userEmail") == null) {
            return "redirect:/login";
        }
        return "students";
    }

    @GetMapping("/books")
    public String books(HttpSession session) {
        if (session.getAttribute("userEmail") == null) {
            return "redirect:/login";
        }
        return "books";
    }

    @GetMapping("/issue")
    public String issue(HttpSession session) {
        if (session.getAttribute("userEmail") == null) {
            return "redirect:/login";
        }
        return "issue";
    }
}
