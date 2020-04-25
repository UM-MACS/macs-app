package com.example.project1.chat.component;

public class ContactItem {

    private String name;
    private String email;
    private String type;

    public ContactItem(String name, String email, String type) {
        this.name = name;
        this.email = email;
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
