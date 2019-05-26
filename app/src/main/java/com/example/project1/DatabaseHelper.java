package com.example.project1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String patientData= "PatientTable";
    public static final String caregiverData= "CaregiverTable";
    public static final String patientEmotionData= "PatientEmotions";
    public static final String appointment = "Appointment";
    public static final String eventAssessmentTable = "EventAssessmentTable";

    public DatabaseHelper(Context context) {
        super(context, "UserDatabase.db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + patientData + "(Name text PRIMARY KEY,Email text, Password text, Contact text, Age text)");
        db.execSQL("CREATE TABLE " + caregiverData+ "(Name text PRIMARY KEY,Email text, Password text, Contact text, Age text,Relationship text)");
        db.execSQL("CREATE TABLE " + patientEmotionData + "(Email text,Type text, Date text, Expression text)");
        db.execSQL("CREATE TABLE " + appointment+ "(Email text,Type text, Remark text, AppointmentDate text, AppointmentTime text)");
        db.execSQL("CREATE TABLE " + eventAssessmentTable+ "(Email text, q1 text, q2 text, q3 text, q4 text, q5 text, q6 text, q7 text, q8 text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + patientData);
        db.execSQL("DROP TABLE IF EXISTS " + caregiverData);
        db.execSQL("DROP TABLE IF EXISTS " + patientEmotionData);
        db.execSQL("DROP TABLE IF EXISTS " + appointment);
        db.execSQL("DROP TABLE IF EXISTS " + eventAssessmentTable);
    }

    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "message" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){
            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }


    public boolean insertEventAssessment(String email, String text, String text2, String text3, String text4, String text5, String text6, String text7, String text8){
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
        values.put("q8",text8);
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
    public Boolean setAppointment(String type,String email,String date,String time, String remark){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Email",email);
        values.put("Type",type);
        values.put("AppointmentDate",date);
        values.put("AppointmentTime",time);
        values.put("Remark",remark);
            long ins = db.insert(appointment, null, values);
            if (ins == -1) return false;
            return true;
    }

    public Cursor getAppointment(String type, String email){
        SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + appointment +" WHERE Email=? AND Type =? " , new String[]{email, type});
            return cursor;

    }

    public Boolean deleteAppointment(String type, String email, String date,String time,String remark){
        SQLiteDatabase db = this.getWritableDatabase();
            long c = db.delete(appointment," Email=? AND Type =? AND AppointmentDate=? AND AppointmentTime =? AND Remark=? ",new String[]{email,type,date,time,remark});
            if(c==-1) return false;
            return true;
    }

    /* End */



    /* for user's emotion table */

    public Boolean insertEmotion(String type,String email,String date, String emotion){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Date",date);
        contentValues.put("Email",email);
        contentValues.put("Type", type);
        contentValues.put("Expression",emotion);
        long ins = db.insert(patientEmotionData,null,contentValues);
        if (ins==-1) return false;
        return true;

    }

//    public Boolean checkDate (String email, String date, String type){
//        SQLiteDatabase db = this.getReadableDatabase();
//        if(type.equals("Patient")) {
//            Cursor cursor = db.rawQuery("Select * from " + patientEmotionData + " where Email =? AND Date =? ", new String[]{email,date});
//            if (cursor.getCount() > 0) {
//                return true;
//            } else {
//                return false;
//            }
//        }
//        else{
//            Cursor cursor = db.rawQuery("Select * from " + caregiverEmotionData + " where Email =? AND Date =? ", new String[]{email,date});
//            if (cursor.getCount() > 0) {
//                return true;
//            } else {
//                return false;
//            }
//        }

//    }


//    public Boolean setHappyCounter(String type, String email, String date, int count){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("Happy",count);
//            if (type.equals("Patient")) {
//                long c = db.update(patientEmotionData, contentValues, " Email =? AND Date =?", new String[]{email,date});
//                if (c == -1)
//                    return false;
//                else return true;
//            } else {
//                long c = db.update(caregiverEmotionData, contentValues, " Email =? AND Date =?", new String[]{email,date});
//                if (c == -1)
//                    return false;
//                else return true;
//            }
//        }
//
//
//
//
//    public Boolean setSmileyCounter(String type,String email,String date, int count){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("Smiley",count);
//        if(type.equals("Patient")){
//            long c = db.update(patientEmotionData,contentValues, " Email =? AND Date =?", new String[]{email,date});
//            if(c==-1)
//                return false;
//            else return true;
//        }
//        else{
//            long c = db.update(caregiverEmotionData,contentValues, " Email =? AND Date =?", new String[]{email,date});
//            if(c==-1)
//                return false;
//            else return true;
//        }
//    }
//
//    public Boolean setUnhappyCounter(String type,String email,String date, int count){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("Unhappy",count);
//        if(type.equals("Patient")){
//            long c = db.update(patientEmotionData,contentValues, " Email =? AND Date =?", new String[]{email,date});
//            if(c==-1)
//                return false;
//            else return true;
//        }
//        else{
//            long c = db.update(caregiverEmotionData,contentValues, " Email =? AND Date =?", new String[]{email,date});
//            if(c==-1)
//                return false;
//            else return true;
//        }
//    }
//
//    public Boolean setAngryCounter(String type,String email,String date, int count){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("Angry",count);
//        if(type.equals("Patient")){
//            long c = db.update(patientEmotionData,contentValues, " Email =? AND Date =?", new String[]{email,date});
//            if(c==-1)
//                return false;
//            else return true;
//        }
//        else{
//            long c = db.update(caregiverEmotionData,contentValues, " Email =? AND Date =?", new String[]{email,date});
//            if(c==-1)
//                return false;
//            else return true;
//        }
//    }
//
//    public Boolean setSadCounter(String type,String email,String date, int count){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("Sad",count);
//        if(type.equals("Patient")){
//            long c = db.update(patientEmotionData,contentValues, " Email =? AND Date =?", new String[]{email,date});
//            if(c==-1)
//                return false;
//            else return true;
//        }
//        else{
//            long c = db.update(caregiverEmotionData,contentValues, " Email =? AND Date =?", new String[]{email,date});
//            if(c==-1)
//                return false;
//            else return true;
//        }
//    }
//
//    public Cursor getHappyCounter(String type,String email,String date){
//        SQLiteDatabase db = this.getReadableDatabase();
//        if(type.equals("Patient")){
//            Cursor cursor = db.rawQuery("SELECT Happy FROM " + patientEmotionData +" WHERE Email=? AND Date =? " , new String[]{email, date});
//            return cursor;
//        }
//        else{
//            Cursor cursor = db.rawQuery("SELECT Happy FROM " + caregiverEmotionData+" WHERE Email=? AND Date=? " , new String[]{email,date});
//            return cursor;
//        }
//
//    }
//
//    public Cursor getSmileyCounter(String type,String email, String date){
//        SQLiteDatabase db = this.getReadableDatabase();
//        if(type.equals("Patient")){
//            Cursor cursor = db.rawQuery("SELECT Smiley FROM " + patientEmotionData +" WHERE Email =? AND Date =? " , new String[]{email, date});
//            return cursor;
//        }
//        else{
//            Cursor cursor = db.rawQuery("SELECT Smiley FROM " + caregiverEmotionData +" WHERE Email=? AND Date =? " , new String[]{email, date});
//            return cursor;
//        }
//    }
//
//    public Cursor getUnhappyCounter(String type,String email, String date){
//        SQLiteDatabase db = this.getReadableDatabase();
//        if(type.equals("Patient")){
//            Cursor cursor = db.rawQuery("SELECT Unhappy FROM " + patientEmotionData +" WHERE Email=? AND Date =? " , new String[]{email, date});
//            return cursor;
//        }
//        else{
//            Cursor cursor = db.rawQuery("SELECT Unhappy FROM " + caregiverEmotionData +" WHERE Email=? AND Date =? " , new String[]{email, date});
//            return cursor;
//        }
//
//    }
//
//    public Cursor getAngryCounter(String type,String email,String date){
//        SQLiteDatabase db = this.getReadableDatabase();
//        if(type.equals("Patient")){
//            Cursor cursor = db.rawQuery("SELECT Angry FROM " + patientEmotionData +" WHERE Email=? AND Date =? " , new String[]{email, date});
//            return cursor;
//        }
//        else{
//            Cursor cursor = db.rawQuery("SELECT Angry FROM " + caregiverEmotionData +" WHERE Email=? AND Date =? " , new String[]{email, date});
//            return cursor;
//        }
//    }
//
//    public Cursor getSadCounter(String type,String email,String date){
//        SQLiteDatabase db = this.getReadableDatabase();
//        if(type.equals("Patient")){
//            Cursor cursor = db.rawQuery("SELECT Sad FROM " + patientEmotionData +" WHERE Email=? AND Date =? " , new String[]{email, date});
//            return cursor;
//        }
//        else{
//            Cursor cursor = db.rawQuery("SELECT Sad FROM " + caregiverEmotionData +" WHERE Email=? AND Date =? " , new String[]{email, date});
//            return cursor;
//        }
//    }
//
//    public Boolean setExpressionText(String type, String email, String date, String text){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("Expression",text);
//        if(type.equals("Patient")){
//            long c = db.update(patientEmotionData,values," Email =? AND Date =? ",new String[]{email,date});
//            if(c==-1) return false;
//            return true;
//        }
//        else{
//            long c = db.update(caregiverEmotionData,values," Email =? AND Date =? ",new String[]{email,date});
//            if(c==-1) return false;
//            return true;
//        }
//    }
    /* end of emotion table*/
}
