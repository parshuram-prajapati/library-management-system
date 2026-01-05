package com.example.library_management.model;

/*
 * Book Model Class
 * ----------------
 * OOP Concept: Encapsulation
 *  - All data members are private
 *  - Accessed using public getters and setters
 *
 * DSA Concept:
 *  - This class is used as VALUE object inside HashMap
 *    (Map<String, Book>)
 */

public class Book {

    // DSA: Used as KEY in HashMap for fast O(1) access
    private String id;

    // Normal data fields
    private String title;
    private String author;

    // Category stored as String (can also be Enum)
    private String category;

    /*
     * DSA Concept:
     * Quantity management helps in counting available copies
     * Used with conditional logic in LibraryService
     */
    private int quantity;

    /*
     * Issue / Return Flags
     * -------------------
     * Used in Queue & HashMap based logic
     * Helps track book availability
     */
    private boolean issued;

    // Stores studentId (String) who has issued the book
    private String issuedTo;

    // Default constructor (required for frameworks like Firebase / JSON)
    public Book() {
    }

    /*
     * Getter & Setter Methods
     * -----------------------
     * OOP Concept: Encapsulation
     * Data is accessed safely using methods
     */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    /*
     * DSA + Logic:
     * Quantity is checked before issuing a book
     * Example:
     * if(quantity == 0) -> book not available
     */
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /*
     * DSA Logic:
     * Boolean flag used for quick availability check
     * Helps reduce unnecessary searching
     */
    public boolean isIssued() {
        return issued;
    }

    public void setIssued(boolean issued) {
        this.issued = issued;
    }

    /*
     * DSA:
     * Stores studentId (unique key from Set or HashMap)
     * Helps in mapping Book -> Student
     */
    public String getIssuedTo() {
        return issuedTo;
    }

    public void setIssuedTo(String issuedTo) {
        this.issuedTo = issuedTo;
    }
}
