package com.example.raafat.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Raafat on 16/09/2015.
 */
public class ProfileDay {

    public static String TABLE_NAME = "PROFILE_DAY_TALBE";
    public static String COL_PROFILE_ID = "PROFILE_ID";
    public static String COL_DAY_ID = "DAY_ID";
    public static String COL_ID = "ID";

    public static String getSql (){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( "+
                COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT ,"+
                COL_DAY_ID +" INTEGER ,"+
                COL_PROFILE_ID + " INTEGER );";
    }

    public  long save(SQLiteDatabase db){
        ContentValues cv = new ContentValues();

        cv.put(COL_PROFILE_ID,prfileId);
        cv.put(COL_DAY_ID,dayId);
        return db.insert(TABLE_NAME,null,cv);
    }

    private int id;
    private int dayId;
    private long prfileId;


    public ProfileDay(int id, int dayId, long prfileId) {
        this.id = id;
        this.dayId = dayId;
        this.prfileId = prfileId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDayId() {
        return dayId;
    }

    public void setDayId(int dayId) {
        this.dayId = dayId;
    }

    public long getPrfileId() {
        return prfileId;
    }

    public void setPrfileId(long prfileId) {
        this.prfileId = prfileId;
    }
}
