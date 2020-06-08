package com.example.project1.chat.component;

public class ContactItem {

    private String name;
    private String nric;
    private String type;
    private String photo;

    public ContactItem(String name, String nric, String type, String photo) {
        this.name = name;
        this.nric = nric;
        this.type = type;
        this.photo = photo;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getNRIC() {
        return nric;
    }

    public void setNRIC(String nric) {
        this.nric = nric;
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
