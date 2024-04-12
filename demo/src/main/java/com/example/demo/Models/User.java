package com.example.demo.Models;

import org.springframework.data.annotation.Id;

public class User {
    @Id
    private String uid;
    private String name;
    private String password; // Password hash?

    public User(String uid, String name, String password) {
        this.uid = uid;
        this.name = name;
        this.password = password; // Maybe input password is plaintext and must hash it?
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password; // You may want to hash the password before setting it
    }
}
