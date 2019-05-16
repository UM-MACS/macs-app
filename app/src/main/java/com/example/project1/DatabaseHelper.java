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
    public static final String patientAppointment = "PatientAppointment";
    public static final String caregiverAppointment = "CaregiverAppointment";
    public static final String eventAssessmentTable = "EventAssessmentTable";

    public DatabaseHelper(Context context) {
        super(context, "UserDatabase.db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + patientData + "(Name text PRIMARY KEY,Email text, Password text, Contact text, Age text)");
        db.execSQL("CREATE TABLE " + caregiverData+ "(Name text PRIMARY KEY,Email text, Password text, Contact text, Age text,Relationship text)");
        db.execSQL("CREATE TABLE " + patientEmotionData + "(Email text PRIMARY KEY, Happy INTEGER, Smiley INTEGER, Unhappy INTEGER, Angry INTEGER,Sad INTEGER, Expression text)");
        db.execSQL("CREATE TABLE " + caregiverEmotionData+ "(Email text PRIMARY KEY, Happy INTEGER, Moderate INTEGER,Sad INTEGER)");
        db.execSQL("CREATE TABLE " + patientAppointment+ "(Email text,Remark text, Appointment text)");
        db.execSQL("CREATE TABLE " + caregiverAppointment+ "(Email text,Remark text, Appointment text)");
        db.execSQL("CREATE TABLE " + eventAssessmentTable+ "(Email text, q1 text, q2 text, q3 text, q4 text, q5 text, q6 text, q7 text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + patientData);
        db.execSQL("DROP TABLE IF EXISTS " + caregiverData);
        db.execSQL("DROP TABLE IF EXISTS " + patientEmotionData);
        db.execSQL("DROP TABLE IF EXISTS " + caregiverEmotionData);
        db.execSQL("DROP TABLE IF EXISTS " + patientAppointment);
        db.execSQL("DROP TABLE IF EXISTS " + caregiverAppointment);
        db.execSQL("DROP TABLE IF EXISTS " + eventAssessmentTable);
    }

    public boolean insertEventAssessment(String email, String text, String text2, String text3, String text4, String text5, String text6, String text7){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Email",email);
        values.put("q1",text);
        values.put("q2",text2);
        values.put("q3",text3);
        values.put("q4",text4);
        values.put("q5",text5);
        values.put("q6",text6);
        values.put("q7",text7);

        long ins = db.insert(eventAssessmentTable,null,values);
        if (ins == -1) return false;
        else return true;
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



    /* For Appointment Scheduling */
    public Boolean setAppointment(String type,String email,String app,String remark){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Email",email);
        values.put("Appointment",app);
        values.put("Remark",remark);
        if(type.equals("Patient")) {
            long ins = db.insert(patientAppointment, null, values);
            if (ins == -1) return false;
            return true;
        }
        else{
            long ins = db.insert(caregiverAppointment, null, values);
            if (ins == -1) return false;
            return true;
        }
    }

    public Cursor getAppointment(String type, String email){
        SQLiteDatabase db = this.getReadableDatabase();
        if(type.equals("Patient")){
            Cursor cursor = db.rawQuery("SELECT * FROM " + patientAppointment +" WHERE Email=? " , new String[]{email});
            return cursor;
        }
        else{
            Cursor cursor = db.rawQuery("SELECT * FROM " + caregiverAppointment +" WHERE Email=? " , new String[]{email});
            return cursor;
        }
    }

    public Boolean deleteAppointment(String type, String email, String app,String remark){
        SQLiteDatabase db = this.getWritableDatabase();
        if(type.equals("Patient")) {
            long c = db.delete(patientAppointment," Email=? AND Appointment=? AND Remark=? ",new String[]{email,app,remark});
            if(c==-1) return false;
            return true;
        }
        else{
            long c =db.delete(caregiverAppointment," Email=? AND Appointment=? AND Remark=? ",new String[]{email,app,remark});
            if(c==-1) return false;
            return true;
        }
    }




    /* for user's emotion table */
    public void createCounter(String type,String email, int count1, int count2, int count3, int count4, int count5){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Email",email);
        contentValues.put("Happy",count1);
        contentValues.put("Smiley",count2);
        contentValues.put("Unhappy",count3);
        contentValues.put("Angry",count4);
        contentValues.put("Sad",count5);
        contentValues.put("Expression","");
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

    public Boolean setSmileyCounter(String type,String email,int count){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Smiley",count);
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

    public Boolean setUnhappyCounter(String type,String email,int count){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Unhappy",count);
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

    public Boolean setAngryCounter(String type,String email,int count){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Angry",count);
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
            Cursor cursor = db.rawQuery("SELECT Happy FROM " + caregiverEmotionData+" WHERE Email=? " , new String[]{email});
            return cursor;
        }

    }

    public Cursor getSmileyCounter(String type,String email){
        SQLiteDatabase db = this.getReadableDatabase();
        if(type.equals("Patient")){
            Cursor cursor = db.rawQuery("SELECT Smiley FROM " + patientEmotionData +" WHERE Email=? " , new String[]{email});
            return cursor;
        }
        else{
            Cursor cursor = db.rawQuery("SELECT Smiley FROM " + caregiverEmotionData +" WHERE Email=? " , new String[]{email});
            return cursor;
        }
    }

    public Cursor getUnhappyCounter(String type,String email){
        SQLiteDatabase db = this.getReadableDatabase();
        if(type.equals("Patient")){
            Cursor cursor = db.rawQuery("SELECT Unhappy FROM " + patientEmotionData +" WHERE Email=? " , new String[]{email});
            return cursor;
        }
        else{
            Cursor cursor = db.rawQuery("SELECT Unhappy FROM " + caregiverEmotionData +" WHERE Email=? " , new String[]{email});
            return cursor;
        }

    }

    public Cursor getAngryCounter(String type,String email){
        SQLiteDatabase db = this.getReadableDatabase();
        if(type.equals("Patient")){
            Cursor cursor = db.rawQuery("SELECT Angry FROM " + patientEmotionData +" WHERE Email=? " , new String[]{email});
            return cursor;
        }
        else{
            Cursor cursor = db.rawQuery("SELECT Angry FROM " + caregiverEmotionData +" WHERE Email=? " , new String[]{email});
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
            Cursor cursor = db.rawQuery("SELECT Sad FROM " + caregiverEmotionData +" WHERE Email=? " , new String[]{email});
            return cursor;
        }
    }

    public Boolean setExpressionText(String type, String email, String text){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Expression",text);
        if(type.equals("Patient")){
            long c = db.update(patientEmotionData,values," Email =?",new String[]{email});
            if(c==-1) return false;
            return true;
        }
        else{
            long c = db.update(caregiverEmotionData,values," Email =?",new String[]{email});
            if(c==-1) return false;
            return true;
        }
    }
    /* end of emotion table*/
}
