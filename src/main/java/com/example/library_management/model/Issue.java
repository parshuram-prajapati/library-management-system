package com.example.library_management.model;

/*
 * Issue Model Class
 * -----------------
 * OOP Concept: Encapsulation
 *  - All variables are private
 *  - Accessed using getters and setters
 *
 * DSA Concepts:
 *  - Objects of this class are stored in:
 *      • Queue (FIFO issue handling)
 *      • PriorityQueue (overdue priority handling)
 *
 * This class acts as a NODE / ELEMENT inside data structures
 */

public class Issue {

    /*
     * DSA:
     * bookId is used to SEARCH issues during return operation
     * Linear search is applied on Queue
     */
    private String bookId;

    // Used for display/reporting purpose (no DSA impact)
    private String bookTitle;

    /*
     * DSA:
     * studentId is a UNIQUE identifier
     * Often stored in Set<String> to avoid duplicates
     */
    private String studentId;

    // For UI / reporting
    private String studentName;

    /*
     * Date fields
     * -----------
     * Used in:
     * - Due date comparison
     * - Fine calculation
     * - PriorityQueue ordering
     *
     * Stored as String for DB / Firebase / UI compatibility
     */
    private String issueDate;
    private String dueDate;
    private String returnDate;

    /*
     * Multithreading Support
     * ---------------------
     * Used by Reminder Thread
     * Ensures reminder is sent only once
     */
    private boolean reminderSent;

    // Default constructor (required for JSON / Firebase mapping)
    public Issue() {
    }

    /*
     * Getter & Setter Methods
     * ----------------------
     * OOP Concept: Encapsulation
     */

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    /*
     * DSA Logic:
     * Issue date is used for calculating due date
     */
    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    /*
     * 🔥 IMPORTANT METHOD
     * -------------------
     * getDueDate() is REQUIRED for:
     * - PriorityQueue sorting
     * - Overdue detection
     * - Fine calculation
     *
     * Comparator uses this method directly
     */
    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    /*
     * Used when book is returned
     * Helps calculate late days and fine
     */
    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    /*
     * Multithreading + DSA:
     * Flag checked by Reminder Thread
     * Prevents duplicate reminders
     */
    public boolean isReminderSent() {
        return reminderSent;
    }

    public void setReminderSent(boolean reminderSent) {
        this.reminderSent = reminderSent;
    }
}
