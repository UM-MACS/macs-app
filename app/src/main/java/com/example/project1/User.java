package com.example.project1;

import java.util.ArrayList;

public class User {
    private ArrayList<String> Appointment = new ArrayList<String>();
    private int id;
    private String Name;
    private String Email;
    private String Password;
    private String Contact;
    private String Age;
    private String userType;
    private static User ourInstance = new User();



    public static User getInstance() {
        if(ourInstance == null){
            ourInstance = new User();
        }
        return ourInstance;
    }

    private User() {
    }

    public ArrayList<String> getPatientAppointment() {
        return Appointment;
    }

    public void setUserType (String type){
        this.userType = type;
    }

    public String getUserType(){
        return userType;
    }

    public void setPatientAppointment(String app) {
        Appointment.add(app);
    }

    public int getSizeAppointment(){
        return Appointment.size();
    }

    public String getUserName() {
        return Name;
    }

    public void setUserName(String name) {
        this.Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email= email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        this.Age= age;
    }

    public String getContact() {
        return Contact;
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
