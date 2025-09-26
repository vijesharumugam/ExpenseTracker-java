package com.tracker.model;

import java.time.LocalDateTime;

public class Expense {
    private int id;
    private int categoryId;
    private String description;
    private int amount;
    private LocalDateTime date;

    // For new expenses
    public Expense(int categoryId, String description, int amount) {
        this.categoryId = categoryId;
        this.description = description;
        this.amount = amount;
        this.date = LocalDateTime.now();  // ✅ Auto set current date
    }

    // For existing expenses (e.g., update)
    public Expense(int id, int categoryId, String description, int amount, LocalDateTime date) {
        this.id = id;
        this.categoryId = categoryId;
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    public int getId() { return id; }
    public int getCategoryId() { return categoryId; }
    public String getDescription() { return description; }
    public int getAmount() { return amount; }
    public LocalDateTime getDate() { return date; }

    public void setDate(LocalDateTime date) { this.date = date; }
}
