package com.example.raafat.data;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.widget.Toast;

import com.example.raafat.profiles.MyApplication;
import com.example.raafat.profiles.R;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Raafat on 10/09/2015.
 */
public class Profile {

    public static final String TABLE_NAME = "PROFILES_TABLE";
    public static final String COL_ID = "ID";
    public static final String COL_NAME = "NAME";
    public static final String COL_SUB_TILTE = "SUB_TITLE";
    public static final String COL_AUTO_ACTIVATION_MODE = "ACTIVATIN_MODE";
    public static final String COL_FROM_TIME = "FROM_TIME";
    public static final String COL_TO_TIME = "TO_TIME";
    public static final String COL_LATITUDE = "LATITUDE";
    public static final String COL_LONITUDE = "LONGITUDE";
    public static final String COL_RINGER_MODE = "RINGER_MODE";
    public static final String COL_CHANGE_RINGTONE = "CHANGE_RINGTOE";
    public static final String COL_RINGTONE_URI = "RINGTONE_URI";
    public static final String COL_CHANGE_NOTITONE = "CHANGE_NOTITONE";
    public static final String COL_NOTITONE_URI = "NOTITONE_URI";
    public static final String COL_IS_ENABLED = "IS_ENABLED";
    public static final String COL_WIFI_MODE = "WIFI_MODE";
    public static final String COL_BLUETOOTH_MODE = "BLUETOOTH_MODE";

    public static final String[] COLS = {COL_ID, COL_NAME, COL_SUB_TILTE, COL_AUTO_ACTIVATION_MODE, COL_FROM_TIME,
            COL_TO_TIME, COL_LATITUDE, COL_LONITUDE, COL_RINGER_MODE, COL_CHANGE_NOTITONE, COL_NOTITONE_URI,
            COL_CHANGE_RINGTONE, COL_RINGTONE_URI, COL_IS_ENABLED, COL_WIFI_MODE, COL_BLUETOOTH_MODE};

