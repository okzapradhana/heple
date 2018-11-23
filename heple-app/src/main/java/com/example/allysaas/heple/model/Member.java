package com.example.allysaas.heple.model;

public class Member{
    public String name;
    public String email;

    public Member() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Member(String name, String email) {
        this.name = name;
        this.email = email;
    }
}