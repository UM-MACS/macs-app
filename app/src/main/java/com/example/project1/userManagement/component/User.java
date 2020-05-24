package com.example.project1.userManagement.component;

public class User {

    private String name,email,nric,contactNo,age,type,id;

    public User() {
    }

    public User(String name, String email, String NRIC, String contactNo, String age, String type, String id) {
        this.name = name;
        this.email = email;
        this.nric = NRIC;
        this.contactNo = contactNo;
        this.age = age;
        this.type = type;
        this.id = id;
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

    public String getNric() {
        return nric;
    }

    public void setNric(String NRIC) {
        this.nric = NRIC;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
