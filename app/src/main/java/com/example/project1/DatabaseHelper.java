package com.example.project1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String patientData= "PatientTable";
    public static final String caregiverData= "CaregiverTable";
    public static final String patientEmotionData= "PatientEmotions";
    public static final String caregiverEmotionData= "CaregiverEmotions";
    public DatabaseHelper(Context context) {
        super(context, "UserDatabase.db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + patientData + "(Name text PRIMARY KEY,Email text, Password text, Contact text, Age text, Appointment text)");
        db.execSQL("CREATE TABLE " + caregiverData+ "(Name text PRIMARY KEY,Email text, Password text, Contact text, Age text,Appointment text,Relationship text)");
        db.execSQL("CREATE TABLE " + patientEmotionData + "(Email text PRIMARY KEY, Happy INTEGER, Moderate INTEGER,Sad INTEGER)");
        db.execSQL("CREATE TABLE " + caregiverEmotionData+ "(Email text PRIMARY KEY, Happy INTEGER, Moderate INTEGER,Sad INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + patientData);
            db.execSQL("DROP TABLE IF EXISTS " + caregiverData);
            db.execSQL("DROP TABLE IF EXISTS " + patientEmotionData);
            db.execSQL("DROP TABLE IF EXISTS " + caregiverEmotionData);
    }

    //inserting in database
    public boolean insertPatientTable(String email, String name, String password, String contact, String age){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name",name);
        contentValues.put("Email",email);
        contentValues.put("Password",password);
        contentValues.put("Contact",contact);
        contentValues.put("Age",age);
        contentValues.put("Appointment","");
        long ins = db.insert(patientData,null, contentValues);
        //check if insert is successful
        db.rawQuery(" SELECT name FROM "+patientData+" ORDER BY  name " + " COLLATE NOCASE; ",null);
        if(ins ==-1) return false;
        else return true;

    }

    public boolean insertCaregiverTable(String email, String name, String password, String contact, String age,String relationship){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name",name);
        contentValues.put("Email",email);
        contentValues.put("Password",password);
        contentValues.put("Contact",contact);
        contentValues.put("Age",age);
        contentValues.put("Appointment","");
        contentValues.put("Relationship",relationship);
//        contentValues.put("Appointment","");
        long ins = db.insert(caregiverData,null, contentValues);
        //check if insert is successful
        if(ins ==-1) return false;
        else return true;

    }


    //check if the email is registered before
    public Boolean checkMail(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + patientData + " where email = ?", new String[]{email});
        if (cursor.getCount()>0) return false;
        else return true;
    }

    public Boolean checkMail2(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + caregiverData+ " where email = ?", new String[]{email});
        if (cursor.getCount()>0) return false;
        else return true;
    }

    //check if email and password matches
    public Boolean verifyAccount(String email, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + patientData +" WHERE email =? AND password = ?", new String[]{email,password});
        if (cursor.getCount()>0) return true;
        else return false;
    }

    public Boolean verifyAccount2(String email, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + caregiverData+" WHERE email =? AND password = ?", new String[]{email,password});
        if (cursor.getCount()>0) return true;
        else return false;
    }

    public Boolean setAppointment(String type,String email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        int i = Patient.getInstance().getSizeAppointment()-1;
        String app= Patient.getInstance().getPatientAppointment().get(i);
        values.put("Appointment",app);
        if(type.equals("Patient")) {
            long ins = db.update(patientData, values, "Email =?", new String[]{email});
            if (ins == -1) return false;
            return true;
        }
        else{
            long ins = db.update(caregiverData, values, "Email =?", new String[]{email});
            if (ins == -1) return false;
            return true;
        }
    }



    /* for user's emotion table */
    public void createCounter(String type,String email, int count1, int count2, int count3){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Email",email);
        contentValues.put("Happy",count1);
        contentValues.put("Moderate",count2);
        contentValues.put("Sad",count3);
        if(type.equals("Patient")){
        db.insert(patientEmotionData,null,contentValues);
        }
        else{
            db.insert(caregiverEmotionData,null,contentValues);
        }
    }

    public Boolean setHappyCounter(String type, String email,int count){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Happy",count);
        if(type.equals("Patient")){
            long c = db.update(patientEmotionData,contentValues, " Email =?",new String[]{email});
            if(c==-1)
                return false;
            else return true;
        }
        else{
            long c = db.update(caregiverEmotionData,contentValues, " Email =?",new String[]{email});
            if(c==-1)
                return false;
            else return true;
        }
    }

    public Boolean setModerateCounter(String type,String email,int count){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Moderate",count);
        if(type.equals("Patient")){
            long c = db.update(patientEmotionData,contentValues, " Email =?",new String[]{email});
            if(c==-1)
                return false;
            else return true;
        }
        else{
            long c = db.update(caregiverEmotionData,contentValues, " Email =?",new String[]{email});
            if(c==-1)
                return false;
            else return true;
        }
    }

    public Boolean setSadCounter(String type,String email,int count){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Sad",count);
        if(type.equals("Patient")){
            long c = db.update(patientEmotionData,contentValues, " Email =?",new String[]{email});
            if(c==-1)
                return false;
            else return true;
        }
        else{
            long c = db.update(caregiverEmotionData,contentValues, " Email =?",new String[]{email});
            if(c==-1)
                return false;
            else return true;
        }
    }

    public Cursor getHappyCounter(String type,String email){
        SQLiteDatabase db = this.getReadableDatabase();
        if(type.equals("Patient")){
            Cursor cursor = db.rawQuery("SELECT Happy FROM " + patientEmotionData +" WHERE Email=? " , new String[]{email});
            return cursor;
        }
        else{
            Cursor cursor = db.rawQuery("SELECT Happy FROM " + patientEmotionData +" WHERE Email=? " , new String[]{email});
            return cursor;
        }

    }

    public Cursor getModerateCounter(String type,String email){
        SQLiteDatabase db = this.getReadableDatabase();
        if(type.equals("Patient")){
            Cursor cursor = db.rawQuery("SELECT Moderate FROM " + patientEmotionData +" WHERE Email=? " , new String[]{email});
            return cursor;
        }
        else{
            Cursor cursor = db.rawQuery("SELECT Moderate FROM " + patientEmotionData +" WHERE Email=? " , new String[]{email});
            return cursor;
        }
    }

    public Cursor getSadCounter(String type,String email){
        SQLiteDatabase db = this.getReadableDatabase();
        if(type.equals("Patient")){
            Cursor cursor = db.rawQuery("SELECT Sad FROM " + patientEmotionData +" WHERE Email=? " , new String[]{email});
            return cursor;
        }
        else{
            Cursor cursor = db.rawQuery("SELECT Sad FROM " + patientEmotionData +" WHERE Email=? " , new String[]{email});
            return cursor;
        }
    }
    /* end of emotion table*/
}
