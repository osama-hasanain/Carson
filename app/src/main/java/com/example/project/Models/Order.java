package com.example.project.Models;

import java.util.ArrayList;

public class Order {
    private int order_id;
    private String email;
    private ArrayList<OrderItem> order_list= new ArrayList<>();

    public Order(int order_id, String email, ArrayList<OrderItem> order_list) {
        this.order_id = order_id;
        this.email = email;
        this.order_list = order_list;
    }
    public Order(){

    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<OrderItem> getOrder_list() {
        return order_list;
    }

    public void setOrder_list(ArrayList<OrderItem> order_list) {
        this.order_list = order_list;
    }
}
