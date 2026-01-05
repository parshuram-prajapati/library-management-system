package com.example.library_management.model;

/*
 * Student Model Class
 * -------------------
 * OOP Concept:
 *  - Encapsulation (private fields + public getters/setters)
 *
 * DSA Concepts:
 *  - Objects of this class are stored in:
 *      • HashMap<String, Student>  -> fast lookup using studentId
 *      • Set<Student>              -> to avoid duplicate students
 *
 * System Design:
 *  - Represents library users who can issue and return books
 */

public class Student {

    /*
     * Student ID
     * ----------
     * DSA:
     * Used as KEY in HashMap
     * Provides O(1) average time complexity for search
     */
    private String id;

    /*
     * Student Name
     * ------------
     * Used for display and reporting
     */
    private String name;

    /*
     * Student Email
     * -------------
     * Used for:
     * - Notifications
     * - Due date reminders
     *
     * Can be processed by background thread
     */
    private String email;

    /*
     * Default constructor
     * -------------------
     * Required for JSON / Firebase / ORM frameworks
     */
    public Student() {
    }

    // ---------- GETTERS & SETTERS ----------
    // OOP: Encapsulation

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /*
     * DSA:
     * Name is accessed after retrieving student
     * from HashMap or Set
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*
     * Used in:
     * - Email notification service
     * - Reminder thread
     */
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
