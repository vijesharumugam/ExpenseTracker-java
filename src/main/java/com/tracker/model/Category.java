package com.tracker.model;

public class Category {
    private int id;
    private String type;

    public Category(int id,String type){
        this.id = id;
        this.type = type;
     }

     public int getId(){
        return id;
     }
     public String getType(){
        return type;
     }
     public void setId(){
        this.id = id;
     }
     public void setType(){
        this.type = type;
     }
}
