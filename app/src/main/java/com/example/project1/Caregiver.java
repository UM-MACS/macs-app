package com.example.project1;

public class Caregiver {
    private String relation;
    private static Caregiver ourInstance = new Caregiver();

    public static Caregiver getInstance() {
        if (ourInstance == null){
            ourInstance = new Caregiver();
        }
        return ourInstance;
    }

    private Caregiver() {
    }

    public void setRelation(String relation){
        this.relation = relation;
    }
}
