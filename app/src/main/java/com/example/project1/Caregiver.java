package com.example.project1;

import java.util.ArrayList;

public class Caregiver {
    private ArrayList<String> appointment = new ArrayList<String>();
    private int id;
    private String name;
    private String email;
    private String password;
    private String contact;
    private String age;
    private static Caregiver ourInstance = new Caregiver();

    public static Caregiver getInstance() {
        if(ourInstance == null){
            ourInstance = new Caregiver();
        }
        return ourInstance;
    }

    private Caregiver() {
    }

//    public ArrayList<String> getCaregiverAppointment() {
//        return appointment;
//    }

//    public void setCaregiverAppointment(String app) {
//        appointment.add(app);
//    }

//    public int getSizeAppointment(){
//        return appointment.size();
//    }

    public String getCaregiverName() {
        return name;
    }

    public void setCaregiverName(String name) {
        this.name = name;
    }

    public String getCaregiverEmail() {
        return email;
    }

    public void setCaregiverEmail(String email) {
        this.email = email;
    }

    public String getCaregiverPassword() {
        return password;
    }

    public void setCaregiverPassword(String password) {
        this.password = password;
    }

    public String getCaregiverAge() {
        return age;
    }

    public void setCaregiverAge(String age) {
        this.age = age;
    }

    public String getCaregiverContact() {
        return contact;
    }

    public void setCaregiverContact(String contact) {
        this.contact = contact;
    }
}
