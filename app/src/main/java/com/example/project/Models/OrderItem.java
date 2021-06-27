package com.example.project.Models;

import android.view.Menu;

public class OrderItem {
    Menu_Categories orderItem;
    int number;

    public OrderItem(Menu_Categories orderItem , int number) {
        this.orderItem = orderItem ;
        this.number = number;
    }
    public OrderItem() {
    }

    public Menu_Categories getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(Menu_Categories orderItem) {
        this.orderItem = orderItem;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
