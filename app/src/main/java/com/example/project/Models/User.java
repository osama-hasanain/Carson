package com.example.project.Models;

public class User {
    int id ;
    String img ;
    String email ;
    String name ;
    String password ;

    public User(int id, String img, String email, String name, String password) {
        this.id = id;
        this.img = img;
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public User() {

    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
