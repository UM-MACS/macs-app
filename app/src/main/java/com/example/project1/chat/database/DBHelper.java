package com.example.project1.chat.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "masc.db", factory, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tableContact = "create table if not exists Contact (Name text, Type text, Email text, Photo text);";
//        String tableContact = "create table if not exists Contact (Name text, Type text, Email text, Photo text);";
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
