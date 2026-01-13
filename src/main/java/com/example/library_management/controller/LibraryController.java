package com.example.library_management.controller;

import com.example.library_management.model.*;
import com.example.library_management.service.FirebaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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
    public ResponseEntity<List<Book>> getBooks() {
        try {
            return ResponseEntity.ok(service.getAllBooks());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @PostMapping("/books")
    public ResponseEntity<String> addBook(@ModelAttribute Book book) {
        try {
            return ResponseEntity.ok(service.addBook(book));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to add book");
        }
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable String id) {
        return ResponseEntity.ok(service.deleteBook(id));
    }

    // ---------- STUDENTS ----------
    @GetMapping("/students")
    public ResponseEntity<List<Student>> getStudents() {
        try {
            return ResponseEntity.ok(service.getAllStudents());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @PostMapping("/students")
    public ResponseEntity<String> addStudent(@ModelAttribute Student student) {
        try {
            return ResponseEntity.ok(service.addStudent(student));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to add student");
        }
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable String id) {
        return ResponseEntity.ok(service.deleteStudent(id));
    }

    // ---------- ISSUE ----------
    @GetMapping("/issues")
    public ResponseEntity<List<Issue>> getIssues() {
        try {
            return ResponseEntity.ok(service.getAllIssues());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @PostMapping("/issue")
    public ResponseEntity<String> issue(@ModelAttribute Issue issue) {
        try {
            return ResponseEntity.ok(service.issueBook(issue));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Issue failed");
        }
    }

    @PostMapping("/return/{id}")
    public ResponseEntity<String> ret(@PathVariable String id) {
        return ResponseEntity.ok(service.returnBook(id));
    }

    // ---------- SEED BOOKS ----------
    @GetMapping("/seed-books")
    public ResponseEntity<String> seedBooks() {
        try {
            return ResponseEntity.ok(service.addBranchWiseBooks());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Seed failed");
        }
    }
}
