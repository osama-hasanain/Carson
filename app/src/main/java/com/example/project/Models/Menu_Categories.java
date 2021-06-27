package com.example.project.Models;

public class Menu_Categories {
    private int item_category_id ;
    private String item_description ;
    private int item_id ;
    private String item_image;
    private int item_price ;
    private String item_title  ;

    public Menu_Categories() {
    }
    public Menu_Categories(OrderItem orderItem , int num) {
    }

    public Menu_Categories(int item_category_id, String item_description, int item_id, String item_image, int item_price, String item_title) {
        this.item_category_id = item_category_id;
        this.item_description = item_description;
        this.item_id = item_id;
        this.item_image = item_image;
        this.item_price = item_price;
        this.item_title = item_title;
    }

    public int getItem_category_id() {
        return item_category_id;
    }

    public void setItem_category_id(int item_category_id) {
        this.item_category_id = item_category_id;
    }

    public String getItem_description() {
        return item_description;
    }

    public void setItem_description(String item_description) {
        this.item_description = item_description;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }

    public int getItem_price() {
        return item_price;
    }

    public void setItem_price(int item_price) {
        this.item_price = item_price;
    }

    public String getItem_title() {
        return item_title;
    }

    public void setItem_title(String item_title) {
        this.item_title = item_title;
    }
}
