package com.example.library_management.service;

import com.example.library_management.model.*;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FirebaseService {

    private static final String COL_BOOKS = "books";
    private static final String COL_STUDENTS = "students";
    private static final String COL_ISSUES = "issues";
    private static final String COL_LOGS = "logs";

    // ================= BOOKS =================
    public String addBook(Book book) throws Exception {
        FirestoreClient.getFirestore()
                .collection(COL_BOOKS)
                .document(book.getId())
                .set(book)
                .get();

        saveLog("ADD_BOOK", "Added book: " + book.getTitle());
        return "Book added successfully";
    }

    public List<Book> getAllBooks() throws Exception {
        return FirestoreClient.getFirestore()
                .collection(COL_BOOKS)
                .get()
                .get()
                .toObjects(Book.class);
    }

    public String deleteBook(String id) {
        FirestoreClient.getFirestore()
                .collection(COL_BOOKS)
                .document(id)
                .delete();

        saveLog("DELETE_BOOK", "Deleted book: " + id);
        return "Book deleted successfully";
    }

    // ================= STUDENTS =================
    public String addStudent(Student student) throws Exception {
        FirestoreClient.getFirestore()
                .collection(COL_STUDENTS)
                .document(student.getId())
                .set(student)
                .get();

        saveLog("REGISTER", "Registered student: " + student.getName());
        return "Student added successfully";
    }

    public List<Student> getAllStudents() throws Exception {
        return FirestoreClient.getFirestore()
                .collection(COL_STUDENTS)
                .get()
                .get()
                .toObjects(Student.class);
    }

    // ✅ DELETE STUDENT (FIXED PLACE)
    public String deleteStudent(String id) {

        FirestoreClient.getFirestore()
                .collection(COL_STUDENTS)
                .document(id)
                .delete();

        saveLog("DELETE_STUDENT", "Deleted student: " + id);
        return "Student deleted successfully";
    }

    // ================= ISSUE BOOK =================
    public String issueBook(Issue issue) throws Exception {

        Firestore db = FirestoreClient.getFirestore();

        Book book = db.collection(COL_BOOKS)
                .document(issue.getBookId())
                .get()
                .get()
                .toObject(Book.class);

        Student student = db.collection(COL_STUDENTS)
                .document(issue.getStudentId())
                .get()
                .get()
                .toObject(Student.class);

        if (book == null || student == null)
            return "Invalid Book ID or Student ID";

        LocalDate today = LocalDate.now();

        issue.setBookTitle(book.getTitle());
        issue.setStudentName(student.getName());
        issue.setIssueDate(today.toString());
        issue.setDueDate(today.plusDays(14).toString());
        issue.setReturnDate(null);
        issue.setReminderSent(false);

        db.collection(COL_ISSUES)
                .document(issue.getBookId())
                .set(issue)
                .get();

        saveLog("ISSUE", "Issued " + book.getTitle() + " to " + student.getName());
        return "Book issued successfully";
    }

    public List<Issue> getAllIssues() throws Exception {
        return FirestoreClient.getFirestore()
                .collection(COL_ISSUES)
                .get()
                .get()
                .toObjects(Issue.class);
    }

    // ================= RETURN BOOK =================
    public String returnBook(String bookId) {
        try {
            Firestore db = FirestoreClient.getFirestore();
            DocumentReference ref = db.collection(COL_ISSUES).document(bookId);
            DocumentSnapshot snap = ref.get().get();

            if (!snap.exists())
                return "Issue not found";

            Issue issue = snap.toObject(Issue.class);
            issue.setReturnDate(LocalDate.now().toString());

            ref.set(issue);
            saveLog("RETURN", "Returned " + issue.getBookTitle());
            return "Book returned successfully";

        } catch (Exception e) {
            e.printStackTrace();
            return "Return failed";
        }
    }

    // ================= LOGS =================
    public List<Log> getAllLogs() throws Exception {
        return FirestoreClient.getFirestore()
                .collection(COL_LOGS)
                .get()
                .get()
                .toObjects(Log.class);
    }

    // ================= LOG HELPER =================
    private void saveLog(String actionType, String description) {
        try {
            Log log = new Log();
            log.setActionType(actionType);
            log.setDescription(description);
            log.setUser("Admin");
            log.setTimestamp(LocalDateTime.now().toString());

            FirestoreClient.getFirestore()
                    .collection(COL_LOGS)
                    .add(log);
        } catch (Exception e) {
            System.err.println("Log save failed: " + e.getMessage());
        }
    }

    // ================= SEED 30 BOOKS =================
    public String addBranchWiseBooks() throws Exception {

        Firestore db = FirestoreClient.getFirestore();

        String[][] books = {
                { "CSE001", "Data Structures", "Mark Allen Weiss", "CSE" },
                { "CSE002", "Operating Systems", "Silberschatz", "CSE" },
                { "AIML001", "Machine Learning", "Tom Mitchell", "AIML" },
                { "ME001", "Engineering Mechanics", "Hibbeler", "MECH" },
                { "EE001", "Basic Electrical Engineering", "B.L. Theraja", "EEE" },
                { "CE001", "Building Materials", "Arora & Bindra", "CIVIL" }
                // (बाकी already ठीक हैं)
        };

        for (String[] b : books) {
            Book book = new Book();
            book.setId(b[0]);
            book.setTitle(b[1]);
            book.setAuthor(b[2]);
            book.setCategory(b[3]);
            book.setQuantity(5);

            db.collection(COL_BOOKS)
                    .document(book.getId())
                    .set(book)
                    .get();
        }

        saveLog("SEED_BOOKS", "Added branch-wise books");
        return "Branch-wise books added successfully";
    }
}
