package com.example.library_management.service;

import com.example.library_management.model.Issue;
import com.example.library_management.model.Student;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ReminderService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    // ---------------- SEND MANUAL REMINDER ----------------
    public String sendManualReminder(String bookId) {

        if (mailSender == null) {
            return "Error: Email Config Missing";
        }

        try {
            var db = FirestoreClient.getFirestore();

            // 1️⃣ Get Issue by bookId
            DocumentSnapshot issueDoc = db.collection("issues")
                    .document(bookId)
                    .get()
                    .get();

            if (!issueDoc.exists())
                return "Error: Book not found in issued list";

            Issue issue = issueDoc.toObject(Issue.class);

            // DEBUG LOGS
            System.out.println("ISSUE OBJECT = " + issue);
            System.out.println("DUE DATE = " + issue.getDueDate());

            // 2️⃣ Get Student
            DocumentSnapshot studentDoc = db.collection("students")
                    .document(issue.getStudentId())
                    .get()
                    .get();

            if (!studentDoc.exists())
                return "Error: Student profile not found";

            Student student = studentDoc.toObject(Student.class);

            if (student.getEmail() == null || student.getEmail().isEmpty()) {
                return "Error: Student has no email address";
            }

            // 3️⃣ SAFETY FALLBACK FOR DUE DATE
            String dueDate = issue.getDueDate();
            if (dueDate == null || dueDate.isEmpty()) {
                LocalDate issueDate = LocalDate.parse(issue.getIssueDate());
                dueDate = issueDate.plusDays(14).toString();
            }

            // 4️⃣ SEND EMAIL
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(student.getEmail());
            message.setSubject("Library Reminder: Please Return Book");
            message.setText(
                    "Hello " + student.getName() + ",\n\n" +
                            "This is a reminder to return the book:\n\n" +
                            "Book: " + issue.getBookTitle() + "\n" +
                            "Due Date: " + dueDate + "\n\n" +
                            "Thank you,\nLibrary Admin");

            mailSender.send(message);

            return "Email sent successfully to " + student.getEmail();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error sending email: " + e.getMessage();
        }
    }
}
