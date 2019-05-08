package com.example.project1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper2 extends SQLiteOpenHelper {
    public static final String emotions = "UserEmotions";
    public DatabaseHelper2(Context context) {
        super(context, "EmotionDatabase.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + emotions + "(Emotions text PRIMARY KEY, Amount INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + emotions);
    }

    public void createCounter(String Emotion, int count){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Emotions",Emotion);
        contentValues.put("Amount",count);
        db.insert(emotions,null,contentValues);
    }

    public Boolean setCounter(String Emotion,Integer count){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Amount",count);
        long c = db.update(emotions,contentValues, " Emotions = ?",new String[]{Emotion});
        if(c==-1) return false;
        else return true;
    }

    public Cursor getCounter(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + emotions , null);
        return cursor;
    }


}
