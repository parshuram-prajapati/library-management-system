package com.example.library_management.model;

/*
 * Log Model Class
 * ---------------
 * Purpose:
 *  - Stores activity logs such as ISSUE and RETURN actions
 *
 * OOP Concept:
 *  - Encapsulation (private fields + getters/setters)
 *
 * DSA Concepts:
 *  - Objects of this class are stored in:
 *      • List / Queue (for sequential logs)
 *      • Map (timestamp -> Log) for fast lookup
 *
 * System Design:
 *  - Acts as AUDIT TRAIL for the Library Management System
 */

public class Log {

    /*
     * Timestamp
     * ---------
     * Used as:
     * - Sorting key
     * - Searching logs by time
     * - Index key in Map data structures
     */
    private String timestamp;

    /*
     * 🔥 IMPORTANT FIELD
     * ------------------
     * actionType MUST match dashboard.js
     *
     * DSA:
     * Used for filtering logs
     * Example:
     * - ISSUE logs
     * - RETURN logs
     */
    private String actionType; // ISSUE / RETURN

    /*
     * Description
     * -----------
     * Human-readable log message
     * Used only for display
     */
    private String description;

    /*
     * User
     * ----
     * Identifies who performed the action
     * Can be stored in Set to avoid duplicate users
     */
    private String user;

    /*
     * Default constructor
     * -------------------
     * Required for Firestore / JSON deserialization
     */
    public Log() {
    }

    /*
     * Parameterized constructor
     * -------------------------
     * Used when creating log entry during Issue/Return
     */
    public Log(String timestamp, String actionType, String description, String user) {
        this.timestamp = timestamp;
        this.actionType = actionType;
        this.description = description;
        this.user = user;
    }

    // ---------------- GETTERS & SETTERS ----------------
    // OOP: Encapsulation

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
