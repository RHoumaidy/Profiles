package com.example.raafat.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;

/**
 * Created by Raafat on 16/09/2015.
 */
public class DbHelper extends SQLiteOpenHelper {

    public static String DB_NAME = "PROFILES_DB";
    public static Integer DB_VERSION = 1;

    public static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    public DbHelper(Context context) {
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Profile.getSql());
        db.execSQL(Day.getSql());
        db.execSQL(ProfileDay.getSql());
        Day.insert(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+Profile.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+Day.TABLE_NAME);

        onCreate(db);
    }
}
