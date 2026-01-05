package com.example.library_management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // ROOT → LOGIN
    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    // MAIN APP PAGES ONLY
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/students")
    public String students() {
        return "students";
    }

    @GetMapping("/books")
    public String books() {
        return "books";
    }

    @GetMapping("/issue")
    public String issue() {
        return "issue";
    }
}
