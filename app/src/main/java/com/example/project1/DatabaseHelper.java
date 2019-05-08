package com.example.project1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String patientTable = "PatientTable";
    public DatabaseHelper(Context context) {
        super(context, "Database.db", null, 1);
    }
//    Patient p = new Patient();

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + patientTable + "(Name text PRIMARY KEY,Email text, Password text, Contact text, Age text, Appointment text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + patientTable);
//            db.execSQL("DROP TABLE IF EXISTS " +emotions);
    }

    //inserting in database
    public boolean insert(String email, String name, String password, String contact, String age){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name",name);
        contentValues.put("Email",email);
        contentValues.put("Password",password);
        contentValues.put("Contact",contact);
        contentValues.put("Age",age);
        contentValues.put("Appointment","");
        long ins = db.insert(patientTable,null, contentValues);
        //check if insert is successful
        if(ins ==-1) return false;
        else return true;

    }


    //check if the email is registered before
    public Boolean checkMail(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + patientTable + " where email = ?", new String[]{email});
        if (cursor.getCount()>0) return false;
        else return true;
    }

    //check if email and password matches
    public Boolean verifyAccount(String email, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + patientTable +" WHERE email =? AND password = ?", new String[]{email,password});
        if (cursor.getCount()>0) return true;
        else return false;
    }

    public Boolean setAppointment(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        int i = Patient.getInstance().getSizeAppointment()-1;
        String app= Patient.getInstance().getPatientAppointment().get(i);
        values.put("Appointment",app);
        long ins = db.update(patientTable,values,"Email =?",new String[]{email});
        if(ins==-1) return false;
        return true;
    }
}
