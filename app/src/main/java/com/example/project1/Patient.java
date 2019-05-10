package com.example.project1;

import java.util.ArrayList;

public class Patient {
    private ArrayList<String> appointment = new ArrayList<String>();
    private int id;
    private String name;
    private String email;
    private String password;
    private String contact;
    private String age;
    private static Patient ourInstance;

    private Patient() {
    }

    public static Patient getInstance() {
        if(ourInstance == null){
            ourInstance = new Patient();
        }
        return ourInstance;
    }


        public ArrayList<String> getPatientAppointment() {
            return appointment;
        }

        public void setPatientAppointment(String app) {
            appointment.add(app);
        }

        public int getSizeAppointment(){
            return appointment.size();
        }

        public String getPatientName() {
            return name;
        }

        public void setPatientName(String name) {
            this.name = name;
        }

        public String getPatientEmail() {
            return email;
        }

        public void setPatientEmail(String email) {
            this.email = email;
        }

        public String getPatientPassword() {
            return password;
        }

        public void setPatientPassword(String password) {
            this.password = password;
        }

        public String getPatientAge() {
            return age;
        }

        public void setPatientAge(String age) {
            this.age = age;
        }

        public String getPatientContact() {
            return contact;
        }

        public void setPatientContact(String contact) {
            this.contact = contact;
        }
    }