    public static String getSql() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                COL_NAME + " TEXT ," +
                COL_SUB_TILTE + " TEXT ," +
                COL_AUTO_ACTIVATION_MODE + " SHORT ," +
                COL_FROM_TIME + " INTEGER ," +
                COL_TO_TIME + " INTEGER ," +
                COL_LATITUDE + " INTEGER ," +
                COL_LONITUDE + " INTEGER ," +
                COL_RINGER_MODE + " SHORT ," +
                COL_CHANGE_NOTITONE + " BOOLEAN ," +
                COL_NOTITONE_URI + " TEXT , " +
                COL_CHANGE_RINGTONE + " BOOLEAN ," +
                COL_RINGTONE_URI + " TEXT , " +
                COL_IS_ENABLED + " BOOLEAN DEFAULT 1 ," +
                COL_BLUETOOTH_MODE + " INTEGER ," +
                COL_WIFI_MODE + " INTEGER );";
    }


    public long save(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, this.name);
        cv.put(COL_SUB_TILTE, this.subTitle);
        cv.put(COL_FROM_TIME, this.fromTime);
        cv.put(COL_AUTO_ACTIVATION_MODE, this.acivationMode);
        cv.put(COL_TO_TIME, this.toTime);
        cv.put(COL_LATITUDE, this.latitude);
        cv.put(COL_LONITUDE, this.longitude);
        cv.put(COL_RINGER_MODE, this.ringerMode);
        cv.put(COL_CHANGE_NOTITONE, this.changeNotiTone);
        cv.put(COL_NOTITONE_URI, this.notiToneURI);
        cv.put(COL_CHANGE_RINGTONE, this.ringeToneURI);
        cv.put(COL_IS_ENABLED, this.isEnabled);
        cv.put(COL_WIFI_MODE, this.getWifiMode());
        cv.put(COL_BLUETOOTH_MODE, this.getBlueToothMode());

        this.id = db.insert(TABLE_NAME, null, cv);
        for (Integer day : repeatingDays) {
            Day profileDay = new Day(0, day, this.id);
            profileDay.saveCurrent(MyApplication.db);
        }
        this.prevId = this.id + 100000;
        return this.id;
    }

    public static Profile load(SQLiteDatabase db, long id) {
        Cursor c = db.query(TABLE_NAME, null, COL_ID + " = ? ", new String[]{String.valueOf(id)}, null, null, null);
        Profile resProfile = new Profile();
        if (c.moveToFirst()) {

            resProfile.setId(c.getLong(c.getColumnIndex(COL_ID)));
            resProfile.setName(c.getString(c.getColumnIndex(COL_NAME)));
            resProfile.setSubTitle(c.getString(c.getColumnIndex(COL_SUB_TILTE)));
            resProfile.setEnabled(c.getInt(c.getColumnIndex(COL_IS_ENABLED)) == 1);
            resProfile.setFromTime(c.getLong(c.getColumnIndex(COL_FROM_TIME)));
            resProfile.setToTime(c.getLong(c.getColumnIndex(COL_TO_TIME)));
            resProfile.setChangeNotiTone(c.getInt(c.getColumnIndex(COL_CHANGE_NOTITONE)) == 1);
            resProfile.setChangeRingtone(c.getInt(c.getColumnIndex(COL_CHANGE_RINGTONE)) == 1);
            resProfile.setAcivationMode(c.getInt(c.getColumnIndex(COL_AUTO_ACTIVATION_MODE)));
            resProfile.setRingerMode(c.getInt(c.getColumnIndex(COL_RINGER_MODE)));
            resProfile.setRingeToneURI(c.getString(c.getColumnIndex(COL_RINGTONE_URI)));
            resProfile.setNotiToneURI(c.getColumnName(c.getColumnIndex(COL_NOTITONE_URI)));
            resProfile.setRepeatingDays(Day.getRepeatingDaysForProfile(id, MyApplication.db));
            resProfile.setWifiMode(c.getInt(c.getColumnIndex(COL_WIFI_MODE)));
            resProfile.setBlueToothMode(c.getInt(c.getColumnIndex(COL_BLUETOOTH_MODE)));
        }
        return resProfile;
    }

    public static List<Profile> getAll(SQLiteDatabase db) {
        Cursor c = db.query(TABLE_NAME, COLS, null, null, null, null, null);

        List<Profile> res = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                res.add(Profile.load(db, c.getLong(c.getColumnIndex(COL_ID))));
            } while (c.moveToNext());
        }

        return res;
    }

    public void delete(SQLiteDatabase db) {
        Day.deleteAllRepeatingDayForProfile(this.getId(), MyApplication.db);
        db.delete(TABLE_NAME, COL_ID + "=?", new String[]{String.valueOf(this.getId())});
        this.unRigsterProfile();

    }


    private long prevId;
    private long id;
    private String name;
    private int acivationMode;
    private long fromTime;
    private long toTime;
    private long latitude;
    private long longitude;
    private List<Integer> repeatingDays;
    private int ringerMode;
    private boolean changeRingtone;
    private String ringeToneURI;
    private boolean changeNotiTone;
    private String notiToneURI;
    private boolean isEnabled;
    private int wifiMode;
    private int blueToothMode;
    private String subTitle;

    private Profile prevProfile;

    public Profile() {
        this.ringerMode = MyApplication.CURRENT_RINGER_MODE;
        this.changeNotiTone = false;
        this.changeRingtone = false;
        this.isEnabled = true;
        this.toTime = 0;

    }

    public void unRigsterProfile() {
        for (int day : repeatingDays) {
            Intent intent = new Intent(MyApplication.ACTION_ACTIVATION);
            PendingIntent pendingIntent = PendingIntent
                    .getBroadcast(MyApplication.APP_CNTX, (int) (id--), intent, PendingIntent.FLAG_NO_CREATE);
            if (pendingIntent != null) {
                pendingIntent.cancel();
                MyApplication.alarmManager.cancel(pendingIntent);
            }
        }
    }

    public void registerProfileByTime(int day, int repeating) {

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        //day = cal1.get(Calendar.DAY_OF_WEEK);
        cal1.set(Calendar.DAY_OF_WEEK, day);
        cal2.set(Calendar.DAY_OF_WEEK, day);


        Time from = new Time(fromTime);
        Time to = new Time(fromTime + 1 * 90 * 1000);

        cal1.set(Calendar.HOUR, from.getHours());
        cal2.set(Calendar.HOUR, to.getHours());
        cal1.set(Calendar.MINUTE, from.getMinutes());
        cal2.set(Calendar.MINUTE, to.getMinutes());
        cal1.set(Calendar.SECOND, 0);
        cal2.set(Calendar.SECOND, 0);


        Intent intent1 = new Intent(MyApplication.ACTION_ACTIVATION);
        Intent intent2 = new Intent(MyApplication.ACTIN_DIS_ACTIVATION);

        intent1.putExtra(MyApplication.APP_CNTX.getString(R.string.profile_name_intent_extra), this.getName());
        intent1.putExtra(MyApplication.APP_CNTX.getString(R.string.ringer_mode_intent_extra), this.getRingerMode());
        intent1.putExtra(MyApplication.APP_CNTX.getString(R.string.ring_tone_intent_extra), this.getRingeToneURI());
        intent1.putExtra(MyApplication.APP_CNTX.getString(R.string.noti_tone_intent_extra), this.getNotiToneURI());
        intent1.putExtra(MyApplication.APP_CNTX.getString(R.string.change_ring_tone_intent_extra), this.isChangeRingtone());
        intent1.putExtra(MyApplication.APP_CNTX.getString(R.string.change_noti_tone_intent_extra), this.isChangeNotiTone());
        intent1.putExtra(MyApplication.APP_CNTX.getString(R.string.activation_mode_intent_extra), this.getAcivationMode());
        intent1.putExtra(MyApplication.APP_CNTX.getString(R.string.from_time_intent_extra), this.getFromTime());
        intent1.putExtra(MyApplication.APP_CNTX.getString(R.string.to_time_intent_extra), this.getToTime());
        intent1.putExtra(MyApplication.APP_CNTX.getString(R.string.curr_id_intent_extras), this.prevId);
        intent1.putExtra(MyApplication.APP_CNTX.getString(R.string.wifi_mode_intent_extra), this.getWifiMode());
        intent1.putExtra(MyApplication.APP_CNTX.getString(R.string.bluetooth_mode_intent_extra), this.getBlueToothMode());

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(MyApplication.APP_CNTX, (int) (id + 1), intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        if (pendingIntent == null) {
            Toast.makeText(MyApplication.APP_CNTX, "nullllllllll", Toast.LENGTH_LONG).show();
        }

        ContentValues cv = new ContentValues();
        cv.put(COL_ID, id + 1);
        MyApplication.db.update(TABLE_NAME, cv, COL_ID + "=?", new String[]{String.valueOf(id)});
        this.setId(id + 1);

        long fireTime = cal1.getTimeInMillis();
        if ((cal1.getTimeInMillis() - Calendar.getInstance().getTimeInMillis()) <= 0 &&
                (cal2.getTimeInMillis() - Calendar.getInstance().getTimeInMillis()) <= 0)
            fireTime += MyApplication.ONE_WEEK;

        if (repeating == 1)
            MyApplication.alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, fireTime, 60 * 3 * 1000, pendingIntent);
        else
            MyApplication.alarmManager.set(AlarmManager.RTC_WAKEUP, fireTime, pendingIntent);


    }

    public void registerPrevProfile(int day) {
        Calendar cal1 = Calendar.getInstance();

        //day = cal1.get(Calendar.DAY_OF_WEEK);
        cal1.set(Calendar.DAY_OF_WEEK, day);


        Time from = new Time(fromTime);

        cal1.set(Calendar.HOUR, from.getHours());
        cal1.set(Calendar.MINUTE, from.getMinutes());
        cal1.set(Calendar.SECOND, 0);


        Intent intent1 = new Intent(MyApplication.ACTION_ACTIVATION);

        intent1.putExtra(MyApplication.APP_CNTX.getString(R.string.profile_name_intent_extra), this.getName());
        intent1.putExtra(MyApplication.APP_CNTX.getString(R.string.ringer_mode_intent_extra), this.getRingerMode());
        intent1.putExtra(MyApplication.APP_CNTX.getString(R.string.ring_tone_intent_extra), this.getRingeToneURI());
        intent1.putExtra(MyApplication.APP_CNTX.getString(R.string.noti_tone_intent_extra), this.getNotiToneURI());
        intent1.putExtra(MyApplication.APP_CNTX.getString(R.string.change_ring_tone_intent_extra), this.isChangeRingtone());
        intent1.putExtra(MyApplication.APP_CNTX.getString(R.string.change_noti_tone_intent_extra), this.isChangeNotiTone());
        intent1.putExtra(MyApplication.APP_CNTX.getString(R.string.activation_mode_intent_extra), this.getAcivationMode());
        intent1.putExtra(MyApplication.APP_CNTX.getString(R.string.from_time_intent_extra), this.getFromTime());
        intent1.putExtra(MyApplication.APP_CNTX.getString(R.string.wifi_mode_intent_extra), this.getWifiMode());
        intent1.putExtra(MyApplication.APP_CNTX.getString(R.string.bluetooth_mode_intent_extra), this.getBlueToothMode());

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(MyApplication.APP_CNTX, (int) (id), intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        if (pendingIntent == null) {
            Toast.makeText(MyApplication.APP_CNTX, "nullllllllll", Toast.LENGTH_LONG).show();
        }

        ContentValues cv = new ContentValues();
        cv.put(COL_ID, id + 1);
        MyApplication.db.update(TABLE_NAME, cv, COL_ID + "=?", new String[]{String.valueOf(id)});
        this.setId(id + 1);

        long fireTime = cal1.getTimeInMillis();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            MyApplication.alarmManager.setExact(AlarmManager.RTC_WAKEUP, fireTime, pendingIntent);
        }else{
            MyApplication.alarmManager.set(AlarmManager.RTC_WAKEUP, fireTime, pendingIntent);
        }


    }

    public void registerProfileByTime() {

        if (this.isEnabled()) {
            if (this.acivationMode == MyApplication.BY_TIME)
                for (Integer day : repeatingDays) {
                    this.registerProfileByTime(day, 1);
                }


        }


    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public int getBlueToothMode() {
        return blueToothMode;
    }

    public void setBlueToothMode(int blueToothMode) {
        this.blueToothMode = blueToothMode;
    }

    public int getWifiMode() {
        return wifiMode;
    }

    public void setWifiMode(int wifiMode) {
        this.wifiMode = wifiMode;
    }

    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public long getPrevId() {
        return prevId;
    }

    public void setPrevId(long prevId) {
        this.prevId = prevId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public Profile getPrevProfile() {
        return prevProfile;
    }

    public void setPrevProfile(Profile prevProfile) {
        this.prevProfile = prevProfile;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public boolean validateTimes() {
        return (this.toTime - this.fromTime < 24 * 60 * 60 * 1000) &&
                (this.toTime - this.fromTime > 0);
    }

    public int getAcivationMode() {
        return acivationMode;
    }

    public void setAcivationMode(int acivationMode) {
        this.acivationMode = acivationMode;
    }

    public long getFromTime() {
        return fromTime;
    }

    public void setFromTime(long fromTime) {
        this.fromTime = fromTime;
    }

    public long getToTime() {
        return toTime;
    }

    public void setToTime(long toTime) {
        this.toTime = toTime;
    }

    public List<Integer> getRepeatingDays() {
        return repeatingDays;
    }

    public void setRepeatingDays(List<Integer> repeatingDays) {
        this.repeatingDays = repeatingDays;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRingerMode() {
        return ringerMode;
    }

    public void setRingerMode(int ringerMode) {
        this.ringerMode = ringerMode;
    }

    public boolean isChangeRingtone() {
        return changeRingtone;
    }

    public void setChangeRingtone(boolean changeRingtone) {
        this.changeRingtone = changeRingtone;
    }

    public String getRingeToneURI() {
        return ringeToneURI;
    }

    public void setRingeToneURI(String ringeToneURI) {
        this.ringeToneURI = ringeToneURI;
    }

    public boolean isChangeNotiTone() {
        return changeNotiTone;
    }

    public void setChangeNotiTone(boolean changeNotiTone) {
        this.changeNotiTone = changeNotiTone;
    }

    public String getNotiToneURI() {
        return notiToneURI;
    }

    public void setNotiToneURI(String notiToneURI) {
        this.notiToneURI = notiToneURI;
    }
}
