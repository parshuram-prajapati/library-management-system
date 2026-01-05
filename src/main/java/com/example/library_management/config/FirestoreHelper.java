package com.example.library_management.config;

import com.example.library_management.model.Book;
import com.example.library_management.model.Student;
import com.example.library_management.model.Issue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FirestoreHelper {

    private Firestore getDb() {
        if (FirebaseApp.getApps().isEmpty()) {
            throw new IllegalStateException("Firebase not initialized");
        }
        return FirestoreClient.getFirestore();
    }

    // ---------- BOOKS ----------
    public List<Book> getAllBooks() throws Exception {
        List<Book> books = new ArrayList<>();

        for (QueryDocumentSnapshot doc : getDb().collection("books").get().get().getDocuments()) {

            Book book = doc.toObject(Book.class);
            book.setId(doc.getId()); // IMPORTANT
            books.add(book);
        }
        return books;
    }

    // ---------- STUDENTS ----------
    public List<Student> getAllStudents() throws Exception {
        List<Student> students = new ArrayList<>();

        for (QueryDocumentSnapshot doc : getDb().collection("students").get().get().getDocuments()) {

            Student student = doc.toObject(Student.class);
            student.setId(doc.getId());
            students.add(student);
        }
        return students;
    }

    // ---------- ISSUES ----------
    public List<Issue> getAllIssues() throws Exception {
        List<Issue> issues = new ArrayList<>();

        for (QueryDocumentSnapshot doc : getDb().collection("issues").get().get().getDocuments()) {

            Issue issue = doc.toObject(Issue.class);
            issue.setId(doc.getId());
            issues.add(issue);
        }
        return issues;
    }

    // ---------- ADD / DELETE (example) ----------
    public String addBook(Book book) throws Exception {
        getDb().collection("books").add(book);
        return "Book added";
    }

    public String deleteBook(String id) {
        getDb().collection("books").document(id).delete();
        return "Book deleted";
    }
}
