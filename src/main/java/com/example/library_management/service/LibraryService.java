package com.example.library_management.service;

import com.example.library_management.model.Book;
import com.example.library_management.model.Student;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LibraryService {

    private final Map<String, Book> books = new ConcurrentHashMap<>();
    private final Map<String, Student> students = new ConcurrentHashMap<>();

    public Collection<Book> getAllBooks() {
        return books.values();
    }

    public Book getBook(String id) {
        return books.get(id);
    }

    public Book addBook(Book book) {
        if (book.getId() == null || book.getId().isEmpty()) {
            throw new IllegalArgumentException("Book id is required");
        }
        books.put(book.getId(), book);
        return book;
    }

    public Book updateBook(String id, Book updated) {
        Book existing = books.get(id);
        if (existing == null) return null;
        existing.setTitle(updated.getTitle());
        existing.setAuthor(updated.getAuthor());
        existing.setIssued(updated.isIssued());
        existing.setIssuedTo(updated.getIssuedTo());
        books.put(id, existing);
        return existing;
    }

    public boolean deleteBook(String id) {
        return books.remove(id) != null;
    }

    public Collection<Student> getAllStudents() {
        return students.values();
    }

    public Student addStudent(Student student) {
        if (student.getId() == null || student.getId().isEmpty()) {
            throw new IllegalArgumentException("Student id is required");
        }
        students.put(student.getId(), student);
        return student;
    }

    public synchronized String borrowBook(String studentId, String bookId) {
        Student s = students.get(studentId);
        if (s == null) return "Student not found";
        Book b = books.get(bookId);
        if (b == null) return "Book not found";
        if (b.isIssued()) return "Book already issued";
        b.setIssued(true);
        b.setIssuedTo(studentId);
        books.put(bookId, b);
        return "Book issued to " + s.getName();
    }

    public synchronized String returnBook(String studentId, String bookId) {
        Book b = books.get(bookId);
        if (b == null) return "Book not found";
        if (!b.isIssued()) return "Book is not issued";
        if (b.getIssuedTo() == null || !b.getIssuedTo().equals(studentId)) return "Book was not issued to this student";
        b.setIssued(false);
        b.setIssuedTo(null);
        books.put(bookId, b);
        return "Book returned successfully";
    }
}
