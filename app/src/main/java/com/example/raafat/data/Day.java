package com.example.raafat.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.raafat.profiles.MyApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Raafat on 16/09/2015.
 */
public class Day {

    public static final String TABLE_NAME = "DAYS_TABLE";
    public static final String COL_ID= "DAYS_ID";
    public static final String COL_DAY_ID = "DAY_ID";
    public static final String COL_PROFILE_ID = "PROFILE_ID";

    public static final String[] COLS = {COL_ID,COL_DAY_ID,COL_PROFILE_ID};

    public static String getSql(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("+
                COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT ,"+
                COL_DAY_ID + " INTEGER ,"+
                COL_PROFILE_ID + " INTEGER );";
    }

    public static void insert(SQLiteDatabase db){
        ContentValues cv = new ContentValues();
        for(int i = 1;i<7;++i) {
            cv.put(COL_ID, i);
            db.insert(TABLE_NAME,null,cv);
        }
        cv.put(COL_ID, 7);
        db.insert(TABLE_NAME,null,cv);

    }



    public static List<Integer> getRepeatingDaysForProfile(long profileId, SQLiteDatabase db){
        Cursor c = db.query(TABLE_NAME,new String[]{COL_DAY_ID},COL_PROFILE_ID +"=?",new String[]{String.valueOf(profileId)},null,null,null);
        List<Integer> res = new ArrayList<>();

        if(c.moveToFirst()){
            do {
                res.add(c.getInt(c.getColumnIndex(COL_DAY_ID)));
            }while(c.moveToNext());
        }

        Collections.sort(res);

        return res;
    }

    public static void deleteAllRepeatingDayForProfile(long profileId,SQLiteDatabase db){
        db.delete(TABLE_NAME,COL_PROFILE_ID +" = ? ",new String[]{String.valueOf(profileId)});
    }

    public void saveCurrent(SQLiteDatabase db){
        ContentValues cv = new ContentValues();
        cv.put(COL_DAY_ID,this.getDayId());
        cv.put(COL_PROFILE_ID,this.getPrifileId());

        db.insert(TABLE_NAME,null,cv);
    }

    private long id;
    private long dayId;
    private long prifileId;

    public Day(long id, long dayId , long prifileId) {
        this.id = id;
        this.dayId = dayId;
        this.prifileId = prifileId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDayId() {
        return dayId;
    }

    public void setDayId(long dayId) {
        this.dayId = dayId;
    }

    public long getPrifileId() {
        return prifileId;
    }

    public void setPrifileId(long prifileId) {
        this.prifileId = prifileId;
    }
}
