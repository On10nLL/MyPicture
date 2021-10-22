package com.example.mypicture;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

//创建item数据库

public class ItemDataBase extends SQLiteOpenHelper {
    private String CREATE_TABLE_COLLECT = "create table collect(" +
            "id integer primary key autoincrement," +
            "author text," +
            "user text ," +
            "title text," +
            "imageurl text," +
            "imageid integer)";
    private String CREATE_TABLE_LIKES = "create table likes(" +
            "id integer primary key autoincrement," +
            "author text," +
            "user text ," +
            "title text," +
            "imageurl text," +
            "imageid integer)";
    private String CREATE_TABLE_ITEMS = "create table items(" +
            "id integer primary key autoincrement," +
            "author text," +
            "title text," +
            "imageurl text," +
            "imageid integer)";

    public ItemDataBase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_COLLECT);
        sqLiteDatabase.execSQL(CREATE_TABLE_ITEMS);
        sqLiteDatabase.execSQL(CREATE_TABLE_LIKES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
