package com.example.project1.login.component;

public class User {
    private String appointment;
    private int id;
    private String Name;
    private String NRIC;
    private String Password;
    private String Contact;
    private String Age;
    private String userType;
    private int appointmentId;
    private static User ourInstance = new User();



    public static User getInstance() {
        if(ourInstance == null){
            ourInstance = new User();
        }
        return ourInstance;
    }

    private User() {
    }

    public void setUserType (String type){
        this.userType = type;
    }

    public String getUserType(){
        return userType;
    }

    public void setAppointment(String app) {
        this.appointment = app;
    }

    public String getUserName() {
        return Name;
    }

    public void setUserName(String name) {
        this.Name = name;
    }

    public String getNRIC() {
        return NRIC;
    }

    public void setNRIC(String nric) {
        this.NRIC= nric;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public void setAge(String age) {
        this.Age= age;
    }

    public void setContact(String contact) {
        this.Contact= contact;
    }

    //caregiver

//    public String getCaregiverName() {
//        return name;
//    }
//
//    public void setCaregiverName(String name) {
//        this.name = name;
//    }
//
//    public String getCaregiverEmail() {
//        return email;
//    }
//
//    public void setCaregiverEmail(String email) {
//        this.email = email;
//    }
//
//    public String getCaregiverPassword() {
//        return password;
//    }
//
//    public void setCaregiverPassword(String password) {
//        this.password = password;
//    }
//
//    public String getCaregiverAge() {
//        return age;
//    }
//
//    public void setCaregiverAge(String age) {
//        this.age = age;
//    }
//
//    public String getCaregiverContact() {
//        return contact;
//    }
//
//    public void setCaregiverContact(String contact) {
//        this.contact = contact;
//    }
}
