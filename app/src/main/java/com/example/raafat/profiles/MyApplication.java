package com.example.raafat.profiles;

import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.raafat.data.Day;
import com.example.raafat.data.DbHelper;
import com.gitonway.lee.niftynotification.lib.NiftyNotificationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Raafat on 10/09/2015.
 */
public class MyApplication extends Application {


    public static SharedPreferences SP;
    public static Context APP_CNTX;
    public static AudioManager audioManager;
    public static RingtoneManager ringtoneManager;
    public static AlarmManager alarmManager;
    public static NotificationManager notificationManager;
    public static WifiManager wifiManager;
    public static BluetoothAdapter bluetoothAdapter;
    public static LocationManager locationManager;

    public static Location location;

    public static final String PROFILE_NAME_KEY = "profile_name";
    public static final String RING_MODE_KEY = "ring_mode";
    public static final String CHANGE_RINGTONE_KEY = "change_ringtone_checkbox";
    public static final String CHOOSE_RINGTONE_KEY = "ringtone_choose";
    public static final String CHANGE_NOTIFICATION_KEY = "change_notification_checkbox";
    public static final String CHOOSE_NOTIFICATION_KEY = "notification_choose";
    public static final String CHANGE_WIFI_KEY = "change_wifi";
    public static final String AUTO_ACTIVATION_KEY = "auto_activation_mode";
    public static final String REPEATIN_DAYS_KEY = "repeating_days";
    public static final String CHANGE_BLUETOOEH_KEY = "change_bluetooth";

    public static String[] DAYS_OF_WEEK;
    public static final String PREF_NAME = "PREF_NAME";

    public static final int CURRENT_RINGER_MODE = 0;
    public static final int RING_VIBERATE_RINGER_MODE = 1;
    public static final int VIBERATE_RINGER_MODE = 2;
    public static final int SILANCE_RINGER_MODE = 3;

    public static final int TOGLE_WIFI_MODE = 3;
    public static final int ON_WIFI_MODE = 1;
    public static final int OFF_WIFI_MODE = 2;

    public static final long ONE_WEEK = 60 * 60 * 24 * 7 * 1000;

    public static int CURR_ID = 245;

    public static final String ACTION_ACTIVATION = "ACTION_ACTIVATION";
    public static final String ACTIN_DIS_ACTIVATION = "ACTIN_DIS_ACTIVATION";

    public static final int BY_TIME = 0;
    public static final int BY_LOCATOIN = 1;


    public static DbHelper dbHelper;
    public static SQLiteDatabase db;
    public static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    @Override
    public void onCreate() {
        super.onCreate();


        SP = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        DAYS_OF_WEEK = getApplicationContext().getResources().getStringArray(R.array.repeating_day_entries);
        APP_CNTX = getApplicationContext();

        dbHelper = new DbHelper(APP_CNTX);
        db = dbHelper.getWritableDatabase();
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    }



}
