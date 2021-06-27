package com.example.project.Models;

public class Category {
    private int category_id;
    private String category_title;

    public Category(int category_id, String category_title) {
        this.category_id = category_id;
        this.category_title = category_title;
    }

    public Category() {
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCategory_title() {
        return category_title;
    }

    public void setCategory_title(String category_title) {
        this.category_title = category_title;
    }
}
