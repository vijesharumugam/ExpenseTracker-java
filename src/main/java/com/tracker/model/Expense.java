package com.tracker.model;

import java.time.LocalDateTime;

public class Expense {
    private int id;
    private int category_id;
    private String description;
    private int amount;
    private LocalDateTime date;

    public Expense(){
        this.date = LocalDateTime.now();
    }
    public Expense(int category_id, String description){
        this.category_id = category_id;
        this.description = description;
    }
    public Expense(int category_id, String description,int amount){
        this.category_id = category_id;
        this.description = description;
        this.amount = amount;
    }
    public Expense(int id,int category_id,String description,int amount,LocalDateTime date){
        this.id = id;
        this.category_id = category_id;
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    public int getId(){
        return id;
    }
    public int getCategoryId(){
        return category_id;
    }
    public void setId(){
        this.id = id;
    }
    public void setCategoryId(){
        this.category_id = category_id;
    }
    public String getDescription(){
        return description;
    }
    public void setDescription(){
        this.description = description;
    }
    public int getAmount(){
        return amount;
    }
    public void setAmount(){
        this.amount = amount;
    }
    public LocalDateTime getDate(){
        return date;
    }
    public void setDate(){
        this.date = date;
    }
}
