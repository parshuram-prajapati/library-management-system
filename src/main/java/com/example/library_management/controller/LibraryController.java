package com.example.library_management.controller;

import com.example.library_management.model.*;
import com.example.library_management.service.FirebaseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LibraryController {

    private final FirebaseService service;

    public LibraryController(FirebaseService service) {
        this.service = service;
    }

    // ---------- BOOKS ----------
    @GetMapping("/books")
    public List<Book> getBooks() throws Exception {
        return service.getAllBooks();
    }

    @PostMapping("/books")
    public String addBook(@ModelAttribute Book book) throws Exception {
        return service.addBook(book);
    }

    @DeleteMapping("/books/{id}")
    public String deleteBook(@PathVariable String id) {
        return service.deleteBook(id);
    }

    // ---------- STUDENTS ----------
    @GetMapping("/students")
    public List<Student> getStudents() throws Exception {
        return service.getAllStudents();
    }

    @PostMapping("/students")
    public String addStudent(@ModelAttribute Student student) throws Exception {
        return service.addStudent(student);
    }

    @DeleteMapping("/students/{id}")
    public String deleteStudent(@PathVariable String id) {
        return service.deleteStudent(id);
    }

    // ---------- ISSUE ----------
    @PostMapping("/issue")
    public String issue(@ModelAttribute Issue issue) throws Exception {
        return service.issueBook(issue);
    }

    @GetMapping("/issues")
    public List<Issue> getIssues() throws Exception {
        return service.getAllIssues();
    }

    @PostMapping("/return/{id}")
    public String ret(@PathVariable String id) {
        return service.returnBook(id);
    }

    // ---------- SEED BOOKS ----------
    @GetMapping("/seed-books")
    public String seedBooks() throws Exception {
        return service.addBranchWiseBooks();
    }
}
